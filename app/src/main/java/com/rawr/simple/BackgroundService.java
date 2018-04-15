package com.rawr.simple;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class BackgroundService extends Service {

  private View rootView;
  private WindowManager windowManager;

  public BackgroundService() {

  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();

    final FloatingActionButton floatingActionButton = new FloatingActionButton(this);
    rootView = floatingActionButton.getRootView();
    final ImageView icon = floatingActionButton.getIconView();

    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_PHONE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT);

    params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    params.gravity = Gravity.TOP | Gravity.LEFT;
    params.x = 0;
    params.y = 100;

    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    windowManager.addView(rootView, params);

//    final Handler handler = new Handler();

    icon.setOnTouchListener(new View.OnTouchListener() {
      private int lastAction;
      private int initialX;
      private int initialY;
      private float initialTouchX;
      private float initialTouchY;
      private boolean focused;

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
              focused = !focused;

              if(focused) params.flags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
              else params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

              floatingActionButton.toggleView(focused);
              windowManager.updateViewLayout(rootView, params);
            }

//            handler.removeCallbacks(longPressed);

            lastAction = motionEvent.getAction();
            return true;

          case MotionEvent.ACTION_MOVE:
            // Consider displacement because touch on real device always triggers ACTION_MOVE
            int displacementX = Math.abs((int) (motionEvent.getRawX() - initialTouchX));
            int displacementY = Math.abs((int) (motionEvent.getRawY() - initialTouchY));

            if(displacementX >= 1 || displacementY >= 1) {
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
  public void onDestroy() {
    super.onDestroy();
    if (rootView != null) windowManager.removeView(rootView);
  }
}
