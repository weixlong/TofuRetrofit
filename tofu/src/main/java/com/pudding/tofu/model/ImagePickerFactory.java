package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/6/27 0027.
 * 邮箱：632716169@qq.com
 */

public class ImagePickerFactory implements UnBind{

    private static ImagePickerFactory factory = new ImagePickerFactory();

    private ImagePickerBuilder builder;

    protected static ImagePickerFactory get(){
        synchronized (ImagePickerFactory.class){
            return factory;
        }
    }

    private ImagePickerFactory() {
    }

    protected ImagePickerBuilder build(){
        if(builder == null){
            builder = new ImagePickerBuilder();
        }
        builder.clear();
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
