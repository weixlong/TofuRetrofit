package com.pudding.tofu.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.webkit.URLUtil;

import com.facebook.common.util.UriUtil;

import java.util.List;

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


    /**
     * 判断手机是否安装某个应用
     * @param context
     * @param appPackageName  应用包名
     * @return   true：安装，false：未安装
     */
    public static boolean isApplicationAvailable(Context context, String appPackageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (appPackageName.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 打开一个app
     * @param packageName
     * @param context
     */
    public static void openOtherApp(String packageName,Context context){
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }
}
