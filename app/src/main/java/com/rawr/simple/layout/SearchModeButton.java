package com.rawr.simple.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rawr.simple.R;

public class SearchModeButton extends ImageViewOpacity {
  public Context context;

  public SearchModeButton(Context context) {
    super(context);
  }

  public SearchModeButton(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public SearchModeButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void init(Context context) {
    this.context = context;
  }

  public void setToLoading() {
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        (int) LayoutUtil.pxFromDp(context, 40),
        (int) LayoutUtil.pxFromDp(context, 40));
    params.rightMargin = 0;
    params.topMargin = (int) LayoutUtil.pxFromDp(context, -5);
    this.setLayoutParams(params);
    Glide.with(context)
        .load(R.raw.spin)
        .asGif()
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(this);
  }

  public void setToImageSearch() {
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        (int) LayoutUtil.pxFromDp(context, 25),
        (int) LayoutUtil.pxFromDp(context, 25));
    params.rightMargin = (int) LayoutUtil.pxFromDp(context, 3);
    params.topMargin = (int) LayoutUtil.pxFromDp(context, 3);
    this.setLayoutParams(params);

    Glide.with(context)
        .load(android.R.drawable.ic_search_category_default)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(this);
  }
}
