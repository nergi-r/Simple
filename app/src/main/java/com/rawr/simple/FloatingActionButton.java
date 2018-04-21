package com.rawr.simple;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rawr.simple.seach.image.SearchImage;
import com.rawr.simple.seach.image.SearchImageContainer;

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
  private final ViewGroup rootView;
  private final ImageView iconView;
  private final AutoCompleteTextView searchView;
  private final Button searchBtn;

  private final SearchImageContainer searchImageContainer;
  private final RelativeLayout.LayoutParams searchImageContainerParams;

  private EndlessRecyclerViewScrollListener scrollListener;

  private Search searchUtil;

  public FloatingActionButton(final Context context) {
    this.context = context;
    rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.layout_fab, null);
    iconView = rootView.findViewById(R.id.imageView);
    searchView = rootView.findViewById(R.id.autoCompleteTextView);
    searchBtn = rootView.findViewById(R.id.searchButton);

    searchView.setVisibility(View.INVISIBLE);
    searchBtn.setVisibility(View.INVISIBLE);

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
      public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        // Done button or Enter
        if (i == 0 || i == 6) {
          String query = searchView.getText().toString();
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
        Log.i("Query", query);
        searchImage(query);
      }
    });

    scrollListener = new EndlessRecyclerViewScrollListener(searchImageContainer.getLayoutManager()) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        String query = searchView.getText().toString();
        Log.i("Query", query + " page: " + page + 1);
        searchNextImage();
      }
    };
    searchImageContainer.getRecyclerView().addOnScrollListener(scrollListener);
  }

  public ViewGroup getRootView() {
    return rootView;
  }

  public ImageView getIconView() {
    return iconView;
  }

  public void toggleView(boolean toggled) {
    toggleSearch(toggled);
    toggleIcon(toggled);
    rootView.removeView(searchImageContainer.getRecyclerView());
    scrollListener.resetState();
  }

  private void toggleIcon(boolean toggled) {
    if (toggled) {
      iconView.getLayoutParams().width = (int) LayoutUtil.pxFromDp(context, ICON_VIEW_TOGGLED_SIZE);
      iconView.getLayoutParams().height = (int) LayoutUtil.pxFromDp(context, ICON_VIEW_TOGGLED_SIZE);
    } else {
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

//      Animation fadeIn = new AlphaAnimation(0, 1);
//      fadeIn.setInterpolator(new DecelerateInterpolator());
//      fadeIn.setDuration(1000);
//      searchView.setAnimation(fadeIn);
    } else {
      searchBtn.setVisibility(View.INVISIBLE);
      searchView.setVisibility(View.INVISIBLE);
      searchView.setWidth(0);
    }
    searchView.setText("");
  }

  private void searchImage(String query) {
    searchUtil = new Search(context, query, "image").build().execute(new JSONRequestCallback() {
      @Override
      public void completed(JSONObject jsonObject) {
        try {
          toggleSearch(false);

          JSONArray results = jsonObject.getJSONArray("items");
          List<SearchImage> searchImageResults = new ArrayList<>();

          for (int index = 0; index < results.length(); index++) {
            JSONObject thumbnailImage = results.getJSONObject(index).getJSONObject("image");
            final String thumbnailLink = results.getJSONObject(index).getString("link");
            final int height = thumbnailImage.getInt("thumbnailHeight");
            final int width = thumbnailImage.getInt("thumbnailWidth");
            Log.i("imageUrl " + index, thumbnailLink + " " + height + " " + width);

            searchImageResults.add(new SearchImage(thumbnailLink, width, height));
          }

          searchImageContainer.getAdapter().setSearchImageResults(searchImageResults);
          rootView.addView(searchImageContainer.getRecyclerView(), 0, searchImageContainerParams);
        } catch (Exception e) {

        }
      }

      @Override
      public void failed(Exception e) {

      }
    });
  }

  private void searchNextImage() {
    searchUtil.nextPage().build().execute(new JSONRequestCallback() {
      @Override
      public void completed(JSONObject jsonObject) {
        try {
          JSONArray results = jsonObject.getJSONArray("items");
          List<SearchImage> searchImageResults = searchImageContainer
              .getAdapter()
              .getSearchImageResults();

          for (int index = 0; index < results.length(); index++) {
            JSONObject thumbnailImage = results.getJSONObject(index).getJSONObject("image");
            final String thumbnailLink = results.getJSONObject(index).getString("link");
            final int height = thumbnailImage.getInt("thumbnailHeight");
            final int width = thumbnailImage.getInt("thumbnailWidth");
            Log.i("imageUrl " + index, thumbnailLink + " " + height + " " + width);

            searchImageResults.add(new SearchImage(thumbnailLink, width, height));
          }

          searchImageContainer.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {

        }
      }

      @Override
      public void failed(Exception e) {

      }
    });
  }
}
