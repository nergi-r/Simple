package com.rawr.simple.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.rawr.simple.R;

public class TranslationWebView {
  private final Context context;
  private final WebView webView;

  public TranslationWebView(Context context) {
    this.context = context;

    webView = LayoutInflater
        .from(context)
        .inflate(R.layout.layout_web_view, null)
        .findViewById(R.id.web_view);

    ((ViewGroup) webView.getParent()).removeView(webView);
  }

  public WebView getWebView() {
    return webView;
  }
}
