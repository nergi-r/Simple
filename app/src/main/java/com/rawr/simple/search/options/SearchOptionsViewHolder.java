package com.rawr.simple.search.options;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rawr.simple.R;
import com.rawr.simple.layout.ImageViewOpacity;

public class SearchOptionsViewHolder extends RecyclerView.ViewHolder {
  private ImageViewOpacity imageView;

  public SearchOptionsViewHolder(View itemView) {
    super(itemView);
    this.imageView = itemView.findViewById(R.id.search_options_view_item);
  }

  public ImageViewOpacity getImageView() {
    return imageView;
  }

  public SearchOptionsViewHolder setImageView(ImageViewOpacity imageView) {
    this.imageView = imageView;
    return this;
  }
}
