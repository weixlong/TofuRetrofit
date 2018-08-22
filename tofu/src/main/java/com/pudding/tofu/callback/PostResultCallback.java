package com.pudding.tofu.callback;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public interface PostResultCallback {

    /**
     * 请求成功
     * @param url
     * @param result
     * @param <Result>
     */
    <Result> void onSuccess(String url, Result result);

    /**
     * 请求失败
     * @param response
     */
    void onFailed(String response,boolean isOutTime);
}
