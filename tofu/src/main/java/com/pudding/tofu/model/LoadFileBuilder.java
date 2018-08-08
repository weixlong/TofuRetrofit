package com.pudding.tofu.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lzy.okgo.model.HttpHeaders;
import com.pudding.tofu.callback.BaseInterface;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;

/**
 * Created by wxl on 2018/6/24 0024.
 * 邮箱：632716169@qq.com
 */

public class LoadFileBuilder implements UnBind{


    /**
     * 下载路径
     */
    private String url;

    /**
     * 目标地址
     */
    private String destPath;



    /**
     * 解绑集合
     */
    private List<BaseInterface> unBinds = new ArrayList<>();

    /**
     * 是否可以显示进度
     */
    private boolean isShowDialog = false;


    /**
     * 下载
     */
    private LoadImpl impl;

    /**
     * 参数
     */
    private Map<String, String> params = new HashMap<>();

    /**
     * 头
     */
    private HttpHeaders heads = new HttpHeaders();

    /**
     * 用户自己添加的Cookie
     */
    protected List<Cookie> userCookies = new ArrayList<>();

    /**
     * 上下文
     */
    private Context context;

    private String label;


    protected LoadFileBuilder() {
    }

    /**
     * 开始下载
     */
    public synchronized void start(){
        checkParamsAvailable();
        if(AndPermission.hasPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (impl == null) {
                impl = new LoadImpl(url, destPath, userCookies,heads,params, label);
            } else {
                impl.setLoadImpl(url, destPath,userCookies,heads, params, label);
            }

            if (isShowDialog) {
                if (context != null) {
                    impl.showDialog(context);
                } else {
                    System.err.print("Tofu : your context is null cannot show dialog !");
                }
            }
            unBinds.add(impl);
            impl.execute();
        } else {
            System.out.println("Tofu : Are you sure has storage permission ? ");
        }
    }

    /**
     * 参数检查
     */
    private void checkParamsAvailable(){
        if(TextUtils.isEmpty(url))throw  new NullPointerException("your url is null !!");
        if(TextUtils.isEmpty(destPath))throw  new NullPointerException("your destPath is null !!");
    }


    /**
     * 设置回调tag，如未设置，则回调到url注解中
     * @param label
     */
    public LoadFileBuilder label(String label) {
        this.label = label;
        return this;
    }

    /**
     * 下载路径
     * 如果没有设置label ,则label为url
     * @param url
     * @return
     */
    public LoadFileBuilder setUrl(@NonNull String url) {
        this.url = url;
        return this;
    }

    /**
     * 参数
     * @param key
     * @param param
     * @return
     */
    public LoadFileBuilder putParam(@NonNull String key, @NonNull String param){
        params.put(key,param);
        return this;
    }

    /**
     * 移除参数
     * @param key
     * @return
     */
    public LoadFileBuilder remove(@NonNull String key){
        params.remove(key);
        return this;
    }

    /**
     * 移除请求头
     * @param key
     * @return
     */
    public LoadFileBuilder removeHead(@NonNull String key){
        heads.remove(key);
        return this;
    }

    /**
     * 请求头
     * @param key
     * @param head
     * @return
     */
    public LoadFileBuilder putHead(@NonNull String key, @NonNull String head){
        heads.put(key,head);
        return this;
    }

    /***
     * 添加Cookie
     * @param cookie
     * @return
     */
    public LoadFileBuilder addCookie(@NonNull Cookie cookie){
        userCookies.add(cookie);
        return this;
    }

    /***
     * 添加Cookies
     * @param cookies
     * @return
     */
    public LoadFileBuilder addCookie(@NonNull List<Cookie> cookies){
        userCookies.addAll(cookies);
        return this;
    }

    /***
     * 添加Cookie
     * @param
     * @return
     */
    public LoadFileBuilder addCookie(@NonNull String name,@NonNull String value){
        Cookie.Builder builder = new Cookie.Builder();
        Cookie cookie = builder.name(name).value(value).domain(name).build();
        userCookies.add(cookie);
        return this;
    }

    /**
     * 下载存入文件地址
     * @param destPath
     * @return
     */
    public LoadFileBuilder setDestPath(@NonNull String destPath) {
        this.destPath = destPath;
        return this;
    }

    /**
     * 下载存入文件地址
     * @param destFile
     * @return
     */
    public LoadFileBuilder setDestFile(@NonNull File destFile) {
        this.destPath = destFile.getAbsolutePath();
        return this;
    }
    /**
     * 显示dialog
     * @param
     * @return
     */
    public LoadFileBuilder asDialog(@NonNull Context context) {
        isShowDialog = true;
        this.context = context;
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
     * 清除参数
     */
    protected void clear() {
        isShowDialog = false;
        this.destPath = "";
        this.url = "";
        params.clear();
        heads.clear();
        userCookies.clear();
        context = null;
    }
}
