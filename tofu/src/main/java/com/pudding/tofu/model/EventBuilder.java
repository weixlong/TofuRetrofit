package com.pudding.tofu.model;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.View;

import com.pudding.tofu.callback.OnDistinctClickCallBack;


/**
 * Created by wxl on 2018/8/6 0006.
 * 邮箱：632716169@qq.com
 */

public class EventBuilder<Result> implements UnBind {

    private View view;

    private boolean setClick, setLClick;

    private OnDistinctClickCallBack clickCallBack = new ClickDistinct();

    private static int SPACE_TIME = 800;


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
     * 重复点击规避条件
     *
     * @param distinct
     */
    public EventBuilder setDistinct(OnDistinctClickCallBack distinct) {
        clickCallBack = distinct;
        return this;
    }

    /**
     * 设置点击事件默认过滤间隔时间,未设置默认800ms
     *
     * @param spaceTime
     * @return
     */
    public EventBuilder twiceSpaceTime(@IntRange(from = 0) int spaceTime) {
        SPACE_TIME = spaceTime;
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
                    if (clickCallBack != null && !clickCallBack.isDistinctClick()) {
                        Tofu.go().to(label, results);
                    }
                }
            });
        }

        if (setLClick) {
            setLClick = false;
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Tofu.go().to(label, results);
                    return false;
                }
            });
        }
    }

    /**
     * 取消点击事件
     */
    public void unClick() {
        checkAvailableParam();
        setClick = false;
        view.setOnClickListener(null);
    }

    /**
     * 取消长按事件
     */
    public void unLongClick() {
        checkAvailableParam();
        setLClick = false;
        view.setOnLongClickListener(null);
    }


    /**
     * 参数检查
     */
    private void checkAvailableParam() {
        if (view == null) throw new IllegalArgumentException("please with available view !");
    }


    private class ClickDistinct implements OnDistinctClickCallBack {
        private long lastClickTime = 0;

        @Override
        public synchronized boolean isDistinctClick() {
            long currentTime = System.currentTimeMillis();
            boolean isClick2;
            if (currentTime - lastClickTime > SPACE_TIME) {
                isClick2 = false;
            } else {
                isClick2 = true;
            }
            lastClickTime = currentTime;
            return isClick2;
        }
    }


    @Override
    public void unbind() {
        view = null;
        setClick = false;
        setLClick = false;
    }
}
