package com.rawr.simple;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rawr.simple.api.Search;
import com.rawr.simple.layout.BackButtonAwareRelativeLayout;
import com.rawr.simple.layout.EndlessRecyclerViewScrollListener;
import com.rawr.simple.layout.LayoutUtil;
import com.rawr.simple.layout.SearchBox;
import com.rawr.simple.layout.SearchModeButton;
import com.rawr.simple.layout.SearchOptionsView;
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
  private static final float SEARCH_IMAGE_CONTAINER_SIZE = 400;
  private static final float SEARCH_IMAGE_CONTAINER_MARGIN = 30;

  private final Context context;
  private final BackButtonAwareRelativeLayout rootView;
  private final ImageView iconView;
  private final SearchBox searchBox;
  private final SearchModeButton searchBtn;
  private final ImageView searchOptionButton;
  private final SearchOptionsView searchOptionsView;

  private final SearchImageContainer searchImageContainer;
  private final RelativeLayout.LayoutParams searchImageContainerParams;

  private EndlessRecyclerViewScrollListener scrollListener;

  private final SearchSuggestion searchSuggestion;
  private final Search searchUtil;

  public FloatingActionButton(final Context context) {
    this.context = context;
    rootView = (BackButtonAwareRelativeLayout) LayoutInflater
        .from(context).inflate(R.layout.layout_fab, null);
    iconView = rootView.findViewById(R.id.imageView);
    searchBtn = rootView.findViewById(R.id.searchButton);
    searchBtn.init(context);
    searchBox = rootView.findViewById(R.id.autoCompleteTextView);
    searchBox.init(context, rootView);
    searchOptionsView = rootView.findViewById(R.id.searchOptionsView);
    searchOptionsView.init(context);
    searchOptionButton = rootView.findViewById(R.id.searchOptionButton);
    searchOptionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        System.out.println("Clicked");
        searchOptionsView.toggle();
      }
    });

    searchUtil = new Search(context);
    searchSuggestion = new SearchSuggestion(context, searchBox);

    searchImageContainer = new SearchImageContainer(context);
    searchImageContainerParams = new RelativeLayout.LayoutParams(
        (int) LayoutUtil.pxFromDp(context, SEARCH_IMAGE_CONTAINER_SIZE),
        (int) LayoutUtil.pxFromDp(context, SEARCH_IMAGE_CONTAINER_SIZE));
    searchImageContainerParams.leftMargin = (int) LayoutUtil.pxFromDp(
        context, SEARCH_IMAGE_CONTAINER_MARGIN);
    searchImageContainerParams.topMargin = (int) LayoutUtil.pxFromDp(
        context, SEARCH_IMAGE_CONTAINER_MARGIN);

    searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int keyCode, KeyEvent keyEvent) {
        // Done button or Enter
        if (keyCode == 0 || keyCode == 6) {
          String query = searchBox.getText().toString();
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

  public void toggleView(boolean toggled) {
    toggleSearch(toggled);
    toggleSearchView(toggled);
    toggleIcon(toggled);
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
    if (toggled) searchBtn.setToImageSearch();
    searchBox.toggle(toggled);
    searchBtn.setVisibility(toggled ? View.VISIBLE : View.INVISIBLE);
    searchOptionButton.setVisibility(toggled ? View.VISIBLE : View.INVISIBLE);
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
    searchBox.setEnabled(false);
    searchBtn.setToLoading();
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
