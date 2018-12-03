package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/6/25 0025.
 * 邮箱：632716169@qq.com
 */

public class UploadBuilderFactory implements UnBind{

    private static class FactoryInstance {
        private static volatile UploadBuilderFactory INSTANCE = new UploadBuilderFactory();
        private static volatile UpLoadBuilder builder;

        private static UpLoadBuilder build() {
            synchronized (FactoryInstance.class) {
                if (builder == null) {
                    synchronized (FactoryInstance.class) {
                        if (builder == null) {
                            builder = new UpLoadBuilder();
                        }
                    }
                }
            }
            return builder;
        }
    }

   protected static UploadBuilderFactory get(){
        return FactoryInstance.INSTANCE;
   }

   protected UpLoadBuilder build(){
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
