package com.pudding.tofu.widget.JSBridge;

import android.graphics.Bitmap;
import android.text.TextUtils;
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
            //$JsD&{"methodName":"JsToPhone","data":[{"name":"小伟闷","age":13,"code":"a8d8feasdf7"},{"name":"小力火","age":18,"code":"easdf7fa8sd"}]}
            //{"methodName":"JsToPhone","data":[{"name":"小伟闷","age":13,"code":"a8d8feasdf7"},{"name":"小力火","age":18,"code":"easdf7fa8sd"}]}
            data = URLDecoder.decode(data, "UTF-8");
            if(!TextUtils.isEmpty(adapter.splitDex())){
                if (data.contains(adapter.splitDex()) && !data.endsWith(adapter.splitDex())) {
                    if (obj != null) {
                        data = data.split(adapter.splitDex())[1];
                        Method method = obj.getClass().getDeclaredMethod("dispatchQueue", String.class);
                        method.setAccessible(true);
                        method.invoke(obj, data);
                        return true;
                    }
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
        adapter.onPageStarted(view,url,favicon);
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
}
