package com.rawr.simple.seach.image;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.rawr.simple.R;

public class SearchImageViewHolder extends RecyclerView.ViewHolder {

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
