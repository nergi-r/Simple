package com.rawr.simple.search.image;

public class SearchImageResultAttributes {
  private String url;
  private int width;
  private int height;

  public SearchImageResultAttributes() {
  }

  public SearchImageResultAttributes(String url, int width, int height) {
    this.url = url;
    this.width = width;
    this.height = height;
  }

  public String getUrl() {
    return url;
  }

  public SearchImageResultAttributes setUrl(String url) {
    this.url = url;
    return this;
  }

  public int getWidth() {
    return width;
  }

  public SearchImageResultAttributes setWidth(int width) {
    this.width = width;
    return this;
  }

  public int getHeight() {
    return height;
  }

  public SearchImageResultAttributes setHeight(int height) {
    this.height = height;
    return this;
  }
}
