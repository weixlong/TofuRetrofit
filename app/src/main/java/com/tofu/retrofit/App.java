package com.tofu.retrofit;

import android.app.Application;

import com.pudding.tofu.model.Tofu;

/**
 * Created by wxl on 2018/7/27 0027.
 * 邮箱：632716169@qq.com
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
//        FlowManager.init(this);
        Tofu.initialize(this,true,"Tofu.db");
    }
}
