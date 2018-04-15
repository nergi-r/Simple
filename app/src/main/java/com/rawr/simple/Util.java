package com.rawr.simple;

import android.content.Context;

/**
 * Created by Rawr on 4/15/2018.
 */

public class Util {
  public static float dpFromPx(final Context context, final float px) {
    return px / context.getResources().getDisplayMetrics().density;
  }

  public static float pxFromDp(final Context context, final float dp) {
    return dp * context.getResources().getDisplayMetrics().density;
  }
}
