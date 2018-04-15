package com.rawr.simple;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
    final View rootView = floatingActionButton.getRootView();
    final ImageView icon = floatingActionButton.getIconView();

    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_PHONE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT);

    params.gravity = Gravity.TOP | Gravity.LEFT;
    params.x = 0;
    params.y = 100;

    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    windowManager.addView(rootView, params);

    icon.setOnTouchListener(new View.OnTouchListener() {
      private int lastAction;
      private int initialX;
      private int initialY;
      private float initialTouchX;
      private float initialTouchY;
      private boolean focused;

      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {

          case MotionEvent.ACTION_DOWN:

            initialX = params.x;
            initialY = params.y;

            initialTouchX = motionEvent.getRawX();
            initialTouchY = motionEvent.getRawY();

            lastAction = motionEvent.getAction();
            return true;

          case MotionEvent.ACTION_UP:
            int displacementX = Math.abs((int) (motionEvent.getRawX() - initialTouchX));
            int displacementY = Math.abs((int) (motionEvent.getRawY() - initialTouchY));

            if (lastAction == MotionEvent.ACTION_DOWN
                || (displacementX < 1 && displacementY < 1)) {
              focused = !focused;

              if(focused) {
                params.flags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
              }
              else {
                params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
              }
              floatingActionButton.toggleView(focused);
              windowManager.updateViewLayout(rootView, params);
            }

            lastAction = motionEvent.getAction();
            return true;

          case MotionEvent.ACTION_MOVE:
            params.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);
            params.y = initialY + (int) (motionEvent.getRawY() - initialTouchY);

            windowManager.updateViewLayout(rootView, params);
            lastAction = motionEvent.getAction();
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
