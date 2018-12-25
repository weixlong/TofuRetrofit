package com.pudding.tofu.model;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.pudding.tofu.callback.BaseInterface;
import com.pudding.tofu.widget.Util;
import com.pudding.tofu.widget.scale.PhotoDraweeView;

import java.io.File;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class SimpleImageImpl implements BaseInterface {

    /**
     * Fresco 加载
     */
    private SimpleDraweeView simpleView;

    /**
     * 图片路径
     */
    private String url;

    /**
     * 自适应宽度
     */
    private int adaptiveWidth = -1;

    /**
     * 圆角
     */
    private float radius = -1;

    /**
     * 显示区域
     */
    private int width = -1, height = -1;

    /**
     * 是否显示圆形
     */
    private boolean isCircle = false;

    /**
     * 错误显示的资源
     */
    private int errorRes = -1;


    private ControllerListener controllerListener;

    private ImageRequest request;

    private File file;

    private boolean isGif;

    private int gifRes = 0x00fffff;

    protected SimpleImageImpl() {
    }

    protected SimpleImageImpl(SimpleDraweeView simpleView, String url, int adaptiveWidth,
                              float radius, int width, int height, boolean isCircle, int errorRes, File file, boolean isGif, int gifRes) {
        this.simpleView = simpleView;
        this.url = url;
        this.adaptiveWidth = adaptiveWidth;
        this.radius = radius;
        this.width = width;
        this.height = height;
        this.isCircle = isCircle;
        this.errorRes = errorRes;
        this.file = file;
        this.isGif = isGif;
        this.gifRes = gifRes;
    }


    /**
     * 设置数据
     *
     * @param simpleView
     * @param url
     * @param adaptiveWidth
     * @param radius
     * @param width
     * @param height
     * @param isCircle
     * @param errorRes
     * @param file
     */
    protected void setSimpleImageImpl(SimpleDraweeView simpleView, String url, int adaptiveWidth,
                                      float radius, int width, int height, boolean isCircle, int errorRes, File file, boolean isGif, int gifRes) {
        this.simpleView = simpleView;
        this.url = url;
        this.adaptiveWidth = adaptiveWidth;
        this.radius = radius;
        this.width = width;
        this.height = height;
        this.isCircle = isCircle;
        this.errorRes = errorRes;
        this.file = file;
        this.isGif = isGif;
        this.gifRes = gifRes;
    }

    @Override
    public void execute() {

        if (file != null) {
            showImageFile(file);
        }

        if (adaptiveWidth != -1) {
            showAdaptive(adaptiveWidth);
        }

        if (radius != -1) {
            showRadiusImage(radius);
        }

        if (isCircle) {
            showRadiusImage(-1);
        }

        if (width != -1 && height != -1) {
            showThumb(width, height);
        }

        if (errorRes != -1) {
            showError();
        }

        simpleShow();
    }


    /**
     * 显示
     */
    private void simpleShow() {
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();
        if (request == null) {
            builder.setControllerListener(controllerListener);
        } else {
            builder.setImageRequest(request)
                    .setOldController(simpleView.getController())
                    .setControllerListener(new BaseControllerListener<ImageInfo>());
        }

        if (isGif) {
            builder.setAutoPlayAnimations(true);
            if (gifRes != 0x00fffff) {
                Uri uri = new Uri.Builder()
                        .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                        .path(String.valueOf(gifRes))
                        .build();
                builder.setUri(uri);
                this.url = uri.getPath();
            } else {
                builder.setUri(Uri.parse(url));
            }
        } else {
            builder.setUri(Uri.parse(url));
        }

        if (simpleView instanceof PhotoDraweeView) {
            ((PhotoDraweeView) simpleView).setPhotoUri(Uri.parse(url));
        } else {
            simpleView.setController(builder.build());
        }

    }

    /**
     * 加载错误时的图片
     */
    private void showError() {
        GenericDraweeHierarchy hierarchy = simpleView.getHierarchy();
        hierarchy.setFailureImage(errorRes);
        hierarchy.setProgressBarImage(errorRes);
    }


    /**
     * 显示高匹配宽
     *
     * @param width
     * @return
     */
    private void showAdaptive(final int width) {
        final ViewGroup.LayoutParams layoutParams = simpleView.getLayoutParams();
        if (!UriUtil.isNetworkUri(Uri.parse(url))) {
            layoutParams.width = width;
            simpleView.setBackgroundColor(ContextCompat.getColor(simpleView.getContext(), android.R.color.darker_gray));
            simpleView.setLayoutParams(layoutParams);
            return;
        }

        controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                int height = imageInfo.getHeight();
                int width1 = imageInfo.getWidth();
                layoutParams.width = width;
                layoutParams.height = (int) ((float) (width * height) / (float) width1);
                simpleView.setLayoutParams(layoutParams);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                Log.d("Tofu", "Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                throwable.printStackTrace();
            }
        };

    }


    /**
     * 显示圆角图片
     *
     * @param
     * @param
     * @param radius radiu ： <0 时显示圆形
     */
    private void showRadiusImage(float radius) {
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(radius);
        if (radius > 0) {
            roundingParams.setCornersRadius(radius);
        } else {
            roundingParams.setRoundAsCircle(true);
        }
        simpleView.getHierarchy().setRoundingParams(roundingParams);
    }


    /**
     * 显示图片文件
     *
     * @param
     * @param file
     */
    private void showImageFile(File file) {
        if (file == null || !file.exists()) return;
        if (file.getAbsolutePath().endsWith(".png")
                || file.getAbsolutePath().endsWith(".jpg")
                || file.getAbsolutePath().endsWith(".jpeg")
                || file.getAbsolutePath().endsWith(".gif")) {
            this.url = file.toURI().toString();
        }
    }

    /**
     * 显示缩略图
     *
     * @param
     * @param
     * @param resizeWidthDp  resizeWidth
     * @param resizeHeightDp resizeHeight
     */
    private void showThumb(int resizeWidthDp, int resizeHeightDp) {
        request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setResizeOptions(new ResizeOptions(Util.dip2px(simpleView.getContext(), resizeWidthDp),
                        Util.dip2px(simpleView.getContext(), resizeHeightDp)))
                .build();
    }

    @Override
    public void unBind() {
        this.simpleView = null;
        this.url = null;
        this.adaptiveWidth = -1;
        this.radius = -1;
        this.width = -1;
        this.height = -1;
        this.isCircle = false;
        this.errorRes = -1;
        this.file = null;
        this.controllerListener = null;
        this.request = null;
        this.isGif = false;
        this.gifRes = 0x00fffff;
    }


}
