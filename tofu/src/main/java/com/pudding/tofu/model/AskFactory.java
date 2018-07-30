package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/7/18 0018.
 * 邮箱：632716169@qq.com
 */

public class AskFactory implements UnBind{

    private static volatile AskFactory factory = new AskFactory();

    private AskBuilder builder;

    protected AskFactory() {
    }

    protected static AskFactory get(){
        synchronized (AskFactory.class){
            return factory;
        }
    }

    protected AskBuilder build(){
        if(builder == null){
            builder = new AskBuilder();
        }
        return builder;
    }

    @Override
    public void unbind() {
        if(builder != null){
            builder.unbind();
        }
        builder = null;
    }
}
