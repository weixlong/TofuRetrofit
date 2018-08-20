package com.pudding.tofu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pudding.tofu.model.Tofu;


import indi.yume.tools.fragmentmanager.BaseManagerFragment;
import indi.yume.tools.fragmentmanager.SwipeBackUtil;

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

//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_1,container,false);
//        return SwipeBackUtil.enableSwipeBackAtFragment(this,view);侧滑返回
//    }
}
