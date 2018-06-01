package com.rawr.simple.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("AppCompatCustomView")
public class CloseArea extends ImageView {
  private Context context;

  public CloseArea(Context context) {
    super(context);
  }

  public CloseArea(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public CloseArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void init(Context context) {
    this.context = context;
  }

  public boolean isCollideWith(View view) {
    boolean isCollide = getRectFrom(this).intersect(getRectFrom(view));
    handleCollision(isCollide);
    return isCollide;
  }

  private Rect getRectFrom(View v) {
    int[] locations = new int[2];
    v.getLocationOnScreen(locations);
    Rect r = new Rect(
        locations[0],
        locations[1],
        locations[0] + v.getWidth() * 3 / 4,
        locations[1] + v.getHeight() / 2);
    return r;
  }

  private void handleCollision(boolean isCollide) {
    ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
    if (isCollide) {
      layoutParams.width = (int) LayoutUtil.pxFromDp(context, 90);
      layoutParams.height = (int) LayoutUtil.pxFromDp(context, 90);
      this.setLayoutParams(layoutParams);
    } else {
      layoutParams.width = (int) LayoutUtil.pxFromDp(context, 75);
      layoutParams.height = (int) LayoutUtil.pxFromDp(context, 75);
      this.setLayoutParams(layoutParams);
    }
  }
}
