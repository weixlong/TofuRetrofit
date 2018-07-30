package com.pudding.tofu.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class LoadDialog extends ProgressDialog {


    public LoadDialog(Context context ,int theme) {
        super(context,theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(createView());
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    private View createView(){
        RelativeLayout layout = new RelativeLayout(this.getContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(ContextCompat.getColor(getContext(),android.R.color.transparent));


        ImageView imageView = new ImageView(getContext());
        RelativeLayout.LayoutParams imageParam = new RelativeLayout.LayoutParams(dipToPX(getContext(),100), dipToPX(getContext(),100));
        imageParam.addRule(CENTER_IN_PARENT);
        layout.setLayoutParams(imageParam);
        RoundRectDrawable drawable = new RoundRectDrawable();

        drawable.setColor(ContextCompat.getColor(getContext(),android.R.color.tertiary_text_dark));
        imageView.setBackground(drawable);
        layout.addView(imageView,imageParam);

        ProgressBar bar = new ProgressBar(getContext());
        RelativeLayout.LayoutParams barParam = new RelativeLayout.LayoutParams(dipToPX(getContext(),40), dipToPX(getContext(),40));
        barParam.addRule(CENTER_IN_PARENT);
        bar.setLayoutParams(barParam);
        layout.addView(bar,barParam);

        return layout;
    }

    public void showDialog(){
        if(!isShowing()){
            show();
        }
    }

    public void closeDialog(){
        if(isShowing()){
            dismiss();
        }
    }

    class RoundRectDrawable extends Drawable {
        private static final float DEFAULT_RADIUS = 6.f;
        private Paint mPaint = new Paint();
        private RoundRectShape mShape;
        private float[] mOuter;
        private int mColor;
        private int mPressColor;
        private float mTopLeftRadius = DEFAULT_RADIUS;
        private float mTopRightRadius = DEFAULT_RADIUS;
        private float mBottomLeftRadius = DEFAULT_RADIUS;
        private float mBottomRightRadius = DEFAULT_RADIUS;
        public RoundRectDrawable() {
            mColor = Color.WHITE;
            mPressColor = Color.WHITE;
            mPaint.setColor(mColor);
            mPaint.setAntiAlias(true);
        }
        public float getTopLeftRadius() {
            return mTopLeftRadius;
        }
        public void setTopLeftRadius(float topLeftRadius) {
            this.mTopLeftRadius = topLeftRadius;
        }
        public float getTopRightRadius() {
            return mTopRightRadius;
        }
        public void setTopRightRadius(float topRightRadius) {
            this.mTopRightRadius = topRightRadius;
        }
        public float getBottomLeftRadius() {
            return mBottomLeftRadius;
        }
        public void setBottomLeftRadius(float bottomLeftRadius) {
            this.mBottomLeftRadius = bottomLeftRadius;
        }
        public float getBottomRightRadius() {
            return mBottomRightRadius;
        }
        public void setBottomRightRadius(float bottomRightRadius) {
            this.mBottomRightRadius = bottomRightRadius;
        }
        public int getPressColor() {
            return mPressColor;
        }
        public void setPressColor(int pressColor) {
            this.mPressColor = pressColor;
        }
        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            refreshShape();
            mShape.resize(bounds.right - bounds.left, bounds.bottom - bounds.top);
        }
        private void refreshShape(){
            mOuter = new float[]{mTopLeftRadius, mTopLeftRadius
                    , mTopRightRadius, mTopRightRadius
                    , mBottomLeftRadius, mBottomLeftRadius
                    , mBottomRightRadius, mBottomLeftRadius};
            mShape = new RoundRectShape(mOuter, null, null);
        }
        public void setColor(int color){
            mColor = color;
            mPaint.setColor(color);
        }
        @Override
        public void draw(Canvas canvas) {
            mShape.draw(canvas, mPaint);
        }
        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
        }
        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
        }
        @Override
        public int getOpacity() {
            return mPaint.getAlpha();
        }
    }

    public static int dipToPX(final Context ctx, float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, ctx.getResources().getDisplayMetrics());
    }
}
