package com.pudding.tofu.model;

/**
 * Created by wxl on 2018/6/25 0025.
 * 邮箱：632716169@qq.com
 */

public class UploadBuilderFactory implements UnBind{

   private static UploadBuilderFactory factory = new UploadBuilderFactory();


   private UpLoadBuilder builder;

   protected static UploadBuilderFactory get(){
       synchronized (UploadBuilderFactory.class){
           return factory;
       }
   }

   protected UpLoadBuilder build(){
       if(builder == null){
           builder = new UpLoadBuilder();
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
