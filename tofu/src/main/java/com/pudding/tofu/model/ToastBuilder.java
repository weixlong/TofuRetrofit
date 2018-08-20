package com.pudding.tofu.model;

import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Created by wxl on 2018/8/20 0020.
 * 邮箱：632716169@qq.com
 */

public class ToastBuilder implements UnBind {

    protected ToastBuilder() {

    }

    /**
     * 短提示
     *
     * @param text
     */
    public void tell(@NonNull String text) {
        try {
            Toast.makeText(TofuKnife.app, text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 长提示
     *
     * @param text
     */
    public void tall(@NonNull String text) {
        try {
            Toast.makeText(TofuKnife.app, text, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unbind() {

    }


}
