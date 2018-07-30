package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/7/12 0012.
 * 邮箱：632716169@qq.com
 */

public class XmlOrmFactory implements UnBind {

   private volatile static XmlOrmFactory factory = new XmlOrmFactory();

   private XmlOrmBuilder builder;

    private XmlOrmFactory() {
    }

    protected static XmlOrmFactory get(){
        synchronized (XmlOrmFactory.class){
            return factory;
        }
    }


    protected XmlOrmBuilder build(){
        if(builder == null){
            builder = new XmlOrmBuilder();
        }
        return builder;
    }

    @Override
    public void unbind() {
        if(builder != null){
            builder.unbind();
        }
    }
}
