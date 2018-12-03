package com.pudding.tofu.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.model.HttpHeaders;
import com.pudding.tofu.R;
import com.pudding.tofu.callback.PostInterface;
import com.pudding.tofu.callback.UploadCallback;
import com.pudding.tofu.widget.LoadDialog;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileBatchCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;

/**
 * Created by wxl on 2018/6/25 0025.
 * 邮箱：632716169@qq.com
 */

public class UpLoadImpl<Result> implements PostInterface,UploadCallback {

    /**
     * 上传
     */
    private Upload upload;

    /**
     * 进度条
     */
    private LoadDialog dialog;

    /**
     * 上传路径
     */
    private String url;


    /**
     * 参数
     */
    private Map<String, String> params = new HashMap<>();

    /**
     * 参数
     */
    private HttpHeaders heads = new HttpHeaders();

    /**
     * 上传文件集合
     */
    private List<UpLoadBuilder.UploadFile> uploadFiles = new ArrayList<>();

    /**
     * 回调结果
     */
    private Class<Result> aClass;

    /**
     * 是否开启压缩
     */
    private boolean compress ;

    /**
     * 上下文
     */
    private Context context;


    /**
     * 当前窗口类名
     */
    private String runtimeName;

    private String label;

   private List<Cookie> cookies;

   private LoadResult result = new LoadResult();

    protected UpLoadImpl() {
    }

    protected UpLoadImpl(String url, Map<String, String> params, HttpHeaders heads,List<Cookie> cookies,
                         List<UpLoadBuilder.UploadFile> uploadFiles,
                         Class<Result> aClass, boolean compress, Context context,
                         String label) {
        this.url = url;
        this.params = params;
        this.heads = heads;
        this.uploadFiles = uploadFiles;
        this.aClass = aClass;
        this.compress = compress;
        this.context = context;
        this.cookies = cookies;
        this.label = label;
    }

    protected void setUpLoadImpl(String url, Map<String, String> params, HttpHeaders heads,List<Cookie> cookies,
                                 List<UpLoadBuilder.UploadFile> uploadFiles,
                                 Class<Result> aClass, boolean compress,
                                 Context context, String label) {
        this.url = url;
        this.params = params;
        this.heads = heads;
        this.uploadFiles = uploadFiles;
        this.aClass = aClass;
        this.compress = compress;
        this.context = context;
        this.label = label;
        this.cookies  = cookies;
    }


    @Override
    public void execute() {
        if(upload == null){
            upload = new Upload();
        }
        if(compress){
            compress();
        } else {
            upload.upload(url, uploadFiles, heads,cookies, params, this);
        }
    }

    @Override
    public void unBind() {
        closeDialog();
        if(upload != null){
            upload.cencalUpLoad();
        }
        upload = null;
    }

    @Override
    public void showDialog(Context context) {
        String runningActivityName = getRunningActivityName(context);
        if(runtimeName == null){
            runtimeName = runningActivityName;
            dialog = new LoadDialog(context, R.style.dialog);
        } else {
            if(!TextUtils.equals(runtimeName,runningActivityName)){
                runtimeName = runningActivityName;
                dialog = new LoadDialog(context, R.style.dialog);
            }
        }
        if(dialog != null) {
            dialog.showDialog();
        }
    }

    /**
     * 获取当前窗口名
     * @param context
     * @return
     */
    private String getRunningActivityName(Context context) {
        String contextString = context.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
    }

    /**
     * 文件压缩
     */
    protected void compress(){
        File[] files = new File[uploadFiles.size()];
        Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
        Bitmap.Config mConfig = Bitmap.Config.RGB_565;
        compressOptions.config = mConfig;
        for (int i = 0; i < uploadFiles.size(); i++) {
            files[i] = new File(uploadFiles.get(i).path);
        }
        Tiny.getInstance().source(files).batchAsFile().withOptions(compressOptions).batchCompress(new FileBatchCallback() {
            @Override
            public void callback(boolean isSuccess, String[] outfile) {
                if(isSuccess && outfile.length == uploadFiles.size()) {
                    for (int i = 0; i < uploadFiles.size(); i++) {
                        if(context != null) {
                            File file = new File(outfile[i]);
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                        }
                        uploadFiles.get(i).path = outfile[i];
                    }
                    upload.upload(url, uploadFiles, heads,cookies, params, UpLoadImpl.this);
                }
            }
        });
    }



    @Override
    public void closeDialog() {
        if(dialog != null){
            dialog.closeDialog();
        }
    }

    @Override
    public void inProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        result.totalSize = totalSize;
        result.progress = progress;
        result.networkSpeed  =networkSpeed;
        result.currentSize = currentSize;
        if(TextUtils.isEmpty(label)) {
            TofuBus.get().executeUploadProgressMethod(url, result);
        } else {
            TofuBus.get().executeUploadProgressMethod(label, result);
        }
    }

    @Override
    public void onError(String url,boolean isOutTime) {
        closeDialog();
        if(TextUtils.isEmpty(label)) {
            TofuBus.get().executeUploadErrorMethod(url, url,isOutTime);
        } else {
            TofuBus.get().executeUploadErrorMethod(label, url,isOutTime);
        }
    }

    @Override
    public void onResponse(String response) {
        closeDialog();
        if(aClass == null || aClass.equals(String.class)){
            if(TextUtils.isEmpty(label)) {
                TofuBus.get().executeUploadMethod(url, response);
            } else {
                TofuBus.get().executeUploadMethod(label, response);
            }
        } else {
            try {
                Result result = JSON.parseObject(response,aClass);
                if(TextUtils.isEmpty(label)) {
                    TofuBus.get().executeUploadMethod(url, result);
                } else {
                    TofuBus.get().executeUploadMethod(label, result);
                }
            }catch (JSONException e){
                if(TofuConfig.isDebug()){
                    System.err.println("Tofu : "+e);
                }
            }
        }
    }
}
