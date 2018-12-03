package com.pudding.tofu.widget.JSBridge;

import android.graphics.Bitmap;
import android.webkit.WebView;

public class WebClientAdapter {

    public String splitDex() {
        return "";
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon){}

    public void onPageFinished(WebView view, String url){}

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){}
}
