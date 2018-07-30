package com.pudding.tofu.model;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lzy.okgo.OkGo;
import com.zxy.recovery.callback.RecoveryCallback;
import com.zxy.recovery.core.Recovery;
import com.zxy.tiny.Tiny;

import net.tsz.afinal.FinalDb;

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

    private static FinalDb orm;


    private Tofu() {
        //todo nothing
    }

    /**
     * 初始化
     *
     * @param context
     */
    public static void initialize(@NonNull Application context) {
        initialize(context, false);
    }


    /**
     * 初始化
     *
     * @param context
     */
    public static void initialize(@NonNull Application context, boolean debug) {
        OkGo.init(context);
        OkGo.getInstance().debug("Tofu", debug);
        TofuConfig.debug(debug);
        Fresco.initialize(context);
        initRecovery(context, debug);
        Tiny.getInstance().debug(debug);
    }


    /**
     * 数据库初始化
     *
     * @param context
     * @param debug
     * @param dbName
     */
    public static void initialize(@NonNull Application context, boolean debug, @NonNull String dbName) {
        orm = FinalDb.create(context, dbName, debug);
        initialize(context, debug);
    }

    /**
     * 数据库初始化
     *
     * @param context
     * @param dbName
     */
    public static void initialize(@NonNull Application context, @NonNull String dbName) {
        orm = FinalDb.create(context, dbName, false);
        initialize(context);
    }

    /**
     * 数据库初始化
     *
     * @param context
     */
    public static void initialize(@NonNull Application context, FinalDb.DaoConfig daoConfig) {
        orm = FinalDb.create(daoConfig);
        initialize(context);
    }

    /**
     * 数据库初始化
     *
     * @param context
     */
    public static void initialize(@NonNull Application context, boolean debug, FinalDb.DaoConfig daoConfig) {
        orm = FinalDb.create(daoConfig);
        initialize(context, debug);
    }

    /**
     * crash框架
     *
     * @param context
     */
    private static void initRecovery(Application context, boolean debug) {
        Recovery.getInstance().debug(debug).callback(new RecoveryCallback() {
            @Override
            public void stackTrace(String stackTrace) {
                System.err.println("Tofu : " + stackTrace);
            }

            @Override
            public void cause(String cause) {
                System.err.println("Tofu :" + cause);
            }

            @Override
            public void exception(String throwExceptionType, String throwClassName, String throwMethodName, int throwLineNumber) {

            }

            @Override
            public void throwable(Throwable throwable) {

            }
        }).init(context);
    }

    /**
     * 绑定操作对象
     *
     * @param target
     * @return
     */
    public static void bind(@NonNull Object target) {
        TofuBus.get().findSubscribe(target);
    }


    /**
     * 数据库操作
     * <p>
     * 数据库建表，已com.pudding.tofu.Text为例
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
        return factory.build().setOrm(orm);
    }

    /**
     * xml数据存储
     *
     * @param context
     * @return
     */
    public static XmlOrmBuilder xml(@NonNull Context context) {
        XmlOrmFactory factory = (XmlOrmFactory) builders.get("XmlOrmFactory");
        if (factory == null) {
            factory = XmlOrmFactory.get();
            builders.put("XmlOrmFactory", factory);
        }
        return factory.build().onBindContext(context);
    }


    /**
     * 普通回调
     *
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
     * post请求
     * 响应post 、postError注解
     *
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
     *
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
     * 响应loadFile 、loadError、loadProgress注解
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
     * 响应upload、uploadError、uploadProgress注解
     *
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
     * 响应photoPick注解
     *
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
