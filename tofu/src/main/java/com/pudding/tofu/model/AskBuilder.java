package com.pudding.tofu.model;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

/**
 * Created by wxl on 2018/7/17 0017.
 * 邮箱：632716169@qq.com
 */
public class AskBuilder implements UnBind {

    private Activity appActivity;

    private android.app.Fragment appF;

    private android.support.v4.app.Fragment supF;

    private Context context;

    private int request_permission_code = 1789 >> 89;

    /**
     * 权限集合
     */
    private String[] permissions;


    protected AskBuilder() {
    }

    public AskBuilder with(@NonNull android.app.Fragment fragment) {
        appF = fragment;
        context = fragment.getActivity();
        return this;
    }

    public AskBuilder with(@NonNull android.support.v4.app.Fragment fragment) {
        supF = fragment;
        context = fragment.getActivity();
        return this;
    }

    /**
     * 设置请求体
     *
     * @param act
     * @return
     */
    public AskBuilder with(@NonNull Activity act) {
        appActivity = act;
        context = act;
        return this;
    }

    /**
     * 设置请求的权限集合
     *
     * @param permissions
     * @return
     */
    public AskBuilder on(@NonNull String... permissions) {
        this.permissions = permissions;
        return this;
    }


    /**
     * 请求权限
     */
    public void ask() {
        checkParamAvailable();
        Permission with = null;
        if (appF != null) {
            with = AndPermission.with(appF.getActivity());
        }
        if (supF != null) {
            with = AndPermission.with(supF);
        }
        if (appActivity != null) {
            with = AndPermission.with(appActivity);
        }
        with.permission(permissions)
                .requestCode(request_permission_code)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
                        if(requestCode == request_permission_code) {
                            new AlertDialog.Builder(context)
                                    .setTitle("权限提醒")
                                    .setMessage("您已拒绝该权限，拒绝该权限将会影响部分功能的使用，建议您开启权限")
                                    .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            rationale.resume();
                                        }
                                    })
                                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            rationale.cancel();
                                        }
                                    }).show();
                        }
                    }
                }).send();
    }

    /**
     * 参数检查
     */
    private void checkParamAvailable() {
        if (appActivity == null && appF == null && supF == null) {
            throw new NullPointerException("your activity is null !");
        }
        if (permissions == null) {
            throw new NullPointerException("your permissions is null !");
        }
    }

    @Override
    public void unbind() {
        appActivity = null;
        appF = null;
        supF = null;
        context = null;
    }
}
