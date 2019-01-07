package com.pudding.tofu.model;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by wxl on 2018/6/26 0026.
 * 邮箱：632716169@qq.com
 */

public class SimpleBuilder implements UnBind {


    /**
     * 目标对象
     */
    private HashMap<String, Object> targets = new HashMap<>();


    protected SimpleBuilder() {
    }


    /**
     * <p>
     * 执行label回调
     * <p>
     * 如果lable相同将会执行最近一次注册的方法
     * <p>
     * 回调{@link com.pudding.tofu.retention.subscribe}注解
     * <p>
     * 使用该方法时请注意参数必须一一对应
     *
     * @param label
     * @param results
     */
    public <Result> void to(@NonNull String label, Result... results) {
        checkParamIsAvailable(label);
        TofuBus.get().executeSimpleMethod(label, results);
    }

    /**
     * 延时发送
     * <p>
     * 执行label回调
     * <p>
     * 如果lable相同将会执行最近一次注册的方法
     * <p>
     * 回调{@link com.pudding.tofu.retention.subscribe}注解
     * <p>
     * 使用该方法时请注意参数必须一一对应
     *
     * @param label
     * @param delay    延时时间
     * @param results
     * @param <Result>
     */
    public <Result> Disposable delayTo(@NonNull String label, @IntRange(from = 0) int delay, Result... results) {
        checkParamIsAvailable(label);
        return TofuBus.get().executeSimpleDelayMethod(label, delay, results);
    }

    /**
     * 循环发送
     * <p>
     * 执行label回调
     * <p>
     * 如果lable相同将会执行最近一次注册的方法
     * <p>
     * 回调{@link com.pudding.tofu.retention.subscribe}注解
     * <p>
     * 使用该方法时请注意参数必须一一对应
     *
     * @param label
     * @param interval 间隔时间
     * @param results
     * @param <Result>
     */
    public <Result> Disposable valTo(@NonNull String label, @IntRange(from = 0) int interval, Result... results) {
        checkParamIsAvailable(label);
        return TofuBus.get().executeSimpleIntervalMethod(label, interval, results);
    }

    /**
     * 穿透执行
     * <p>
     * 执行label回调
     * <p>
     * 将回调{@link com.pudding.tofu.retention.pierce}pierce注解lable相同的注册方法
     * <p>
     * 使用该方法时请注意results参数必须一一对应，参数不对应则继续寻找参数符合的方法
     * <p>
     * 执行顺序为注册的先后顺序
     *
     * @param label
     * @param results 参数
     * @param <Result>
     */
    public <Result> void pierceTo(@NonNull String label, Result... results) {
        checkParamIsAvailable(label);
        TofuBus.get().executeSimplePierceMethod(label, results);
    }

    /**
     * <p>
     * 回调到指定的target下的label方法
     * <p>
     * 如果lable相同将会被覆盖
     * <p>
     * 与当前宿主对象同步释放
     * <p>
     * 也可调用unbind()释放
     * <p>
     * 使用该方法时请注意参数必须一一对应
     * </p>
     * 无注解限制
     *
     * @param target
     * @param label
     * @param results
     * @param <Result>
     */
    public <Result> void into(@NonNull Object target, @NonNull String label, Result... results) {
        checkParamIsAvailable(target);
        checkParamIsAvailable(label);
        targets.put(target.getClass().getName(), target);
        TofuBus.get().executeTargetMethod(target, label, results);
    }


    /**
     * 参数检查
     *
     * @param target
     */
    private void checkParamIsAvailable(Object target) {
        if (target == null) {
            throw new NullPointerException("your target is null , please set target is available .");
        }
    }


    /**
     * 参数检查
     *
     * @param label
     */
    private void checkParamIsAvailable(String label) {
        if (TextUtils.isEmpty(label)) {
            throw new NullPointerException("your label is null , please set label is available .");
        }
    }

    @Override
    public void unbind() {
        Iterator<Map.Entry<String, Object>> iterator = targets.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            Object value = next.getValue();
            if (value != null) {
                TofuBus.get().unBindTarget(value);
                value = null;
            }
        }
        targets.clear();
    }
}
