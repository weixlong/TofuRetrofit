package com.pudding.tofu.rx.retion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wxl on 2018/8/6 0006.
 * 邮箱：632716169@qq.com
 * 响应参数RefreshBuilder builder, Boolean isOutTime
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface loadMoreFailed {
    String[] value();
}
