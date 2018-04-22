package com.rawr.simple.layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

public class BackButtonAwareRelativeLayout extends RelativeLayout {

  public interface BackButtonListener {
    void onBackButtonPressed();
  }

  @Nullable
  private BackButtonListener backButtonListener;

  public BackButtonAwareRelativeLayout(Context context) {
    super(context);
  }

  public BackButtonAwareRelativeLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public BackButtonAwareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setBackButtonListener(@Nullable BackButtonListener listener) {
    backButtonListener = listener;
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_BACK
        && backButtonListener != null) {
      backButtonListener.onBackButtonPressed();
      return true;
    }
    return super.dispatchKeyEvent(event);
  }
}
