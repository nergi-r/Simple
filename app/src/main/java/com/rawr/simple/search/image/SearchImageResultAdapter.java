package com.rawr.simple.search.image;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rawr.simple.BackgroundService;
import com.rawr.simple.MainActivity;
import com.rawr.simple.layout.LayoutUtil;
import com.rawr.simple.R;
import com.rawr.simple.layout.TouchImageView;

import java.util.ArrayList;
import java.util.List;

public class SearchImageResultAdapter extends RecyclerView.Adapter<SearchImageViewHolder> {
  private static final float SEARCH_IMAGE_DEFAULT_WIDTH = 135;
  private static final float EXPAND_IMAGE_DEFAULT_WIDTH = 600;

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

    float scale = LayoutUtil.pxFromDp(context,
        SEARCH_IMAGE_DEFAULT_WIDTH) / searchImageResult.getThumbnail().getWidth();
    float width = scale * searchImageResult.getThumbnail().getWidth();
    float height = scale * searchImageResult.getThumbnail().getHeight();
    imageView.getLayoutParams().width = (int) width;
    imageView.getLayoutParams().height = (int) height;

    Glide.with(context)
        .load(searchImageResult.getThumbnail().getUrl())
        .thumbnail(Glide.with(context).load(R.raw.spin))
        .crossFade()
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imageView);

    holder.getImageView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final Dialog dialog = BackgroundService.getDialog();
        final TouchImageView expandedImageView = dialog.findViewById(R.id.expandedImageView2);

        Glide.with(context)
            .load(R.raw.loader256)
            .asGif()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(new SimpleTarget<GifDrawable>() {
              @Override
              public void onResourceReady(
                  GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
                resource.start();
                expandedImageView.setImageDrawable(resource);
                expandedImageView.setZoom(1f);
              }
            });

        Glide.with(context)
            .load(searchImageResult.getContent().getUrl())
            .asBitmap()
            .crossFade()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(new SimpleTarget<Bitmap>() {
              @Override
              public void onResourceReady(
                  Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                expandedImageView.setImageBitmap(resource);
              }
            });
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
          @Override
          public void onCancel(DialogInterface dialogInterface) {
            expandedImageView.setImageDrawable(null);
          }
        });
      }
    });
  }

  @Override
  public void onViewDetachedFromWindow(SearchImageViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    holder.getImageView().setOnClickListener(null);
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
