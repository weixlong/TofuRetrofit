package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/7/12 0012.
 * 邮箱：632716169@qq.com
 */

public class XmlOrmFactory implements UnBind {


    private static class FactoryInstance {
        private static volatile XmlOrmFactory INSTANCE = new XmlOrmFactory();
        private static volatile XmlOrmBuilder builder;

        private static XmlOrmBuilder build() {
            synchronized (FactoryInstance.class) {
                if (builder == null) {
                    synchronized (FactoryInstance.class) {
                        if (builder == null) {
                            builder = new XmlOrmBuilder();
                        }
                    }
                }
            }
            return builder;
        }
    }

    private XmlOrmFactory() {
    }

    protected static XmlOrmFactory get(){
        return FactoryInstance.INSTANCE;
    }


    protected XmlOrmBuilder build(){
        return FactoryInstance.build();
    }

    @Override
    public void unbind() {
        if(FactoryInstance.builder != null){
            FactoryInstance.builder.unbind();
        }
    }
}
