package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/8/20 0020.
 * 邮箱：632716169@qq.com
 */

public class ToastFactory implements UnBind {

    private ToastBuilder builder;

    private static volatile ToastFactory factory = new ToastFactory();

    protected static ToastFactory get() {
        synchronized (ToastFactory.class) {
            return factory;
        }
    }

    protected ToastBuilder build() {
        if (builder == null) {
            builder = new ToastBuilder();
        }
        return builder;
    }


    protected ToastFactory() {
    }

    @Override
    public void unbind() {
        if (builder != null) {
            builder.unbind();
        }
    }
}
