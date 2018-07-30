package com.pudding.tofu.model;

import java.lang.ref.WeakReference;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class HttpBuilderFactory implements UnBind {

    private static WeakReference<HttpBuilderFactory> weakReference ;

    private HttpBuilder httpBuilder;

    private HttpBuilderFactory() {
        weakReference = new WeakReference<>(this);
    }

    protected static HttpBuilderFactory get() {
        synchronized (HttpBuilderFactory.class){
            if (weakReference == null || weakReference.get() == null) {
                synchronized (HttpBuilderFactory.class) {
                    return new HttpBuilderFactory();
                }
            }
        }
        return weakReference.get();
    }

    protected HttpBuilder build() {
        if(httpBuilder == null){
            httpBuilder = new HttpBuilder();
        }
        httpBuilder.clear();
        return httpBuilder;
    }

    @Override
    public void unbind() {
        if(httpBuilder != null) {
            httpBuilder.unbind();
            httpBuilder = null;
        }
        if(weakReference != null){
            weakReference.clear();
            weakReference = null;
        }
    }
}
