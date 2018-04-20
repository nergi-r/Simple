package com.rawr.simple;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SearchImageResultAdapter extends RecyclerView.Adapter<SearchImageViewHolder> {
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

    final double scale = (LayoutUtil.SCREEN_WIDTH / 2.0) / searchImage.getWidth();
    final float width = (float) (scale * searchImage.getWidth());
    final float height = (float) (scale * searchImage.getHeight());
    System.out.println(height + " " + width);

    imageView.getLayoutParams().width = (int) LayoutUtil.pxFromDp(context, width);
    imageView.getLayoutParams().height = (int) LayoutUtil.pxFromDp(context, height);

    Glide.with(context)
        .load(searchImage.getUrl())
        .placeholder(android.R.drawable.ic_menu_compass)
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
    this.searchImageResults.clear();
    this.searchImageResults = searchImageResults;
    notifyDataSetChanged();
  }
}
