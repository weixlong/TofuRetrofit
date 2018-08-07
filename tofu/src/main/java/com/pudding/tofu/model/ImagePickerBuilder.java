package com.pudding.tofu.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.utils.PermissionsUtils;

import static me.iwf.photopicker.PhotoPicker.REQUEST_CODE;

/**
 * Created by wxl on 2018/6/27 0027.
 * 邮箱：632716169@qq.com
 */

public class ImagePickerBuilder implements UnBind {


    private PhotoPicker.PhotoPickerBuilder photoPickerBuilder;

    protected ImagePickerBuilder() {
        photoPickerBuilder = PhotoPicker.builder().setShowCamera(false).setShowGif(false).setPreviewEnabled(false);
    }


    /**
     * 选择图片
     * @param path
     * @param num
     * @return
     */
    public ImagePickerBuilder picker(List<String> path, @IntRange(from = 0) int num) {
        photoPickerBuilder.setSelected((ArrayList<String>) path).setPhotoCount(num);
        return this;
    }

    /**
     * 显示拍照
     * @return
     */
    public ImagePickerBuilder asCamera() {
        if (photoPickerBuilder != null) {
            photoPickerBuilder.setShowCamera(true);
        }
        return this;
    }

    /**
     * 开启gif播放
     * @return
     */
    public ImagePickerBuilder asGif() {
        if (photoPickerBuilder != null) {
            photoPickerBuilder.setShowGif(true);
        }
        return this;
    }

    /**
     * 开启预览
     * @return
     */
    public ImagePickerBuilder asPreView() {
        if (photoPickerBuilder != null) {
            photoPickerBuilder.setPreviewEnabled(true);
        }
        return this;
    }

    /**
     * 展示的列数
     * @param column
     * @return
     */
    public ImagePickerBuilder setGridColumn(@IntRange(from = 0)int column) {
        if (photoPickerBuilder != null) {
            photoPickerBuilder.setGridColumnCount(column);
        }
        return this;
    }


    /**
     * 本地图片选择
     * @param context
     * @param label
     * 响应photoPick注解
     */
    public void start(@NonNull Activity context, @NonNull String label) {
        checkParamsAvailable(context,label);
        if (photoPickerBuilder != null) {
            if (AndPermission.hasPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)) {
                context.startActivityForResult(photoPickerBuilder.getIntent(context)
                                .setClassName(context,PhotoPickerActivity2.class.getName())
                        .putExtra("label",label),
                        REQUEST_CODE);
            } else {
                System.out.println("Tofu : Are you sure has storage and camera permission ? ");
            }
        }
    }

    /**
     * 参数检查
     * @param context
     * @param label
     */
    private void checkParamsAvailable(Context context,String label){
        if(context == null){
            throw new NullPointerException("your context is null , please set context is availabel .");
        }
        if(TextUtils.isEmpty(label)){
            throw new NullPointerException("your label is null , please set label is availabel .");
        }
    }


    /**
     * 数据恢复
     */
    protected void clear() {
        if (photoPickerBuilder != null) {
            photoPickerBuilder
                    .setShowCamera(false)
                    .setShowGif(false)
                    .setPreviewEnabled(false)
                    .setPhotoCount(1)
                    .setSelected(new ArrayList<String>());
        }
    }

    @Override
    public void unbind() {
        if (photoPickerBuilder != null) {
            photoPickerBuilder
                    .setShowCamera(false)
                    .setShowGif(false)
                    .setPreviewEnabled(false)
                    .setPhotoCount(1)
                    .setSelected(new ArrayList<String>());
        }
    }
}
