package com.rawr.simple;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.rawr.simple.layout.BackButtonAwareRelativeLayout;

public class BackgroundService extends Service
    implements BackButtonAwareRelativeLayout.BackButtonListener {

  private FloatingActionButton floatingActionButton;
  private BackButtonAwareRelativeLayout rootView;
  private WindowManager windowManager;
  private WindowManager.LayoutParams params;
  private boolean isFocused;
  private boolean isLongPressed;
  private static Dialog dialog;

  public BackgroundService() {

  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public void onCreate() {
    super.onCreate();

    this.initDialog();
    floatingActionButton = new FloatingActionButton(this);
    rootView = floatingActionButton.getRootView();
    rootView.setBackButtonListener(this);

    final ImageView icon = floatingActionButton.getIconView();

    params = new WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_PHONE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT);

    params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    params.gravity = Gravity.TOP | Gravity.START;
    params.x = 0;
    params.y = 100;

    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    if (windowManager != null) {
      windowManager.addView(rootView, params);
    }
    final Handler handler = new Handler();

    icon.setOnTouchListener(new View.OnTouchListener() {
      private int lastAction;
      private int initialX;
      private int initialY;
      private float initialTouchX;
      private float initialTouchY;

      final Runnable longPressed = new Runnable() {
        public void run() {
          if (lastAction == MotionEvent.ACTION_UP || isLongPressed || isFocused) return;
          isLongPressed = true;
          isFocused = !isFocused;

          if (isFocused) params.flags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
          else params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

          floatingActionButton.getSearchView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              stopSelf();
            }
          });
          floatingActionButton.showCloseOption(true);
          windowManager.updateViewLayout(rootView, params);
        }
      };

      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {

          case MotionEvent.ACTION_DOWN:
            initialX = params.x;
            initialY = params.y;
            initialTouchX = motionEvent.getRawX();
            initialTouchY = motionEvent.getRawY();

            if (!isFocused) {
              handler.postDelayed(longPressed, ViewConfiguration.getLongPressTimeout());
            }

            lastAction = motionEvent.getAction();
            return true;

          case MotionEvent.ACTION_UP:
            if (!isLongPressed && lastAction == MotionEvent.ACTION_DOWN) {
              isFocused = !isFocused;

              if (isFocused) params.flags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
              else params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

              floatingActionButton.toggleView(isFocused);
              windowManager.updateViewLayout(rootView, params);
            }

            handler.removeCallbacks(longPressed);
            isLongPressed = false;

            lastAction = motionEvent.getAction();
            return true;

          case MotionEvent.ACTION_MOVE:
            // Consider displacement because touch on real device always triggers ACTION_MOVE
            int displacementX = Math.abs((int) (motionEvent.getRawX() - initialTouchX));
            int displacementY = Math.abs((int) (motionEvent.getRawY() - initialTouchY));

            if (displacementX >= 1 || displacementY >= 1) {
              params.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);
              params.y = initialY + (int) (motionEvent.getRawY() - initialTouchY);

              windowManager.updateViewLayout(rootView, params);
              handler.removeCallbacks(longPressed);

              lastAction = motionEvent.getAction();
            }

            return true;
        }

        return false;
      }
    });
  }

  @Override
  public void onBackButtonPressed() {
    if (isFocused) {
      isFocused = false;
      params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
      floatingActionButton.toggleView(false);
      windowManager.updateViewLayout(rootView, params);
    }
  }

  private void initDialog() {
    dialog = new Dialog(getApplicationContext(),
        android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.layout_expand_image);
    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    dialog.getWindow().setDimAmount(0.7f);
  }

  public static Dialog getDialog() {
    return dialog;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (rootView != null) windowManager.removeView(rootView);
  }
}
