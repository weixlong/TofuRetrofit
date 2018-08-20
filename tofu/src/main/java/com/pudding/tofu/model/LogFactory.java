package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/8/20 0020.
 * 邮箱：632716169@qq.com
 */

public class LogFactory implements UnBind{

    private LogBuilder builder;

    private static volatile LogFactory factory = new LogFactory();

    protected static LogFactory get(){
        synchronized (LogFactory.class){
            return factory;
        }
    }

    protected LogBuilder build(){
        if(builder == null){
            builder = new LogBuilder();
        }
        return builder;
    }

    protected LogFactory() {
    }

    @Override
    public void unbind() {
        if(builder != null){
            builder.unbind();
        }
    }
}
