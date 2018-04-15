package com.rawr.simple;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRequestQueue {

  private static VolleyRequestQueue instance;
  private RequestQueue requestQueue;
  private static Context context;

  private VolleyRequestQueue(Context context) {
    this.context = context;
    requestQueue = getRequestQueue();
  }

  public static synchronized VolleyRequestQueue getInstance(Context context) {
    if(instance == null) instance = new VolleyRequestQueue(context);
    return instance;
  }

  public RequestQueue getRequestQueue() {
    if(requestQueue == null) requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    return requestQueue;
  }

  public <T> void addToRequestQueue(Request<T> request) {
    getRequestQueue().add(request);
  }
}
