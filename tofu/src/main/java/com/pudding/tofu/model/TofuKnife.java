package com.pudding.tofu.model;

import android.app.Application;
import android.support.annotation.NonNull;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lzy.okgo.OkGo;
import com.zxy.recovery.callback.RecoveryCallback;
import com.zxy.recovery.core.Recovery;
import com.zxy.tiny.Tiny;

import net.tsz.afinal.FinalDb;

/**
 * Created by wxl on 2018/7/31 0031.
 * 邮箱：632716169@qq.com
 */

public class TofuKnife {

    protected TofuKnife() {
    }

    protected static FinalDb orm;

    protected static Application app;

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
        app = context;
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


}
