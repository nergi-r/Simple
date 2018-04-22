package com.rawr.simple;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rawr.simple.api.Search;
import com.rawr.simple.layout.BackButtonAwareRelativeLayout;
import com.rawr.simple.layout.EndlessRecyclerViewScrollListener;
import com.rawr.simple.layout.LayoutUtil;
import com.rawr.simple.seach.image.SearchImage;
import com.rawr.simple.seach.image.SearchImageContainer;
import com.rawr.simple.seach.suggestion.SearchSuggestion;

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
  private final Button searchBtn;

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
          if(query.length() == 0) return false;
          Log.i("Query", query);
          searchImage(query);
        }
        return false;
      }
    });

    searchBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String query = searchView.getText().toString();
        if(query.length() == 0) return;
        Log.i("Query", query);
        searchImage(query);
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
    if(!toggled) {
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
    if (toggled) {
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

  private void searchImage(String query) {
    searchUtil.reset(query, "image").build().execute(new JSONRequestCallback() {
      @Override
      public void completed(JSONObject jsonObject) {
        try {
          toggleSearch(false);

          JSONArray results = jsonObject.getJSONArray("data");
          List<SearchImage> searchImageResults = new ArrayList<>();

          for (int index = 0; index < results.length(); index++) {
            JSONObject item = results.getJSONObject(index);
            final String thumbnailLink = item.getString("link");
            final int height = item.getInt("height");
            final int width = item.getInt("width");

            searchImageResults.add(new SearchImage(thumbnailLink, width, height));
          }

          searchUtil.nextPage(results.length());

          TransitionManager.beginDelayedTransition(rootView);
          searchImageContainer.setSearchImageResults(searchImageResults);
          rootView.addView(searchImageContainer.getRecyclerView(), 0, searchImageContainerParams);
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
            JSONObject item = results.getJSONObject(index);
            final String thumbnailLink = item.getString("link");
            final int height = item.getInt("height");
            final int width = item.getInt("width");

            searchImageContainer.addSearchImageResults(
                new SearchImage(thumbnailLink, width, height));
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
