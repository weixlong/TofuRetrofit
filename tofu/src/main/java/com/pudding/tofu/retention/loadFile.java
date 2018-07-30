package com.pudding.tofu.retention;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 * <p>
 * 下载文件返回注解，value请填写对应的label(loadProgress,loadError相同)
 * <p>
 * 返回参数为File,在使用注释时没有找到则不会调用
 * <p>
 * 如果返回值大于1，且返回值的第一个是String类型，则第一个返回值是label(loadProgress,loadError相同)
 * <p>
 * 如果lable相同则只响应最近绑定这个lable的这个对象(loadProgress,loadError相同)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface loadFile {
    String[] value();
}
