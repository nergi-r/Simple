package com.rawr.simple.layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.rawr.simple.R;

public class SearchOptionsView extends LinearLayout {
  private Context context;
  private boolean isToggled;

  public SearchOptionsView(Context context) {
    super(context);
  }

  public SearchOptionsView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public SearchOptionsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void init(Context context) {
    this.context = context;
    isToggled = false;
  }

  public void toggle() {
    isToggled = !isToggled;
    if (isToggled) {
      Animation fadeIn = new AlphaAnimation(0, 1);
      fadeIn.setInterpolator(new DecelerateInterpolator());
      fadeIn.setDuration(500);
      this.setVisibility(VISIBLE);
      this.setAnimation(fadeIn);
    } else {
      Animation fadeOut = new AlphaAnimation(1, 0);
      fadeOut.setInterpolator(new AccelerateInterpolator());
      fadeOut.setDuration(500);
      this.setAnimation(fadeOut);
      this.setVisibility(INVISIBLE);
    }
  }
}
