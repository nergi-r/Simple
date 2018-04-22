package com.rawr.simple.layout;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView
 */

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
  // Sets the starting page index
  private final int startingPageIndex = 0;
  // The minimum amount of items to have below your current scroll position
  // before loading more.
  private int visibleThreshold = 5;
  // The current offset index of data you have loaded
  private int currentPage = 0;
  // The total number of items in the dataset after the last load
  private int previousTotalItemCount = 0;
  // True if we are still waiting for the last set of data to load.
  private boolean loading = true;

  private StaggeredGridLayoutManager layoutManager;

  public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
    this.layoutManager = layoutManager;
    visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
  }

  public int getLastVisibleItem(int[] lastVisibleItemPositions) {
    int maxSize = 0;
    for (int i = 0; i < lastVisibleItemPositions.length; i++) {
      if (i == 0) maxSize = lastVisibleItemPositions[0];
      else if (lastVisibleItemPositions[i] > maxSize) maxSize = lastVisibleItemPositions[i];
    }
    return maxSize;
  }

  @Override
  public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    int lastVisibleItemPosition = getLastVisibleItem(
        layoutManager.findLastVisibleItemPositions(null));
    int totalItemCount = layoutManager.getItemCount();

    // If the total item count is zero and the previous isn't, assume the
    // list is invalidated and should be reset back to initial state
    if (totalItemCount < previousTotalItemCount) {
      currentPage = startingPageIndex;
      previousTotalItemCount = totalItemCount;
      if (totalItemCount == 0) loading = true;
    }

    if (loading && totalItemCount > previousTotalItemCount) {
      loading = false;
      previousTotalItemCount = totalItemCount;
    }

    if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
      currentPage++;
      onLoadMore(currentPage, totalItemCount, recyclerView);
      loading = true;
    }
  }

  public void reset() {
    layoutManager = null;
    currentPage = startingPageIndex;
    previousTotalItemCount = 0;
    loading = true;
  }

  public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);
}
