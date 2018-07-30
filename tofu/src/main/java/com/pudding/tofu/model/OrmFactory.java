package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/7/27 0027.
 * 邮箱：632716169@qq.com
 */

public class OrmFactory implements UnBind{

    private static OrmFactory factory = new OrmFactory();

    private OrmBuilder builder;

    protected OrmFactory() {

    }

    protected static OrmFactory get(){
        synchronized (OrmFactory.class){
            return factory;
        }
    }


    protected OrmBuilder build(){
        if(builder == null){
            builder = new OrmBuilder();
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
