package com.pudding.tofu.model;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.PostRequest;
import com.pudding.tofu.callback.UploadCallback;
import com.pudding.tofu.widget.CollectUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wxl on 2018/6/25 0025.
 * 邮箱：632716169@qq.com
 */

public class Upload {

    /**
     * 上传文件
     *
     * @param url
     * @param uploadFiles
     * @param headers
     * @param params
     * @param uploadCallback
     */
    protected void upload(String url, List<UpLoadBuilder.UploadFile> uploadFiles, Map<String, String> headers,
                          Map<String, String> params, final UploadCallback uploadCallback) {
        if (url == null || CollectUtil.isEmpty(uploadFiles)) return;

        PostRequest upLoadFile = OkGo.post(url).tag("upLoadFile");
        if (!CollectUtil.isEmpty(headers)) {
            HttpHeaders httpHeaders = new HttpHeaders();
            headers.putAll(headers);
            upLoadFile.headers(httpHeaders);
        }

        if (!CollectUtil.isEmpty(params)) {
            upLoadFile.params(params);
        }

        HttpParams httpParams = upLoadFile.getParams();
        for (int i = 0; i < uploadFiles.size(); i++) {
            File file = new File(uploadFiles.get(i).path);
            httpParams.put(uploadFiles.get(i).key,file);
        }
        upLoadFile.execute(new StringCallback() {

            @Override
            public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                if (uploadCallback != null) {
                    uploadCallback.inProgress(currentSize,totalSize,progress,networkSpeed);
                }
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (uploadCallback != null) {
                    if(response.isSuccessful()) {
                        uploadCallback.onResponse(s);
                    } else {
                        uploadCallback.onError(response.request().url().toString());
                    }
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                if (uploadCallback != null) {
                    uploadCallback.onError(response.request().url().toString());
                }
            }
        });

    }

    protected void cencalUpLoad(){
        OkGo.getInstance().cancelTag("upLoadFile");
    }
}
