package com.pudding.tofu.retention;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wxl on 2018/6/27 0027.
 * 邮箱：632716169@qq.com
 * 响应参数类型为ArrayList<String></>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface photoPick {
    String[] value();
}
