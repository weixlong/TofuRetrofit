package com.pudding.tofu.widget.JSBridge;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

public class WebClientAdapter {

    public String splitResultMatchData(String data) {
        return "";
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon){}

    public void onPageFinished(WebView view, String url){}

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){}

    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

    }
}
