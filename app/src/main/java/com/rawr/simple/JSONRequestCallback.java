package com.rawr.simple;

import org.json.JSONObject;

public interface JSONRequestCallback {
  public void completed(JSONObject jsonObject);

  public void failed(Exception e);
}
