package com.pudding.tofu.widget;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.webkit.URLUtil;

import com.facebook.common.util.UriUtil;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class Util {

    public static int dip2px(final Context ctx, float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, ctx.getResources().getDisplayMetrics());
    }

    /**
     * 是否是网址
     *
     * @param url
     * @return
     */
    public static boolean isUri(String url) {

        if (TextUtils.isEmpty(url)) {
            Log.d("Tofu", "load url is null");
            return false;
        }

        if (!URLUtil.isNetworkUrl(url) && !UriUtil.isLocalFileUri(Uri.parse(url))) {
            Log.d("Tofu", "load url is not url or uri");
            return false;
        }

        return true;
    }
}
