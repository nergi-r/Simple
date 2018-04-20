package com.rawr.simple;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FloatingActionButton {
  private static final float ICON_VIEW_SIZE = 50;
  private static final float ICON_VIEW_TOGGLED_SIZE = 60;
  private static final float SEARCH_VIEW_SIZE = 280;

  private final Context context;
  private final ViewGroup rootView;
  private final ImageView iconView;
  private final AutoCompleteTextView searchView;
  private final Button searchBtn;

  private final SearchImageContainer searchImageContainer;
  private final RelativeLayout.LayoutParams searchImageContainerParams;

  public FloatingActionButton(final Context context) {
    this.context = context;
    rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.layout_fab, null);
    iconView = rootView.findViewById(R.id.imageView);
    searchView = rootView.findViewById(R.id.autoCompleteTextView);
    searchBtn = rootView.findViewById(R.id.searchButton);

    searchView.setVisibility(View.INVISIBLE);
    searchBtn.setVisibility(View.INVISIBLE);

    searchImageContainer = new SearchImageContainer(context);

    searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        Log.i("a", i + " " + keyEvent);
        return false;
      }
    });

    final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
    final int height = displayMetrics.heightPixels;
    final int width = displayMetrics.widthPixels;

    searchImageContainerParams = new RelativeLayout.LayoutParams(
        width - 150,
        Math.max(width - 150, height - 800));
    searchImageContainerParams.leftMargin = 70;
    searchImageContainerParams.topMargin = 70;

    searchBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String query = searchView.getText().toString();
        Log.i("Query", query);

        new Search(context, query, "image").build().execute(new JSONRequestCallback() {
          @Override
          public void completed(JSONObject jsonObject) {
            try {
              toggleSearch(false);

              JSONArray results = jsonObject.getJSONArray("items");
              List<SearchImage> searchImageResults = new ArrayList<>();

              for (int index = 0; index < results.length(); index++) {
                JSONObject thumbnailImage = results.getJSONObject(index).getJSONObject("image");
                final String thumbnailLink = thumbnailImage.getString("thumbnailLink");
                final int height = thumbnailImage.getInt("thumbnailHeight");
                final int width = thumbnailImage.getInt("thumbnailWidth");
                Log.i("imageUrl " + index, thumbnailLink + " " + height + " " + width);

                searchImageResults.add(new SearchImage(thumbnailLink, height, width));
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
    });
  }

  public ViewGroup getRootView() {
    return rootView;
  }

  public ImageView getIconView() {
    return iconView;
  }

  public void toggleView(boolean toggled) {
    toggleIcon(toggled);
    toggleSearch(toggled);
    rootView.removeView(searchImageContainer.getRecyclerView());
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
      searchBtn.setVisibility(View.VISIBLE);
      searchView.setVisibility(View.VISIBLE);
      searchView.setWidth((int) LayoutUtil.pxFromDp(context, SEARCH_VIEW_SIZE));

      Animation fadeIn = new AlphaAnimation(0, 1);
      fadeIn.setInterpolator(new DecelerateInterpolator());
      fadeIn.setDuration(1000);
      searchView.setAnimation(fadeIn);
    } else {
      searchBtn.setVisibility(View.INVISIBLE);
      searchView.setVisibility(View.INVISIBLE);
      searchView.setWidth(0);
    }
    searchView.setText("");
  }
}
