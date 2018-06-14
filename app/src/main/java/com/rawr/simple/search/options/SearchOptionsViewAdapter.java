package com.rawr.simple.search.options;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rawr.simple.R;
import com.rawr.simple.layout.ImageViewOpacity;

import java.util.List;

public class SearchOptionsViewAdapter extends RecyclerView.Adapter<SearchOptionsViewHolder> {
  private final Context context;
  private final List<Integer> list;

  public SearchOptionsViewAdapter(Context context, List<Integer> list) {
    this.context = context;
    this.list = list;
  }

  @Override
  public SearchOptionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final Context context = parent.getContext();
    final View imageViewParent = LayoutInflater
        .from(context)
        .inflate(R.layout.layout_search_options_view_item, parent, false);

    return new SearchOptionsViewHolder(imageViewParent);
  }

  @Override
  public void onBindViewHolder(SearchOptionsViewHolder holder, int position) {
    final ImageViewOpacity imageViewOpacity = holder.getImageView();
    Glide.with(context)
        .load(list.get(position))
        .crossFade()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imageViewOpacity);
  }

  @Override
  public void onViewDetachedFromWindow(SearchOptionsViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    holder.getImageView().setOnClickListener(null);
  }

  @Override
  public int getItemCount() {
    return list.size();
  }
}
