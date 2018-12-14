package com.pudding.tofu.widget.JSBridge;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;

public class JsWebViewClient extends WebViewClient {

    private Object obj;

    private WebClientAdapter adapter;

    public JsWebViewClient(Object obj) {
        this.obj = obj;
    }

    public void setWebClientCallback(WebClientAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String data) {
        try {
            data = URLDecoder.decode(data, "UTF-8");
            String matchData = adapter.splitResultMatchData(data);
            if (!TextUtils.isEmpty(matchData)) {
                if (obj != null) {
                    Method method = obj.getClass().getDeclaredMethod("dispatchQueue", String.class);
                    method.setAccessible(true);
                    method.invoke(obj, matchData);
                    return true;
                }
            } else {
                Method method = obj.getClass().getDeclaredMethod("dispatchQueue", String.class);
                method.setAccessible(true);
                method.invoke(obj, data);
                return true;
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return super.shouldOverrideUrlLoading(view, data);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        adapter.onPageStarted(view, url, favicon);
    }

    /**
     * 网页加载完成后才能进行通信
     *
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        adapter.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        adapter.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        adapter.onReceivedSslError(view,handler,error);
//        super.onReceivedSslError(view,handler,error);
    }
}
