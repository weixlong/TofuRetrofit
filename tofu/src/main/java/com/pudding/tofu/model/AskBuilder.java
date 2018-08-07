package com.pudding.tofu.model;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

/**
 * Created by wxl on 2018/7/17 0017.
 * 邮箱：632716169@qq.com
 */
public class AskBuilder implements UnBind {

    private Activity appActivity;

    private android.app.Fragment appF;

    private android.support.v4.app.Fragment supF;

    public static final int request_permission_default_code = 1452635;

    private int request_permission_code = request_permission_default_code;

    private RationaleListener mRationaleListener;

    /**
     * 权限集合
     */
    private String[] permissions;


    protected AskBuilder() {
    }

    public AskBuilder with(@NonNull android.app.Fragment fragment) {
        appF = fragment;
        return this;
    }

    public AskBuilder with(@NonNull android.support.v4.app.Fragment fragment) {
        supF = fragment;
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
     * 设置用户选择回调
     *
     * @param listener
     * @return
     */
    public AskBuilder setRationaleListener(RationaleListener listener) {
        mRationaleListener = listener;
        return this;
    }

    /**
     * 请求码
     *
     * @param requestCode
     * @return
     */
    public AskBuilder code(int requestCode) {
        request_permission_code = requestCode;
        return this;
    }


    /**
     * 设置权限获取回调结果
     *
     * @param grantResults
     * @param permissionListener
     */
    public void onRequestPermissionsResult(int[] grantResults, PermissionListener permissionListener) {
        AndPermission.onRequestPermissionsResult(request_permission_code, permissions, grantResults, permissionListener);
    }

    /**
     * 注解方式回调权限请求结果
     *
     * @param grantResults
     */
    public void onRequestPermissionsResult(int[] grantResults) {
        if (appActivity != null) {
            AndPermission.onRequestPermissionsResult(appActivity, request_permission_code, permissions, grantResults);
        } else if (supF != null) {
            AndPermission.onRequestPermissionsResult(supF, request_permission_code, permissions, grantResults);
        } else if (appF != null) {
            AndPermission.onRequestPermissionsResult(appF, request_permission_code, permissions, grantResults);
        } else {
            System.err.println("Tofu : Not find your call object , please with it !");
        }
    }

    /**
     * 是否已经有权限
     * @param permissions
     * @return
     */
    public boolean hasPermission(@NonNull String ... permissions){
        return AndPermission.hasPermission(checkHasPermissionParam(permissions),permissions);
    }

    /**
     * 权限检查
     * @param permissions
     * @return
     */
    private Context checkHasPermissionParam(String ... permissions){
        if (appActivity == null && appF == null && supF == null) {
            throw new NullPointerException("your activity is null !");
        }
        if(permissions==null)new NullPointerException("your check permissions is null !");

        if(appActivity != null)return appActivity;
        if(appF != null) return appF.getActivity();
        if(supF != null)return supF.getActivity();
        return null;
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
                .rationale(mRationaleListener).send();
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
    }
}
