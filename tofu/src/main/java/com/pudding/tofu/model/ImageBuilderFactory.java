package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class ImageBuilderFactory implements UnBind {

    private static ImageBuilderFactory factory = new ImageBuilderFactory();

    private ImageBuilder builder;

    protected static ImageBuilderFactory get(){
        synchronized (ImageBuilderFactory.class){
            return factory;
        }
    }

    private ImageBuilderFactory(){

    }

    protected ImageBuilder build(){
        if(builder == null){
            builder = new ImageBuilder();
        }
        return builder;
    }

    @Override
    public void unbind() {
        if(builder != null) {
            builder.unbind();
            builder = null;
        }
    }
}
