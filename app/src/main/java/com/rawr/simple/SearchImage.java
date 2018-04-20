package com.rawr.simple;

/**
 * Created by Rawr on 4/18/2018.
 */

public class SearchImage {
  private String url;
  private int height;
  private int width;

  public SearchImage(String url, int height, int width) {
    this.url = url;
    this.height = height;
    this.width = width;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }
}
