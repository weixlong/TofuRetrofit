package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/6/27 0027.
 * 邮箱：632716169@qq.com
 */

public class ImagePickerFactory implements UnBind{

    private static class FactoryInstance {
        private static volatile ImagePickerFactory INSTANCE = new ImagePickerFactory();
        private static volatile ImagePickerBuilder builder ;
        private static ImagePickerBuilder build(){
            synchronized (FactoryInstance.class){
                if(builder ==  null){
                    synchronized (FactoryInstance.class){
                        if(builder == null){
                            builder = new ImagePickerBuilder();
                        }
                    }
                }
            }
            return builder;
        }
    }

    protected static ImagePickerFactory get(){
        return FactoryInstance.INSTANCE;
    }

    private ImagePickerFactory() {
    }

    protected ImagePickerBuilder build(){
        FactoryInstance.build().clear();
        return FactoryInstance.builder;
    }

    @Override
    public void unbind() {
        if(FactoryInstance.builder != null){
            FactoryInstance.builder.unbind();
            FactoryInstance.builder = null;
        }
    }
}
