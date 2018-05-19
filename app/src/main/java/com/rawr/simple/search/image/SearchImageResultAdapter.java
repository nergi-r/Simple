package com.rawr.simple.search.image;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rawr.simple.layout.LayoutUtil;
import com.rawr.simple.R;

import java.util.ArrayList;
import java.util.List;

public class SearchImageResultAdapter extends RecyclerView.Adapter<SearchImageViewHolder> {
  private static final float SEARCH_IMAGE_DEFAULT_WIDTH = 135;

  private final Context context;
  private List<SearchImageResult> searchImageResults;

  public SearchImageResultAdapter(Context context) {
    this.context = context;
    searchImageResults = new ArrayList<>();
  }

  @Override
  public SearchImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final Context context = parent.getContext();
    final View imageViewParent = LayoutInflater
        .from(context)
        .inflate(R.layout.layout_image, parent, false);

    return new SearchImageViewHolder(imageViewParent);
  }

  @Override
  public void onBindViewHolder(SearchImageViewHolder holder, int position) {
    final SearchImageResult searchImageResult = searchImageResults.get(position);
    final ImageView imageView = holder.getImageView();

    float scale = LayoutUtil.pxFromDp(context, SEARCH_IMAGE_DEFAULT_WIDTH) / searchImageResult.getWidth();
    float width = scale * searchImageResult.getWidth();
    float height = scale * searchImageResult.getHeight();
    imageView.getLayoutParams().width = (int) width;
    imageView.getLayoutParams().height = (int) height;

    Glide.with(context)
        .load(searchImageResult.getUrl())
        .thumbnail(Glide.with(context).load(R.raw.spin))
        .crossFade()
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imageView);
  }

  @Override
  public int getItemCount() {
    return searchImageResults.size();
  }

  public List<SearchImageResult> getSearchImageResults() {
    return searchImageResults;
  }

  public void setSearchImageResults(List<SearchImageResult> searchImageResults) {
    reset();
    this.searchImageResults = searchImageResults;
    notifyDataSetChanged();
  }

  public void addSearchImageResults(SearchImageResult searchImageResult) {
    this.searchImageResults.add(searchImageResult);
    notifyItemInserted(searchImageResults.size());
  }

  public void reset() {
    Glide.get(context).clearMemory();
    searchImageResults.clear();
    notifyDataSetChanged();
  }
}