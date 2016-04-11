package com.kgkg.imagevieweffects;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Krzysiek on 2016-03-30.
 */
public class ImageFrame extends FrameLayout implements View.OnClickListener{
    private final String TAG="kgkg";
    private ImageTitle mImageTitle;
    private ImageMask mImageMask;
    private ImageView mImage;
    private View mView;

    private int mImageRes;
    private int mTitleBlockPosition;
    private boolean mReverseOnSecondClick;
    private IOnPlayEffect EffectListener;
    private long startMils = 0;

    public ImageFrame(Context context) {
        super(context);
        init();
    }

    public ImageFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageFrame, 0, 0);
        try {
            mImageRes = a.getResourceId(R.styleable.ImageFrame_mImage, 0);
            mTitleBlockPosition = a.getInt(R.styleable.ImageFrame_mTitleBlockPosition, 0);
            mReverseOnSecondClick = a.getBoolean(R.styleable.ImageFrame_mReverseOnSecondClick, true);
        } finally {
            a.recycle();
        }

        if (mImage != null){
            mImage.setImageResource(mImageRes);
        }
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.image_frame_layout, this, true);
        mImage = (ImageView) mView.findViewById(R.id.mImage);
        this.setOnClickListener(this);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof ImageTitle){
            mImageTitle = (ImageTitle) child;
            if (mImageTitle.getEffect() == 5){

                mImageTitle.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

                if (mTitleBlockPosition == 1 || mTitleBlockPosition == 0){
                    if (mImageTitle.getEffectDirection() == 1){
                        //top right
                        mImageTitle.setGravity(Gravity.RIGHT | Gravity.TOP);
                    } else {
                        //top left
                        mImageTitle.setGravity(Gravity.LEFT | Gravity.TOP);
                    }
                } else {
                    if (mImageTitle.getEffectDirection() == 1){
                        //bottom right
                        mImageTitle.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                    } else {
                        //bottom left
                        mImageTitle.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                    }
                }
            }
            mImageTitle.requestLayout();
        }
        if (child instanceof  ImageMask){
            mImageMask = (ImageMask) child;
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mImage.getHeight() > 0 && mTitleBlockPosition > 0){
            if (mImageTitle.getEffect() == 5){

            } else {
                if (mTitleBlockPosition == 1){
                    mImageTitle.setY((mImage.getHeight() - mImageTitle.getHeight()) / 2);
                    mImageTitle.requestLayout();
                } else if (mTitleBlockPosition == 2){
                    mImageTitle.setY(mImage.getHeight() - mImageTitle.getHeight());
                    mImageTitle.requestLayout();
                }
            }
        }

    }

    public void toggleMaskAndTitle(){
        toggleTitle();
        toggleMask();
    }

    private void toggleTitle(){
        if (mImageTitle.getVisibility() == View.INVISIBLE){
            mImageTitle.setVisibility(View.VISIBLE);
        } else {
            mImageTitle.setVisibility(View.INVISIBLE);
        }
    }

    private void toggleMask(){
        if (mImageMask.getVisibility() == View.INVISIBLE){
            mImageMask.setVisibility(View.VISIBLE);
        } else {
            mImageMask.setVisibility(View.INVISIBLE);
        }
    }

    public void showEffects(){
        showTitleBlockEffect();
        showMaskEffect();
    }

    public void showTitleBlockEffect(){
        if (mImageTitle != null){
            showTitleBlockEffect(mImageTitle.getEffect());
        }
    }

    public void showTitleBlockEffect(int effect){
        if (mImageTitle != null){
            if (effect == 3 || effect == 4){
                float ratio;
                if (mTitleBlockPosition ==2){
                    ratio = -(float)mImageTitle.getHeight() / mImage.getHeight();
                } else {
                    ratio = (float)mImageTitle.getHeight() / mImage.getHeight();
                }
                if (effect == 3){
                    ratio = ratio /2;
                }
                Animation mAnimation = new TranslateAnimation(
                        TranslateAnimation.ABSOLUTE, 0f,
                        TranslateAnimation.ABSOLUTE, 0f,
                        TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                        TranslateAnimation.RELATIVE_TO_PARENT, ratio);
                mAnimation.setDuration(mImageTitle.getEffectDuration());
                mAnimation.setFillAfter(true);
                if (mImageTitle.isEffectToggled()){
                    mAnimation.setInterpolator(new ReverseInterpolator());
                } else {
                    mAnimation.setInterpolator(new LinearInterpolator());
                }
                mImage.startAnimation(mAnimation);
            }

            AnimatorSet anim = new AnimatorSet();
            ValueAnimator[] anims = mImageTitle.getEffectAnimation(effect);
            anim.setDuration(mImageTitle.getEffectDuration());
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                    if (EffectListener != null){
                        EffectListener.startEffect();
                    }
                    startMils = System.currentTimeMillis();
                    //Log.i(TAG, "onAnimationStart: startMils: " + startMils);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (mImageTitle.isEffectToggled()){
                        mImageTitle.setEffectToggled(false);
                    } else {
                        mImageTitle.setEffectToggled(true);
                    }
                    if (EffectListener != null){
                        EffectListener.endEffect();
                    }
                    //Log.i(TAG, "onAnimationEnd: " + (System.currentTimeMillis() - startMils));
                }
                @Override
                public void onAnimationCancel(Animator animator) {}
                @Override
                public void onAnimationRepeat(Animator animator) {}
            });

            if (mImageTitle.isEffectToggled()){
                for (ValueAnimator a: anims){
                    a.setInterpolator(new ReverseInterpolator());
                }
                if (mReverseOnSecondClick){
                    anim.playTogether(anims);
                    anim.start();
                } else {
                    mImageTitle.setEffectToggled(false);
                    mImageTitle.setVisibility(View.INVISIBLE);
                }
            } else {
                for (ValueAnimator a: anims){
                    a.setInterpolator(new LinearInterpolator());
                }
                if (mImageTitle.getVisibility() == View.INVISIBLE)
                    mImageTitle.setVisibility(View.VISIBLE);
                anim.playTogether(anims);
                anim.start();
            }
        }
    }

    public void showMaskEffect(){
        if (mImageMask != null){
            showMaskEffect(mImageMask.getEffect());
        }
    }

    public void showMaskEffect(int effect){
        if (mImageMask != null){
            ValueAnimator anim = mImageMask.getEffectAnimation();
            if (anim != null){
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (mImageMask.isEffectToggled()){
                            mImageMask.setEffectToggled(false);
                        } else {
                            mImageMask.setEffectToggled(true);
                        }
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {}
                    @Override
                    public void onAnimationRepeat(Animator animator) {}
                });

                if (mImageMask.isEffectToggled()){
                    anim.setInterpolator(new ReverseInterpolator());
                    if (mReverseOnSecondClick){
                        anim.start();
                    } else {
                        mImageMask.setEffectToggled(false);
                        mImageMask.setVisibility(View.INVISIBLE);
                    }
                } else {
                    anim.setInterpolator(new LinearInterpolator());
                    if (mImageMask.getVisibility() == View.INVISIBLE)
                        mImageMask.setVisibility(View.VISIBLE);
                    anim.start();
                }


            } else {
                Log.i(TAG, "showMaskEffect: mask animation is null");
            }
        }
    }

    protected int getTitleBlockPosition() {
        return mTitleBlockPosition;
    }

    protected final ImageView getImage() {
        return mImage;
    }



    @Override
    public String toString() {
        return "ImageFrame{" +
                "mImageTitle=" + mImageTitle +
                ", mImageMask=" + mImageMask +
                ", mImage=" + mImage +
                ", mView=" + mView +
                ", mImageRes=" + mImageRes +
                ", mTitleBlockPosition=" + mTitleBlockPosition +
                ", mReverseOnSecondClick=" + mReverseOnSecondClick +
                '}';
    }

    public void setEffectListener(IOnPlayEffect effectListener) {
        EffectListener = effectListener;
    }


    @Override
    public void onClick(View view) {
        showTitleBlockEffect();
        //showMaskEffect();
    }

    private class  ReverseInterpolator extends LinearInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float input) {
            return Math.abs(input - 1f);
        }
    }
}


