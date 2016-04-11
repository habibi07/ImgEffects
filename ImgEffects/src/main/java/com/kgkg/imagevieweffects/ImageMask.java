package com.kgkg.imagevieweffects;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
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

    public ImageMask(Context context) {
        super(context);
    }

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

    public int getEffect() {
        return mEffect;
    }

    public void setEffect(int mEffect) {
        this.mEffect = mEffect;
    }

    public int getEffectDuration() {
        return mEffectDuration;
    }

    public void setEffectDuration(int mEffectDuration) {
        this.mEffectDuration = mEffectDuration;
    }

    public int getMaskColor() {
        return mMaskColor;
    }

    public void setMaskColor(int mMaskColor) {
        this.mMaskColor = mMaskColor;
    }

    public float getMaskOpacity() {
        return mMaskOpacity;
    }

    public void setMaskOpacity(float mMaskOpacity) {
        this.mMaskOpacity = mMaskOpacity;
    }

    public boolean isEffectToggled() {
        return isEffectToggled;
    }

    public void setEffectToggled(boolean effectToggled) {
        isEffectToggled = effectToggled;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public String toString() {
        return "ImageMask{" +
                "mEffect=" + mEffect +
                ", mEffectDuration=" + mEffectDuration +
                ", mMaskColor=" + mMaskColor +
                ", mMaskOpacity=" + mMaskOpacity +
                '}';
    }

    protected ValueAnimator getEffectAnimation(){
        return getEffectAnimation(mEffect);
    }

    protected ValueAnimator getEffectAnimation(int effect){
        parentHeight = ((ImageFrame) this.getParent()).getHeight();
        parentWidth = ((ImageFrame) this.getParent()).getWidth();
        setVisibility(View.VISIBLE);
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
                return getFadeAnimation();
            default:
                return null;
        }
        return null;
    }

    private ValueAnimator getSlideTopAnimation(){
        ObjectAnimator topSlide = ObjectAnimator.ofFloat(this, "y", - this.getHeight(), this.getY()).setDuration(mEffectDuration);
        return topSlide;
    }
    private ValueAnimator getSlideBottomAnimation(){
        return ObjectAnimator.ofFloat(this, "y", parentHeight, this.getY()).setDuration(mEffectDuration);
    }
    private ValueAnimator getSlideLeftAnimation(){
        return ObjectAnimator.ofFloat(this, "x", -parentWidth, 0).setDuration(mEffectDuration);
    }
    private ValueAnimator getSlideRightAnimation(){
        return ObjectAnimator.ofFloat(this, "x", parentWidth, 0).setDuration(mEffectDuration);
    }
    private ValueAnimator getFadeAnimation(){
        return null;
    }
}
