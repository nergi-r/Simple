package com.rawr.simple.seach.image;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rawr.simple.layout.LayoutUtil;
import com.rawr.simple.R;

import java.util.ArrayList;
import java.util.List;

public class SearchImageResultAdapter extends RecyclerView.Adapter<SearchImageViewHolder> {
  private static final float SEARCH_IMAGE_DEFAULT_WIDTH = 135;

  private final Context context;
  private List<SearchImage> searchImageResults;

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
    final SearchImage searchImage = searchImageResults.get(position);
    final ImageView imageView = holder.getImageView();

    float scale = LayoutUtil.pxFromDp(context, SEARCH_IMAGE_DEFAULT_WIDTH) / searchImage.getWidth();
    float width = scale * searchImage.getWidth();
    float height = scale * searchImage.getHeight();
    imageView.getLayoutParams().width = (int) width;
    imageView.getLayoutParams().height = (int) height;

    Glide.with(context)
        .load(searchImage.getUrl())
        .thumbnail(Glide.with(context).load(R.raw.spin))
        .crossFade()
        .into(imageView);
  }

  @Override
  public int getItemCount() {
    return searchImageResults.size();
  }

  public List<SearchImage> getSearchImageResults() {
    return searchImageResults;
  }

  public void setSearchImageResults(List<SearchImage> searchImageResults) {
    reset();
    this.searchImageResults = searchImageResults;
    notifyDataSetChanged();
  }

  public void addSearchImageResults(SearchImage searchImage) {
    this.searchImageResults.add(searchImage);
    notifyItemInserted(searchImageResults.size());
  }

  public void reset() {
    searchImageResults.clear();
    notifyDataSetChanged();
  }
}
