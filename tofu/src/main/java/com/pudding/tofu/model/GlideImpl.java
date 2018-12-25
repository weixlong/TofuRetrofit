package com.pudding.tofu.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.pudding.tofu.callback.BaseInterface;

import java.io.File;

import io.reactivex.disposables.Disposable;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class GlideImpl implements BaseInterface {

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

    private DrawableTypeRequest request;

    private boolean isGif;

    private int resId = 0x00fffff;

    private String gif_label;

    private int duration;

    private int maxLoopCount;

    private Disposable disposable;


    protected GlideImpl(ImageView imageView, String url,
                        int adaptiveWidth, float radius,
                        int width, int height, boolean isCircle,
                        int errorRes, File file, boolean isGif, int resId, String gif_label, int maxLoopCount) {
        this.imageView = imageView;
        this.url = url;
        this.adaptiveWidth = adaptiveWidth;
        this.radius = radius;
        this.width = width;
        this.height = height;
        this.isCircle = isCircle;
        this.errorRes = errorRes;
        this.file = file;
        this.isGif = isGif;
        this.resId = resId;
        this.gif_label = gif_label;
        this.maxLoopCount = maxLoopCount;
    }

    /**
     * 数据设置
     *
     * @param imageView
     * @param url
     * @param adaptiveWidth
     * @param radius
     * @param width
     * @param height
     * @param isCircle
     * @param errorRes
     * @param file
     * @param maxLoopCount
     */
    protected void setGlideImpl(ImageView imageView, String url,
                                int adaptiveWidth, float radius,
                                int width, int height,
                                boolean isCircle, int errorRes,
                                File file, boolean isGif, int resId, String gif_label, int maxLoopCount) {
        this.imageView = imageView;
        this.url = url;
        this.adaptiveWidth = adaptiveWidth;
        this.radius = radius;
        this.width = width;
        this.height = height;
        this.isCircle = isCircle;
        this.errorRes = errorRes;
        this.file = file;
        this.isGif = isGif;
        this.resId = resId;
        this.gif_label = gif_label;
        this.maxLoopCount = maxLoopCount;
    }

    @Override
    public void execute() {

        if (file != null) {
            request = Glide.with(imageView.getContext()).load(file);
        } else if (resId != 0x00fffff && isGif) {
            loadGif();
            return;
        } else {
            request = Glide.with(imageView.getContext()).load(url);
            if (isGif) {
                request.asGif();
            }
        }

        if (errorRes != -1) {
            request.error(errorRes); //设置错误图片
        }

        if (adaptiveWidth != -1) {
            request.asBitmap()//强制Glide返回一个Bitmap对象
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            ViewGroup.LayoutParams params = imageView.getLayoutParams();
                            params.width = adaptiveWidth;
                            params.height = (int) ((adaptiveWidth / width * 1.0)) * height;
                            imageView.setLayoutParams(params);
                        }
                    });
        }

        if (width != -1 && height != -1) {
            request.into(width, height);
        }

        if (radius != -1) {
            request.transform(new GlideRoundTransform(imageView.getContext(), (int) radius));
        }

        if (isCircle) {
            request.transform(new GlideCircleTransform(imageView.getContext()));
        }

        request.centerCrop()
                .crossFade() //设置淡入淡出效果，默认300ms，可以传参
                .into(imageView);
    }


    private void loadGif() {
        Glide.with(imageView.getContext())
                .load(resId)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<Integer, GlideDrawable>() {

                    @Override
                    public boolean onException(Exception arg0, Integer arg1,
                                               Target<GlideDrawable> arg2, boolean arg3) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource,
                                                   Integer model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        // 计算动画时长
                        GifDrawable drawable = (GifDrawable) resource;
                        GifDecoder decoder = drawable.getDecoder();
                        for (int i = 0; i < drawable.getFrameCount(); i++) {
                            duration += decoder.getDelay(i);
                        }
                        if (!TextUtils.isEmpty(gif_label)) {
                            if (maxLoopCount > 0) {
                                disposable = Tofu.go().delayTo(gif_label, duration * maxLoopCount);
                            } else {
                                disposable = Tofu.go().delayTo(gif_label, duration);
                            }
                        }
                        return false;
                    }
                }) //仅仅加载一次gif动画
                .into(new GlideDrawableImageViewTarget(imageView, maxLoopCount));
    }


    @Override
    public void unBind() {
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
        this.gif_label = null;
        this.resId = 0x00fffff;
        this.maxLoopCount = 1;
        if(disposable != null){
            if(!disposable.isDisposed()){
                disposable.dispose();
            }
            disposable = null;
        }
    }

    private class GlideCircleTransform extends BitmapTransformation {
        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    private class GlideRoundTransform extends BitmapTransformation {

        private float radius = 0f;

        public GlideRoundTransform(Context context) {
            this(context, 4);
        }

        public GlideRoundTransform(Context context, int dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }
}
