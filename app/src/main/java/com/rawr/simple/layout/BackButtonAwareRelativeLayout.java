package com.rawr.simple.layout;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

public class BackButtonAwareRelativeLayout extends RelativeLayout {

  private final Handler handler = new Handler();
  private final Runnable runnable = new Runnable() {
    @Override
    public void run() {
      backButtonEnabled = true;
    }
  };
  private boolean backButtonEnabled = true;

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
    if (event != null
        && event.getKeyCode() == KeyEvent.KEYCODE_BACK
        && backButtonEnabled
        && backButtonListener != null) {
      backButtonListener.onBackButtonPressed();
      backButtonEnabled = false;
      handler.postDelayed(runnable, 200);
      return true;
    }
    return super.dispatchKeyEvent(event);
  }
}
