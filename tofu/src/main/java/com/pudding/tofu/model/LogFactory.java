package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/8/20 0020.
 * 邮箱：632716169@qq.com
 */

public class LogFactory implements UnBind {


    private static class FactoryInstance {
        private static volatile LogFactory INSTANCE = new LogFactory();
        private static volatile LogBuilder builder;

        private static LogBuilder build() {
            synchronized (FactoryInstance.class) {
                if (builder == null) {
                    synchronized (FactoryInstance.class) {
                        if (builder == null) {
                            builder = new LogBuilder();
                        }
                    }
                }
            }
            return builder;
        }
    }

    protected static LogFactory get() {
        return FactoryInstance.INSTANCE;
    }

    protected LogBuilder build() {
        return FactoryInstance.build();
    }

    protected LogFactory() {
    }

    @Override
    public void unbind() {
        if (FactoryInstance.builder != null) {
            FactoryInstance.builder.unbind();
            FactoryInstance.builder = null;
        }
    }
}
