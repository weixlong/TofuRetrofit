package com.pudding.tofu.model;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;

public class TIOBuilder implements UnBind {

    /**
     * io和线程的选择标志
     */
    private int TIOPOSITION = 1;


    protected TIOBuilder() {
    }

    /**
     * 与thread互斥
     *
     * @return
     */
    public TIOBuilder io() {
        TIOPOSITION = 2;
        return this;
    }

    /**
     * 与io互斥
     *
     * @return
     */
    public TIOBuilder thread() {
        TIOPOSITION = 1;
        return this;
    }


    /**
     * 执行，默认在线程中执行，回调tio注解,请保证参数类型一一对应
     * <p>
     *
     *     返回Disposable 管理执行
     * </p>
     * @param label
     * @param results
     * @param <Result>
     */
    public <Result> Disposable to(String label, Result... results) {
        ObjectHelper.requireNonNull(label,"label must not be empty !");
        return TofuBus.get().executeTIOMethod(label,TIOPOSITION == 2,results);
    }

    @Override
    public void unbind() {
        TIOPOSITION = 1;
    }
}
