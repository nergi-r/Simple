package com.rawr.simple.seach.image;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rawr.simple.GridSpacingItemDecoration;
import com.rawr.simple.R;

public class SearchImageContainer {

  private final Context context;
  private final StaggeredGridLayoutManager layoutManager;
  private final RecyclerView recyclerView;
  private final SearchImageResultAdapter adapter;

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
    adapter = new SearchImageResultAdapter(context);
    recyclerView.setAdapter(adapter);
    recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 25, true, 0));
    recyclerView.setItemAnimator(null);
  }

  public RecyclerView getRecyclerView() {
    return recyclerView;
  }

  public SearchImageResultAdapter getAdapter() {
    return adapter;
  }

  public StaggeredGridLayoutManager getLayoutManager() {
    return layoutManager;
  }
}
