package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/7/18 0018.
 * 邮箱：632716169@qq.com
 */

public class AskFactory implements UnBind {

    private static class FactoryInstance {
        private static volatile AskFactory INSTANCE = new AskFactory();
        private static volatile AskBuilder builder ;
        private static AskBuilder build(){
            synchronized (FactoryInstance.class){
                if(builder ==  null){
                    synchronized (FactoryInstance.class){
                        if(builder == null){
                            builder = new AskBuilder();
                        }
                    }
                }
            }
            return builder;
        }
    }



    protected AskFactory() {
    }

    protected static AskFactory get() {
        return FactoryInstance.INSTANCE;
    }

    protected AskBuilder build() {
        return FactoryInstance.build();
    }

    @Override
    public void unbind() {
        if (FactoryInstance.builder != null) {
            FactoryInstance.builder.unbind();
        }
        FactoryInstance.builder = null;
    }
}
