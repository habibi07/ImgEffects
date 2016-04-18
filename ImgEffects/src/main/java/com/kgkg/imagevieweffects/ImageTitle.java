/*
 * Copyright (C) 2016 Krzysztof Gregorowicz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
    private Paint paint;
    private float animVal = 0;
    private float ribDx;
    private float ribDy;
    private final float factor = 0.2f;
    protected FrameLayout.LayoutParams p;

    /**
     * Constructor
     * @param context
     */
    public ImageTitle(Context context) {
        super(context);
    }

    /**
     * constructor
     * @param context
     * @param attrs
     */
    public ImageTitle(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageTitle, 0, 0);
        try {
            mEffect = a.getInt(R.styleable.ImageTitle_TitleEffect, 0);
            mEffectDuration = a.getInt(R.styleable.ImageTitle_mEffectDuration, 0);
            mEffectDirection = a.getInt(R.styleable.ImageTitle_mEffectDirection, 2);
            ribDx = a.getFloat(R.styleable.ImageTitle_mCornerDx, -30f);
            ribDy = a.getFloat(R.styleable.ImageTitle_mCornerDy, 30f);
            triangleWidth = a.getFloat(R.styleable.ImageTitle_mCornerDx, 100f);
            triangleHeight = a.getFloat(R.styleable.ImageTitle_mCornerDy, 100f);
        } finally {
            a.recycle();
        }
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        bgColor = ((ColorDrawable) getBackground()).getColor();
        paint.setColor(bgColor);

        if (mEffect != 5){
            setVisibility(View.INVISIBLE);
        } else {
            drawSmallTriangle = true;
            setVisibility(View.VISIBLE);
            setBackgroundColor(Color.TRANSPARENT);
            setDrawingCacheEnabled(true);
            setDrawingCacheBackgroundColor(Color.TRANSPARENT);
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

                float znak = ribDy / Math.abs(ribDy);
                canvas.drawText(text,
                        (rect.centerX() - (textPaint.getTextSize() * getResources().getDisplayMetrics().density) / 2) + ribDx * f2,
                        rect.centerY() - ((znak * ribDy) - textBounds.height()) / 2,
                        textPaint);
            }
        } else {
            super.onDraw(canvas);
        }

    }

    /**
     * getter for effect property
     * @return int mEffect
     */
    public int getEffect() {
        return mEffect;
    }

    /**
     * setter for effect property
     * @param mEffect - int value <0; 6>
     */
    public void setEffect(int mEffect) {
        setVisibility(View.INVISIBLE);
        setTextScaleX(1.0f);
        setAlpha(1f);
        setX(0.0f);
        setTextAlpha(255);
        Rect r = new Rect();
        getLocalVisibleRect(r);
        int pos = ((ImageFrame) getParent()).getTitleBlockPosition();
        if (pos == 1){
            setY((((ImageFrame) getParent()).getImage().getHeight() - r.bottom) / 2);
        } else if (pos == 2){
            float y = ((ImageFrame) getParent()).getImage().getHeight() - r.bottom;
            setY(y);
        }

        if (mEffect == 6){
            drawSmallTriangle = false;
            drawBigTriangle = false;
            drawRibbon = true;
            resetLayout();
        } else if (mEffect == 5) {
            drawSmallTriangle = true;
            drawBigTriangle = false;
            drawRibbon = false;

            setBackgroundColor(Color.TRANSPARENT);
            setCornerLayout();
            requestLayout();
            setVisibility(View.VISIBLE);
        } else {
            drawSmallTriangle = false;
            drawBigTriangle = false;
            drawRibbon = false;
            setGravity(Gravity.CENTER_HORIZONTAL);
            setBackgroundColor(bgColor);
            resetLayout();
        }
        getLocalVisibleRect(r);
        //Log.i(TAG, "setEffect: " + rectToString(r));
        this.mEffect = mEffect;
    }

    /**
     * getter for effect duration
     * @return - int effect duration (i know, should be long type)
     */
    public int getEffectDirection() {
        return mEffectDirection;
    }

    /**
     * setter for effect duration
     * @param mEffectDirection  - int effect duration (i know, should be long type)
     */
    public void setEffectDirection(int mEffectDirection) {
        this.mEffectDirection = mEffectDirection;
    }

    /**
     * getter for effect duration
     * @return - int effect duration
     */
    public int getEffectDuration() {
        return mEffectDuration;
    }

    /**
     * Setter for effect duration
     * @param mEffectDuration - int effect duration
     */
    public void setEffectDuration(int mEffectDuration) {
        this.mEffectDuration = mEffectDuration;
    }

    /**
     * method returning animation for current effect property
     * @return ValueAnimator[] - ValueAnimators composed for current effect property
     */
    protected ValueAnimator[] getEffectAnimation(){
        return getEffectAnimation(mEffect);
    }

    /**
     * getter for small triangle width in corner animation
     * @return - float - small triangle width
     */
    public float getTriangleWidth() {
        return triangleWidth;
    }

    /**
     * setter for small triangle width in corner animation
     * @param triangleWidth - float - small triangle width
     */
    public void setTriangleWidth(float triangleWidth) {
        this.triangleWidth = triangleWidth;
    }
    /**
     * getter for small triangle height in corner animation
     * @return - float - small triangle height
     */
    public float getTriangleHeight() {
        return triangleHeight;
    }
    /**
     * setter for small triangle height in corner animation
     * @param triangleHeight - float - small triangle height
     */
    public void setTriangleHeight(float triangleHeight) {
        this.triangleHeight = triangleHeight;
    }

    /**
     * getter for ribbon dx
     * @return - float - ribbon dx
     */
    public float getRibDx() {
        return ribDx;
    }

    /**
     * setter for ribbon dx
     * @param ribDx - float
     */
    public void setRibDx(float ribDx) {
        this.ribDx = ribDx;
    }
    /**
     * getter for ribbon dy
     * @return - float - ribbon dy
     */
    public float getRibDy() {
        return ribDy;
    }
    /**
     * setter for ribbon dy
     * @param ribDy - float
     */
    public void setRibDy(float ribDy) {
        this.ribDy = ribDy;
    }

    /**
     * method returning animation depending on effect property
     * @param effect - int value
     * @return ValueAnimator[] - ValueAnimators composed for specified effect
     */
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

    /**
     * private method returning fade animation
     * @return ValueAnimator[] - ValueAnimators composed for fade animation
     */
    private ValueAnimator[] getFadeAnimation(){
        setVisibility(View.VISIBLE);
        ObjectAnimator fade = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).setDuration(mEffectDuration);
        return new ValueAnimator[]{fade};
    }

    /**
     * private method returning slide animation
     * @param direction - slide animation direction
     * @return ValueAnimator[] - ValueAnimators composed for slide animation
     */
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

    /**
     * private method returning slide animation
     * @return ValueAnimator[] - ValueAnimators composed for slide animation
     */
    private ValueAnimator[] getSlideAnimation(){
        return getSlideAnimation(mEffectDirection);
    }
    /**
     * private method returning move animation
     * @return ValueAnimator[] - ValueAnimators composed for move animation
     */
    private ValueAnimator[] getMoveAnimation(){
        ObjectAnimator move;
        int pos = ((ImageFrame) this.getParent()).getTitleBlockPosition();
        int parentHeight = ((ImageFrame) this.getParent()).getHeight();
        Rect r = new Rect();
        getLocalVisibleRect(r);

        if ( pos == 1 || pos == 0){
            move = ObjectAnimator.ofFloat(this, "y", - this.getHeight(), 0).setDuration(mEffectDuration);
        } else {
            move = ObjectAnimator.ofFloat(this, "y", parentHeight, parentHeight - r.bottom).setDuration(mEffectDuration);
        }
        move.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Log.i(TAG, "onAnimationUpdate: x: " + getX() + " y: " + getY());
            }
        });

        return new ValueAnimator[]{move};
    }
    /**
     * private method returning uncover animation
     * @return ValueAnimator[] - ValueAnimators composed for ucover animation
     */
    private ValueAnimator[] getUncoverAnimation(){
        return getMoveAnimation();
    }
    /**
     * private method returning corner animation
     * @return ValueAnimator[] - ValueAnimators composed for corner animation
     */
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

        ValueAnimator corner = ValueAnimator.ofFloat(0, 1f).setDuration(mEffectDuration);
        corner.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
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
        corner.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {

            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });


        return new ValueAnimator[]{textAlpha, corner};
    }

    /**
     * private method returning ribbon animation
     * @return ValueAnimator[] - ValueAnimators composed for ribbon animation
     */
    private ValueAnimator[] getRibbonAnimation(){
/*        FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) getLayoutParams();
        if (p.height == FrameLayout.LayoutParams.MATCH_PARENT){
            p.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            setLayoutParams(p);
        }*/


        ValueAnimator ribbon= ValueAnimator.ofFloat(0f, 1f).setDuration(mEffectDuration);
        ribbon.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animVal = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        drawRibbon = true;
        ribbon.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setBackgroundColor(Color.TRANSPARENT);
                //Log.i(TAG, "onAnimationStart: pos: " + ((ImageFrame) getParent()).getTitleBlockPosition());
            }
            @Override
            public void onAnimationEnd(Animator animation) {

            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        return new ValueAnimator[]{ribbon};
    }
    private String rectToString(Rect r){
        return "bottom: " + r.bottom + " left: " + r.left + " right: " + r.right + " top: " + r.top;
    }
    /*  I might need those methods in future


*/
    private void setTextAlpha(int alpha){
        int color = this.getCurrentTextColor();
        setTextColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
    }
    /**
     *
     * Getter for isEffectToggled property
     * @return
     */
    public boolean isEffectToggled() {
        return isEffectToggled;
    }

    /**
     * Setter for isEffect toggled
     * @param val - boolean value determinating whether effect is toggled or not
     */
    protected void setEffectToggled(boolean val){
        this.isEffectToggled = val;
    }

    private void setCornerLayout(){
        getLayoutParams().height = FrameLayout.LayoutParams.MATCH_PARENT;
        setLayoutParams(getLayoutParams());
        requestLayout();
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                setY(0f);
                if (getEffectDirection() == 1){
                    setGravity(Gravity.RIGHT);
                } else {
                    setGravity(Gravity.LEFT);
                }
                requestLayout();
            }
        };
        handler.post(r2);
    }

    private void resetLayout(){
        //Log.i(TAG, "resetLayout: reset layout");
        if (getLayoutParams().height == FrameLayout.LayoutParams.MATCH_PARENT){
           // Log.i(TAG, "resetLayout: height matching parrent");
            setVisibility(View.INVISIBLE);

            Handler handler = new Handler(Looper.getMainLooper());
            getLayoutParams().height = FrameLayout.LayoutParams.WRAP_CONTENT;
            setLayoutParams(getLayoutParams());

            requestLayout();

            Runnable r2 = new Runnable() {
                @Override
                public void run() {
                    int pos = ((ImageFrame) getParent()).getTitleBlockPosition();
                    Rect r = new Rect();
                    getLocalVisibleRect(r);
                    Log.i(TAG, "run: r2 " + rectToString(r));
                    if (pos == 1){
                        setY((((ImageFrame) getParent()).getImage().getHeight() - r.bottom) / 2);
                    } else if (pos == 2){
                        float y = ((ImageFrame) getParent()).getImage().getHeight() - r.bottom;
                        setY(y);
                    }
                    requestLayout();

                }
            };

            handler.post(r2);
            //handler.postAtFrontOfQueue(r1);
        }
    }

    @Override
    public String toString() {
        return "ImageTitle{" +
                "mEffect=" + mEffect +
                ", mEffectDuration=" + mEffectDuration +
                ", mEffectDirection=" + mEffectDirection +
                ", drawSmallTriangle=" + drawSmallTriangle +
                ", drawBigTriangle=" + drawBigTriangle +
                ", drawRibbon=" + drawRibbon +
                ", triangleWidth=" + triangleWidth +
                ", triangleHeight=" + triangleHeight +
                ", bgColor=" + bgColor +
                ", isEffectToggled=" + isEffectToggled +
                ", paint=" + paint +
                ", animVal=" + animVal +
                ", ribDx=" + ribDx +
                ", ribDy=" + ribDy +
                ", factor=" + factor +
                ", height=" + getHeight() +
                '}';
    }
}
