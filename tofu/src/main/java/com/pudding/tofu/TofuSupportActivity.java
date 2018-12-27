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

    private Map<String, Class<? extends BaseManagerFragment>> map = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //StartBuilder.builder(new Intent()).withEnableAnimation(true);
//        //SwipeBackUtil.enableSwipeBackAtActivity(this); 侧滑返回
//        Tofu.bind(this);
    }


    @Override
    public int fragmentViewId() {
        return onFragmentContainerViewId();
    }

    @Override
    public Map<String, Class<? extends BaseManagerFragment>> baseFragmentWithTag() {
        onBindFragmentIntoMap(map);
        return map;
    }


    /**
     * fragment的容器布局id
     * @return
     */
    protected int onFragmentContainerViewId(){
        return 0;
    }

    /**
     * 把fragment 放入缓存
     * @param map
     */
    protected void onBindFragmentIntoMap(Map<String, Class<? extends BaseManagerFragment>> map){

    }
}
