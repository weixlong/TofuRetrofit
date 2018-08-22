package com.pudding.tofu.model;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.pudding.tofu.callback.LoadFileCallback;
import com.pudding.tofu.widget.CollectUtil;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.Response;

/**
 * Created by wxl on 2018/6/24 0024.
 * 邮箱：632716169@qq.com
 */

public class Load {
    /**
     * 下载文件
     * @param url
     * @param destPath
     * @param loadFileCallback
     */
    protected synchronized void onLoad(String url, String destPath, final LoadFileCallback loadFileCallback, List<Cookie> cookies, HttpHeaders heads, Map<String, String> params){
        final File file = new File(destPath);
        OkGo.post(url).tag("loadFile").addCookies(cookies).headers(heads).params(params).execute(new FileCallback(file.getParent(), file.getName()) {
            @Override
            public void onSuccess(File file, Call call, Response response) {
                if(response.isSuccessful()) {
                    if (loadFileCallback != null) {
                        loadFileCallback.onResponse(file);
                    }
                } else {
                    if (loadFileCallback != null) {
                        loadFileCallback.onError(response.request().url().toString(),false);
                    }
                }
            }

            @Override
            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed){
                if (loadFileCallback != null) {
                    loadFileCallback.inProgress(currentSize,totalSize,progress,networkSpeed);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                if (loadFileCallback != null) {
                    loadFileCallback.onError(response.request().url().toString(),e instanceof SocketTimeoutException);
                }
            }
        });
    }

    /**
     * 取消下载任务
     */
    protected void cancelLoad(){
        OkGo.getInstance().cancelTag("loadFile");
    }
}
