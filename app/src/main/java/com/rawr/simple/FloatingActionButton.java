package com.rawr.simple;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class FloatingActionButton {
  private static final float ICON_VIEW_SIZE = 50;
  private static final float ICON_VIEW_TOGGLED_SIZE = 60;
  private static final float SEARCH_VIEW_SIZE = 280;

  private final Context context;
  private final View rootView;
  private final ImageView iconView;
  private final AutoCompleteTextView searchView;
  private final Button searchBtn;

  public FloatingActionButton(final Context context) {
    this.context = context;
    rootView = LayoutInflater.from(context).inflate(R.layout.layout_fab, null);
    iconView = rootView.findViewById(R.id.imageView);
    searchView = rootView.findViewById(R.id.autoCompleteTextView);
    searchBtn = rootView.findViewById(R.id.searchButton);

    searchView.setVisibility(View.INVISIBLE);
    searchBtn.setVisibility(View.INVISIBLE);

    searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        Log.i("a", i + " " + keyEvent);
        return false;
      }
    });

    searchBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String query = searchView.getText().toString();
        Log.i("Query", query);

        new Search(context, query, "image").build().execute(new JSONRequestCallback() {
          @Override
          public void completed(JSONObject jsonObject) {
            try {
              JSONArray results = jsonObject.getJSONArray("items");

              for(int index = 0; index < results.length(); index++) {
                JSONObject thumbnailImage = results.getJSONObject(index).getJSONObject("image");
                String thumbnailLink = thumbnailImage.getString("thumbnailLink");
                Log.i("imageUrl " + index, thumbnailLink);
              }

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

  public View getRootView() {
    return rootView;
  }

  public ImageView getIconView() {
    return iconView;
  }

  public void toggleView(boolean toggled) {
    toggleIcon(toggled);
    toggleSearch(toggled);
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
