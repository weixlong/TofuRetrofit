package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/6/26 0026.
 * 邮箱：632716169@qq.com
 */

public class SimpleBuilderFactory implements UnBind{

    private static SimpleBuilderFactory factory = new SimpleBuilderFactory();

    private SimpleBuilder builder;

    private SimpleBuilderFactory() {
    }

    protected static SimpleBuilderFactory get(){
        synchronized (SimpleBuilderFactory.class){
            return factory;
        }
    }

    protected SimpleBuilder build(){
        if(builder == null){
            builder = new SimpleBuilder();
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
