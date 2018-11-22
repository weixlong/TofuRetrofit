package com.pudding.tofu.rx.widget;


import com.pudding.tofu.rx.callback.UnBindRx;

/**
 * Created by wxl on 2018/8/20 0020.
 * 邮箱：632716169@qq.com
 */

public class PayFactory implements UnBindRx {

    private static volatile PayFactory factory = new PayFactory();

    private PayBuilder builder;

    public static PayFactory get(){
        synchronized (PayFactory.class){
            return factory;
        }
    }

    public PayBuilder build(){
        if(builder == null){
            builder = new PayBuilder();
        }
        return builder;
    }

    protected PayFactory() {
    }

    @Override
    public void unBind() {
        if(builder != null){
            builder.unBind();
            builder = null;
        }
    }
}
