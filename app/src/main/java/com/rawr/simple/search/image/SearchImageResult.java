package com.rawr.simple.search.image;

public class SearchImageResult {
  private SearchImageResultAttributes thumbnail;
  private SearchImageResultAttributes content;

  public SearchImageResult() {
  }

  public SearchImageResult(SearchImageResultAttributes thumbnail, SearchImageResultAttributes content) {
    this.thumbnail = thumbnail;
    this.content = content;
  }

  public SearchImageResultAttributes getThumbnail() {
    return thumbnail;
  }

  public SearchImageResult setThumbnail(SearchImageResultAttributes thumbnail) {
    this.thumbnail = thumbnail;
    return this;
  }

  public SearchImageResultAttributes getContent() {
    return content;
  }

  public SearchImageResult setContent(SearchImageResultAttributes content) {
    this.content = content;
    return this;
  }
}
