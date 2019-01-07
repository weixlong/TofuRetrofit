package com.pudding.tofu.retention;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 穿透注解
 * <p>
 *     执行该注解且参数符合一一对应的所有注册方法
 * </p>
 * 执行顺序为注册的先后顺序
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface pierce {
    String[] value();
}
