package com.pudding.tofu.model;

import android.support.annotation.NonNull;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by wxl on 2018/5/14 0014.
 * 邮箱：632716169@qq.com
 */

public class Tofu {

    /**
     * 接绑缓存
     */
    private static HashMap<String, UnBind> builders = new HashMap<>();




    private Tofu() {
        //todo nothing
    }


    /**
     * 绑定操作对象
     * @param target
     * @return
     */
    public static void bind(@NonNull Object target) {
        TofuBus.get().findSubscribe(target);
    }



    /**
     * 数据库操作
     * <p>
     * 数据库建表，已{@link com.pudding.tofu.OrmText}为例
     * <p>
     * Table 注解，必须写
     * <p>
     * Id 注解，必须写
     * <p>
     * 每个变量都必须有get和set方法
     * </p>
     */
    public static OrmBuilder orm() {
        OrmFactory factory = (OrmFactory) builders.get("orm");
        if (factory == null) {
            factory = OrmFactory.get();
            builders.put("orm", factory);
        }
        return factory.build().setOrm(TofuKnife.orm);
    }

    /**
     * xml数据存储
     * @return
     */
    public static XmlOrmBuilder xml() {
        XmlOrmFactory factory = (XmlOrmFactory) builders.get("XmlOrmFactory");
        if (factory == null) {
            factory = XmlOrmFactory.get();
            builders.put("XmlOrmFactory", factory);
        }
        return factory.build().onBindContext(TofuKnife.app);
    }


    /**
     * 普通回调
     * 主程中执行回调方法
     * @return
     */
    public static SimpleBuilder go() {
        SimpleBuilderFactory factory = (SimpleBuilderFactory) builders.get("SimpleBuilderFactory");
        if (factory == null) {
            factory = SimpleBuilderFactory.get();
            builders.put("SimpleBuilderFactory", factory);
        }
        return factory.build();
    }


    /**
     * 线程或io中执行方法
     * <p>
     *     切回主程可调用其他注解回调如{@link com.pudding.tofu.retention.subscribe }
     * </p>
     * @return
     */
    public static TIOBuilder tio(){
        TIOFactory factory = (TIOFactory) builders.get("TIOFactory");
        if(factory == null){
            factory = TIOFactory.get();
            builders.put("TIOFactory",factory);
        }
        return factory.build();
    }


    /**
     * post请求
     * 响应{@link com.pudding.tofu.retention.post}{@link com.pudding.tofu.retention.postError}注解
     * 主程中执行回调方法
     * @return
     */
    public static HttpBuilder post() {
        HttpBuilderFactory factory = (HttpBuilderFactory) builders.get("HttpBuilderFactory");
        if (factory == null) {
            factory = HttpBuilderFactory.get();
            builders.put("HttpBuilderFactory", factory);
        }
        return factory.build();
    }

    /**
     * 图片加载
     * @return
     */
    public static ImageBuilder image() {
        ImageBuilderFactory factory = (ImageBuilderFactory) builders.get("ImageBuilderFactory");
        if (factory == null) {
            factory = ImageBuilderFactory.get();
            builders.put("ImageBuilderFactory", factory);
        }
        return factory.build();
    }

    /**
     * 下载文件
     * 响应{@link com.pudding.tofu.retention.loadFile} 、
     * {@link com.pudding.tofu.retention.loadError}、
     * {@link com.pudding.tofu.retention.loadProgress}注解
     * 主程中执行回调方法
     */
    public static LoadFileBuilder load() {
        LoadBuilderFactory factory = (LoadBuilderFactory) builders.get("LoadBuilderFactory");
        if (factory == null) {
            factory = LoadBuilderFactory.get();
            builders.put("LoadBuilderFactory", factory);
        }
        return factory.build();
    }


    /**
     * 上传文件
     * 响应{@link com.pudding.tofu.retention.upload}、
     * {@link com.pudding.tofu.retention.uploadError}、
     * {@link com.pudding.tofu.retention.uploadProgress}注解
     * 主程中执行回调方法
     * @return
     */
    public static UpLoadBuilder upload() {
        UploadBuilderFactory factory = (UploadBuilderFactory) builders.get("UploadBuilderFactory");
        if (factory == null) {
            factory = UploadBuilderFactory.get();
            builders.put("UploadBuilderFactory", factory);
        }
        return factory.build();
    }


    /**
     * 图片选择
     * 响应{@link com.pudding.tofu.retention.photoPick}注解
     * 主程中执行回调方法
     * @return
     */
    public static ImagePickerBuilder pick() {
        ImagePickerFactory factory = (ImagePickerFactory) builders.get("ImagePickerFactory");
        if (factory == null) {
            factory = ImagePickerFactory.get();
            builders.put("ImagePickerFactory", factory);
        }
        return factory.build();
    }

    /**
     * 请求权限
     *
     * @return
     */
    public static AskBuilder ask() {
        AskFactory factory = (AskFactory) builders.get("AskFactory");
        if (factory == null) {
            factory = AskFactory.get();
            builders.put("AskFactory", factory);
        }
        return factory.build();
    }


    /**
     * 事件管理
     * 主程中执行回调方法
     * @return
     */
    public static EventBuilder event(){
        EventFactory factory = (EventFactory) builders.get("event");
        if(factory == null){
            factory = EventFactory.get();
            builders.put("event",factory);
        }
        return factory.build();
    }

    /**
     * Toast
     * @return
     */
    public static ToastBuilder tu(){
        ToastFactory factory = (ToastFactory) builders.get("toast");
        if(factory == null){
            factory = ToastFactory.get();
            builders.put("toast",factory);
        }
        return factory.build();
    }

    /**
     * LOG
     * @return
     */
    public static LogBuilder log(){
        LogFactory factory = (LogFactory) builders.get("log");
        if(factory == null){
            factory = LogFactory.get();
            builders.put("log",factory);
        }
        return factory.build();
    }


    /**
     * 动画
     * @return
     */
    public static AnimBuilder anim(){
        AnimFactory factory = (AnimFactory) builders.get("anim");
        if(factory == null){
            factory = AnimFactory.get();
            builders.put("anim",factory);
        }
        return factory.build();
    }

    /**
     * 解绑
     *
     * @param target
     */
    public static void unBind(@NonNull Object target) {
        Iterator<Map.Entry<String, UnBind>> iterator = builders.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, UnBind> next = iterator.next();
            next.getValue().unbind();
        }
        TofuBus.get().unBindTarget(target);
        builders.clear();
    }

}
