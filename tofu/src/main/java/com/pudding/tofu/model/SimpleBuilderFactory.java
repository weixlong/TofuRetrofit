package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/6/26 0026.
 * 邮箱：632716169@qq.com
 */

public class SimpleBuilderFactory implements UnBind{


    private static class FactoryInstance {
        private static volatile SimpleBuilderFactory INSTANCE = new SimpleBuilderFactory();
        private static volatile SimpleBuilder builder;

        private static SimpleBuilder build() {
            synchronized (FactoryInstance.class) {
                if (builder == null) {
                    synchronized (FactoryInstance.class) {
                        if (builder == null) {
                            builder = new SimpleBuilder();
                        }
                    }
                }
            }
            return builder;
        }
    }

    private SimpleBuilderFactory() {
    }

    protected static SimpleBuilderFactory get(){
        return FactoryInstance.INSTANCE;
    }

    protected SimpleBuilder build(){
        return FactoryInstance.build();
    }

    @Override
    public void unbind() {
        if(FactoryInstance.builder != null){
            FactoryInstance.builder.unbind();
            FactoryInstance.builder = null;
        }
    }
}
