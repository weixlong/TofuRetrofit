package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/7/30 0030.
 * 邮箱：632716169@qq.com
 */

public class SmartFactory implements UnBind {
    private static SmartFactory factory = new SmartFactory();

    private SmartBuilder builder ;

    protected static SmartFactory get(){
        synchronized (SmartFactory.class){
            return factory;
        }
    }

    protected SmartFactory() {
    }

    protected SmartBuilder build(){
        if(builder == null){
            builder = new SmartBuilder();
        }
        return builder;
    }

    @Override
    public void unbind() {
        if(builder != null){
            builder.unbind();
            builder = null;
        }
    }
}
