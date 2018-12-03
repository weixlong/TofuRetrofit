package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/7/27 0027.
 * 邮箱：632716169@qq.com
 */

public class OrmFactory implements UnBind {


    private static class FactoryInstance {
        private static volatile OrmFactory INSTANCE = new OrmFactory();
        private static volatile OrmBuilder builder;

        private static OrmBuilder build() {
            synchronized (FactoryInstance.class) {
                if (builder == null) {
                    synchronized (FactoryInstance.class) {
                        if (builder == null) {
                            builder = new OrmBuilder();
                        }
                    }
                }
            }
            return builder;
        }
    }

    protected OrmFactory() {

    }

    protected static OrmFactory get() {
        return FactoryInstance.INSTANCE;
    }


    protected OrmBuilder build() {
        return FactoryInstance.build();
    }


    @Override
    public void unbind() {
        if (FactoryInstance.builder != null) {
            FactoryInstance.builder.unbind();
            FactoryInstance.builder = null;
        }
    }
}
