package com.rawr.simple.seach.image;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rawr.simple.layout.GridSpacingItemDecoration;
import com.rawr.simple.R;

import java.util.List;

public class SearchImageContainer {

  private final Context context;
  private StaggeredGridLayoutManager layoutManager;
  private RecyclerView recyclerView;
  private SearchImageResultAdapter adapter;

  public SearchImageContainer(Context context) {
    this.context = context;
    layoutManager = new StaggeredGridLayoutManager(
        2, StaggeredGridLayoutManager.VERTICAL);
    layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

    recyclerView = LayoutInflater
        .from(context)
        .inflate(R.layout.layout_image_container, null)
        .findViewById(R.id.rv_image_container);
    ((ViewGroup) recyclerView.getParent()).removeView(recyclerView);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 25, true, 0));
    recyclerView.setItemAnimator(null);
    adapter = new SearchImageResultAdapter(context);
    recyclerView.setAdapter(adapter);
  }

  public void addSearchImageResults(SearchImage searchImage) {
    adapter.addSearchImageResults(searchImage);
  }

  public void setSearchImageResults(List<SearchImage> searchImageResults) {
    adapter.setSearchImageResults(searchImageResults);
  }

  public void reset() {
    recyclerView.getRecycledViewPool().clear();
    adapter.reset();
    recyclerView.setAdapter(null);
    recyclerView.setLayoutManager(null);

    layoutManager = new StaggeredGridLayoutManager(
        2, StaggeredGridLayoutManager.VERTICAL);
    layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

    recyclerView = LayoutInflater
        .from(context)
        .inflate(R.layout.layout_image_container, null)
        .findViewById(R.id.rv_image_container);
    ((ViewGroup) recyclerView.getParent()).removeView(recyclerView);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 25, true, 0));
    recyclerView.setItemAnimator(null);
    adapter = new SearchImageResultAdapter(context);
    recyclerView.setAdapter(adapter);
  }

  public RecyclerView getRecyclerView() {
    return recyclerView;
  }

  public StaggeredGridLayoutManager getLayoutManager() {
    return layoutManager;
  }
}
