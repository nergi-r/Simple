package com.rawr.simple.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rawr.simple.JSONRequestCallback;
import com.rawr.simple.VolleyRequestQueue;

import org.json.JSONObject;

public class Suggestion {

  private final Context context;
  private final String query;
  private String url;

  public Suggestion(Context context, String query) {
    this.context = context;
    this.query = query;
  }

  public Suggestion build() {
    Uri.Builder uriBuilder = new Uri.Builder()
        .scheme("https")
        .encodedAuthority("simple-backend-api.herokuapp.com")
        .appendPath("api")
        .appendPath("search")
        .appendPath("suggestion")
        .appendQueryParameter("mkt", "en-id")
        .appendQueryParameter("q", query);

    this.url = uriBuilder.build().toString();

    return this;
  }

  public Suggestion execute(final JSONRequestCallback callback) {
    Log.i("Search", "executed with url: " + url);

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
        Request.Method.GET,
        url,
        null,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            callback.completed(response);
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            callback.failed(error);
          }
        });

    VolleyRequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);

    return this;
  }
}
