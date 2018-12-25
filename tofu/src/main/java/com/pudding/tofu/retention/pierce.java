package com.pudding.tofu.retention;


/**
 * 穿透注解
 * <p>
 *     执行该注解且参数符合一一对应的所有注册方法
 * </p>
 */
public @interface pierce {
    String[] value();
}
