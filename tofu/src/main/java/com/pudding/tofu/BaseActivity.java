package com.pudding.tofu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.pudding.tofu.model.Tofu;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tofu.bind(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Tofu.unBind(this);
    }
}
