package com.rawr.simple.search.image;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rawr.simple.layout.GridSpacingItemDecoration;
import com.rawr.simple.R;

import java.util.List;

public class SearchImageContainer {

  private final Context context;
  private final ImageView expandedImageView;
  private StaggeredGridLayoutManager layoutManager;
  private RecyclerView recyclerView;
  private SearchImageResultAdapter adapter;

  public SearchImageContainer(Context context, ImageView expandedImageView) {
    this.context = context;
    this.expandedImageView = expandedImageView;
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
    adapter = new SearchImageResultAdapter(context, expandedImageView);
    recyclerView.setAdapter(adapter);
  }

  public void addSearchImageResults(SearchImageResult searchImageResult) {
    adapter.addSearchImageResults(searchImageResult);
  }

  public void setSearchImageResults(List<SearchImageResult> searchImageResults) {
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
    adapter = new SearchImageResultAdapter(context, expandedImageView);
    recyclerView.setAdapter(adapter);
  }

  public RecyclerView getRecyclerView() {
    return recyclerView;
  }

  public StaggeredGridLayoutManager getLayoutManager() {
    return layoutManager;
  }
}
