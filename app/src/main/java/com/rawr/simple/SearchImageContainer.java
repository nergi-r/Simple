package com.rawr.simple;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchImageContainer {

  private final Context context;
  private final RecyclerView recyclerView;
  private final SearchImageResultAdapter adapter;

  public SearchImageContainer(Context context) {
    this.context = context;

    final RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    recyclerView = (RecyclerView) LayoutInflater
                                      .from(context)
                                      .inflate(R.layout.layout_image_container, null)
                                      .findViewById(R.id.rv_image_container);
    ((ViewGroup) recyclerView.getParent()).removeView(recyclerView);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(layoutManager);
    adapter = new SearchImageResultAdapter(context);
    recyclerView.setAdapter(adapter);
  }

  public RecyclerView getRecyclerView() {
    return recyclerView;
  }

  public SearchImageResultAdapter getAdapter() {
    return adapter;
  }
}
