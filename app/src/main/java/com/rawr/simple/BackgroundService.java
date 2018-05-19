package com.rawr.simple;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
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

  public BackgroundService() {

  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();

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
//    final Handler handler = new Handler();

    icon.setOnTouchListener(new View.OnTouchListener() {
      private int lastAction;
      private int initialX;
      private int initialY;
      private float initialTouchX;
      private float initialTouchY;

//      final Runnable longPressed = new Runnable() {
//        public void run() {
//          lastAction = -1;
//        }
//      };

      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {

          case MotionEvent.ACTION_DOWN:
            initialX = params.x;
            initialY = params.y;
            initialTouchX = motionEvent.getRawX();
            initialTouchY = motionEvent.getRawY();

//            handler.postDelayed(longPressed, ViewConfiguration.getLongPressTimeout());

            lastAction = motionEvent.getAction();
            return true;

          case MotionEvent.ACTION_UP:
            if (lastAction == MotionEvent.ACTION_DOWN) {
              isFocused = !isFocused;

              if (isFocused) params.flags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
              else params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

              floatingActionButton.toggleView(isFocused);
              windowManager.updateViewLayout(rootView, params);
            }

//            handler.removeCallbacks(longPressed);

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
//              handler.removeCallbacks(longPressed);

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
      final ImageView expandedImageView = floatingActionButton.getExpandedImageView();
      if (expandedImageView.getVisibility() == View.VISIBLE) {
        expandedImageView.animate().alpha(0).setDuration(250)
            .setInterpolator(new DecelerateInterpolator())
            .withEndAction(new Runnable() {
              @Override
              public void run() {
                expandedImageView.getLayoutParams().width = 0;
                expandedImageView.getLayoutParams().height = 0;
                expandedImageView.setVisibility(View.INVISIBLE);
                // since animate does not have imageAlpha,
                // need to revert back alpha using the deprecated setAlpha function
                expandedImageView.setAlpha((float)1.0);
                expandedImageView.setImageDrawable(null);
              }
            });
        return;
      }
      isFocused = false;
      params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
      floatingActionButton.toggleView(false);
      windowManager.updateViewLayout(rootView, params);
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (rootView != null) windowManager.removeView(rootView);
  }
}
