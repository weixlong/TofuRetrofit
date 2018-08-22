package com.pudding.tofu.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.pudding.tofu.callback.PostResultCallback;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.Response;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class PostImpl {

    protected synchronized <Result> void post(final Class<Result> resultClass, final String url,  List<Cookie> cookies, HttpParams params, HttpHeaders headers, final PostResultCallback callback) {
        OkGo.post(url).tag(url).params(params).addCookies(cookies).headers(headers).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (response.isSuccessful()) {
                    try {
                        Result result;
                        if (resultClass.equals(String.class)) {
                            result = (Result) s;
                        } else {
                            result = JSON.parseObject(s, resultClass);
                        }
                        if (callback != null) {
                            callback.onSuccess(response.request().url().toString(), result);
                        }
                    } catch (JSONException e){
                        if(TofuConfig.isDebug()){
                            System.err.println("Tofu : "+response.request().url().toString()+"   "+e);
                        }
                        if (callback != null) {
                            callback.onFailed(response.request().url().toString(),false);
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onFailed(response.request().url().toString(),false);
                    }
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                if (callback != null ) {
                    if(response != null && response.request() != null) {
                        callback.onFailed(response.request().url().toString(),e instanceof SocketTimeoutException);
                    } else {
                        callback.onFailed(url,e instanceof SocketTimeoutException);
                    }
                }
            }

        });
    }

    /**
     * 取消请求
     * @param tag
     */
    public void cancelPost(String tag){
        OkGo.getInstance().cancelTag(tag);
    }
}
