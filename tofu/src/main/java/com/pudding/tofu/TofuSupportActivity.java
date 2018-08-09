package com.pudding.tofu;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pudding.tofu.model.Tofu;

import java.util.HashMap;
import java.util.Map;

import indi.yume.tools.fragmentmanager.BaseFragmentManagerActivity;
import indi.yume.tools.fragmentmanager.BaseManagerFragment;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public abstract class TofuSupportActivity extends BaseFragmentManagerActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tofu.bind(this);
        //StartBuilder.builder(new Intent()).withEnableAnimation(true);
       // SwipeBackUtil.enableSwipeBackAtActivity(this); 侧滑返回

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Tofu.unBind(this);
    }

    @Override
    public int fragmentViewId() {
        return 0;
    }

    @Override
    public Map<String, Class<? extends BaseManagerFragment>> baseFragmentWithTag() {
        return new HashMap<>();
    }
}
