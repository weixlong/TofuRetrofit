package com.pudding.tofu.widget.loadApk;

import android.content.Context;
import android.text.TextUtils;

public class LoadApk {

    private static class instance {
        private static LoadApk INSTANCE = new LoadApk();
    }


    public static LoadApk get(){
        return instance.INSTANCE;
    }

    /**
     * 下载安装包并安装
     *
     * @param url
     * @param context
     * @param apk_name **.apk
     */
    public LoadApkFile loadInstallApkFile(String url, String apk_name, Context context) {
        if (!TextUtils.isEmpty(url) && context != null && TextUtils.isEmpty(apk_name)) {
            LoadApkFile load = new LoadApkFile(url, apk_name, true,context);
            return load;
        }
        return null;
    }


    /**
     * 下载安装包
     * @param url
     * @param apk_name **.apk
     * @param context
     * @return
     */
    public LoadApkFile loadApkFile(String url, String apk_name, Context context){
        if (!TextUtils.isEmpty(url) && context != null && TextUtils.isEmpty(apk_name)) {
            LoadApkFile load = new LoadApkFile(url, apk_name, false,context);
            return load;
        }
        return null;
    }

}
