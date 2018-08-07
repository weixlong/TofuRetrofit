package com.pudding.tofu.model;

import android.support.annotation.NonNull;
import android.view.View;


/**
 * Created by wxl on 2018/8/6 0006.
 * 邮箱：632716169@qq.com
 */

public class EventBuilder<Result> implements UnBind {

    private View view;

    private boolean setClick, setLClick;


    protected EventBuilder() {
    }

    /**
     * 绑定view
     *
     * @param view
     * @return
     */
    public EventBuilder with(@NonNull View view) {
        this.view = view;
        return this;
    }

    /**
     * 点击事件
     *
     * @return
     */
    public EventBuilder click() {
        setClick = true;
        return this;
    }


    /**
     * 长按
     *
     * @return
     */
    public EventBuilder longClick() {
        setLClick = true;
        return this;
    }


    /**
     * 到@subscribe注解中
     *
     * @param label
     */
    public void to(@NonNull final String label, final Result... results) {
        checkAvailableParam();
        if (setClick) {
            setClick = false;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tofu.go().to(label,results);
                }
            });
        }

        if (setLClick) {
            setLClick = false;
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Tofu.go().to(label,results);
                    return false;
                }
            });
        }
    }



    /**
     * 参数检查
     */
    private void checkAvailableParam() {
        if (view == null) throw new IllegalArgumentException("please with available view !");
    }


    @Override
    public void unbind() {
        view = null;
        setClick = false;
        setLClick = false;
    }
}
