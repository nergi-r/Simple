package com.rawr.simple;

import android.content.Context;

public class LayoutUtil {
  public static final int SCREEN_WIDTH = 300;

  public static float dpFromPx(final Context context, final float px) {
    return px / context.getResources().getDisplayMetrics().density;
  }

  public static float pxFromDp(final Context context, final float dp) {
    return dp * context.getResources().getDisplayMetrics().density;
  }
}
