package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/6/25 0025.
 * 邮箱：632716169@qq.com
 */

public class LoadBuilderFactory implements UnBind {

    private static LoadBuilderFactory factory = new LoadBuilderFactory();

    private LoadFileBuilder builder;

    private LoadBuilderFactory() {
    }

    protected static LoadBuilderFactory get() {
        synchronized (LoadBuilderFactory.class) {
            return factory;
        }
    }

    protected LoadFileBuilder build() {
        if(builder == null){
            builder = new LoadFileBuilder();
        }
        builder.clear();
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
