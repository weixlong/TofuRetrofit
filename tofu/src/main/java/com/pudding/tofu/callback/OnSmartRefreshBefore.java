package com.pudding.tofu.callback;

import com.pudding.tofu.model.SmartBuilder;

/**
 * Created by wxl on 2018/7/30 0030.
 * 邮箱：632716169@qq.com
 */

public interface OnSmartRefreshBefore {

    /**
     * 刷新拦截,返回false则继续，返回true则不往下
     *
     */
    boolean onRefreshBefore(SmartBuilder builder);

}
