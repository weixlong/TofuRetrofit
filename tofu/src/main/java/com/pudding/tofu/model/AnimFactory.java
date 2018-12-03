package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/8/24 0024.
 * 邮箱：632716169@qq.com
 */

public class AnimFactory implements UnBind {

    private static class FactoryInstance {
        private static volatile AnimFactory INSTANCE = new AnimFactory();
    }

    private AnimBuilder builder;

    protected static AnimFactory get() {
        return FactoryInstance.INSTANCE;
    }

    protected AnimBuilder build() {
        synchronized (AnimFactory.class) {
            if (builder == null) {
                synchronized (AnimFactory.class) {
                    if(builder == null) {
                        builder = new AnimBuilder();
                    }
                }
            }
        }
        return builder;
    }

    private AnimFactory() {
    }

    @Override
    public void unbind() {
        if (builder != null) {
            builder.unbind();
            builder = null;
        }
    }
}
