package com.pudding.tofu.rx.widget;


import com.pudding.tofu.rx.callback.UnBindRx;

/**
 * Created by wxl on 2018/8/6 0006.
 * 邮箱：632716169@qq.com
 */

public class RefreshFactory implements UnBindRx {

    private static RefreshFactory factory = new RefreshFactory();

    private RefreshBuilder builder;

    public static RefreshFactory get(){
        synchronized (RefreshFactory.class){
            return factory;
        }
    }

    public RefreshBuilder builder(){
        if(builder == null){
            builder = new RefreshBuilder();
        }
        return builder;
    }


    @Override
    public void unBind() {
        if(builder != null){
            builder.unBind();
            builder = null;
        }
    }
}
