package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/6/25 0025.
 * 邮箱：632716169@qq.com
 */

public class LoadBuilderFactory implements UnBind {


    private static class FactoryInstance {
        private static volatile LoadBuilderFactory INSTANCE = new LoadBuilderFactory();
        private static volatile LoadFileBuilder builder ;
        private static LoadFileBuilder build(){
            synchronized (FactoryInstance.class){
                if(builder ==  null){
                    synchronized (FactoryInstance.class){
                        if(builder == null){
                            builder = new LoadFileBuilder();
                        }
                    }
                }
            }
            return builder;
        }
    }

    private LoadBuilderFactory() {
    }

    protected static LoadBuilderFactory get() {
        return FactoryInstance.INSTANCE;
    }

    protected LoadFileBuilder build() {
        FactoryInstance.build().clear();
        return FactoryInstance.builder;
    }

    @Override
    public void unbind() {
        if(FactoryInstance.builder != null){
            FactoryInstance.builder.unbind();
            FactoryInstance.builder = null;
        }
    }
}
