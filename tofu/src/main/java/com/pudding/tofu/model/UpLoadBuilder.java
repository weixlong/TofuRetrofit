package com.pudding.tofu.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.pudding.tofu.callback.BaseInterface;
import com.pudding.tofu.widget.CollectUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wxl on 2018/6/25 0025.
 * 邮箱：632716169@qq.com
 */

public class UpLoadBuilder<Result> implements UnBind{

    /**
     * 上传路径
     */
    private String url;


    /**
     * 解绑集合
     */
    private List<BaseInterface> unBinds = new ArrayList<>();

    /**
     * 是否可以显示进度
     */
    private boolean isShowDialog = false;

    /**
     * 参数
     */
    private Map<String, String> params = new HashMap<>();

    /**
     * 参数
     */
    private Map<String, String> heads = new HashMap<>();

    /**
     * 上传文件集合
     */
    private List<UploadFile> uploadFiles = new ArrayList<>();

    /**
     * 回调结果
     */
    private Class<Result> aClass;


    /**
     * 上传句柄
     */
    private UpLoadImpl upLoad;

    /**
     * 是否压缩之后再上传
     */
    private boolean isCompress = false;

    /***
     * 上下文
     */
    private Context context;

    private String label;

    protected UpLoadBuilder() {
    }

    /**
     * 开始上传
     */
    public void start(){
        checkParamsAvailable();
        Tofu.ask().with((Activity) context).on(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(upLoad == null){
            upLoad = new UpLoadImpl(url,params,heads,uploadFiles,aClass,isCompress,context,label);
        } else {
            upLoad.setUpLoadImpl(url,params,heads,uploadFiles,aClass,isCompress,context,label);
        }

        if(isShowDialog){
            if(context != null){
                upLoad.showDialog(context);
            }
        }

        upLoad.execute();

        unBinds.add(upLoad);
    }

    /**
     * 设置tag，如果没有设置或为空，则回调url注解方法
     * @param label
     * @return
     */
    public UpLoadBuilder label(String label) {
        this.label = label;
        return this;
    }

    /**
     * 参数检查
     */
    private void checkParamsAvailable(){
        if(TextUtils.isEmpty(url))throw  new NullPointerException("your url is null !!");
        if(CollectUtil.isEmpty(uploadFiles))throw  new NullPointerException("your upload file is null !!");
    }

    /**
     * 添加上传文件
     * @param file
     * @return
     */
    public UpLoadBuilder addFile(@NonNull File file){
        UploadFile uploadFile = new UploadFile();
        uploadFile.key = file.getName();
        uploadFile.path = file.getAbsolutePath();
        uploadFiles.add(uploadFile);
        return this;
    }

    /**
     * 是否压缩之后再上传
     * @param
     * @return
     */
    public UpLoadBuilder asCompress(@NonNull Context context) {
        isCompress = true;
        this.context  =context;
        return this;
    }

    /**
     * 设置返回结果参数
     * @param aClass
     */
    public UpLoadBuilder setClass(@NonNull Class<Result> aClass) {
        this.aClass = aClass;
        return this;
    }

    /**
     * 添加上传文件
     * @param key
     * @param file
     * @return
     */
    public UpLoadBuilder addFile(@NonNull String key, @NonNull File file){
        UploadFile uploadFile = new UploadFile();
        uploadFile.key = key;
        uploadFile.path = file.getAbsolutePath();
        uploadFiles.add(uploadFile);
        return this;
    }

    /**
     * 添加上传文件
     * @param
     * @param path
     * @return
     */
    public UpLoadBuilder addFile(@NonNull String path){
        File file = new File(path);
        UploadFile uploadFile = new UploadFile();
        uploadFile.key = file.getName();
        uploadFile.path = file.getAbsolutePath();
        uploadFiles.add(uploadFile);
        return this;
    }

    /**
     * 添加上传文件
     * @param key
     * @param path
     * @return
     */
    public UpLoadBuilder addFile(@NonNull String key, @NonNull String path){
        File file = new File(path);
        UploadFile uploadFile = new UploadFile();
        uploadFile.key = key;
        uploadFile.path = file.getAbsolutePath();
        uploadFiles.add(uploadFile);
        return this;
    }


    /**
     * 添加上传头
     * @param key
     * @param head
     * @return
     */
    public UpLoadBuilder addHead(@NonNull String key, @NonNull String head){
        heads.put(key,head);
        return this;
    }

    /**
     * 添加上传参数
     * @param key
     * @param value
     * @return
     */
    public UpLoadBuilder addParam(@NonNull String key, @NonNull String value){
        params.put(key,value);
        return this;
    }




    /**
     * 上传路径
     * 如果没有设置label ,则label为url
     * @param url
     */
    public UpLoadBuilder setUrl(@NonNull String url) {
        this.url = url;
        return this;
    }

    /**
     * 显示dialog
     */
    public UpLoadBuilder asDialog(@NonNull Context context) {
        this.context = context;
        isShowDialog = true;
        return this;
    }

    @Override
    public void unbind() {
        clear();
        for (BaseInterface unBind : unBinds) {
            unBind.unBind();
        }
        unBinds.clear();
        context = null;
    }


    /**
     * 清除缓存
     */
    protected void clear(){
        params.clear();
        heads.clear();
        uploadFiles.clear();
        isShowDialog = false;
        isCompress = false;
        context = null;
        url = "";
    }


    class UploadFile {
        String key;
        String path;
    }
}
