package com.pudding.tofu.retention;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wxl on 2018/6/25 0025.
 * 邮箱：632716169@qq.com
 * float progress
 * 如果lable相同则只响应最近绑定这个lable的这个对象
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface uploadProgress {
    String[] value();
}
