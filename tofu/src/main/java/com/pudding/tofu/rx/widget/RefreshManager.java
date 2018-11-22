package com.pudding.tofu.rx.widget;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.pudding.tofu.model.Tofu;
import com.pudding.tofu.model.TofuConfig;
import com.pudding.tofu.retention.post;
import com.pudding.tofu.retention.postError;
import com.pudding.tofu.rx.tofu.TofuBusRx;
import com.pudding.tofu.widget.CollectUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wxl on 2018/8/6 0006.
 * 邮箱：632716169@qq.com
 */

public class RefreshManager<Result> {

    private int outTime;

    private HashMap<String, String> params = new HashMap<>();

    private HashMap<String, String> cache_params = new HashMap<>();

    private String refreshUrl;

    private Class<Result> result;

    private int page = 1;

    private String pageKey = "pageNo";

    private SmartRefreshLayout layout;

    private final String refresh_post_label = "refresh_post_label";

    private String refresh_label = refresh_post_label;

    protected OnRefreshLoadmoreListener callback = new RefreshCallback();

    private boolean refreshAgo, loadAgo;

    private String label;

    private static boolean isOutTimeBack = false, isCallFailedBeforeOutTime = false;

    /**
     * 布局注入
     *
     * @param layout
     */
    public RefreshManager(SmartRefreshLayout layout) {
        this.layout = layout;
        bind();
    }


    public void bind() {
        Tofu.bind(this);
    }

    /**
     * 超时时间
     *
     * @param outTime
     */
    public void setOutTime(int outTime) {
        this.outTime = outTime;
    }

    /**
     * 缓冲区参数
     *
     * @param params
     */
    public void setParams(@NonNull HashMap<String, String> params) {
        cache_params = params;
    }

    /**
     * 请求链接
     *
     * @param refreshUrl
     */
    public void setRefreshUrl(String refreshUrl) {
        this.refreshUrl = refreshUrl;
    }

    /**
     * 返回结果类型
     *
     * @param result
     */
    public void setResult(Class<Result> result) {
        this.result = result;
    }

    /**
     * 页数键值
     *
     * @param pageKey
     */
    public void setPageKey(String pageKey) {
        this.pageKey = pageKey;
    }

    /**
     * 存参数
     *
     * @param key
     * @param param
     */
    public void put(String key, String param) {
        this.params.put(key, param);
    }

    /**
     * 移除参数
     *
     * @param key
     */
    public void remove(String key) {
        this.params.remove(key);
    }

    /**
     * 参数参数
     */
    protected void clear() {
        this.params.clear();
        page = 1;
    }

    /**
     * 指针回到前一页
     */
    protected void pre() {
        if (page > 1) {
            page--;
        }
        params.put(pageKey, page + "");
    }

    /**
     * 指针指向下一页
     */
    protected void next() {
        page++;
        params.put(pageKey, page + "");
    }

    /**
     * 指针回到第一页
     */
    protected void toFirst() {
        page = 1;
        params.put(pageKey, page + "");
    }

    /**
     * 拦截刷新
     */
    protected void asRefreshPre() {
        refreshAgo = true;
    }


    /**
     * 是否拦截加载更多
     */
    protected void asLoadMorePre() {
        loadAgo = true;
    }

    /**
     * 请求上一页
     */
    protected void postPre() {
        pre();
        buildParam();
        Tofu.post().put(params).result(result).url(refreshUrl).label(refresh_post_label).execute();
    }

    /**
     * 请求下一页
     */
    protected void onPostNext() {
        next();
        buildParam();
        Tofu.post().put(params).result(result).url(refreshUrl).label(refresh_post_label).execute();
    }

    /**
     * 请求当前页
     */
    protected void onPost() {
        buildParam();
        Tofu.post().put(params).result(result).url(refreshUrl).label(refresh_post_label).execute();
    }

    /**
     * 目标
     *
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * 自动刷新
     */
    protected void autoRefresh() {
        layout.autoRefresh();
    }

    /**
     * 停止刷新
     */
    protected void stopRefresh() {
        layout.finishRefresh();
    }

    /**
     * 自动加载
     */
    protected void autoLoad() {
        layout.autoLoadmore();
    }

    /**
     * 停止加载
     */
    protected void stopLoad() {
        layout.finishLoadmore();
    }


    /**
     * 填充缓冲区参数
     */
    private void buildParam() {
        if (CollectUtil.isEmpty(cache_params)) return;
        Iterator<Map.Entry<String, String>> iterator = cache_params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            this.params.put(next.getKey(), next.getValue());
        }
    }

    /**
     * 获得请求链接
     *
     * @return
     */
    protected String getUrl() {
        return refreshUrl;
    }

    /**
     * 获得参数
     *
     * @return
     */
    protected Map<String, String> getParams() {
        return params;
    }


    public class RefreshCallback implements OnRefreshLoadmoreListener {
        @Override
        public void onLoadmore(RefreshLayout refreshlayout) {
            if (loadAgo) {
                RefreshBuilder builder = (RefreshBuilder) layout.getTag(0x008889659);
                if (!TextUtils.isEmpty(label)) {
                    TofuBusRx.get().executeLoadBeforeRetention(label, refreshlayout, builder);
                } else {
                    TofuBusRx.get().executeLoadBeforeRetention(refreshUrl, refreshlayout, builder);
                }
            } else {
                startCountOutTime();
                buildParam();
                if (result == null) {
                    Tofu.post().put(params).result(String.class).url(refreshUrl).label(refresh_post_label).execute();
                } else {
                    Tofu.post().put(params).result(result).url(refreshUrl).label(refresh_post_label).execute();
                }
            }
        }

        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            if (refreshAgo) {
                RefreshBuilder builder = (RefreshBuilder) layout.getTag(0x008889659);
                if (!TextUtils.isEmpty(label)) {
                    TofuBusRx.get().executeRefreshBeforeRetention(label, refreshlayout, builder);
                } else {
                    TofuBusRx.get().executeRefreshBeforeRetention(refreshUrl, refreshlayout, builder);
                }
            } else {
                startCountOutTime();
                buildParam();
                toFirst();
                if (result == null) {
                    Tofu.post().put(params).result(String.class).url(refreshUrl).label(refresh_post_label).execute();
                } else {
                    Tofu.post().put(params).result(result).url(refreshUrl).label(refresh_post_label).execute();
                }
            }
        }
    }

    @post(refresh_post_label)
    public synchronized void onPostRefreshLoadResult(Result result) {
        isCallFailedBeforeOutTime = true;
        RefreshBuilder builder = (RefreshBuilder) layout.getTag(0x008889659);
        if (layout.isRefreshing()) {
            layout.finishRefresh();
            if (!TextUtils.isEmpty(label)) {
                TofuBusRx.get().executeRefreshRetention(label, result);
            } else {
                TofuBusRx.get().executeRefreshRetention(refreshUrl, result);
            }
        } else if (layout.isLoading()) {
            layout.finishLoadmore();
            next();
            if (!TextUtils.isEmpty(label)) {
                TofuBusRx.get().executeLoadMoreRetention(label, result);
            } else {
                TofuBusRx.get().executeLoadMoreRetention(refreshUrl, result);
            }
        } else if (builder.getLayout() != null && (builder.getLayout().isRefreshing() || builder.getLayout().isLoading())) {
            if (builder.getLayout().isRefreshing()) {
                builder.getLayout().finishRefresh();
                TofuBusRx.get().executeRefreshRetention(builder.getLabel(), result);
            } else if (builder.getLayout().isLoading()) {
                builder.getLayout().finishLoadmore();
                TofuBusRx.get().executeLoadMoreRetention(builder.getLabel(), result);
            }
        }
    }

    @postError(refresh_post_label)
    public synchronized void onPostRefreshLoadFailed(String label, Boolean isOutTime) {
        isCallFailedBeforeOutTime = true;
        RefreshBuilder builder = (RefreshBuilder) layout.getTag(0x008889659);
        if (layout.isRefreshing()) {
            layout.finishRefresh();
            if (!TextUtils.isEmpty(this.label)) {
                TofuBusRx.get().executeRefreshFailedRetention(this.label, builder, isOutTime);
            } else {
                TofuBusRx.get().executeRefreshFailedRetention(refreshUrl, builder, isOutTime);
            }
        } else if (layout.isLoading()) {
            pre();
            layout.finishLoadmore();
            if (!TextUtils.isEmpty(this.label)) {
                TofuBusRx.get().executeLoadFailedRetention(this.label, builder, isOutTime);
            } else {
                TofuBusRx.get().executeLoadFailedRetention(refreshUrl, builder, isOutTime);
            }
        } else if (builder.getLayout() != null && (builder.getLayout().isRefreshing() || builder.getLayout().isLoading())) {
            if (builder.getLayout().isRefreshing()) {
                builder.getLayout().finishRefresh();
                TofuBusRx.get().executeRefreshFailedRetention(builder.getLabel(), builder, isOutTime);
            } else if (builder.getLayout().isLoading()) {
                builder.getLayout().finishLoadmore();
                pre();
                TofuBusRx.get().executeLoadFailedRetention(builder.getLabel(), builder, isOutTime);
            }
        }
    }

    /**
     * 开始超时计数
     */
    private void startCountOutTime() {
        isOutTimeBack = false;
        isCallFailedBeforeOutTime = false;
        Observable.create(new ObservableOnSubscribe<SmartRefreshLayout>() {
            @Override
            public void subscribe(ObservableEmitter<SmartRefreshLayout> e) throws Exception {
                e.onNext(layout);
            }
        }).delay(outTime > 0 ? outTime : TofuConfig.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .distinct()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SmartRefreshLayout>() {
                    @Override
                    public void accept(SmartRefreshLayout layout) throws Exception {
                        isOutTimeBack = true;
                        OkGo.getInstance().cancelTag(refreshUrl);
                        if (!isCallFailedBeforeOutTime) {
                            onPostRefreshLoadFailed(refresh_post_label, isOutTimeBack);
                        }
                        isOutTimeBack = false;
                    }
                });

    }

    /**
     * 解绑
     */
    protected void destroy() {
        layout = null;
        refreshAgo = false;
        loadAgo = false;
        params.clear();
        refreshUrl = null;
        result = null;
        page = 1;
        pageKey = "pageNo";
        outTime = -1;
        unBind();
    }


    public void unBind() {
        Tofu.unBind(this);
    }
}
