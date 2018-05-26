package com.rawr.simple;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rawr.simple.api.Search;
import com.rawr.simple.layout.BackButtonAwareRelativeLayout;
import com.rawr.simple.layout.EndlessRecyclerViewScrollListener;
import com.rawr.simple.layout.LayoutUtil;
import com.rawr.simple.search.image.SearchImageResult;
import com.rawr.simple.search.image.SearchImageContainer;
import com.rawr.simple.search.image.SearchImageResultAttributes;
import com.rawr.simple.search.suggestion.SearchSuggestion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FloatingActionButton {
  private static final float ICON_VIEW_SIZE = 50;
  private static final float ICON_VIEW_TOGGLED_SIZE = 60;
  private static final float SEARCH_VIEW_SIZE = 280;
  private static final float SEARCH_IMAGE_CONTAINER_SIZE = 400;
  private static final float SEARCH_IMAGE_CONTAINER_MARGIN = 30;

  private final Context context;
  private final BackButtonAwareRelativeLayout rootView;
  private final ImageView iconView;
  private final AutoCompleteTextView searchView;
  private final ImageView searchBtn;

  private final SearchImageContainer searchImageContainer;
  private final RelativeLayout.LayoutParams searchImageContainerParams;

  private EndlessRecyclerViewScrollListener scrollListener;

  private final SearchSuggestion searchSuggestion;
  private final Search searchUtil;

  public FloatingActionButton(final Context context) {
    this.context = context;
    rootView = (BackButtonAwareRelativeLayout) LayoutInflater.from(context).inflate(R.layout.layout_fab, null);
    iconView = rootView.findViewById(R.id.imageView);
    searchView = rootView.findViewById(R.id.autoCompleteTextView);
    searchBtn = rootView.findViewById(R.id.searchButton);
    searchView.setVisibility(View.INVISIBLE);
    searchBtn.setVisibility(View.INVISIBLE);

    searchUtil = new Search(context);
    searchSuggestion = new SearchSuggestion(context, searchView);

    searchImageContainer = new SearchImageContainer(context);
    searchImageContainerParams = new RelativeLayout.LayoutParams(
        (int) LayoutUtil.pxFromDp(context, SEARCH_IMAGE_CONTAINER_SIZE),
        (int) LayoutUtil.pxFromDp(context, SEARCH_IMAGE_CONTAINER_SIZE));
    searchImageContainerParams.leftMargin = (int) LayoutUtil.pxFromDp(
        context, SEARCH_IMAGE_CONTAINER_MARGIN);
    searchImageContainerParams.topMargin = (int) LayoutUtil.pxFromDp(
        context, SEARCH_IMAGE_CONTAINER_MARGIN);

    searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int keyCode, KeyEvent keyEvent) {
        // Done button or Enter
        if (keyCode == 0 || keyCode == 6) {
          String query = searchView.getText().toString();
          if (query.length() == 0) return false;
          Log.i("Query", query);
          searchImage(query);
        }
        return false;
      }
    });

    scrollListener = new EndlessRecyclerViewScrollListener(searchImageContainer.getLayoutManager()) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        searchNextImage();
      }
    };
    searchImageContainer.getRecyclerView().addOnScrollListener(scrollListener);
  }

  public BackButtonAwareRelativeLayout getRootView() {
    return rootView;
  }

  public ImageView getIconView() {
    return iconView;
  }

  public AutoCompleteTextView getSearchView() {
    return searchView;
  }

  public void toggleView(boolean toggled) {
    showCloseOption(false);
    toggleSearch(toggled);
    toggleSearchView(toggled);
    toggleIcon(toggled);
  }

  public void showCloseOption(boolean show) {
    if (show) {
      searchView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
      toggleIcon(show);
      searchView.setText("Exit");
      searchView.setVisibility(View.VISIBLE);
      searchView.setFocusable(false);
      TransitionManager.beginDelayedTransition(rootView);
      searchView.setWidth((int) LayoutUtil.pxFromDp(context, SEARCH_VIEW_SIZE));
    } else {
      searchView.setFocusable(true);
      searchView.setGravity(Gravity.NO_GRAVITY);
      searchView.setOnClickListener(null);
      searchView.setText("");
    }
  }

  private void toggleIcon(boolean toggled) {
    if (toggled) {
      iconView.getLayoutParams().width = (int) LayoutUtil.pxFromDp(context, ICON_VIEW_TOGGLED_SIZE);
      iconView.getLayoutParams().height = (int) LayoutUtil.pxFromDp(context, ICON_VIEW_TOGGLED_SIZE);
    } else {
      TransitionManager.beginDelayedTransition(rootView);
      iconView.getLayoutParams().width = (int) LayoutUtil.pxFromDp(context, ICON_VIEW_SIZE);
      iconView.getLayoutParams().height = (int) LayoutUtil.pxFromDp(context, ICON_VIEW_SIZE);
    }
  }

  private void toggleSearch(boolean toggled) {
    if (toggled) {
      RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
          (int) LayoutUtil.pxFromDp(context, 25),
          (int) LayoutUtil.pxFromDp(context, 25));
      params.rightMargin = params.topMargin = (int) LayoutUtil.pxFromDp(context, 5);
      params.addRule(RelativeLayout.ALIGN_END, R.id.autoCompleteTextView);
      params.addRule(RelativeLayout.ALIGN_TOP, R.id.autoCompleteTextView);
      searchBtn.setLayoutParams(params);

      Glide.with(context)
          .load(android.R.drawable.ic_search_category_default)
          .skipMemoryCache(true)
          .diskCacheStrategy(DiskCacheStrategy.NONE)
          .into(searchBtn);

      searchView.setFocusable(true);
      searchView.setVisibility(View.VISIBLE);
      TransitionManager.beginDelayedTransition(rootView);
      searchView.setWidth((int) LayoutUtil.pxFromDp(context, SEARCH_VIEW_SIZE));
      searchBtn.setVisibility(View.VISIBLE);
    } else {
      searchBtn.setVisibility(View.INVISIBLE);
      searchView.setVisibility(View.INVISIBLE);
      searchView.setWidth(0);
    }
    searchView.setText("");
    searchSuggestion.resetSuggestion();
  }

  private void toggleSearchView(boolean toggled) {
    if (!toggled) {
      rootView.removeView(searchImageContainer.getRecyclerView());
      searchImageContainer.getRecyclerView().removeOnScrollListener(scrollListener);
      searchImageContainer.reset();
      scrollListener.reset();
      scrollListener = new EndlessRecyclerViewScrollListener(searchImageContainer.getLayoutManager()) {
        @Override
        public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
          searchNextImage();
        }
      };
      searchImageContainer.getRecyclerView().addOnScrollListener(scrollListener);
    }
  }

  private void searchImage(String query) {
    searchView.setFocusable(false);
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
        (int) LayoutUtil.pxFromDp(context, 40),
        (int) LayoutUtil.pxFromDp(context, 40));
    params.rightMargin = 0;
    params.topMargin = (int) LayoutUtil.pxFromDp(context, -2);
    params.addRule(RelativeLayout.ALIGN_END, R.id.autoCompleteTextView);
    params.addRule(RelativeLayout.ALIGN_TOP, R.id.autoCompleteTextView);
    searchBtn.setLayoutParams(params);
    Glide.with(context)
        .load(R.raw.spin)
        .asGif()
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(searchBtn);
    searchUtil.reset(query, "image").build().execute(new JSONRequestCallback() {
      @Override
      public void completed(JSONObject jsonObject) {
        try {
          toggleSearch(false);

          JSONArray results = jsonObject.getJSONArray("data");
          List<SearchImageResult> searchImageResults = new ArrayList<>();

          for (int index = 0; index < results.length(); index++) {
            JSONObject thumbnail = results.getJSONObject(index).getJSONObject("thumbnail");
            JSONObject content = results.getJSONObject(index).getJSONObject("image");
            searchImageResults.add(new SearchImageResult()
                .setThumbnail(new SearchImageResultAttributes()
                    .setUrl(thumbnail.getString("url"))
                    .setHeight(thumbnail.getInt("height"))
                    .setWidth(thumbnail.getInt("width")))
                .setContent(new SearchImageResultAttributes()
                    .setUrl(content.getString("url"))
                    .setHeight(content.getInt("height"))
                    .setWidth(content.getInt("width"))));
          }

          searchUtil.nextPage(results.length());

          TransitionManager.beginDelayedTransition(rootView);
          searchImageContainer.setSearchImageResults(searchImageResults);
          rootView.addView(searchImageContainer.getRecyclerView(),
              0,
              searchImageContainerParams);
        } catch (Exception e) {
          Log.i("Search Image", "Failed to parse JSON");
        }
      }

      @Override
      public void failed(Exception e) {

      }
    });
  }

  private void searchNextImage() {
    searchUtil.build().execute(new JSONRequestCallback() {
      @Override
      public void completed(JSONObject jsonObject) {
        try {
          JSONArray results = jsonObject.getJSONArray("data");

          for (int index = 0; index < results.length(); index++) {
            JSONObject thumbnail = results.getJSONObject(index).getJSONObject("thumbnail");
            JSONObject content = results.getJSONObject(index).getJSONObject("image");
            searchImageContainer.addSearchImageResults(new SearchImageResult()
                .setThumbnail(new SearchImageResultAttributes()
                    .setUrl(thumbnail.getString("url"))
                    .setHeight(thumbnail.getInt("height"))
                    .setWidth(thumbnail.getInt("width")))
                .setContent(new SearchImageResultAttributes()
                    .setUrl(content.getString("url"))
                    .setHeight(content.getInt("height"))
                    .setWidth(content.getInt("width"))));
          }

          searchUtil.nextPage(results.length());
        } catch (Exception e) {
          Log.i("Search Next Image", "Failed to parse JSON");
        }
      }

      @Override
      public void failed(Exception e) {

      }
    });
  }
}
