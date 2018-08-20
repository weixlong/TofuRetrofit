package com.pudding.tofu;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pudding.tofu.model.Tofu;


import indi.yume.tools.fragmentmanager.BaseManagerFragment;

/**
 * Created by wxl on 2018/8/20 0020.
 * 邮箱：632716169@qq.com
 */

public abstract class TofuSupportFragment extends BaseManagerFragment{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StartBuilder.builder(new Intent()).withEnableAnimation(true);
        // SwipeBackUtil.enableSwipeBackAtActivity(this); 侧滑返回

    }
}
