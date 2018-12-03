package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class ImageBuilderFactory implements UnBind {

    private static class FactoryInstance {
        private static volatile ImageBuilderFactory INSTANCE = new ImageBuilderFactory();
        private static volatile ImageBuilder builder ;
        private static ImageBuilder build(){
            synchronized (FactoryInstance.class){
                if(builder ==  null){
                    synchronized (FactoryInstance.class){
                        if(builder == null){
                            builder = new ImageBuilder();
                        }
                    }
                }
            }
            return builder;
        }
    }

    protected static ImageBuilderFactory get(){
        return FactoryInstance.INSTANCE;
    }

    private ImageBuilderFactory(){

    }

    protected ImageBuilder build(){
        return FactoryInstance.build();
    }

    @Override
    public void unbind() {
        if(FactoryInstance.builder != null) {
            FactoryInstance.builder.unbind();
            FactoryInstance.builder = null;
        }
    }
}
