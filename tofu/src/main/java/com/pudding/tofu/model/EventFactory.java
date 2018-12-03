package com.pudding.tofu.model;



/**
 * Created by wxl on 2018/8/6 0006.
 * 邮箱：632716169@qq.com
 */

public class EventFactory implements UnBind {

    private static class FactoryInstance {
        private static volatile EventFactory INSTANCE = new EventFactory();
        private static volatile EventBuilder builder ;
        private static EventBuilder build(){
            synchronized (FactoryInstance.class){
                if(builder ==  null){
                    synchronized (FactoryInstance.class){
                        if(builder == null){
                            builder = new EventBuilder();
                        }
                    }
                }
            }
            return builder;
        }
    }


    public static EventFactory get(){
        return FactoryInstance.INSTANCE;
    }

    public EventBuilder build(){
        return FactoryInstance.build();
    }


    private EventFactory() {
    }

    @Override
    public void unbind() {
        if(FactoryInstance.builder != null){
            FactoryInstance.builder.unbind();
            FactoryInstance.builder = null;
        }
    }
}
