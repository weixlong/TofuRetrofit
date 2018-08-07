package com.pudding.tofu.model;



/**
 * Created by wxl on 2018/8/6 0006.
 * 邮箱：632716169@qq.com
 */

public class EventFactory implements UnBind {

    private static EventFactory factory = new EventFactory();

    private EventBuilder builder;

    public static EventFactory get(){
        synchronized (EventFactory.class){
            return factory;
        }
    }

    public EventBuilder build(){
        if(builder == null){
            builder = new EventBuilder();
        }
        return builder;
    }



    @Override
    public void unbind() {
        if(builder != null){
            builder.unbind();
            builder = null;
        }
    }
}
