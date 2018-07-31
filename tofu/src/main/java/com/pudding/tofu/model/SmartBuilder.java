package com.pudding.tofu.model;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.pudding.tofu.callback.OnSmartRefreshBefore;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wxl on 2018/7/30 0030.
 * 邮箱：632716169@qq.com
 */

public class SmartBuilder<Result> implements UnBind {

    private SmartRefreshLayout layout;

    private Class<Result> clazz;

    private String url;

    private Map<String, String> params = new HashMap<>();

    private OnSmartRefreshBefore refreshBefore;

    private int outTime;

    private String pageKey = "page";

    private SmartImpl smart;

    private String label;

    protected SmartBuilder() {
    }

    /**
     * 设置刷新布局
     *
     * @param layout
     * @return
     */
    public SmartBuilder layout(@NonNull SmartRefreshLayout layout) {
        this.layout = layout;
        return this;
    }


    /**
     * 设置刷新路径
     *
     * @param url
     * @return
     */
    public SmartBuilder url(@NonNull String url) {
        this.url = url;
        return this;
    }

    /**
     * 标记,如没有设置则label为url
     * @param label
     */
    public SmartBuilder label(@NonNull String label) {
        this.label = label;
        return this;
    }

    /**
     * 设置刷新返回结果
     *
     * @param clazz
     * @return
     */
    public SmartBuilder result(@NonNull Class<Result> clazz) {
        this.clazz = clazz;
        return this;
    }

    /**
     * 存请求参数
     *
     * @param key
     * @param param
     * @return
     */
    public SmartBuilder put(@NonNull String key, @NonNull String param) {
        params.put(key, param);
        return this;
    }

    /**
     * 添加参数
     *
     * @param params
     * @return
     */
    public SmartBuilder put(@NonNull Map<String, String> params) {
        this.params.putAll(params);
        return this;
    }

    /**
     * 参数覆盖
     *
     * @param params
     * @return
     */
    public SmartBuilder cover(@NonNull Map<String, String> params) {
        this.params = params;
        return this;
    }

    /**
     * 请求超时时间
     *
     * @param time
     * @return
     */
    public SmartBuilder outTime(@IntRange(from = 0) int time) {
        this.outTime = time;
        return this;
    }

    /**
     * 请求分页参数
     * 如果需求分页的参数不是page请自行设置
     */
    public SmartBuilder pageKey(@NonNull String key) {
        this.pageKey = key;
        return this;
    }

    /**
     * 移除参数
     *
     * @param key
     * @return
     */
    public SmartBuilder remove(String key) {
        params.remove(key);
        return this;
    }

    /**
     * 刷新拦截
     *
     * @return
     */
    public SmartBuilder refreshInterceptor(@NonNull OnSmartRefreshBefore refreshBefore) {
        this.refreshBefore = refreshBefore;
        return this;
    }


    /**
     * 参数封包
     */
    public void smart() {
        checkParamAvailable();
        this.layout.setTag(8999, this);
        if (smart == null) {
            smart = new SmartImpl(pageKey,label,layout,clazz,url,params,refreshBefore,outTime);
        } else {
            smart.setSmartImpl(pageKey,label,layout,clazz,url,params,refreshBefore,outTime);
        }
        smart.onSmart();
    }

    /**
     * 回退一页
     */
    public void goBackPage() {
        if (smart != null) {
            smart.goBackPage();
        }
    }

    /**
     * 回到第一页
     */
    public void goToFirst() {
        if (smart != null) {
            smart.toFirst();
        }
    }


    /**
     * 停止刷新和加载更多
     */
    public void stopRefreshMoreLoad(){
        if(layout != null){
            layout.finishLoadmore();
            layout.finishRefresh();
        }
    }


    /**
     * 自动刷新
     */
    public void autoRefresh(){
        if (layout != null) {
            layout.autoRefresh();
        }
    }


    /**
     * 清除参数
     */
    public SmartBuilder clear(){
        if (smart != null) {
            smart.clear();
        }
        return this;
    }


    /**
     * 参数检查
     */
    private void checkParamAvailable() {
        if (layout == null) throw new IllegalArgumentException("smart your layout is null .");
        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("smart your url is null .");
        if (clazz == null) throw new IllegalArgumentException("smart your result type is null.");
    }

    @Override
    public void unbind() {
        if (smart != null) {
            smart.clear();
            smart = null;
        }
        layout = null;
        clazz = null;
        url = null;
    }
}
