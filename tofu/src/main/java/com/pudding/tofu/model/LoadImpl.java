package com.pudding.tofu.model;

import android.content.Context;
import android.text.TextUtils;

import com.pudding.tofu.R;
import com.pudding.tofu.callback.LoadFileCallback;
import com.pudding.tofu.callback.PostInterface;
import com.pudding.tofu.widget.LoadDialog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wxl on 2018/6/24 0024.
 * 邮箱：632716169@qq.com
 */

public class LoadImpl implements PostInterface, LoadFileCallback {

    /**
     * 进度条
     */
    private LoadDialog dialog;

    /**
     * 下载路径
     */
    private String url;

    /**
     * 目标地址
     */
    private String destPath;

    private Map<String, String> params = new HashMap<>();

    /**
     * 下载实体
     */
    private Load load;

    /**
     * 当前窗口类名
     */
    private String runtimeName;


    private String label;

    protected LoadImpl(String url, String destPath, Map<String, String> params, String label) {
        this.url = url;
        this.destPath = destPath;
        this.params = params;
        this.label = label;
    }

    protected void setLoadImpl(String url, String destPath, Map<String, String> params, String label) {
        this.url = url;
        this.destPath = destPath;
        this.params = params;
        this.label = label;
    }

    @Override
    public void execute() {
        if(load == null){
            load = new Load();
        }
        load.onLoad(url,destPath,this,params);
    }

    @Override
    public void unBind() {
        closeDialog();
        if(load != null){
            load.cancelLoad();
        }
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

    @Override
    public void closeDialog() {
        if(dialog != null){
            dialog.closeDialog();
        }
    }

    @Override
    public void inProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        if(TextUtils.isEmpty(label)) {
            TofuBus.get().executeLoadProgressMethod(url, progress);
        } else {
            TofuBus.get().executeLoadProgressMethod(label, progress);
        }
    }

    @Override
    public void onError(String url) {
        if(TextUtils.isEmpty(label)) {
            TofuBus.get().executeLoadFileErrorMethod(url, url);
        } else {
            TofuBus.get().executeLoadFileErrorMethod(label, url);
        }
        closeDialog();
    }

    @Override
    public void onResponse(File file) {
        if(TextUtils.isEmpty(label)) {
            TofuBus.get().executeLoadFileMethod(url, file);
        } else {
            TofuBus.get().executeLoadFileMethod(label, file);
        }
        closeDialog();
    }
}
