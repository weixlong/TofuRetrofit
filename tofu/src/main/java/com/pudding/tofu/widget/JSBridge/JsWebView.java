package com.pudding.tofu.widget.JSBridge;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.reflect.Method;

public class JsWebView extends WebView {

    public JsWebView(Context context) {
        super(context);
        onSetWebViewJsSa();
    }

    public JsWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onSetWebViewJsSa();
    }

    public JsWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onSetWebViewJsSa();
    }

    private void onSetWebViewJsSa(){
        WebSettings webSettings = getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);

        //是否可以缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webSettings.setDisplayZoomControls(false);
        }

        setWebChromeClient(new WebChromeClient());

        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        try {
            Method setTextSize = WebSettings.class.getDeclaredMethod("setTextSize", WebSettings.TextSize.class);
            setTextSize.setAccessible(true);
            setTextSize.invoke(webSettings, WebSettings.TextSize.NORMAL);
            Method setTextZoom = WebSettings.class.getDeclaredMethod("setTextZoom", Integer.class);
            setTextZoom.setAccessible(true);
            setTextZoom.invoke(webSettings,100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDebug(boolean isDebug){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(isDebug);
        }
    }
}
