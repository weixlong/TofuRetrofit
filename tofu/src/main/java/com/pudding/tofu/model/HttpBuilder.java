package com.pudding.tofu.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lzy.okgo.model.HttpParams;
import com.pudding.tofu.callback.BaseInterface;
import com.pudding.tofu.callback.PostInterface;
import com.pudding.tofu.widget.CollectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class HttpBuilder<Result> implements UnBind {


    /**
     * 参数集合
     */
    private HttpParams params = new HttpParams();

    /**
     * 请求链接
     */
    private String url;

    /**
     * 请求返回结果
     */
    private Class<Result> resultClass;

    /**
     * 解绑集合
     */
    private List<BaseInterface> unBinds = new ArrayList<>();

    /**
     * 是否可以显示进度
     */
    private boolean isShowDialog = false;

    /**
     * 上下文对象
     */
    private Context context;

    /**
     * 请求实体
     */
    private PostInterface http;

    /**
     * 缓存参数
     */
    private CacheBuilder cacheBuilder;

    /**
     * label
     */
    private String label;

    protected HttpBuilder() {
    }

    /**
     * 清除参数
     */
    protected void clear() {
        params.clear();
        context = null;
        isShowDialog = false;
        resultClass = null;
    }


    /**
     * 存请求参数
     *
     * @param key
     * @param param
     * @return
     */
    public HttpBuilder put(@NonNull String key, @NonNull String param) {
        params.put(key, param);
        return this;
    }

    /**
     * 请求链接
     * 如果没有设置label ,则label为url
     * @param url
     * @return
     */
    public HttpBuilder url(@NonNull String url) {
        this.url = url;
        return this;
    }

    /**
     * 设置label，如果没有设置则label为url
     * @param label
     * @return
     */
    public HttpBuilder label(String label) {
        this.label = label;
        return this;
    }

    /**
     * 请求的返回结果
     *
     * @param result
     */
    public HttpBuilder result(@NonNull Class<Result> result) {
        this.resultClass = result;
        return this;
    }

    /**
     * 显示dialog
     *
     * @return
     */
    public HttpBuilder asDialog(@NonNull Context context) {
        isShowDialog = true;
        this.context = context;
        return this;
    }


    /**
     * 获取一个参数缓存
     * <p>
     * 需要手动管理缓存
     * </p>
     * @return
     */
    public CacheBuilder cache(@NonNull String cacheKey) {
        if (cacheBuilder == null) {
            cacheBuilder = new CacheBuilder();
        }
        cacheBuilder.add(cacheKey);
        return cacheBuilder;
    }

    /**
     * 填充缓存区参数
     * @param cache
     * @return
     */
    private HttpBuilder cacheIntoPost(HashMap<String,String> cache){
        params.clear();
        Iterator<Map.Entry<String, String>> iterator = cache.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> next = iterator.next();
            params.put(next.getKey(),next.getValue());
        }
        return this;
    }

    /**
     * 执行
     * <p>
     * 回调到注解 post 或 postError中
     */
    public void execute() {
        checkExecuteAvailable();
        if (http == null) {
            http = new HttpImpl(params, url, resultClass,label);
        } else {
            ((HttpImpl) http).setHttpImpl(params, url, resultClass,label);
        }
        if (isShowDialog) {
            http.showDialog(context);
        }
        http.execute();
        unBinds.add(http);
    }


    /**
     * 参数检查
     */
    private void checkExecuteAvailable() {
        if (resultClass == null) throw new NullPointerException("your result class is null !");
        if (TextUtils.isEmpty(url)) throw new NullPointerException("your post url is null !");
    }


    /**
     * 参数缓存区
     */
    public class CacheBuilder {

        private HashMap<String ,HashMap<String,String>>  cache = new HashMap();

        private String cacheKey;

        private CacheBuilder() {

        }

        /**
         * 添加一个缓存区
         * @param key
         */
        private void add(String key){
            if(TextUtils.isEmpty(key)){
                System.out.println("you has nothing register post retention , cache name set default cache!");
                cacheKey = "cache";
            } else {
                cacheKey = key;
            }
            if(!cache.containsKey(cacheKey)) {
                cache.put(cacheKey, new HashMap<String, String>());
            }
        }

        /**
         * 缓存参数
         * @param key
         * @param value
         * @return
         */
        public CacheBuilder put(@NonNull String key, @NonNull String value){
            cache.get(cacheKey).put(key,value);
            return this;
        }

        /**
         * 移除参数
         * @param key
         * @return
         */
        public CacheBuilder remove(@NonNull String key){
            cache.get(cacheKey).remove(key);
            return this;
        }

        /**
         * 是否有该参数
         * @param key
         * @return
         */
        public boolean isContains(@NonNull String key){
           return !TextUtils.isEmpty(cache.get(cacheKey).get(key));
        }

        /**
         * 获取缓冲区参数
         * @return
         */
        public HashMap<String,String> getCache(){
            return cache.get(cacheKey);
        }

        /**
         * 获取String参数值
         * @param key
         * @return
         */
        public String getValue(@NonNull String key){
            return cache.get(cacheKey).get(key);
        }

        /**
         * 参数填充到post中 , 原post中的参数将会被清空后再填充
         * @return
         */
        public HttpBuilder beCache(){
           return cacheIntoPost(cache.get(cacheKey));
        }

        /**
         * 清空缓存区参数
         * <p>
         *     key为全类名
         * </p>
         */
        public void clear(){
            if(TextUtils.isEmpty(cacheKey)){
                cacheKey = "cache";
            }
            HashMap<String, String> map = cache.get(cacheKey);
            if(!CollectUtil.isEmpty(map)){
                map.clear();
            }
        }
    }

    @Override
    public void unbind() {
        for (BaseInterface unBind : unBinds) {
            unBind.unBind();
        }
        params.clear();
        unBinds.clear();
        cacheBuilder = null;
    }
}
