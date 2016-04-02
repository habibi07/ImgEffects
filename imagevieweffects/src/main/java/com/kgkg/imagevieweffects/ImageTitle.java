package com.kgkg.imagevieweffects;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

/**
 * Created by Krzysiek on 2016-03-30.
 */
public class ImageTitle extends TextView {
    private final String TAG="kgkg";
    private int mEffect;
    private int mEffectDuration;
    private int mEffectDirection;

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
        setVisibility(View.INVISIBLE);
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


    /*
    0 - left
    1 - right
    2 - top
    3 - bottom
     */
    private ValueAnimator[] getMoveAnimation(){
        ObjectAnimator b = (ObjectAnimator) getSlideAnimation()[0];
        final ImageFrame parent = (ImageFrame) this.getParent();
        ObjectAnimator a;

        if (parent.getTitleBlockPosition() == 0){
            a = ObjectAnimator.ofFloat(parent.getImage(), "y", 0, this.getHeight() /2 ).setDuration(mEffectDuration);
            return new ValueAnimator[]{b, a};
        } else if (parent.getTitleBlockPosition() == 2){
            return new ValueAnimator[]{b};
        } else {
            a = ObjectAnimator.ofFloat(parent.getImage(), "y", 0, this.getHeight() / 2).setDuration(mEffectDuration);
            return new ValueAnimator[]{b, a};
        }
        // TODO: 2016-03-31 Adjust animation properties depending on title block position and animation direction

    }
    private ValueAnimator[] getUncoverAnimation(){
        ValueAnimator[] a = new ValueAnimator[2];
        return a;
    }
    private ValueAnimator[] getCornerAnimation(){
        ValueAnimator[] a = new ValueAnimator[2];
        return a;
    }
    private ValueAnimator[] getRibbonAnimation(){
        ValueAnimator[] a = new ValueAnimator[2];
        return a;
    }

    private String rectToString(Rect r){
        return "bottom: " + r.bottom + " left: " + r.left + " right: " + r.right + " top: " + r.top;
    }

}
