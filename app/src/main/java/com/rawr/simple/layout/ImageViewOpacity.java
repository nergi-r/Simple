package com.rawr.simple.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class ImageViewOpacity extends ImageView {
  public ImageViewOpacity(Context context) {
    super(context);
  }

  public ImageViewOpacity(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public ImageViewOpacity(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public void setPressed(boolean pressed) {
    super.setPressed(pressed);
    setAlpha(pressed ? 0.5f : 1.0f);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
      setPressed(true);
    } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
      setPressed(false);
      float x = event.getX();
      float y = event.getY();
      boolean isInside = (x > 0 && x < getWidth() && y > 0 && y < getHeight());
      if(isInside){
        performClick();
      }
    }
    return true;
  }
}
