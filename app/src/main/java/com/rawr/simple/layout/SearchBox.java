package com.rawr.simple.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

@SuppressLint("AppCompatCustomView")
public class SearchBox extends AutoCompleteTextView {
  private static final float WIDTH = 280;
  private Context context;
  private ViewGroup rootView;

  public SearchBox(Context context) {
    super(context);
  }

  public SearchBox(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SearchBox(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void init(Context context, ViewGroup rootView) {
    this.context = context;
    this.rootView = rootView;
    this.setVisibility(INVISIBLE);
  }

  public void toggle(boolean toggled) {
    if (toggled) {
      this.setEnabled(true);
      this.setVisibility(View.VISIBLE);
      TransitionManager.beginDelayedTransition(rootView);
      this.setWidth((int) LayoutUtil.pxFromDp(context, WIDTH));
    } else {
      this.setVisibility(View.INVISIBLE);
      this.setWidth(0);
    }
    this.setText("");
  }
}
