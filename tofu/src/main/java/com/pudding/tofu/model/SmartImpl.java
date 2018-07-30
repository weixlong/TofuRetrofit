package com.pudding.tofu.model;

import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.pudding.tofu.callback.OnSmartRefreshBefore;
import com.pudding.tofu.callback.PostResultCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wxl on 2018/7/30 0030.
 * 邮箱：632716169@qq.com
 */

public class SmartImpl<Result> implements OnRefreshLoadmoreListener, PostResultCallback {

    private SmartRefreshLayout layout;

    private Class<Result> clazz;

    private String url;

    private Map<String,String> params ;

    private OnSmartRefreshBefore refreshBefore;

    private int outTime;

    private int page = 1;

    private PostImpl post = new PostImpl();

    private HttpParams param = new HttpParams();

    private boolean isLoadMore , isRefresh;

    private String label;

    private String pageKey;

    protected SmartImpl() {
    }

    protected SmartImpl(String pageKey, String label, SmartRefreshLayout layout, Class<Result> clazz, String url, Map<String, String> params, OnSmartRefreshBefore refreshBefore, int outTime) {
        this.layout = layout;
        this.clazz = clazz;
        this.url = url;
        this.params = params;
        this.refreshBefore = refreshBefore;
        this.outTime = outTime;
        this.label = label;
        this.pageKey = pageKey;
    }

    protected void setSmartImpl(String pageKey, String label, SmartRefreshLayout layout, Class<Result> clazz, String url, Map<String, String> params, OnSmartRefreshBefore refreshBefore, int outTime) {
        this.layout = layout;
        this.clazz = clazz;
        this.url = url;
        this.params = params;
        this.refreshBefore = refreshBefore;
        this.outTime = outTime;
        this.label = label;
        this.pageKey = pageKey;
    }

    protected void onSmart(){
        layout.setOnRefreshLoadmoreListener(this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        onDelayPost();
        isLoadMore = true;
        isRefresh = false;
        page++;
        params.put(pageKey,page+"");
        param.put(params);
        post.post(clazz,url,param,this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if(refreshBefore != null){
            refreshBefore.onRefreshBefore((SmartBuilder) layout.getTag(8999));
        } else {
            onDelayPost();
            isLoadMore = false;
            isRefresh = true;
            page = 1;
            params.put(pageKey,page+"");
            param.put(params);
            post.post(clazz,url,param,this);
        }
    }

    private void onDelayPost(){
        if(outTime>0) {
            Observable.interval(0, outTime, TimeUnit.MILLISECONDS)
                    .filter(new Predicate<Long>() {
                        @Override
                        public boolean test(Long aLong) throws Exception {
                            return layout.isRefreshing() || layout.isLoading();
                        }
                    }).distinct().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            OkGo.getInstance().cancelTag("post");
                            if (layout.isRefreshing()) {
                                layout.finishRefresh();
                                if(TextUtils.isEmpty(label)) {
                                    TofuBus.get().executeRefreshFailedMethod(url,url);
                                } else {
                                    TofuBus.get().executeRefreshFailedMethod(label,url);
                                }
                            }
                            if(layout.isLoading()){
                                layout.finishLoadmore();
                                goBackPage();
                                if(TextUtils.isEmpty(label)) {
                                    TofuBus.get().executeLoadMoreFailedMethod(url,url);
                                } else {
                                    TofuBus.get().executeLoadMoreFailedMethod(label,url);
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public <Result> void onSuccess(String url, Result result) {
        layout.finishRefresh();
        layout.finishLoadmore();
        if(isRefresh){
            if(TextUtils.isEmpty(label)) {
                TofuBus.get().executeRefreshMethod(url,result);
            } else {
                TofuBus.get().executeRefreshMethod(label,result);
            }
        } else if(isLoadMore){
            if(TextUtils.isEmpty(label)) {
                TofuBus.get().executeLoadMoreMethod(url,result);
            } else {
                TofuBus.get().executeLoadMoreMethod(label,result);
            }
        }
    }

    @Override
    public void onFailed(String response) {
        layout.finishRefresh();
        layout.finishLoadmore();
        if(isLoadMore){
            goBackPage();
            if(TextUtils.isEmpty(label)) {
                TofuBus.get().executeLoadMoreFailedMethod(url,response);
            } else {
                TofuBus.get().executeLoadMoreFailedMethod(label,response);
            }
        } else if(isRefresh){
            if(TextUtils.isEmpty(label)) {
                TofuBus.get().executeRefreshFailedMethod(url,response);
            } else {
                TofuBus.get().executeRefreshFailedMethod(label,response);
            }
        }
    }



    /**
     * 回退一页
     */
    protected void goBackPage(){
        page -- ;
        isLoadMore = false;
        isRefresh = false;
    }

    /**
     * 回到第一页
     */
    protected void toFirst(){
        page = 1;
        isLoadMore = false;
        isRefresh = false;
    }

    /**
     * 清除参数
     */
    protected void clear(){
        page = 1;
        isLoadMore = false;
        isRefresh = false;
        param.clear();
        if(params != null) {
            params.clear();
        }
    }
}
