package com.pudding.tofu.retention;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 * <p>
 * 网络请求返回注解，value请填写对应的label
 * <p>
 * 返回值可以是不同类型的参数，如果返回类型相同则会被覆盖
 * <p>
 * 如果返回值大于1，且返回值的第一个是String类型，则第一个返回值是label
 * <p>
 *     如果lable相同则只响应最近绑定这个lable的这个对象
 *     <p>
 *         同一个对象注册相同的lable只会执行第一次注册的lable
 *     </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface post {
    String[] value();
}
