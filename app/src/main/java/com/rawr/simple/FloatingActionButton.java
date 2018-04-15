package com.rawr.simple;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

public class FloatingActionButton {
  private static final float ICON_VIEW_SIZE = 50;
  private static final float ICON_VIEW_TOGGLED_SIZE = 60;
  private static final float SEARCH_VIEW_SIZE = 280;

  private final Context context;
  private final View rootView;
  private final ImageView iconView;
  private final AutoCompleteTextView searchView;
  private final Button searchBtn;

  public FloatingActionButton(Context context) {
    this.context = context;
    rootView = LayoutInflater.from(context).inflate(R.layout.layout_fab, null);
    iconView = rootView.findViewById(R.id.imageView);
    searchView = rootView.findViewById(R.id.autoCompleteTextView);
    searchBtn = rootView.findViewById(R.id.searchButton);

    searchView.setVisibility(View.INVISIBLE);
    searchBtn.setVisibility(View.INVISIBLE);

    searchBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

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
    if(toggled) {
      iconView.getLayoutParams().width = (int) Util.pxFromDp(context, ICON_VIEW_TOGGLED_SIZE);
      iconView.getLayoutParams().height = (int) Util.pxFromDp(context, ICON_VIEW_TOGGLED_SIZE);
    }
    else {
      iconView.getLayoutParams().width = (int) Util.pxFromDp(context, ICON_VIEW_SIZE);
      iconView.getLayoutParams().height = (int) Util.pxFromDp(context, ICON_VIEW_SIZE);
    }
  }

  private void toggleSearch(boolean toggled) {
    if(toggled) {
      searchBtn.setVisibility(View.VISIBLE);
      searchView.setVisibility(View.VISIBLE);
      searchView.setWidth((int) Util.pxFromDp(context, SEARCH_VIEW_SIZE));

      Animation fadeIn = new AlphaAnimation(0, 1);
      fadeIn.setInterpolator(new DecelerateInterpolator());
      fadeIn.setDuration(1000);
      searchView.setAnimation(fadeIn);
    }
    else {
      searchBtn.setVisibility(View.INVISIBLE);
      searchView.setVisibility(View.INVISIBLE);
      searchView.setWidth(0);
    }
    searchView.setText("");
  }
}
