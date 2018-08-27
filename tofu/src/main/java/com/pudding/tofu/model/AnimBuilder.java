package com.pudding.tofu.model;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;

import com.pudding.tofu.widget.CollectUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by wxl on 2018/8/24 0024.
 * 邮箱：632716169@qq.com
 */

public class AnimBuilder implements UnBind {

    private View target;

    private long duration = 500;

    private MoveBuilder moveBuilder;

    private QuadBuilder quadBuilder;

    private CubicBuilder cubicBuilder;

    private ScaleBuilder scaleBuilder;

    private AlphaBuilder alphaBuilder;

    private AlwaysRotateBuilder alwaysRotateBuilder;

    private TogetherBuilder togetherBuilder;

    private RotateBuilder rotateBuilder;

    private List<UnBind> unBinds = new ArrayList<>();

    private ValueAnimator.AnimatorUpdateListener updateListener;

    private Animator.AnimatorPauseListener pauseListener;

    private Animator.AnimatorListener animatorListener;

    private Interpolator interpolator = new LinearInterpolator();

    protected AnimBuilder() {
    }

    /**
     * 需要执行动画的view
     *
     * @param target
     * @return
     */
    public AnimBuilder target(View target) {
        this.target = target;
        return this;
    }

    /**
     * 动画执行时间，不设置默认500ms
     *
     * @param duration
     * @return
     */
    public AnimBuilder duration(long duration) {
        this.duration = duration;
        return this;
    }

    /**
     * 加速动画,不设置则默认匀速
     *
     * @return
     */
    public AnimBuilder accelerate() {
        interpolator = new AccelerateInterpolator();
        return this;
    }

    /**
     * 减速动画,不设置则默认匀速
     *
     * @return
     */
    public AnimBuilder decelerate() {
        interpolator = new DecelerateInterpolator();
        return this;
    }

    /**
     * 设置动画为先加速在减速(开始速度最快 逐渐减慢),不设置则默认匀速
     *
     * @return
     */
    public AnimBuilder accelerateDecelerate() {
        interpolator = new AccelerateDecelerateInterpolator();
        return this;
    }


    /**
     * 先反向执行一段，然后再加速反向回来（相当于我们弹簧，先反向压缩一小段，然后在加速弹出）,不设置则默认匀速
     *
     * @return
     */
    public AnimBuilder anticipate() {
        interpolator = new AnticipateInterpolator();
        return this;
    }


    /**
     * 先反向一段，然后加速反向回来，执行完毕自带回弹效果（更形象的弹簧效果）,不设置则默认匀速
     *
     * @return
     */
    public AnimBuilder anticipateOvershoot() {
        interpolator = new AnticipateOvershootInterpolator();
        return this;
    }

    /**
     * 执行完毕之后会回弹跳跃几段（相当于我们高空掉下一颗皮球，到地面是会跳动几下）,不设置则默认匀速
     *
     * @return
     */
    public AnimBuilder bounce() {
        interpolator = new BounceInterpolator();
        return this;
    }

    /**
     * 循环，动画循环一定次数，值的改变为一正弦函数：Math.sin(2* mCycles* Math.PI* input),不设置则默认匀速
     *
     * @return
     */
    public AnimBuilder cycle(float cycles) {
        interpolator = new CycleInterpolator(cycles);
        return this;
    }


    /**
     * 加速执行，结束之后回弹,不设置则默认匀速
     *
     * @return
     */
    public AnimBuilder overShoot() {
        interpolator = new OvershootInterpolator();
        return this;
    }

    /**
     * 自定义动画执行效果,不设置则默认匀速
     *
     * @return
     */
    public AnimBuilder setInterpolator(@NonNull Interpolator value) {
        if (value != null) {
            interpolator = value;
        }
        return this;
    }

    /**
     * 设置执行过程监听
     *
     * @param updateCallBack
     * @return
     */
    public AnimBuilder setUpdateCallBack(ValueAnimator.AnimatorUpdateListener updateCallBack) {
        updateListener = updateCallBack;
        return this;
    }

    /**
     * 设置显示暂停监听
     *
     * @param pauseCallBack
     * @return
     */
    public AnimBuilder setPauseCallBack(Animator.AnimatorPauseListener pauseCallBack) {
        pauseListener = pauseCallBack;
        return this;
    }

    /**
     * 设置监听
     *
     * @param animatorCallBack
     * @return
     */
    public AnimBuilder setAnimatorCallBack(Animator.AnimatorListener animatorCallBack) {
        animatorListener = animatorCallBack;
        return this;
    }

    /**
     * 平移
     *
     * @return
     */
    public MoveBuilder move() {
        checkViewAvailable();
        if (moveBuilder == null) {
            moveBuilder = new MoveBuilder();
            unBinds.add(moveBuilder);
        }
        return moveBuilder;
    }

    /**
     * 一阶贝塞尔曲线
     *
     * @return
     */
    public QuadBuilder quad() {
        checkViewAvailable();
        if (quadBuilder == null) {
            quadBuilder = new QuadBuilder();
            unBinds.add(quadBuilder);
        }
        return quadBuilder;
    }

    /**
     * 旋转动画
     * @return
     */
    public RotateBuilder rotate(){
        checkViewAvailable();
        if(rotateBuilder == null){
            rotateBuilder = new RotateBuilder();
            unBinds.add(rotateBuilder);
        }
        return rotateBuilder;
    }

    /**
     * 二阶贝塞尔曲线
     *
     * @return
     */
    public CubicBuilder cubic() {
        checkViewAvailable();
        if (cubicBuilder == null) {
            cubicBuilder = new CubicBuilder();
            unBinds.add(cubicBuilder);
        }
        return cubicBuilder;
    }

    /**
     * 缩放动画
     *
     * @return
     */
    public ScaleBuilder scale() {
        checkViewAvailable();
        if (scaleBuilder == null) {
            scaleBuilder = new ScaleBuilder();
            unBinds.add(scaleBuilder);
        }
        return scaleBuilder;
    }

    /**
     * 透明动画
     *
     * @return
     */
    public AlphaBuilder alpha() {
        checkViewAvailable();
        if (alphaBuilder == null) {
            alphaBuilder = new AlphaBuilder();
            unBinds.add(alphaBuilder);
        }
        return alphaBuilder;
    }

    /**
     * 旋转动画
     *
     * @return
     */
    public AlwaysRotateBuilder alwaysRotate() {
        checkViewAvailable();
        if (alwaysRotateBuilder == null) {
            alwaysRotateBuilder = new AlwaysRotateBuilder();
            unBinds.add(alwaysRotateBuilder);
        }
        return alwaysRotateBuilder;
    }


    /**
     * 同时播放动画
     *
     * @return
     */
    public TogetherBuilder together() {
        checkViewAvailable();
        if (togetherBuilder == null) {
            togetherBuilder = new TogetherBuilder();
            unBinds.add(togetherBuilder);
        }
        return togetherBuilder;
    }


    /**
     * 参数检查
     */
    private void checkViewAvailable() {
        if (target == null) throw new IllegalArgumentException("Are you sure set view target ???");
    }

    /**
     * 获取ObjectAnimator
     *
     * @param typeEvaluator
     * @param startPoint
     * @param endPoint
     * @return
     */
    protected ObjectAnimator getAnimator(TypeEvaluator typeEvaluator, PointF startPoint, PointF endPoint) {
        ObjectAnimator animator = ObjectAnimator.ofObject(this, "move", typeEvaluator, startPoint, endPoint);
        animator.setDuration(duration);
        animator.setInterpolator(interpolator);
        if (updateListener != null) {
            animator.addUpdateListener(updateListener);
        }
        if (pauseListener != null) {
            animator.addPauseListener(pauseListener);
        }
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        return animator;
    }


    /**
     * call from object animator of object method 'move' char
     *
     * @param pointF {@hide}
     */
    public void setMove(PointF pointF) {
        target.setX(pointF.x);
        target.setY(pointF.y);
    }

    private PointF getPointF(float x, float y) {
        PointF p = new PointF(x, y);
        return p;
    }

    /**
     * 同时执行
     */
    public class TogetherBuilder implements UnBind {
        List<Animation> animations = new ArrayList<>();
        List<Animator> valueAnimators = new ArrayList<>();
        private AnimatorSet animationSet = new AnimatorSet();

        private TogetherBuilder() {
        }

        /**
         * 旋转动画
         *
         * @param builder
         * @return
         */
        public TogetherBuilder rotate(@NonNull AlwaysRotateBuilder builder) {
            if (builder != null) {
                animations.add(builder.getRotationXYZAnim());
            }
            return this;
        }

        /**
         * 透明动画
         *
         * @param builder
         * @return
         */
        public TogetherBuilder alpha(@NonNull AlphaBuilder builder) {
            if (builder != null) {
                valueAnimators.add(builder.getAlphaAnimation());
            }
            return this;
        }

        /**
         * 一阶贝塞尔动画
         *
         * @param builder
         * @return
         */
        public TogetherBuilder cubic(@NonNull CubicBuilder builder) {
            if (builder != null) {
                valueAnimators.add(builder.getAnim());
            }
            return this;
        }

        /**
         * 二阶贝塞尔动画
         *
         * @param builder
         * @return
         */
        public TogetherBuilder quad(@NonNull QuadBuilder builder) {
            if (builder != null) {
                valueAnimators.add(builder.getAnim());
            }
            return this;
        }

        /**
         * 移动动画
         *
         * @param builder
         * @return
         */
        public TogetherBuilder move(@NonNull MoveBuilder builder) {
            if (builder != null) {
                valueAnimators.add(builder.getAnim());
            }
            return this;
        }

        /**
         * 缩放动画
         *
         * @param builder
         * @return
         */
        public TogetherBuilder scale(@NonNull ScaleBuilder builder) {
            if (builder != null) {
                valueAnimators.addAll(builder.getScaleAnimation());
            }
            return this;
        }

        /**
         * 旋转动画
         * @param builder
         * @return
         */
        public TogetherBuilder rotate(@NonNull RotateBuilder builder){
            if(builder != null){
                valueAnimators.add(builder.getAnim());
            }
            return this;
        }

        /**
         * 执行动画
         */
        public void start() {
            animationSet.playTogether(valueAnimators);
            animationSet.start();
            for (Animation animation : animations) {
                animation.start();
            }
        }

        @Override
        public void unbind() {
            duration = 500;
            target = null;
            updateListener = null;
            pauseListener = null;
            animatorListener = null;
            for (Animation animation : animations) {
                ((UnBind) animation).unbind();
            }
            for (Animator valueAnimator : valueAnimators) {
                ((UnBind) valueAnimator).unbind();
            }
            animations.clear();
            valueAnimators.clear();
        }
    }


    public class RotateBuilder implements UnBind {
        private int count = 0, mode = RESTART;
        private boolean rotateX, rotateY, rotateZ;
        private float[] values;

        private RotateBuilder() {
        }

        /**
         * When the animation reaches the end and <code>repeatCount</code> is INFINITE
         * or a positive value, the animation restarts from the beginning.
         * <p>
         * public static final int RESTART = 1;
         * <p>
         * When the animation reaches the end and <code>repeatCount</code> is INFINITE
         * or a positive value, the animation reverses direction on every iteration.
         * <p>
         * public static final int REVERSE = 2;
         * <p>
         * This value used used with the {@link #setRepeatCount(int)} property to repeat
         * the animation indefinitely.
         * <p>
         * public static final int INFINITE = -1;
         *
         * @param count
         * @return
         */
        public RotateBuilder setRepeatCount(int count) {
            this.count = count;
            return this;
        }

        /**
         * Defines what this animation should do when it reaches the end. This
         * setting is applied only when the repeat count is either greater than
         * 0 or {@link # INFINITE}. Defaults to {@link # RESTART}.
         *
         * @param mode {@link # RESTART} or {@link # REVERSE}
         */
        public RotateBuilder setRepeatMode(@RepeatMode int mode) {
            this.mode = mode;
            return this;
        }


        /**
         * 绕X轴转动,三种转动互斥
         *
         * @return
         */
        public RotateBuilder rotateX() {
            rotateX = true;
            rotateY = false;
            rotateZ = false;
            return this;
        }

        /**
         * 绕Y轴转动,三种转动互斥
         *
         * @return
         */
        public RotateBuilder rotateY() {
            rotateX = false;
            rotateY = true;
            rotateZ = false;
            return this;
        }

        /**
         * 绕Z轴转动,三种转动互斥
         *
         * @return
         */
        public RotateBuilder rotateZ() {
            rotateZ = true;
            rotateY = false;
            rotateX = false;
            return this;
        }

        /**
         * 设置值
         *
         * @param values
         * @return
         */
        public RotateBuilder values(float... values) {
            this.values = values;
            return this;
        }

        /***
         * 开始动画
         */
        public void start(){
            getAnim().start();
        }

        /**
         * 获取动画
         * @return
         */
        @SuppressLint("WrongConstant")
        private ObjectAnimator getAnim() {
            String propertyName = "rotation";
            if (rotateX) {
                propertyName = "rotationX";
            }
            if (rotateY) {
                propertyName = "rotationY";
            }
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, propertyName, values);
            objectAnimator.setDuration(duration);
            objectAnimator.setRepeatCount(count);
            objectAnimator.setRepeatMode(mode);
            if (updateListener != null) {
                objectAnimator.addUpdateListener(updateListener);
            }
            if (pauseListener != null) {
                objectAnimator.addPauseListener(pauseListener);
            }
            if (animatorListener != null) {
                objectAnimator.addListener(animatorListener);
            }
            objectAnimator.setInterpolator(interpolator);
            return objectAnimator;
        }


        @Override
        public void unbind() {
            count = 0;
            mode = RESTART;
            duration = 500;
            rotateX = rotateY = rotateZ = false;
            target = null;
            updateListener = null;
            pauseListener = null;
            animatorListener = null;
            values = null;
        }
    }


    /**
     * 旋转动画
     */
    public class AlwaysRotateBuilder implements UnBind {
        private Animation.AnimationListener mListener;
        private boolean rotateX, rotateY, rotateZ;
        private int count = 0, mode = RESTART;

        private AlwaysRotateBuilder() {
        }

        /**
         * 绕X轴转动
         *
         * @return
         */
        public AlwaysRotateBuilder rotateX() {
            rotateX = true;
            return this;
        }

        /**
         * 绕Y轴转动
         *
         * @return
         */
        public AlwaysRotateBuilder rotateY() {
            rotateY = true;
            return this;
        }

        /**
         * 绕Z轴转动
         *
         * @return
         */
        public AlwaysRotateBuilder rotateZ() {
            rotateZ = true;
            return this;
        }

        /**
         * 动画执行监听
         *
         * @param callBack
         * @return
         */
        public AlwaysRotateBuilder setCallBack(Animation.AnimationListener callBack) {
            mListener = callBack;
            return this;
        }

        /**
         * When the animation reaches the end and <code>repeatCount</code> is INFINITE
         * or a positive value, the animation restarts from the beginning.
         * <p>
         * public static final int RESTART = 1;
         * <p>
         * When the animation reaches the end and <code>repeatCount</code> is INFINITE
         * or a positive value, the animation reverses direction on every iteration.
         * <p>
         * public static final int REVERSE = 2;
         * <p>
         * This value used used with the {@link #setRepeatCount(int)} property to repeat
         * the animation indefinitely.
         * <p>
         * public static final int INFINITE = -1;
         *
         * @param count
         * @return
         */
        public AlwaysRotateBuilder setRepeatCount(int count) {
            this.count = count;
            return this;
        }

        /**
         * Defines what this animation should do when it reaches the end. This
         * setting is applied only when the repeat count is either greater than
         * 0 or {@link # INFINITE}. Defaults to {@link # RESTART}.
         *
         * @param mode {@link # RESTART} or {@link # REVERSE}
         */
        public AlwaysRotateBuilder setRepeatMode(@RepeatMode int mode) {
            this.mode = mode;
            return this;
        }

        /**
         * 开始动画
         */
        public void start() {
            getRotationXYZAnim().start();
        }

        /**
         * 获得旋转动画
         *
         * @return
         */
        private Animation getRotationXYZAnim() {
            RotationXYZAnimation anim = new RotationXYZAnimation();
            anim.setRepeatCount(count);
            anim.setRepeatMode(mode);
            if (mListener != null) {
                anim.setAnimationListener(mListener);
            }
            return anim;
        }

        @Override
        public void unbind() {
            count = 0;
            mode = RESTART;
            mListener = null;
            rotateX = rotateY = rotateZ = false;
            duration = 500;
            target = null;
            updateListener = null;
            pauseListener = null;
            animatorListener = null;
        }


        /**
         * 绕XY轴旋转动画
         */
        private class RotationXYZAnimation extends Animation {

            int centerX, centerY;

            Camera camera = new Camera();

            @Override
            public void initialize(int width, int height, int parentWidth,
                                   int parentHeight) {
                super.initialize(width, height, parentWidth, parentHeight);

                //获取中心点坐标
                centerX = width / 2;
                centerY = height / 2;

                //动画执行时间  自行定义
                setDuration(duration);
                setFillAfter(true);
                setInterpolator(interpolator);
            }

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                final Matrix matrix = t.getMatrix();
                camera.save();
                //中心是绕Y轴旋转  这里可以自行设置X轴 Y轴 Z轴
                if (rotateY) {
                    camera.rotateY(360 * interpolatedTime);
                }

                if (rotateZ) {
                    camera.rotateZ(360 * interpolatedTime);
                }

                if (rotateX) {
                    camera.rotateX(360 * interpolatedTime);
                }
                //把我们的摄像头加在变换矩阵上
                camera.getMatrix(matrix);
                //设置翻转中心点
                matrix.preTranslate(-centerX, -centerY);
                matrix.postTranslate(centerX, centerY);
                camera.restore();
            }
        }

    }

    /**
     * 透明动画
     */
    public class AlphaBuilder implements UnBind {

        private float fromAlphaValue, toAlphaValue;
        private int count = 0, mode = RESTART;
        private float[] values;

        private AlphaBuilder() {
        }

        /**
         * 设置起止透明值
         *
         * @param fromValue
         * @param toValue
         * @return
         */
        public AlphaBuilder alphaValue(float fromValue, float toValue) {
            fromAlphaValue = fromValue;
            toAlphaValue = toValue;
            return this;
        }


        /**
         * When the animation reaches the end and <code>repeatCount</code> is INFINITE
         * or a positive value, the animation restarts from the beginning.
         * <p>
         * public static final int RESTART = 1;
         * <p>
         * When the animation reaches the end and <code>repeatCount</code> is INFINITE
         * or a positive value, the animation reverses direction on every iteration.
         * <p>
         * public static final int REVERSE = 2;
         * <p>
         * This value used used with the {@link #setRepeatCount(int)} property to repeat
         * the animation indefinitely.
         * <p>
         * public static final int INFINITE = -1;
         *
         * @param count
         * @return
         */
        public AlphaBuilder setRepeatCount(int count) {
            this.count = count;
            return this;
        }

        /**
         * Defines what this animation should do when it reaches the end. This
         * setting is applied only when the repeat count is either greater than
         * 0 or {@link # INFINITE}. Defaults to {@link # RESTART}.
         *
         * @param mode {@link # RESTART} or {@link # REVERSE}
         */
        public AlphaBuilder setRepeatMode(@RepeatMode int mode) {
            this.mode = mode;
            return this;
        }

        /**
         * 设置缩放值，与alphaValue不可重用
         *
         * @param values
         * @return
         */
        public AlphaBuilder alphaValues(float... values) {
            this.values = values;
            return this;
        }

        /**
         * 开始动画
         */
        public void start() {
            getAlphaAnimation().start();
        }

        /**
         * 获得透明度动画
         *
         * @return
         */
        @SuppressLint("WrongConstant")
        private ObjectAnimator getAlphaAnimation() {
            ObjectAnimator alphaAnimation;
            if (values != null && values.length > 0) {
                alphaAnimation = ObjectAnimator.ofFloat(target, "alpha", values);
            } else {
                alphaAnimation = ObjectAnimator.ofFloat(target, "alpha", fromAlphaValue, toAlphaValue);
            }
            alphaAnimation.setDuration(duration);
            alphaAnimation.setRepeatCount(count);
            alphaAnimation.setRepeatMode(mode);
            if (updateListener != null) {
                alphaAnimation.addUpdateListener(updateListener);
            }
            if (pauseListener != null) {
                alphaAnimation.addPauseListener(pauseListener);
            }
            if (animatorListener != null) {
                alphaAnimation.addListener(animatorListener);
            }
            alphaAnimation.setInterpolator(interpolator);
            return alphaAnimation;
        }

        @Override
        public void unbind() {
            count = 0;
            mode = RESTART;
            duration = 500;
            target = null;
            updateListener = null;
            pauseListener = null;
            values = null;
            animatorListener = null;
        }
    }

    @IntDef({RESTART, REVERSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RepeatMode {
    }
    /**
     * When the animation reaches the end and <code>repeatCount</code> is INFINITE
     * or a positive value, the animation restarts from the beginning.
     */
    public static final int RESTART = 1;
    /**
     * When the animation reaches the end and <code>repeatCount</code> is INFINITE
     * or a positive value, the animation reverses direction on every iteration.
     */
    public static final int REVERSE = 2;
    /**
     * This value used used with the {@link # setRepeatCount(int)} property to repeat
     * the animation indefinitely.
     */
    public static final int INFINITE = -1;

    /**
     * 缩放动画
     */
    public class ScaleBuilder implements UnBind {
        private int count = 0, mode = RESTART;
        private float fromXValue, toXValue, fromYValue, toYValue;
        private AnimatorSet animationSet = new AnimatorSet();
        private boolean scaleX, scaleY;
        private float[] values;

        private ScaleBuilder() {
        }

        /**
         * When the animation reaches the end and <code>repeatCount</code> is INFINITE
         * or a positive value, the animation restarts from the beginning.
         * <p>
         * public static final int RESTART = 1;
         * <p>
         * When the animation reaches the end and <code>repeatCount</code> is INFINITE
         * or a positive value, the animation reverses direction on every iteration.
         * <p>
         * public static final int REVERSE = 2;
         * <p>
         * This value used used with the {@link #setRepeatCount(int)} property to repeat
         * the animation indefinitely.
         * <p>
         * public static final int INFINITE = -1;
         *
         * @param count
         * @return
         */
        public ScaleBuilder setRepeatCount(int count) {
            this.count = count;
            return this;
        }

        /**
         * Defines what this animation should do when it reaches the end. This
         * setting is applied only when the repeat count is either greater than
         * 0 or {@link # INFINITE}. Defaults to {@link # RESTART}.
         *
         * @param mode {@link # RESTART} or {@link # REVERSE}
         */
        public ScaleBuilder setRepeatMode(@RepeatMode int mode) {
            this.mode = mode;
            return this;
        }

        /**
         * x轴缩放值
         *
         * @param fromValue
         * @param toValue
         * @return
         */
        public ScaleBuilder scaleXValue(float fromValue, float toValue) {
            this.fromXValue = fromValue;
            this.toXValue = toValue;
            scaleX = true;
            return this;
        }

        /**
         * y轴缩放值
         *
         * @param fromValue
         * @param toValue
         * @return
         */
        public ScaleBuilder scaleYValue(float fromValue, float toValue) {
            this.fromYValue = fromValue;
            this.toYValue = toValue;
            scaleY = true;
            return this;
        }

        /**
         * 缩放值
         *
         * @param fromValue
         * @param toValue
         * @return
         */
        public ScaleBuilder scaleValue(float fromValue, float toValue) {
            this.fromYValue = fromValue;
            this.toYValue = toValue;
            this.fromXValue = fromValue;
            this.toXValue = toValue;
            scaleX = true;
            scaleY = true;
            return this;
        }

        /**
         * 设置缩放值，与scaleValue，scaleXValue，scaleYValue不可重用
         *
         * @param values
         * @return
         */
        public ScaleBuilder scaleValues(float... values) {
            this.values = values;
            return this;
        }

        /**
         * 执行X轴缩放
         *
         * @return
         */
        public ScaleBuilder scaleX() {
            this.scaleX = true;
            return this;
        }

        /**
         * 执行Y轴缩放
         *
         * @return
         */
        public ScaleBuilder scaleY() {
            this.scaleY = true;
            return this;
        }

        /**
         * 开始动画
         */
        public void start() {
            if (pauseListener != null) {
                animationSet.addPauseListener(pauseListener);
            }
            if (animatorListener != null) {
                animationSet.addListener(animatorListener);
            }
            animationSet.playTogether(getScaleAnimation());
            animationSet.start();
        }


        /**
         * 获得缩放动画
         *
         * @return
         */
        @SuppressLint("WrongConstant")
        private Collection<Animator> getScaleAnimation() {
            List<Animator> animators = new ArrayList<>();

            if (scaleX) {
                ObjectAnimator sx;
                if (values != null && values.length > 0) {
                    sx = ObjectAnimator.ofFloat(target, "scaleX", values);
                } else {
                    sx = ObjectAnimator.ofFloat(target, "scaleX", fromXValue, toXValue);
                }
                sx.setRepeatCount(count);
                sx.setRepeatMode(mode);
                sx.setDuration(duration);
                sx.setInterpolator(interpolator);
                animators.add(sx);
            }

            if (scaleY) {
                ObjectAnimator sy;
                if (values != null && values.length > 0) {
                    sy = ObjectAnimator.ofFloat(target, "scaleY", values);
                } else {
                    sy = ObjectAnimator.ofFloat(target, "scaleY", fromYValue, toYValue);
                }
                sy.setRepeatCount(count);
                sy.setRepeatMode(mode);
                sy.setDuration(duration);
                sy.setInterpolator(interpolator);
                animators.add(sy);
            }
            return animators;
        }

        @Override
        public void unbind() {
            count = 0;
            mode = RESTART;
            duration = 500;
            scaleX = scaleY = false;
            target = null;
            updateListener = null;
            pauseListener = null;
            animatorListener = null;
            values = null;
        }
    }

    /**
     * 二阶贝塞尔曲线
     */
    public class CubicBuilder implements UnBind {

        private float spinX0, spinY0, spinX1, spinY1, sX = target.getX(), sY = target.getY(), eX, eY;

        private CubicBuilder() {
        }

        /**
         * 设置拐点
         *
         * @param spinX0
         * @param spinY0
         * @return
         */
        public CubicBuilder spin(float spinX0, float spinY0, float spinX1, float spinY1) {
            this.spinX0 = spinX0;
            this.spinY0 = spinY0;
            this.spinX1 = spinX1;
            this.spinY1 = spinY1;
            return this;
        }

        /**
         * 起始点不设置则为view当前的位置
         *
         * @param sX
         * @param sY
         * @return
         */
        public CubicBuilder begin(float sX, float sY) {
            this.sX = sX;
            this.sY = sY;
            return this;
        }

        /**
         * 结束点
         *
         * @param eX
         * @param eY
         * @return
         */
        public CubicBuilder end(float eX, float eY) {
            this.eX = eX;
            this.eY = eY;
            return this;
        }

        /**
         * 开始动画
         */
        public void start() {
            getAnim().start();
        }

        private ObjectAnimator getAnim() {
            return getAnimator(new CubicEvaluator(spinX0, spinY0, spinX1, spinY1), getPointF(sX, sY), getPointF(eX, eY));
        }

        @Override
        public void unbind() {
            duration = 500;
            target = null;
            updateListener = null;
            pauseListener = null;
            animatorListener = null;
        }

        class CubicEvaluator implements TypeEvaluator<PointF> {

            private float x1, y1, x2, y2;

            public CubicEvaluator(float x1, float y1, float x2, float y2) {
                this.x1 = x1;
                this.y1 = y1;
                this.x2 = x2;
                this.y2 = y2;
            }

            @Override
            public PointF evaluate(float t, PointF p0, PointF p3) {
                float x = p0.x * (1 - t) * (1 - t) * (1 - t) + 3 * x1 * t * (1 - t) * (1 - t) + 3 * x2 * t * t * (1 - t) + p3.x * t * t * t;
                float y = p0.y * (1 - t) * (1 - t) * (1 - t) + 3 * y1 * t * (1 - t) * (1 - t) + 3 * y2 * t * t * (1 - t) + p3.y * t * t * t;
                PointF pointF = new PointF();
                pointF.y = y;
                pointF.x = x;
                return pointF;
            }
        }
    }


    /**
     * 一阶贝塞尔曲线
     */
    public class QuadBuilder implements UnBind {

        private float spinX, spinY, sX = target.getX(), sY = target.getY(), eX, eY;

        private QuadBuilder() {
        }

        /**
         * 设置拐点
         *
         * @param spinX
         * @param spinY
         * @return
         */
        public QuadBuilder spin(float spinX, float spinY) {
            this.spinX = spinX;
            this.spinY = spinY;
            return this;
        }

        /**
         * 起始点不设置则为view当前的位置
         *
         * @param sX
         * @param sY
         * @return
         */
        public QuadBuilder begin(float sX, float sY) {
            this.sX = sX;
            this.sY = sY;
            return this;
        }

        /**
         * 结束点
         *
         * @param eX
         * @param eY
         * @return
         */
        public QuadBuilder end(float eX, float eY) {
            this.eX = eX;
            this.eY = eY;
            return this;
        }

        /**
         * 开始动画
         */
        public void start() {
            getAnim().start();
        }

        private ObjectAnimator getAnim() {
            return getAnimator(new QuadEvaluator(spinX, spinY), getPointF(sX, sY), getPointF(eX, eY));
        }

        @Override
        public void unbind() {
            duration = 500;
            target = null;
            updateListener = null;
            pauseListener = null;
            animatorListener = null;
        }

        class QuadEvaluator implements TypeEvaluator<PointF> {

            float x1, y1;

            public QuadEvaluator(float x1, float y1) {
                this.x1 = x1;
                this.y1 = y1;
            }

            @Override
            public PointF evaluate(float t, PointF p0, PointF p2) {
                float x = (1 - t) * (1 - t) * p0.x + 2 * t * (1 - t) * x1 + t * t * p2.x;
                float y = (1 - t) * (1 - t) * p0.y + 2 * t * (1 - t) * y1 + t * t * p2.y;
                PointF pointF = new PointF();
                pointF.x = x;
                pointF.y = y;
                return pointF;
            }
        }
    }

    public class MoveBuilder implements UnBind {

        private float sx = target.getX(), sy = target.getY(), ex, ey;

        private MoveBuilder() {
        }

        /**
         * 从当前位置移动到x,y处
         *
         * @param x
         * @param y
         */
        public MoveBuilder moveTo(float x, float y) {
            ex = x;
            ey = y;
            return this;
        }

        /**
         * 从sx,sy处移动到ex,ey处
         *
         * @param sx
         * @param sy
         * @param ex
         * @param ey
         */
        public MoveBuilder moveTo(float sx, float sy, float ex, float ey) {
            this.sx = sx;
            this.sy = sy;
            this.ex = ex;
            this.ey = ey;
            return this;
        }

        /**
         * 开始动画
         */
        public void start() {
            if (getAnim() != null) {
                getAnim().start();
            }
        }

        private ObjectAnimator getAnim() {
            return getAnimator(new MoveLineEvaluator(), getPointF(sx, sy), getPointF(ex, ey));
        }

        @Override
        public void unbind() {
            duration = 500;
            target = null;
            updateListener = null;
            pauseListener = null;
            animatorListener = null;
        }

        class MoveLineEvaluator implements TypeEvaluator<PointF> {
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                int x = (int) ((endValue.x - startValue.x) * fraction);
                int y = (int) ((endValue.y - startValue.y) * fraction);

                PointF pointF = new PointF();
                pointF.x = startValue.x + x;
                pointF.y = startValue.y + y;
                return pointF;
            }
        }
    }

    @Override
    public void unbind() {
        if(!CollectUtil.isEmpty(unBinds)){
            for (UnBind unBind : unBinds) {
                unBind.unbind();
            }
        }
        duration = 500;
        target = null;
        updateListener = null;
        pauseListener = null;
        animatorListener = null;
    }
}
