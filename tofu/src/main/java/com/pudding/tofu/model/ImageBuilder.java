package com.pudding.tofu.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pudding.tofu.widget.scale.ShowBigPic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class ImageBuilder implements UnBind {

    /**
     * simpleBuilder
     */
    private SimpleBuilder simpleBuilder = new SimpleBuilder();

    /**
     * simple加载
     */
    private SimpleImageImpl simpleImage;

    /**
     * glide
     */
    private GlideImpl glide;

    /**
     * glideBuilder
     */
    private GlidedBuilder glideBuilder = new GlidedBuilder();


    protected ImageBuilder() {
    }

    /**
     * 设置View
     *
     * @param simpleView
     * @return
     */
    public SimpleBuilder setShowView(@NonNull SimpleDraweeView simpleView) {
        simpleBuilder.setSimpleView(simpleView);
        return simpleBuilder;
    }

    /**
     * 设置View
     *
     * @param glideView
     * @return
     */
    public GlidedBuilder setShowView(@NonNull ImageView glideView) {
        glideBuilder.setImageView(glideView);
        return glideBuilder;
    }


    /**
     * 全屏图
     *
     * @param urls
     */
    public void showShuff(@NonNull Context context, @NonNull List<String> urls) {
        if (context == null) {
            System.err.println("your context is release !!!");
            return;
        }
        Intent intent = new Intent(context, ShowBigPic.class);
        intent.putStringArrayListExtra("paths", (ArrayList<String>) urls);
        context.startActivity(intent);
    }

    /**
     * 全屏图
     *
     * @param urls
     */
    public void showShuff(@NonNull Context context, @NonNull List<String> urls, int position) {
        if (context == null) {
            System.err.println("your context is release !!!");
            return;
        }
        Intent intent = new Intent(context, ShowBigPic.class);
        intent.putStringArrayListExtra("paths", (ArrayList<String>) urls);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    /**
     * 显示大图
     *
     * @param path
     */
    public void showBigView(@NonNull Context context, @NonNull String path) {
        if (context == null) {
            System.err.println("your context is release !!!");
            return;
        }
        Intent intent = new Intent(context, ShowBigPic.class);
        intent.putExtra("path", path);
        context.startActivity(intent);
    }


    @Override
    public void unbind() {
        if (simpleImage != null) {
            simpleImage.unBind();
        }

        if (glide != null) {
            glide.unBind();
        }

        simpleBuilder.unBind();
        glideBuilder.unBind();
    }


    public class GlidedBuilder {
        /**
         * Fresco 加载
         */
        private ImageView imageView;

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

        /**
         * 显示文件
         */
        private File file;


        private boolean isGif;

        private int resId = 0x00fffff;

        private String gif_label;

        private int maxLoopCount = 1;


        private void setImageView(@NonNull ImageView imageView) {
            this.imageView = imageView;
            this.url = "";
            this.height = -1;
            this.width = -1;
            this.errorRes = -1;
            this.adaptiveWidth = -1;
            this.radius = -1;
            this.isCircle = false;
        }

        /**
         * 图片路径
         *
         * @param url
         * @return
         */
        public GlidedBuilder setUrl(@NonNull String url) {
            this.url = url;
            return this;
        }

        /**
         * 高度适应宽度
         *
         * @param adaptiveWidth
         * @return
         */
        public GlidedBuilder setAdaptiveWidth(int adaptiveWidth) {
            this.adaptiveWidth = adaptiveWidth;
            return this;
        }

        /**
         * 圆角
         *
         * @param radius
         * @return
         */
        public GlidedBuilder setRadius(float radius) {
            this.radius = radius;
            return this;
        }

        /**
         * 固定宽高
         *
         * @param width
         * @param height
         * @return
         */
        public GlidedBuilder setWidth(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }


        /**
         * 是否圆形
         *
         * @param circle
         * @return
         */
        public GlidedBuilder setCircle(boolean circle) {
            isCircle = circle;
            return this;
        }

        /**
         * 错误占位图
         *
         * @param errorRes
         * @return
         */
        public GlidedBuilder setErrorRes(int errorRes) {
            this.errorRes = errorRes;
            return this;
        }

        /**
         * 需要显示的图片文件
         *
         * @param file
         * @return
         */
        public GlidedBuilder setFile(@NonNull File file) {
            this.file = file;
            return this;
        }

        /**
         * 当url为gif时播放
         *
         * @return
         */
        public GlidedBuilder asGif() {
            this.isGif = true;
            return this;
        }

        /**
         * 本地gif资源播放，与其他属性互斥
         *
         * @param gifRes
         * @return
         */
        public GlidedBuilder asGif(@DrawableRes int gifRes) {
            this.resId = gifRes;
            this.isGif = true;
            return this;
        }


        /**
         * 本地gif资源播放，与其他属性互斥
         *
         * @param gifRes
         * @return
         */
        public GlidedBuilder asGif(@DrawableRes int gifRes,int maxLoopCount) {
            this.resId = gifRes;
            this.isGif = true;
            this.maxLoopCount = maxLoopCount;
            return this;
        }


        /**
         * 本地gif资源播放，与其他属性互斥
         *
         * @param gifRes
         * @param label gif播放结束回调，可不设
         * @return
         */
        public GlidedBuilder asGif(@DrawableRes int gifRes,String label) {
            this.resId = gifRes;
            this.isGif = true;
            this.gif_label = label;
            return this;
        }

        /**
         * 本地gif资源播放，与其他属性互斥
         *
         * @param gifRes
         * @param label gif播放结束回调，可不设
         * @param maxLoopCount {@link com.bumptech.glide.load.resource.drawable.GlideDrawable} in an {@link android.widget.ImageView}.
         * @return
         */
        public GlidedBuilder asGif(@DrawableRes int gifRes,int maxLoopCount,String label) {
            this.resId = gifRes;
            this.isGif = true;
            this.gif_label = label;
            this.maxLoopCount = maxLoopCount;
            return this;
        }

        /**
         * 显示
         */
        public void show() {
            checkParams();
            if (glide == null) {
                glide = new GlideImpl(imageView, url, adaptiveWidth, radius, width, height, isCircle, errorRes, file,isGif,resId,gif_label,maxLoopCount);
            } else {
                glide.setGlideImpl(imageView, url, adaptiveWidth, radius, width, height, isCircle, errorRes, file,isGif,resId,gif_label,maxLoopCount);
            }
            glide.execute();
        }

        /**
         * 解绑
         */
        private void unBind() {
            this.imageView = null;
            this.url = "";
            this.height = -1;
            this.width = -1;
            this.errorRes = -1;
            this.adaptiveWidth = -1;
            this.radius = -1;
            this.isCircle = false;
            this.file = null;
            this.isGif = false;
            this.resId = 0x00fffff;
            this.gif_label = null;
            this.maxLoopCount = 1;
        }

        /**
         * 参数检查
         */
        private void checkParams() {
            if (imageView == null) throw new NullPointerException("your imageView is null !");
            if (TextUtils.isEmpty(url) && file == null && resId == 0x00fffff)
                throw new NullPointerException("your url is null !");
        }

    }


    public class SimpleBuilder {

        /**
         * SimpleDraweeView 属性
         * <p>
         * fresco:fadeDuration="300"//图片淡出的事件(ms)
         * <p>
         * fresco:actualImageScaleType="focusCrop"
         * <p>
         * fresco:placeholderImage="@color/wait_color"//占位图
         * <p>
         * fresco:placeholderImageScaleType="fitCenter"//占位图的缩放类型
         * <p>
         * fresco:failureImage="@drawable/error"//失败图片
         * <p>
         * fresco:failureImageScaleType="centerInside"//失败图片的类型
         * <p>
         * fresco:retryImage="@drawable/retrying"//重试图片
         * <p>
         * fresco:retryImageScaleType="centerCrop"//图的重试缩放类型
         * <p>
         * fresco:progressBarImage="@drawable/progress_bar"//进度图片
         * <p>
         * fresco:progressBarImageScaleType="centerInside"//进度图的缩放类型
         * <p>
         * fresco:progressBarAutoRotateInterval="1000"//进度图自动旋转的间隔时间(ms)
         * <p>
         * fresco:backgroundImage="@color/blue"//背景图片
         * <p>
         * fresco:overlayImage="@drawable/watermark"//叠加图片
         * <p>
         * fresco:pressedStateOverlayImage="@color/red"//按压状态下显示的叠加图
         * <p>
         * fresco:roundAsCircle="false"//是否设置为圆形图片
         * <p>
         * fresco:roundedCornerRadius="1dp"//圆角半径
         * <p>
         * fresco:roundTopLeft="true"//左上角是否为圆角
         * <p>
         * fresco:roundTopRight="false"//右上角是否为圆角
         * <p>
         * fresco:roundBottomLeft="false"//左下角是否为圆角
         * <p>
         * fresco:roundBottomRight="true"//右下角是否为圆角
         * <p>
         * fresco:roundWithOverlayColor="@color/corner_color"//圆形或者圆角图片底下的叠加颜色
         * <p>
         * fresco:roundingBorderWidth="2dp"//圆形或者圆角图片边框的宽度
         * <p>
         * fresco:roundingBorderColor="@color/border_color"//圆形或者圆角边框的颜色
         * <p>
         * fresco:viewAspectRatio="1.33"  //宽高比例
         * <p>
         * <p>
         * 缩放类型—ScaleType：
         * <p>
         * 类型  描述
         * center  居中，无缩放
         * <p>
         * centerCrop  保持宽高比缩小或放大，使得两边都大于或等于显示边界。居中显示。
         * <p>
         * focusCrop   同centerCrop, 但居中点不是中点，而是指定的某个点
         * <p>
         * centerInside    使两边都在显示边界内，居中显示。如果图尺寸大于显示边界，则保持长宽比缩小图片。
         * <p>
         * fitCenter   保持宽高比，缩小或者放大，使得图片完全显示在显示边界内。居中显示
         * <p>
         * fitStart    同上。但不居中，和显示边界左上对齐
         * <p>
         * fitEnd  同fitCenter， 但不居中，和显示边界右下对齐
         * <p>
         * fitXY   不保存宽高比，填充满显示边界
         * <p>
         * none    如要使用tile mode显示, 需要设置为none
         */


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

        /**
         * 显示文件
         */
        private File file;

        private boolean isGif;

        private int resId = 0x00fffff;

        /**
         * 图片路径
         *
         * @param url
         * @return
         */
        public SimpleBuilder setUrl(@NonNull String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置view
         *
         * @param simpleView
         */
        private void setSimpleView(@NonNull SimpleDraweeView simpleView) {
            this.simpleView = simpleView;
            this.url = "";
            this.height = -1;
            this.width = -1;
            this.errorRes = -1;
            this.adaptiveWidth = -1;
            this.radius = -1;
            this.isCircle = false;
        }


        /**
         * 高度自适应宽度
         *
         * @param width
         * @return
         */
        public SimpleBuilder setAdaptive(int width) {
            adaptiveWidth = width;
            return this;
        }

        /**
         * 设置显示大小
         *
         * @param width
         * @param height
         * @return
         */
        public SimpleBuilder setRect(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        /**
         * 圆角
         *
         * @param radius
         * @return
         */
        public SimpleBuilder setRadius(float radius) {
            this.radius = radius;
            return this;
        }

        /**
         * 是否显示圆形
         *
         * @param isCircle
         */
        public SimpleBuilder setCircle(boolean isCircle) {
            this.isCircle = isCircle;
            return this;
        }

        /**
         * 设置错误显示的资源
         *
         * @param errorRes
         * @return
         */
        public SimpleBuilder setErrorRes(@DrawableRes int errorRes) {
            this.errorRes = errorRes;
            return this;
        }

        /**
         * 显示文件
         *
         * @param file
         * @return
         */
        public SimpleBuilder setFile(File file) {
            this.file = file;
            return this;
        }

        /**
         * 当url为gif时播放
         *
         * @return
         */
        public SimpleBuilder asGif() {
            this.isGif = true;
            return this;
        }

        /**
         * 本地gif资源播放，与url互斥
         *
         * @param gifRes
         * @return
         */
        public SimpleBuilder asGif(@DrawableRes int gifRes) {
            this.resId = gifRes;
            this.isGif = true;
            return this;
        }

        /**
         * 解绑
         */
        private void unBind() {
            this.simpleView = null;
            this.url = "";
            this.height = -1;
            this.width = -1;
            this.errorRes = -1;
            this.adaptiveWidth = -1;
            this.radius = -1;
            this.isCircle = false;
            this.file = null;
            this.isGif = false;
            this.resId = 0x00fffff;
        }

        /**
         * 简单显示
         */
        public void show() {
            checkParams();
            if (simpleImage == null) {
                simpleImage = new SimpleImageImpl(simpleView, url, adaptiveWidth, radius, width, height, isCircle, errorRes, file, isGif, resId);
            } else {
                simpleImage.setSimpleImageImpl(simpleView, url, adaptiveWidth, radius, width, height, isCircle, errorRes, file, isGif, resId);
            }
            simpleImage.execute();
        }

        /**
         * 参数检查
         */
        private void checkParams() {
            if (simpleView == null) throw new NullPointerException("your imageView is null !");
            if (TextUtils.isEmpty(url) && file == null && resId == 0x00fffff)
                throw new NullPointerException("your url is null !");
        }
    }
}
