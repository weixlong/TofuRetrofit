package com.pudding.tofu.model;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.pudding.tofu.widget.CollectUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by wxl on 2018/8/24 0024.
 * 邮箱：632716169@qq.com
 */

public class AnimBuilder implements UnBind {

    private TogetherBuilder togetherBuilder;

    private PlayOnBuilder playOnBuilder;

    private List<ViewBind> unBinds = new ArrayList<>();

    private int unLockNum = 0;

    private class ViewBind {
        UnBind unBind;
        View target;
        String clazzName;

        public ViewBind(UnBind unBind, View target, String clazzName) {
            this.unBind = unBind;
            this.target = target;
            this.clazzName = clazzName;
        }

        private boolean equals(@NonNull View target, String clazzName) {
            if (this.target == null || target == null) return false;
            if (TextUtils.isEmpty(this.clazzName) || !TextUtils.equals(this.clazzName, clazzName))
                return false;

            Object tag = this.target.getTag(0x0066966589);
            Object tag1 = target.getTag(0x0066966589);

            if (tag1 == null || tag == null || ((Long) tag).intValue() != ((Long) tag1).intValue())
                return false;

            return true;
        }
    }


    protected AnimBuilder() {
    }


    /**
     * 平移
     *
     * @return
     */
    public MoveBuilder move(@NonNull View target) {
        return build(MoveBuilder.class, target);
    }


    /**
     * 构建一个builder
     *
     * @param clazz
     * @param target
     * @param <Builder>
     * @return
     */
    private <Builder> Builder build(Class<Builder> clazz, View target) {
        try {
            if (!CollectUtil.isEmpty(unBinds)) {
                for (ViewBind unBind : unBinds) {
                    if (unBind.equals(target, clazz.getName())) {
                        if (unBind.unBind instanceof MoveBuilder) {
                            MoveBuilder move = (MoveBuilder) unBind.unBind;
                            move.positionF.clear();
                            move.target = unBind.target;
                        }

                        if (unBind.unBind instanceof ColorBuilder) {
                            ColorBuilder color = (ColorBuilder) unBind.unBind;
                            color.colors.clear();
                            color.isText = false;
                            color.target = unBind.target;
                        }

                        return (Builder) unBind.unBind;
                    }
                }
            }
            Constructor con2 = clazz.getDeclaredConstructor(this.getClass());
            con2.setAccessible(true);
            Object obj2 = con2.newInstance(this);
            BaseAnim baseAnim = (BaseAnim) obj2;
            baseAnim.target(target);
            unBinds.add(new ViewBind((UnBind) baseAnim, target, clazz.getName()));
            return (Builder) baseAnim;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 一阶贝塞尔曲线
     *
     * @return
     */
    public QuadBuilder quad(@NonNull View target) {
        return build(QuadBuilder.class, target);
    }

    /**
     * 旋转动画
     *
     * @return
     */
    public RotateBuilder rotate(@NonNull View target) {
        return build(RotateBuilder.class, target);
    }

    /**
     * 二阶贝塞尔曲线
     *
     * @return
     */
    public CubicBuilder cubic(@NonNull View target) {
        return build(CubicBuilder.class, target);
    }

    /**
     * 缩放动画
     *
     * @return
     */
    public ScaleBuilder scale(@NonNull View target) {
        return build(ScaleBuilder.class, target);
    }

    /**
     * 透明动画
     *
     * @return
     */
    public AlphaBuilder alpha(@NonNull View target) {
        return build(AlphaBuilder.class, target);
    }

    /**
     * 旋转动画
     *
     * @return
     */
    public AlwaysRotateBuilder alwaysRotate(@NonNull View target) {
        return build(AlwaysRotateBuilder.class, target);
    }


    /**
     * 同时播放动画
     *
     * @return
     */
    public TogetherBuilder together() {
        togetherBuilder = new TogetherBuilder();
        unBinds.add(new ViewBind(togetherBuilder, null, TogetherBuilder.class.getName()));
        togetherBuilder.clearAnim();
        return togetherBuilder;
    }

    /**
     * 连续播放动画
     *
     * @return
     */
    public PlayOnBuilder playOn() {
        playOnBuilder = new PlayOnBuilder();
        unBinds.add(new ViewBind(playOnBuilder, null, PlayOnBuilder.class.getName()));
        return playOnBuilder;
    }

    /**
     * 帧动画
     *
     * @return
     */
    public FrameBuilder frame(@NonNull View target) {
        return build(FrameBuilder.class, target);
    }

    /**
     * 颜色动画
     *
     * @return
     */
    public ColorBuilder color(@NonNull View target) {
        return build(ColorBuilder.class, target);
    }


    private PointF getPointF(float x, float y) {
        PointF p = new PointF(x, y);
        return p;
    }


    /**
     * 颜色动画
     */
    public class ColorBuilder extends BaseAnim<ColorBuilder> implements UnBind {

        private List<Integer> colors = new ArrayList<>();

        private boolean isText;

        private int count = 0, mode = RESTART;

        private ColorBuilder() {
        }

        /**
         * 添加颜色值
         *
         * @param colorValues
         * @return
         */
        public ColorBuilder colorValues(@ColorInt int... colorValues) {
            if (colorValues != null) {
                for (int colorValue : colorValues) {
                    colors.add(colorValue);
                }
            }
            return this;
        }

        /**
         * 添加颜色资源
         *
         * @param colorRes
         * @return
         */
        public ColorBuilder colorRes(@ColorRes int... colorRes) {
            checkViewAvailable();
            if (colorRes != null) {
                for (int i : colorRes) {
                    int colorValue = ContextCompat.getColor(target.getContext(), i);
                    colors.add(colorValue);
                }
            }
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
        public ColorBuilder setRepeatCount(int count) {
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
        public ColorBuilder setRepeatMode(@RepeatMode int mode) {
            this.mode = mode;
            return this;
        }


        /**
         * 对TextView类型改变字的颜色
         *
         * @return
         */
        public ColorBuilder text(boolean isText) {
            this.isText = isText;
            return this;
        }

        /**
         * 开始动画
         */
        public void start() {
            getAnim().start();
            colors.clear();
            duration = 500;
            delay = 0;
        }

        /**
         * 获得动画
         *
         * @return
         */
        @SuppressLint("WrongConstant")
        private ValueAnimator getAnim() {
            Object[] ints = new Integer[colors.size()];
            ints = colors.toArray(ints);
            final ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), ints);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (isText) {
                        if (target instanceof TextView) {
                            TextView textView = (TextView) target;
                            textView.setTextColor((Integer) animation.getAnimatedValue());
                        } else {
                            throw new ClassCastException("Are you sure target view is TextView class ???");
                        }
                    } else {
                        target.setBackgroundColor((Integer) animation.getAnimatedValue());
                    }
                }
            });
            valueAnimator.setDuration(duration);
            valueAnimator.setStartDelay(delay);
            if (animatorListener != null) {
                valueAnimator.addListener(animatorListener);
            }

            if (pauseListener != null) {
                valueAnimator.addPauseListener(pauseListener);
            }

            valueAnimator.setInterpolator(interpolator);
            valueAnimator.setRepeatCount(count);
            valueAnimator.setRepeatMode(mode);
            return valueAnimator;
        }

        @Override
        public void unbind() {
            duration = 500;
            delay = 0;
            target = null;
            isText = false;
            colors.clear();
            count = 0;
            mode = RESTART;
            adapter = null;
        }
    }

    /**
     * 帧动画
     */
    public class FrameBuilder extends BaseAnim<FrameBuilder> implements UnBind {

        private AnimationDrawable anim = new AnimationDrawable();

        private FrameBuilder() {

        }

        /**
         * 添加帧
         *
         * @param duration
         * @param drawableIds
         * @return
         */
        public FrameBuilder add(@IntRange(from = 0) int duration, @DrawableRes int... drawableIds) {
            checkViewAvailable();
            if (drawableIds != null) {
                for (int drawableId : drawableIds) {
                    Drawable drawable = target.getContext().getResources().getDrawable(drawableId);
                    anim.addFrame(drawable, duration);
                }
            }
            return this;
        }

        /**
         * 播放
         */
        public void start() {
            checkViewAvailable();
            if (target instanceof ImageView) {
                ImageView imageView = (ImageView) target;
                imageView.setImageDrawable(anim);
                anim.start();
            } else {
                throw new ClassCastException("Are your sure target view is ImageView class ???");
            }
        }

        /**
         * 停止播放
         */
        public void stop() {
            anim.stop();
        }

        /**
         * 移除动画
         */
        public void removeAnim() {
            checkViewAvailable();
            if (target instanceof ImageView) {
                ImageView imageView = (ImageView) target;
                imageView.clearAnimation();
            }
        }

        /**
         * false为循环播放，true为仅播放一次
         *
         * @param isOneShot
         * @return
         */
        public FrameBuilder oneShot(boolean isOneShot) {
            anim.setOneShot(isOneShot);
            return this;
        }

        @Override
        public void unbind() {
            if (target == null) return;
            if (target instanceof ImageView) {
                ImageView imageView = (ImageView) target;
                imageView.clearAnimation();
                imageView.setImageDrawable(null);
                duration = 500;
                target = null;
            }
        }
    }

    /**
     * 序列播放动画
     */
    public class PlayOnBuilder implements UnBind {

        private List<Object> anims = new ArrayList<>();

        private List<UnBind> unBinds = new ArrayList<>();

        private AnimRunAdapter adapter;

        protected PlayOnBuilder() {

        }

        /**
         * 旋转动画
         *
         * @param builder
         * @return
         */
        public PlayOnBuilder rotate(@NonNull AlwaysRotateBuilder builder) {
            if (builder != null) {
                anims.add(builder.getRotationXYZAnim());
                unBinds.add(builder);
            }
            return this;
        }

        /**
         * 序列动画监听
         *
         * @param callback
         * @return
         */
        public PlayOnBuilder setAnimRunBack(AnimRunAdapter callback) {
            adapter = callback;
            return this;
        }

        /**
         * 透明动画
         *
         * @param builder
         * @return
         */
        public PlayOnBuilder alpha(@NonNull AlphaBuilder builder) {
            if (builder != null) {
                anims.add(builder.getAlphaAnimation());
                unBinds.add(builder);
            }
            return this;
        }

        /**
         * 一阶贝塞尔动画
         *
         * @param builder
         * @return
         */
        public PlayOnBuilder cubic(@NonNull CubicBuilder builder) {
            if (builder != null) {
                anims.add(builder.getAnim());
                unBinds.add(builder);
            }
            return this;
        }

        /**
         * 二阶贝塞尔动画
         *
         * @param builder
         * @return
         */
        public PlayOnBuilder quad(@NonNull QuadBuilder builder) {
            if (builder != null) {
                anims.add(builder.getAnim());
                unBinds.add(builder);
            }
            return this;
        }

        /**
         * 移动动画
         *
         * @param builder
         * @return
         */
        public PlayOnBuilder move(@NonNull MoveBuilder builder) {
            if (builder != null) {
                anims.add(builder.getAnim());
                unBinds.add(builder);
            }
            return this;
        }

        /**
         * 缩放动画
         *
         * @param builder
         * @return
         */
        public PlayOnBuilder scale(@NonNull ScaleBuilder builder) {
            if (builder != null) {
                anims.addAll(builder.getScaleAnimation());
                unBinds.add(builder);
            }
            return this;
        }

        /**
         * 颜色动画
         *
         * @param builder
         * @return
         */
        public PlayOnBuilder color(@NonNull ColorBuilder builder) {
            if (builder != null) {
                anims.add(builder.getAnim());
                unBinds.add(builder);
            }
            return this;
        }

        /**
         * 旋转动画
         *
         * @param builder
         * @return
         */
        public PlayOnBuilder rotate(@NonNull RotateBuilder builder) {
            if (builder != null) {
                anims.add(builder.getAnim());
                unBinds.add(builder);
            }
            return this;
        }


        /**
         * together动画
         *
         * @param builder
         * @return
         */
        public PlayOnBuilder together(@NonNull TogetherBuilder builder) {
            if (builder != null) {
                anims.add(builder);
                unBinds.add(builder);
            }
            return this;
        }

        /**
         * 播放,将会覆盖所有动画监听
         */
        public void start() {
            if (!CollectUtil.isEmpty(anims)) {
                final Object o = anims.get(0);
                if (adapter != null) {
                    adapter.onPlayOnAnimStart(this);
                }
                if (o instanceof Animation) {
                    Animation anim = (Animation) o;
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            if (adapter != null) {
                                adapter.onAnimationStart(animation);
                            }
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            anims.remove(o);
                            start();
                            if (adapter != null) {
                                adapter.onAnimationEnd(animation);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            if (adapter != null) {
                                adapter.onAnimationRepeat(animation);
                            }
                        }
                    });
                    anim.start();
                } else if (o instanceof Animator) {
                    Animator anim = (Animator) o;
                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            if (adapter != null) {
                                adapter.onAnimationStart(animation);
                            }
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            anims.remove(o);
                            start();
                            if (adapter != null) {
                                adapter.onAnimationEnd(animation);
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            if (adapter != null) {
                                adapter.onAnimationCancel(animation);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                            if (adapter != null) {
                                adapter.onAnimationRepeat(animation);
                            }
                        }
                    });
                    anim.start();
                } else if (o instanceof TogetherBuilder) {
                    final TogetherBuilder builder = (TogetherBuilder) o;
                    builder.setListener(new TogetherAnimEndListener() {
                        @Override
                        public void onTogetherAnimEnd() {
                            anims.remove(o);
                            builder.setListener(null);
                            start();
                        }
                    });
                    builder.start();
                }
            } else {
                if (adapter != null) {
                    adapter.onPlayOnAnimEnd(this);
                }
            }
        }


        @Override
        public void unbind() {
            for (UnBind unBind : unBinds) {
                unBind.unbind();
            }
            adapter = null;
            unBinds.clear();
            anims.clear();
        }
    }

    /**
     * 同时执行
     */
    public class TogetherBuilder extends BaseAnim<TogetherBuilder> implements UnBind {
        List<Animation> animations = new ArrayList<>();
        List<Animator> valueAnimators = new ArrayList<>();
        private List<UnBind> unBinds = new ArrayList<>();
        private AnimatorSet animationSet = new AnimatorSet();
        private long duration_max;
        private int anInt = -1, valueInt = -1;

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
                Animation anim = builder.getRotationXYZAnim();
                animations.add(anim);
                unBinds.add(builder);
                if (duration_max < anim.getDuration() + anim.getStartOffset()) {
                    duration_max =anim.getDuration() + anim.getStartOffset();
                    anInt = animations.size() - 1;
                }
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
                ValueAnimator anim = builder.getAlphaAnimation();
                valueAnimators.add(anim);
                unBinds.add(builder);
                long l = anim.getDuration() + anim.getStartDelay();
                if (duration_max < l) {
                    duration_max = l;
                    valueInt = valueAnimators.size() - 1;
                }
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
                ValueAnimator anim = builder.getAnim();
                valueAnimators.add(anim);
                unBinds.add(builder);
                long l = anim.getDuration() + anim.getStartDelay();
                if (duration_max < l) {
                    duration_max = l;
                    valueInt = valueAnimators.size() - 1;
                }
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
                ValueAnimator anim = builder.getAnim();
                valueAnimators.add(anim);
                unBinds.add(builder);
                long l = anim.getDuration() + anim.getStartDelay();
                if (duration_max < l) {
                    duration_max = l;
                    valueInt = valueAnimators.size() - 1;
                }
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
                ValueAnimator anim = builder.getAnim();
                valueAnimators.add(anim);
                unBinds.add(builder);
                long l = anim.getDuration() + anim.getStartDelay();
                if (duration_max < l) {
                    duration_max = l;
                    valueInt = valueAnimators.size() - 1;
                }
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
                List<Animator> scaleAnimation = (List<Animator>) builder.getScaleAnimation();
                if(!CollectUtil.isEmpty(scaleAnimation)) {
                    valueAnimators.addAll(scaleAnimation);
                    unBinds.add(builder);
                    Animator animator = scaleAnimation.get(0);
                    if (duration_max < animator.getDuration() + animator.getStartDelay()) {
                        duration_max = animator.getDuration() + animator.getStartDelay();
                        valueInt = valueAnimators.size() - 1;
                    }
                }
            }
            return this;
        }

        /**
         * 旋转动画
         *
         * @param builder
         * @return
         */
        public TogetherBuilder rotate(@NonNull RotateBuilder builder) {
            if (builder != null) {
                ValueAnimator anim = builder.getAnim();
                valueAnimators.add(anim);
                unBinds.add(builder);
                long l = anim.getDuration() + anim.getStartDelay();
                if (duration_max < l) {
                    duration_max = l;
                    valueInt = valueAnimators.size() - 1;
                }
            }
            return this;
        }

        /**
         * 颜色动画
         *
         * @param builder
         * @return
         */
        public TogetherBuilder color(@NonNull ColorBuilder builder) {
            if (builder != null) {
                ValueAnimator anim = builder.getAnim();
                valueAnimators.add(anim);
                unBinds.add(builder);
                long l = anim.getDuration() + anim.getStartDelay();
                if (duration_max < l) {
                    duration_max = l;
                    valueInt = valueAnimators.size() - 1;
                }
            }
            return this;
        }

        /**
         * 执行动画
         */
        public void start() {
            AnimatorSet clone = animationSet.clone();
            if (!CollectUtil.isEmpty(valueAnimators)) {
                clone.playTogether(new ArrayList<>(valueAnimators));
                clone.start();
            }
            if (!CollectUtil.isEmpty(animations)) {
                for (Animation animation : animations) {
                    animation.start();
                }
            }

            if (adapter != null) {
                adapter.onTogetherAnimStart(this);
            }

            if (anInt >= 0 && valueInt >= 0) {
                if (animations.get(anInt).getDuration() > valueAnimators.get(valueInt).getDuration()) {
                    animations.get(anInt).setAnimationListener(tionListener);
                } else {
                    valueAnimators.get(valueInt).addListener(animListener);
                }
            } else if (anInt < 0 && valueInt >= 0) {
                valueAnimators.get(valueInt).addListener(animListener);
            } else if (anInt >= 0 && valueInt < 0) {
                animations.get(anInt).setAnimationListener(tionListener);
            }

        }

        private Animation.AnimationListener tionListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (adapter != null) {
                    adapter.onAnimationStart(animation);
                }

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listener != null) {
                    listener.onTogetherAnimEnd();
                    duration_max = 0;
                    anInt = -1;
                    valueInt = -1;
                }
                if (adapter != null) {
                    adapter.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (adapter != null) {
                    adapter.onAnimationRepeat(animation);
                }
            }
        };

        private Animator.AnimatorListener animListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (adapter != null) {
                    adapter.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onTogetherAnimEnd();
                    duration_max = 0;
                    anInt = -1;
                    valueInt = -1;
                }
                if (adapter != null) {
                    adapter.onAnimationEnd(animation);
                    adapter.onTogetherAnimEnd(TogetherBuilder.this);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (adapter != null) {
                    adapter.onAnimationCancel(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (adapter != null) {
                    adapter.onAnimationRepeat(animation);
                }
            }
        };

        private void clearAnim() {
            animations.clear();
            valueAnimators.clear();
        }

        @Override
        public void unbind() {
            duration = 500;
            target = null;
            updateListener = null;
            pauseListener = null;
            animatorListener = null;
            adapter = null;
            animations.clear();
            valueAnimators.clear();
            for (UnBind unBind : unBinds) {
                unBind.unbind();
                unBind = null;
            }
            unBinds.clear();
        }

        private TogetherAnimEndListener listener;

        private void setListener(TogetherAnimEndListener listener) {
            this.listener = listener;
        }
    }


    private interface TogetherAnimEndListener {
        void onTogetherAnimEnd();
    }

    public class RotateBuilder extends BaseAnim<RotateBuilder> implements UnBind {
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
        public void start() {
            getAnim().start();
        }


        /**
         * 获取动画
         *
         * @return
         */
        @SuppressLint("WrongConstant")
        private ObjectAnimator getAnim() {
            checkViewAvailable();
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
            objectAnimator.setStartDelay(delay);
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
            count = 0;
            mode = RESTART;
            duration = 500;
            delay = 0;
            rotateX = rotateY = rotateZ = false;
            return objectAnimator;
        }


        @Override
        public void unbind() {
            count = 0;
            mode = RESTART;
            duration = 500;
            delay = 0;
            rotateX = rotateY = rotateZ = false;
            target = null;
            updateListener = null;
            pauseListener = null;
            animatorListener = null;
            values = null;
            adapter = null;
        }
    }


    /**
     * 旋转动画
     */
    public class AlwaysRotateBuilder extends BaseAnim<AlwaysRotateBuilder> implements UnBind {

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
            checkViewAvailable();
            RotationXYZAnimation anim = new RotationXYZAnimation();
            anim.setRepeatCount(count);
            anim.setRepeatMode(mode);
            anim.setStartOffset(delay);
            if (animationListener != null) {
                anim.setAnimationListener(animationListener);
            }
            count = 0;
            mode = RESTART;
            duration = 500;
            delay = 0;
            return anim;
        }

        @Override
        public void unbind() {
            count = 0;
            mode = RESTART;
            animationListener = null;
            rotateX = rotateY = rotateZ = false;
            duration = 500;
            delay = 0;
            target = null;
            updateListener = null;
            pauseListener = null;
            animatorListener = null;
            adapter = null;
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
    public class AlphaBuilder extends BaseAnim<AlphaBuilder> implements UnBind {

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
            checkViewAvailable();
            ObjectAnimator alphaAnimation;
            if (values != null && values.length > 0) {
                alphaAnimation = ObjectAnimator.ofFloat(target, "alpha", values);
            } else {
                alphaAnimation = ObjectAnimator.ofFloat(target, "alpha", fromAlphaValue, toAlphaValue);
            }
            alphaAnimation.setDuration(duration);
            alphaAnimation.setRepeatCount(count);
            alphaAnimation.setRepeatMode(mode);
            alphaAnimation.setStartDelay(delay);
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
            count = 0;
            mode = RESTART;
            duration = 500;
            delay = 0;
            return alphaAnimation;
        }

        @Override
        public void unbind() {
            count = 0;
            mode = RESTART;
            duration = 500;
            delay = 0;
            target = null;
            updateListener = null;
            pauseListener = null;
            values = null;
            animatorListener = null;
            adapter = null;
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
    public class ScaleBuilder extends BaseAnim<ScaleBuilder> implements UnBind {
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
            AnimatorSet clone = animationSet.clone();
            clone.playTogether(getScaleAnimation());
            clone.start();

        }


        /**
         * 获得缩放动画
         *
         * @return
         */
        @SuppressLint("WrongConstant")
        private Collection<Animator> getScaleAnimation() {
            checkViewAvailable();
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
                sx.setStartDelay(delay);
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
                sy.setStartDelay(delay);
                animators.add(sy);
            }
            delay = 0;
            count = 0;
            mode = RESTART;
            duration = 500;
            scaleX = scaleY = false;
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
            adapter = null;
        }
    }

    /**
     * 二阶贝塞尔曲线
     */
    public class CubicBuilder extends BaseAnim<CubicBuilder> implements UnBind {

        private float spinX0, spinY0, spinX1, spinY1, sX, sY, eX, eY;

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
            ObjectAnimator anim = getAnim();
            anim.setStartDelay(delay);
            anim.start();
            duration = 500;
            delay = 0;
        }

        private ObjectAnimator getAnim() {
            checkViewAvailable();
            return getAnimator(new CubicEvaluator(spinX0 == 0 ? target.getX() : spinX0, spinY0 == 0 ? target.getY() : spinY0, spinX1, spinY1), getPointF(sX, sY), getPointF(eX, eY));
        }

        @Override
        public void unbind() {
            duration = 500;
            target = null;
            updateListener = null;
            pauseListener = null;
            animatorListener = null;
            adapter = null;
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
    public class QuadBuilder extends BaseAnim<QuadBuilder> implements UnBind {

        private float spinX, spinY, sX, sY, eX, eY;

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
            ObjectAnimator anim = getAnim();
            anim.setStartDelay(delay);
            anim.start();
            delay = 0;
            duration = 500;
        }

        private ObjectAnimator getAnim() {
            checkViewAvailable();
            return getAnimator(new QuadEvaluator(spinX, spinY), getPointF(sX == 0 ? target.getX() : sX, sY == 0 ? target.getY() : sY), getPointF(eX, eY));
        }

        @Override
        public void unbind() {
            duration = 500;
            target = null;
            updateListener = null;
            pauseListener = null;
            animatorListener = null;
            adapter = null;
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


    /**
     * 移动
     */
    public class MoveBuilder extends BaseAnim<MoveBuilder> implements UnBind {


        List<PointF> positionF = new ArrayList<>();


        /**
         * 从当前位置移动到x,y处
         *
         * @param x
         * @param y
         */
        public MoveBuilder moveTo(float x, float y) {
            positionF.add(new PointF(x, y));
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
            positionF.add(new PointF(sx, sy));
            positionF.add(new PointF(ex, ey));
            return this;
        }

        /**
         * 开始动画
         */
        public void start() {
            ObjectAnimator anim = getAnim();
            anim.start();
        }



        private ObjectAnimator getAnim() {
            checkViewAvailable();
            if (positionF.size() == 1) {
                positionF.add(0, new PointF(target.getX(), target.getY()));
            }
            ObjectAnimator animator = getAnimator(new MoveLineEvaluator(), null, null);
            return animator;
        }

        @Override
        protected ObjectAnimator getAnimator(TypeEvaluator typeEvaluator, PointF startPoint, PointF endPoint) {
            Object[] objects = new Object[positionF.size()];
            objects = positionF.toArray(objects);
            ObjectAnimator animator = ObjectAnimator.ofObject(this, "move", typeEvaluator, objects);
            animator.setDuration(duration);
            animator.setInterpolator(interpolator);
            animator.setStartDelay(delay);
            if (updateListener != null) {
                animator.addUpdateListener(updateListener);
            }
            if (pauseListener != null) {
                animator.addPauseListener(pauseListener);
            }
            if (animatorListener != null) {
                animator.addListener(animatorListener);
            }
            delay = 0;
            duration = 500;
            return animator;
        }

        @Override
        public void unbind() {
            positionF.clear();
            duration = 500;
            target = null;
            updateListener = null;
            pauseListener = null;
            animatorListener = null;
            adapter = null;
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
        if (!CollectUtil.isEmpty(unBinds)) {
            for (ViewBind unBind : unBinds) {
                unBind.unBind.unbind();
                unBind.target = null;
            }
        }
        unBinds.clear();
    }


    private class BaseAnim<Anim extends BaseAnim> {

        View target;

        long duration = 500;

        long delay = 0;

        ValueAnimator.AnimatorUpdateListener updateListener;

        Animation.AnimationListener animationListener;

        Animator.AnimatorPauseListener pauseListener;

        Animator.AnimatorListener animatorListener;

        Interpolator interpolator = new LinearInterpolator();

        AnimRunAdapter adapter;

        protected BaseAnim() {

        }

        /**
         * 需要执行动画的view
         *
         * @param target
         * @return
         */
        private synchronized Anim target(View target) {
            this.target = target;
            target.setTag(0x0066966589, System.currentTimeMillis() + unLockNum);
            unLockNum++;
            return (Anim) this;
        }

        /**
         * 停止动画
         */
        public void stop() {
            if (target != null) {
                Animation animation = target.getAnimation();
                if (animation != null) {
                    animation.cancel();
                }
            }
        }

        /**
         * 动画执行时间，不设置默认500ms
         *
         * @param duration
         * @return
         */
        public Anim duration(long duration) {
            this.duration = duration;
            return (Anim) this;
        }

        /**
         * 延时
         *
         * @param delay
         * @return
         */
        public Anim delay(long delay) {
            this.delay = delay;
            return (Anim) this;
        }



        /**
         * 设置监听
         *
         * @param listener
         * @return
         */
        public Anim setAnimRunBack(AnimRunAdapter listener) {
            adapter = listener;
            updateListener = listener;
            animationListener = listener;
            pauseListener = listener;
            animatorListener = listener;
            return (Anim) this;
        }

        /**
         * 加速动画,不设置则默认匀速
         *
         * @return
         */
        public Anim accelerate() {
            interpolator = new AccelerateInterpolator();
            return (Anim) this;
        }

        /**
         * 减速动画,不设置则默认匀速
         *
         * @return
         */
        public Anim decelerate() {
            interpolator = new DecelerateInterpolator();
            return (Anim) this;
        }

        /**
         * 设置动画为先加速在减速(开始速度最快 逐渐减慢),不设置则默认匀速
         *
         * @return
         */
        public Anim accelerateDecelerate() {
            interpolator = new AccelerateDecelerateInterpolator();
            return (Anim) this;
        }


        /**
         * 先反向执行一段，然后再加速反向回来（相当于我们弹簧，先反向压缩一小段，然后在加速弹出）,不设置则默认匀速
         *
         * @return
         */
        public Anim anticipate() {
            interpolator = new AnticipateInterpolator();
            return (Anim) this;
        }


        /**
         * 先反向一段，然后加速反向回来，执行完毕自带回弹效果（更形象的弹簧效果）,不设置则默认匀速
         *
         * @return
         */
        public Anim anticipateOvershoot() {
            interpolator = new AnticipateOvershootInterpolator();
            return (Anim) this;
        }

        /**
         * 执行完毕之后会回弹跳跃几段（相当于我们高空掉下一颗皮球，到地面是会跳动几下）,不设置则默认匀速
         *
         * @return
         */
        public Anim bounce() {
            interpolator = new BounceInterpolator();
            return (Anim) this;
        }

        /**
         * 循环，动画循环一定次数，值的改变为一正弦函数：Math.sin(2* mCycles* Math.PI* input),不设置则默认匀速
         *
         * @return
         */
        public Anim cycle(float cycles) {
            interpolator = new CycleInterpolator(cycles);
            return (Anim) this;
        }


        /**
         * 加速执行，结束之后回弹,不设置则默认匀速
         *
         * @return
         */
        public Anim overShoot() {
            interpolator = new OvershootInterpolator();
            return (Anim) this;
        }

        /**
         * 自定义动画执行效果,不设置则默认匀速
         *
         * @return
         */
        public Anim setInterpolator(@NonNull Interpolator value) {
            if (value != null) {
                interpolator = value;
            }
            return (Anim) this;
        }

        /**
         * 参数检查
         */
        protected void checkViewAvailable() {
            if (target == null)
                throw new IllegalArgumentException("Are you sure set view target ???");
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
            animator.setStartDelay(delay);
            if (updateListener != null) {
                animator.addUpdateListener(updateListener);
            }
            if (pauseListener != null) {
                animator.addPauseListener(pauseListener);
            }
            if (animatorListener != null) {
                animator.addListener(animatorListener);
            }
            delay = 0;
            duration = 500;
            interpolator = new LinearInterpolator();
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

    }


    public static class AnimRunAdapter implements Animator.AnimatorListener,
            Animator.AnimatorPauseListener,
            Animation.AnimationListener,
            ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        @Override
        public void onAnimationPause(Animator animation) {

        }

        @Override
        public void onAnimationResume(Animator animation) {

        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {

        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        /**
         * together动画开始
         *
         * @param builder
         */
        public void onTogetherAnimStart(TogetherBuilder builder) {

        }

        /**
         * together动画结束
         *
         * @param builder
         */
        public void onTogetherAnimEnd(TogetherBuilder builder) {

        }

        /**
         * 连续动画开始
         *
         * @param builder
         */
        public void onPlayOnAnimStart(PlayOnBuilder builder) {

        }

        /**
         * 连续动画结束
         *
         * @param builder
         */
        public void onPlayOnAnimEnd(PlayOnBuilder builder) {

        }
    }
}
