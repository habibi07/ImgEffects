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

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Krzysiek on 2016-03-30.
 */

public class ImageMask extends FrameLayout {
    private final String TAG="kgkg";
    private int mEffect;
    private int mEffectDuration;
    private int mMaskColor;
    private float mMaskOpacity;
    private int parentHeight;
    private int parentWidth;
    private boolean isEffectToggled = false;
    private boolean onSlideIn = false;
    private float animVal = 0f;

    /**
     * constructor
     * @param context
     */
    public ImageMask(Context context) {
        super(context);
    }

    /**
     * constructor
     * @param context
     * @param attrs
     */
    public ImageMask(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageMask, 0, 0);
        try {
            mEffect = a.getInt(R.styleable.ImageMask_MaskEffect, 0);
            mEffectDuration = a.getInt(R.styleable.ImageMask_mEffectDuration, 0);
            mMaskColor = a.getColor(R.styleable.ImageMask_mMaskColor, context.getResources().getColor(android.R.color.black));
            mMaskOpacity = a.getFloat(R.styleable.ImageMask_mMaskOpacity, 1);
        } finally {
            a.recycle();
        }


        setBackgroundColor(mMaskColor);
        setAlpha(mMaskOpacity);
        setVisibility(View.INVISIBLE);
    }

    /**
     * getter for effect
     * @return - int <0 ; 6>
     */
    public int getEffect() {
        return mEffect;
    }

    /**
     * setter for effect
     * @param mEffect - int <0 ; 6>
     */
    public void setEffect(int mEffect) {
        setBackgroundColor(mMaskColor);
        setAlpha(mMaskOpacity);
        setX(0f);
        setY(0f);
        if (mEffect != 5){
            onSlideIn = false;
        }
        if (isEffectToggled()){
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.INVISIBLE);
        }
        this.mEffect = mEffect;
    }

    /**
     * getter for effect duration
     * @return - int - duration
     */
    public int getEffectDuration() {
        return mEffectDuration;
    }

    /**
     * setter for effect duration
     * @param mEffectDuration - int - effect duration
     */
    public void setEffectDuration(int mEffectDuration) {
        this.mEffectDuration = mEffectDuration;
    }

    /**
     * getter for mask color
     * @return - int - mask color
     */
    public int getMaskColor() {
        return mMaskColor;
    }

    /**
     * setter for mask color
     * @param mMaskColor - int - mask color
     */
    public void setMaskColor(int mMaskColor) {
        this.mMaskColor = mMaskColor;
    }

    /**
     * getter for mask opacity
     * @return - float - mask opacity
     */
    public float getMaskOpacity() {
        return mMaskOpacity;
    }

    /**
     * setter for mask opacity
     * @param mMaskOpacity - float - mask opacity
     */
    public void setMaskOpacity(float mMaskOpacity) {
        this.mMaskOpacity = mMaskOpacity;
    }

    /**
     * getter for isEffectToggled property
     * @return - boolean
     */
    protected boolean isEffectToggled() {
        return isEffectToggled;
    }

    /**
     * setter for isEffectToggled property
     * @param effectToggled - boolean
     */
    protected void setEffectToggled(boolean effectToggled) {
        isEffectToggled = effectToggled;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    /**
     * getter for current effect animation
     * @return - ValueAnimator - animator
     */
    protected ValueAnimator getEffectAnimation(){
        return getEffectAnimation(this.mEffect);
    }

    /**
     * getter for specified effect argument animation
     * @param effect - int - effect
     * @return - ValueAnimator - animator
     */
    protected ValueAnimator getEffectAnimation(int effect){
        Log.i(TAG, "getEffectAnimation: effect: " + effect);
        parentHeight = ((ImageFrame) this.getParent()).getHeight();
        parentWidth = ((ImageFrame) this.getParent()).getWidth();
        setX(0f);
        setY(0f);
        switch (effect){
            case 0:
                break;
            case 1:
                return getSlideTopAnimation();
            case 2:
                return getSlideBottomAnimation();
            case 3:
                return getSlideLeftAnimation();
            case 4:
                return getSlideRightAnimation();
            case 5:
                return getSlideInAnimation();
            case 6:
                return getFadeAnimation();
            default:
                return null;
        }
        return null;
    }

    /**
     * getter for slide top animation
     * @return - ValueAnimator - top animator
     */
    private ValueAnimator getSlideTopAnimation(){
        ObjectAnimator topSlide = ObjectAnimator.ofFloat(this, "y", - this.getHeight(), this.getY()).setDuration(mEffectDuration);
        return topSlide;
    }
    /**
     * getter for slide bottom animation
     * @return - ValueAnimator - bottom animator
     */
    private ValueAnimator getSlideBottomAnimation(){
        return ObjectAnimator.ofFloat(this, "y", parentHeight, this.getY()).setDuration(mEffectDuration);
    }
    /**
     * getter for slide left animation
     * @return - ValueAnimator - slide animator
     */
    private ValueAnimator getSlideLeftAnimation(){
        return ObjectAnimator.ofFloat(this, "x", -parentWidth, 0).setDuration(mEffectDuration);
    }
    /**
     * getter for slide left animation
     * @return - ValueAnimator - slide animator
     */
    private ValueAnimator getSlideRightAnimation(){
        return ObjectAnimator.ofFloat(this, "x", parentWidth, 0).setDuration(mEffectDuration);
    }
    /**
     * getter for fade animation
     * @return - ValueAnimator - fade animator
     */
    private ValueAnimator getFadeAnimation(){
        ObjectAnimator fade = ObjectAnimator.ofFloat(this, "alpha", 0f, mMaskOpacity).setDuration(mEffectDuration);
        return fade;
    }

    /**
     * getter for slide In animation (i'll rework it soon)
     * @return - ValueAnimator - slide In animator
     */
    private ValueAnimator getSlideInAnimation(){
        //// TODO: 4/12/16 add slide to right side
        ValueAnimator slideIn = ValueAnimator.ofFloat(0f, 1f).setDuration(mEffectDuration);
        setBackgroundColor(Color.TRANSPARENT);
        slideIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animVal = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        onSlideIn = true;
        return slideIn;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (onSlideIn){
            Paint p = new Paint();
            p.setColor(mMaskColor);

            Rect r1 = new Rect(0, 0, (int) (parentWidth * (1 - animVal)), (int) (parentHeight * animVal / 2));
            Rect r2 = new Rect(r1.right, 0, parentWidth, parentHeight);
            Rect r3 = new Rect(0,
                    (int) (parentHeight - (parentHeight * animVal / 2)),
                    r1.right,
                    parentHeight);

            canvas.drawRect(r1, p);
            canvas.drawRect(r2, p);
            canvas.drawRect(r3, p);
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    public String toString() {
        return "ImageMask{" +
                "mEffect=" + mEffect +
                ", mEffectDuration=" + mEffectDuration +
                ", mMaskColor=" + mMaskColor +
                ", mMaskOpacity=" + mMaskOpacity +
                ", parentHeight=" + parentHeight +
                ", parentWidth=" + parentWidth +
                ", isEffectToggled=" + isEffectToggled +
                ", onSlideIn=" + onSlideIn +
                ", animVal=" + animVal +
                '}';
    }
}
