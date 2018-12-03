package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/8/20 0020.
 * 邮箱：632716169@qq.com
 */

public class ToastFactory implements UnBind {



    private static class FactoryInstance {
        private static volatile ToastFactory INSTANCE = new ToastFactory();
        private static volatile ToastBuilder builder;

        private static ToastBuilder build() {
            synchronized (FactoryInstance.class) {
                if (builder == null) {
                    synchronized (FactoryInstance.class) {
                        if (builder == null) {
                            builder = new ToastBuilder();
                        }
                    }
                }
            }
            return builder;
        }
    }

    protected static ToastFactory get() {
        return FactoryInstance.INSTANCE;
    }

    protected ToastBuilder build() {
        return FactoryInstance.build();
    }


    protected ToastFactory() {
    }

    @Override
    public void unbind() {
        if (FactoryInstance.builder != null) {
            FactoryInstance.builder.unbind();
        }
    }
}
