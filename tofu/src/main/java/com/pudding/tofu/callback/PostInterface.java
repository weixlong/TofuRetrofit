package com.pudding.tofu.callback;

import android.content.Context;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public interface PostInterface extends BaseInterface {

    /**
     * 显示进度
     * @param context
     */
    void showDialog(Context context);


    /**
     * 关闭进度
     */
    void closeDialog();
}
