package com.pudding.tofu.rx.widget;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.pudding.tofu.rx.callback.UnBindRx;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wxl on 2018/8/6 0006.
 * 邮箱：632716169@qq.com
 */

public class RefreshBuilder<Result> implements UnBindRx {

    /**
     * 刷新管理
     */
    private RefreshManager manager;

    private SmartRefreshLayout layout;

    private String label, url;

    private int position;

    private class Smart {
        RefreshManager manager;
        SmartRefreshLayout layout;

        public Smart(RefreshManager manager, SmartRefreshLayout layout) {
            this.manager = manager;
            this.layout = layout;
        }
    }

    private HashMap<Long, Smart> smartMap = new HashMap<>();


    protected RefreshBuilder() {
        //todo none
    }

    /**
     * 绑定布局，建议在onCreate中
     *
     * @param layout
     * @return
     */
    public synchronized RefreshBuilder layout(@NonNull SmartRefreshLayout layout) {
        Object tag = layout.getTag(0x0059595959);
        if (tag == null || !smartMap.containsKey(tag)) {
            this.layout = layout;
            layout.setTag(0x0059595959, System.currentTimeMillis()+position);
            manager = new RefreshManager(layout);
            layout.setOnRefreshLoadmoreListener(manager.callback);
            layout.setTag(0x008889659,this);
            smartMap.put((Long) tag, new Smart(manager, layout));
            position++;
        } else {
            Smart smart = smartMap.get(tag);
            this.layout = layout;
            manager = smart.manager;
        }
        return this;
    }

    /**
     * 返回刷新布局
     *
     * @return
     */
    public SmartRefreshLayout getLayout() {
        return layout;
    }

    /**
     * 返回label
     *
     * @return
     */
    public String getLabel() {
        return TextUtils.isEmpty(label) ? url : label;
    }

    /**
     * 超时时间
     *
     * @param outTime
     * @return
     */
    public RefreshBuilder outTime(@IntRange(from = 0) int outTime) {
        checkParamAvailable();
        manager.setOutTime(outTime);
        return this;
    }

    /**
     * 参数设置，与外部参数集合同一个引用
     *
     * @param params
     * @return
     */
    public RefreshBuilder params(@NonNull HashMap<String, String> params) {
        checkParamAvailable();
        manager.setParams(params);
        return this;
    }

    /**
     * 请求链接
     *
     * @param url
     * @return
     */
    public RefreshBuilder url(@NonNull String url) {
        checkParamAvailable();
        manager.setRefreshUrl(url);
        this.url = url;
        return this;
    }

    /**
     * 请求返回结果类型
     *
     * @param result
     * @return
     */
    public RefreshBuilder result(@NonNull Class<Result> result) {
        checkParamAvailable();
        manager.setResult(result);
        return this;
    }

    /**
     * 存放参数，释放外部参数不影响put进来的参数
     *
     * @param key
     * @param param
     * @return
     */
    public RefreshBuilder put(@NonNull String key, @NonNull String param) {
        checkParamAvailable();
        manager.put(key, param);
        return this;
    }

    /**
     * 存放参数，释放外部参数不影响put进来的参数
     *
     * @param isPut
     * @param key
     * @param param
     * @return
     */
    public RefreshBuilder put(boolean isPut, @NonNull String key, @NonNull String param) {
        if (isPut) {
            checkParamAvailable();
            manager.put(key, param);
        }
        return this;
    }

    /**
     * 页数的参数键值
     *
     * @param pageKey
     * @return
     */
    public RefreshBuilder pageKey(@NonNull String pageKey) {
        checkParamAvailable();
        manager.setPageKey(pageKey);
        return this;
    }

    /**
     * 清除参数
     *
     * @return
     */
    public RefreshBuilder clear() {
        checkParamAvailable();
        manager.clear();
        return this;
    }

    /**
     * 清除缓存参数
     * @return
     */
    public RefreshBuilder clearCache(){
        checkParamAvailable();
        manager.clearCache();
        return this;
    }

    /**
     * 获得当前的pageKey
     * @return
     */
    public String getPageKey(){
        checkParamAvailable();
        return manager.getPageKey();
    }




    /**
     * 指针回退一页
     *
     * @return
     */
    public RefreshBuilder goBack() {
        checkParamAvailable();
        manager.pre();
        return this;
    }

    /**
     * 指针到下一页
     *
     * @return
     */
    public RefreshBuilder goNext() {
        checkParamAvailable();
        manager.next();
        return this;
    }


    /**
     * 指针回到第一页
     *
     * @return
     */
    public RefreshBuilder goFirst() {
        checkParamAvailable();
        manager.toFirst();
        return this;
    }

    /**
     * 移除参数
     *
     * @param key
     * @return
     */
    public RefreshBuilder remove(@NonNull String key) {
        checkParamAvailable();
        manager.remove(key);
        return this;
    }

    /**
     * 条件移除参数
     *
     * @param isRemove
     * @param key
     * @return
     */
    public RefreshBuilder remove(boolean isRemove, @NonNull String key) {
        if (isRemove) {
            checkParamAvailable();
            manager.remove(key);
        }
        return this;
    }

    /**
     * 在刷新之前进行拦截，回调@refreshBefore
     *
     * @return
     */
    public RefreshBuilder dropAgo() {
        checkParamAvailable();
        manager.asRefreshPre();
        return this;
    }

    /**
     * 在加载更多之前拦截，回调@loadMoreBefore
     *
     * @return
     */
    public RefreshBuilder loadAgo() {
        checkParamAvailable();
        manager.asLoadMorePre();
        return this;
    }


    /**
     * 回调的label，不设置则为url
     *
     * @param label
     * @return
     */
    public RefreshBuilder label(@NonNull String label) {
        checkParamAvailable();
        manager.setLabel(label);
        this.label = label;
        return this;
    }

    /**
     * 请求当前页数据
     */
    public void post() {
        checkParamAvailable();
        manager.onPost();
    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        checkParamAvailable();
        manager.autoRefresh();
    }

    /**
     * 停止刷新
     *
     * @return
     */
    public RefreshBuilder stopRefresh() {
        checkParamAvailable();
        manager.stopRefresh();
        return this;
    }

    /**
     * 停止加载
     *
     * @return
     */
    public RefreshBuilder stopLoad() {
        checkParamAvailable();
        manager.stopLoad();
        return this;
    }

    /**
     * 停止刷新和加载
     *
     * @return
     */
    public RefreshBuilder stop() {
        checkParamAvailable();
        manager.stopLoad();
        manager.stopRefresh();
        return this;
    }

    /**
     * 自动加载
     */
    public void autoLoad() {
        checkParamAvailable();
        manager.autoLoad();
    }

    /**
     * 获得当前的url
     *
     * @return
     */
    public String getUrl() {
        checkParamAvailable();
        return manager.getUrl();
    }

    /**
     * 获取参数
     *
     * @returnp
     */
    public Map<String, String> param() {
        checkParamAvailable();
        return manager.getParams();
    }

    /**
     * 获取缓存参数
     * @return
     */
    public Map<String, String> chache(){
        checkParamAvailable();
        return manager.getCache();
    }

    private void checkParamAvailable() {
        if (manager == null)
            throw new IllegalArgumentException("your layout is null , please set it !!!");
    }


    /**
     * 解绑指定的layout
     *
     * @param layout
     */
    public void unBind(SmartRefreshLayout layout) {
        Object tag = layout.getTag(0x0059595959);
        if(tag != null && smartMap.containsKey(tag)){
            Smart remove = smartMap.remove(tag);
            if (remove != null && remove.manager != null) {
                remove.manager.destroy();
            }
            remove.layout = null;
            remove.manager = null;
        }
    }

    @Override
    public void unBind() {
        if (manager != null) {
            manager.destroy();
            manager = null;
        }
        if(layout != null) {
            Object tag = layout.getTag(0x0059595959);
            if (tag != null && smartMap.containsKey(tag)) {
                smartMap.remove(tag);
            }
            layout = null;
        }
    }
}
