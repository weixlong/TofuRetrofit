package com.pudding.tofu.model;

public class TIOFactory implements UnBind {

    private static class instance {
        private volatile static TIOFactory INSTANCE = new TIOFactory();
        private volatile static TIOBuilder tio = new TIOBuilder();
    }

    private TIOFactory() {
    }

    public static TIOFactory get() {
        return instance.INSTANCE;
    }

    public TIOBuilder build() {
        return instance.tio;
    }

    @Override
    public void unbind() {
        instance.tio.unbind();
    }
}
