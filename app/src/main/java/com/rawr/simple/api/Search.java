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

public class Search {

  private final Context context;
  private String query;
  private String searchType;
  private int startAt;
  private String url;

  public Search(Context context) {
    this.context = context;
    this.startAt = 0;
  }

  public Search setQuery(String query) {
    this.query = query;
    return this;
  }

  public Search setSearchType(String searchType) {
    this.searchType = searchType;
    return this;
  }

  public Search build() {
    Uri.Builder uriBuilder = new Uri.Builder()
        .scheme("https")
        .encodedAuthority("simple-backend-api.herokuapp.com")
        .appendPath("api")
        .appendPath("search")
        .appendPath("image")
        .appendQueryParameter("mkt", "en-id")
        .appendQueryParameter("q", query)
        .appendQueryParameter("offset", String.valueOf(startAt));

    this.url = uriBuilder.build().toString();

    return this;
  }

  public Search execute(final JSONRequestCallback callback) {
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

  public Search nextPage(int offset) {
    startAt += offset;
    return this;
  }

  public Search reset(String query, String searchType) {
    this.query = query;
    this.searchType = searchType;
    startAt = 0;
    return this;
  }
}
