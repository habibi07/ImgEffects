package com.kgkg.imagevieweffects;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Krzysiek on 2016-03-30.
 */
public class ImageTitle extends TextView {
    private final String TAG="kgkg";
    private int mEffect;
    private int mEffectDuration;
    private int mEffectDirection;
    private boolean drawSmallTriangle = false;
    private boolean drawBigTriangle = false;
    private boolean drawRibbon = false;
    private float triangleWidth = 100.0f;
    private float triangleHeight = 100.0f;
    private int bgColor;
    private boolean isEffectToggled = false;
    Paint paint;
    private float animVal = 0;
    private float ribDx = -50f;
    private float ribDy = -50f;
    private final float factor = 0.2f;


    // TODO: 05.04.16 add corner size properties
    public ImageTitle(Context context) {
        super(context);
    }

    public ImageTitle(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageTitle, 0, 0);
        try {
            mEffect = a.getInt(R.styleable.ImageTitle_TitleEffect, 0);
            mEffectDuration = a.getInt(R.styleable.ImageTitle_mEffectDuration, 0);
            mEffectDirection = a.getInt(R.styleable.ImageTitle_mEffectDirection, 2);
        } finally {
            a.recycle();
        }

        bgColor = ((ColorDrawable) getBackground()).getColor();

        if (mEffect != 5 || mEffect != 6){
            setVisibility(View.INVISIBLE);
        } else {
            drawSmallTriangle = true;
            setVisibility(View.VISIBLE);
            //setTextAlpha(0);


            setBackgroundColor(Color.TRANSPARENT);

            setDrawingCacheEnabled(true);
            setDrawingCacheBackgroundColor(Color.TRANSPARENT);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            //getBackground().setAlpha(0);
            paint.setColor(bgColor);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (drawSmallTriangle || drawBigTriangle){
            ArrayList<PointF> p  = new ArrayList<>();

            int titleBlockPos = ((ImageFrame) this.getParent()).getTitleBlockPosition();
            Path path = new Path();
            if (titleBlockPos == 1 || titleBlockPos == 0){
                if (mEffectDirection == 1){
                    //top right

                    path.moveTo(getWidth(), 0f);
                    p.add(new PointF(getWidth(), 0f));
                    path.lineTo(getWidth(), triangleHeight);
                    p.add(new PointF(getWidth(), triangleHeight));
                    path.lineTo(getWidth() - triangleWidth, 0f);
                    p.add(new PointF(getWidth() - triangleWidth, 0f));
                    path.close();
                } else {
                    //top left
                    path.moveTo(0f, 0f);
                    p.add(new PointF(0f, 0f));
                    path.lineTo(0f, triangleHeight);
                    p.add(new PointF(0f, triangleHeight));
                    path.lineTo(triangleWidth, 0f);
                    p.add(new PointF(triangleWidth, 0f));
                    path.close();
                }
            } else {
                if (mEffectDirection == 1){
                    //bottom right
                    path.moveTo(getWidth(), getHeight());
                    p.add(new PointF(getWidth(), getHeight()));
                    path.lineTo(getWidth() - triangleWidth, getHeight());
                    p.add(new PointF(getWidth() - triangleWidth, getHeight()));
                    path.lineTo(getWidth(), getHeight() - triangleHeight);
                    p.add(new PointF(getWidth(), getHeight() - triangleHeight));
                    path.close();
                } else {

                    //bottom left
                    path.moveTo(0f, getHeight());
                    p.add(new PointF(0f, getHeight()));
                    path.lineTo(triangleWidth, getHeight());
                    p.add(new PointF(triangleWidth, getHeight()));
                    path.lineTo(0f, getHeight() - triangleHeight);
                    p.add(new PointF(0f, getHeight() - triangleHeight));
                    path.close();
                }
            }
            if (drawSmallTriangle){
                canvas.drawPath(path, paint);
            }
            if (drawBigTriangle){
                canvas.drawPath(path, paint);
                super.onDraw(canvas);
            }

        } else if (drawRibbon){
            Rect newRect = canvas.getClipBounds();
            newRect.inset(-Math.abs((int) ribDx), -Math.abs((int) ribDy));

            canvas.clipRect(newRect, Region.Op.REPLACE);

            Paint paintCurrent = new Paint();
            paintCurrent.setColor(bgColor);

            Paint newPaint = new Paint();
            int r = Color.red(bgColor);
            int g = Color.green(bgColor);
            int b = Color.blue(bgColor);
            float fact = 0.7f;
            float fr = Color.red(bgColor) * fact;
            float fg = Color.green(bgColor) * fact;
            float fb = Color.blue(bgColor) * fact;

            int colChange = 50;

            newPaint.setColor(Color.argb(255,(int)fr, (int)fg, (int)fb));
            if (animVal < factor){
                float f1 = (1/ factor) * animVal;

                Path ribPath = new Path();
                if (mEffectDirection == 1){
                    ribPath.moveTo(getWidth(), getHeight());
                    ribPath.lineTo(getWidth() + ribDx * f1, getHeight() + ribDy * f1);
                    ribPath.lineTo(getWidth() + ribDx * f1, ribDy *f1);
                    ribPath.lineTo(getWidth() , 0);
                    ribPath.close();
                } else {
                    ribPath.moveTo(0, getHeight());
                    ribPath.lineTo(ribDx * f1, getHeight() + ribDy * f1);
                    ribPath.lineTo(ribDx * f1, ribDy *f1);
                    ribPath.lineTo(0, 0);
                    ribPath.close();
                }
                canvas.drawPath(ribPath, newPaint);
            } else {
                float f2 = (animVal - factor) / (1f - factor);
                Rect rect;
                Path ribPath = new Path();
                if (mEffectDirection == 1){
                    ribPath.moveTo(getWidth(), getHeight());
                    ribPath.lineTo(getWidth() + ribDx, getHeight() + ribDy);
                    ribPath.lineTo(getWidth() + ribDx, ribDy);
                    ribPath.lineTo(getWidth(), 0);
                    ribPath.close();
                    rect = new Rect((int) (getWidth() * (1 - f2) + (int) ribDx),
                            (int) ribDy,
                            canvas.getWidth() + (int) ribDx,
                            canvas.getHeight() + (int) ribDy);

                } else {
                    ribPath.moveTo(0, getHeight());
                    ribPath.lineTo(ribDx, getHeight() + ribDy);
                    ribPath.lineTo(ribDx, ribDy);
                    ribPath.lineTo(0, 0);
                    ribPath.close();
                    rect = new Rect((int) ribDx, (int) ribDy, canvas.getWidth() + (int) ribDx, canvas.getHeight() + (int) ribDy);

                }
                canvas.drawPath(ribPath, newPaint);

                Log.i(TAG, "onDraw: rect Left: " + rect.left);

                Paint textPaint = super.getPaint();
                Rect textBounds = new Rect();
                
                String text = (String) getText();

                textPaint.getTextBounds(text, 0, text.length(), textBounds);
                textPaint.setColor(getCurrentTextColor());
                textPaint.setTextScaleX(animVal);

                float yPos;
                yPos = ((textBounds.bottom - textBounds.top) / 2) * getResources().getDisplayMetrics().density;

                if (mEffectDirection == 1){
                    canvas.scale(animVal, 1f,getWidth() + ribDx, ribDy);
                } else {
                    canvas.scale(animVal, 1f,ribDx, ribDy);
                }
                canvas.drawRect(rect, paintCurrent);
               // Log.i(TAG, "onDraw: rect.centerY(): " + rect.centerY());
                float znak = ribDy / Math.abs(ribDy);
                canvas.drawText(text,
                        (rect.centerX() - (textPaint.getTextSize() * getResources().getDisplayMetrics().density) / 2) + ribDx * f2,
                        rect.centerY() + (znak * ribDy) - textBounds.height() / 2,
                        textPaint);
            }
        } else {
            Log.i(TAG, "onDraw: when else");
            super.onDraw(canvas);
        }

    }

    public int getEffect() {
        return mEffect;
    }

    public void setEffect(int mEffect) {
        this.mEffect = mEffect;
    }

    public int getEffectDirection() {
        return mEffectDirection;
    }

    public void setEffectDirection(int mEffectDirection) {
        this.mEffectDirection = mEffectDirection;
    }

    public int getEffectDuration() {
        return mEffectDuration;
    }

    public void setEffectDuration(int mEffectDuration) {
        this.mEffectDuration = mEffectDuration;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public String toString() {
        return "ImageTitle{" +
                "mEffect=" + mEffect +
                ", mEffectDuration=" + mEffectDuration +
                ", mEffectDirection=" + mEffectDirection +
                '}';
    }

    protected ValueAnimator[] getEffectAnimation(){
        return getEffectAnimation(mEffect);
    }

    protected ValueAnimator[] getEffectAnimation(int effect){
        switch (effect){
            case 0:
                break;
            case 1:
                return getFadeAnimation();
            case 2:
                return getSlideAnimation();
            case 3:
                return getMoveAnimation();
            case 4:
                return getUncoverAnimation();
            case 5:
                return getCornerAnimation();
            case 6:
                return getRibbonAnimation();
            default:
                return null;
        }
        return null;
    }

    private ValueAnimator[] getFadeAnimation(){
        ObjectAnimator a = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).setDuration(mEffectDuration);
        return new ValueAnimator[]{a};
    }

    private ValueAnimator[] getSlideAnimation(int direction){
        int parentHeight = ((ImageFrame) this.getParent()).getHeight();
        int parentWidth = ((ImageFrame) this.getParent()).getWidth();
        switch (direction){
            case 0:
                ObjectAnimator leftSlide = ObjectAnimator.ofFloat(this, "x", -parentWidth, 0).setDuration(mEffectDuration);
                return new ValueAnimator[]{leftSlide};
            case 1:
                ObjectAnimator rightSlide = ObjectAnimator.ofFloat(this, "x", parentWidth, 0).setDuration(mEffectDuration);
                return new ValueAnimator[]{rightSlide};
            case 2:
                ObjectAnimator topSlide = ObjectAnimator.ofFloat(this, "y", - this.getHeight(), this.getY()).setDuration(mEffectDuration);
                return new ValueAnimator[]{topSlide};
            case 3:
                ObjectAnimator bottomSlide =  ObjectAnimator.ofFloat(this, "y", parentHeight, this.getY()).setDuration(mEffectDuration);
                return new ValueAnimator[]{bottomSlide};
            default:
                return new ValueAnimator[]{null};
        }

    }

    private ValueAnimator[] getSlideAnimation(){
        return getSlideAnimation(mEffectDirection);
    }
    private ValueAnimator[] getMoveAnimation(){
        ObjectAnimator b;
        if (((ImageFrame) this.getParent()).getTitleBlockPosition() == 1){
            b = ObjectAnimator.ofFloat(this, "y", - this.getHeight(), 0).setDuration(mEffectDuration);
        } else {
            b = (ObjectAnimator) getSlideAnimation()[0];
        }
        return new ValueAnimator[]{b};
    }
    private ValueAnimator[] getUncoverAnimation(){
        return getMoveAnimation();
    }
    private ValueAnimator[] getCornerAnimation(){
        ValueAnimator textAlpha;
        final int color = this.getCurrentTextColor();
        textAlpha = ValueAnimator.ofInt(0, 255).setDuration(mEffectDuration);
        textAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int alpha = (int) animation.getAnimatedValue();
                setTextColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
            }
        });

        drawSmallTriangle = false;
        drawBigTriangle = true;

        ValueAnimator b = ValueAnimator.ofFloat(0, 1f).setDuration(mEffectDuration);
        b.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                float newWidth, newHeight;
                newWidth = val * getWidth() +20;
                newHeight = val * getHeight() / 2;

                if (newWidth > 100.0f && newHeight > 100.0f){
                    triangleWidth = newWidth;
                    triangleHeight = newHeight;
                } else {
                    if (newWidth > 100.0f){
                        triangleWidth = newWidth;
                    } else {
                        triangleWidth = 100.0f;
                    }
                    if (newHeight > 100.0f){
                        triangleHeight = newHeight;
                    } else {
                        triangleHeight = 100.0f;
                    }

                }
                invalidate();
            }
        });
        b.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isEffectToggled){
                    isEffectToggled = false;
                } else {
                    isEffectToggled = true;
                }
                drawBigTriangle = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        return new ValueAnimator[]{textAlpha, b};
    }
    private ValueAnimator[] getRibbonAnimation(){
        int firstAnimDuration = (int) (mEffectDuration * 0.3f);
        ValueAnimator ribbon= ValueAnimator.ofFloat(0f, 1f).setDuration(mEffectDuration);

        ribbon.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animVal = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        Log.i(TAG, "getRibbonAnimation: height: " + getHeight());

        drawRibbon = true;
        ribbon.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setBackgroundColor(Color.TRANSPARENT);

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
        });
        return new ValueAnimator[]{ribbon};
    }

    private String rectToString(Rect r){
        return "bottom: " + r.bottom + " left: " + r.left + " right: " + r.right + " top: " + r.top;
    }

    private void setTextAlpha(int alpha){
        int color = this.getCurrentTextColor();
        setTextColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
    }
    private int getTextAlpha(){
        return 255;
    }

    public boolean isEffectToggled() {
        return isEffectToggled;
    }
}
