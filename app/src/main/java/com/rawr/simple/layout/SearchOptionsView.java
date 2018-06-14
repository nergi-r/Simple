package com.rawr.simple.layout;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.rawr.simple.R;
import com.rawr.simple.search.options.SearchOptionsViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchOptionsView extends RecyclerView {
  private Context context;
  private boolean isToggled;
  private SearchOptionsViewAdapter adapter;
  private int chosenOption;

  public SearchOptionsView(Context context) {
    super(context);
  }

  public SearchOptionsView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SearchOptionsView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void init(Context context) {
    this.context = context;
    isToggled = false;
    this.setVisibility(INVISIBLE);

    List<Integer> list = new ArrayList<>();
    list.add(R.drawable.image);
    list.add(R.drawable.dictionary);

    LayoutManager layoutManager = new LinearLayoutManager(
        context,
        LinearLayoutManager.HORIZONTAL,
        false);
    this.setLayoutManager(layoutManager);

    adapter = new SearchOptionsViewAdapter(context, list);
  }

  public void toggle() {
    isToggled = !isToggled;
    show(isToggled);
  }

  public void toggleTo(boolean toggle) {
    if (toggle != isToggled) show(toggle);
    isToggled = toggle;
  }

  private void show(boolean visible) {
    if (visible) {
      this.setAdapter(adapter);
      Animation fadeIn = new AlphaAnimation(0, 1);
      fadeIn.setInterpolator(new DecelerateInterpolator());
      fadeIn.setDuration(300);
      this.setVisibility(VISIBLE);
      this.setAnimation(fadeIn);
    } else {
      Animation fadeOut = new AlphaAnimation(1, 0);
      fadeOut.setInterpolator(new AccelerateInterpolator());
      fadeOut.setDuration(300);
      this.setAnimation(fadeOut);
      this.setVisibility(INVISIBLE);
      this.setAdapter(null);
    }
  }
}
