package com.rawr.simple;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class SearchImageViewHolder extends RecyclerView.ViewHolder  {

  private ImageView imageView;

  public SearchImageViewHolder(View itemView) {
    super(itemView);
    imageView = itemView.findViewById(R.id.imageView2);
  }

  public ImageView getImageView() {
    return imageView;
  }

  public void setImageView(ImageView imageView) {
    this.imageView = imageView;
  }
}
