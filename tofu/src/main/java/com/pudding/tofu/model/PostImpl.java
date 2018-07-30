package com.pudding.tofu.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.pudding.tofu.callback.PostResultCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class PostImpl {

    protected synchronized <Result> void post(final Class<Result> resultClass, final String url, HttpParams params, final PostResultCallback callback) {
        OkGo.post(url).params(params).execute(new StringCallback() {
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
                            callback.onFailed(response.request().url().toString());
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onFailed(response.request().url().toString());
                    }
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                if (callback != null ) {
                    if(response != null && response.request() != null) {
                        callback.onFailed(response.request().url().toString());
                    } else {
                        callback.onFailed(url);
                    }
                }
            }

        });
    }
}
