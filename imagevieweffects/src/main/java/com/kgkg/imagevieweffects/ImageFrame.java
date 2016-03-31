package com.kgkg.imagevieweffects;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Krzysiek on 2016-03-30.
 */
public class ImageFrame extends FrameLayout {
    private final String TAG="kgkg";
    private ImageTitle mImageTitle;
    private ImageMask mImageMask;
    private ImageView mImage;
    private View mView;

    private int mImageRes;
    private int mTitleBlockPosition;
    private boolean mReverseOnSecondClick;

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
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof ImageTitle){
            mImageTitle = (ImageTitle) child;
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
            if (mTitleBlockPosition == 1){
                mImageTitle.setY((mImage.getHeight() - mImageTitle.getHeight()) / 2);
                mImageTitle.requestLayout();
            } else if (mTitleBlockPosition == 2){
                mImageTitle.setY(mImage.getHeight() - mImageTitle.getHeight());
                mImageTitle.requestLayout();
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
        if (mImageTitle != null && mImageMask != null){

        } else if (mImageTitle != null){

        } else if (mImageMask != null){

        }
    }

    public void showTitleBlockEffect(){
        if (mImageTitle != null){
            showTitleBlockEffect(mImageTitle.getEffect());
        }
    }

    public void showTitleBlockEffect(int effect){
        if (mImageTitle != null){
            //Log.i(TAG, "showTitleBlockEffect: effefct: " + effect);
            toggleTitle();
            float ratio = mImageTitle.getHeight() / mImage.getHeight();
            Animation mAnimation = new TranslateAnimation(
                    TranslateAnimation.ABSOLUTE, 0f,
                    TranslateAnimation.ABSOLUTE, 0f,
                    TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                    TranslateAnimation.RELATIVE_TO_PARENT, -ratio);
            mAnimation.setDuration(1000);
            mAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            mAnimation.setFillAfter(true);
            mImage.startAnimation(mAnimation);

            AnimatorSet anim = new AnimatorSet();
            anim.playTogether(getTitleBlockAnimation(effect));
            anim.start();
        }
    }

    public void showMaskEffect(){
        if (mImageMask != null){
            showMaskEffect(mImageMask.getEffect());
        }
    }

    public void showMaskEffect(int effect){
        if (mImageMask != null){
            showMaskAnimation(effect).start();
        }
    }

    private ValueAnimator getTitleBlockAnimation(){
        return new ValueAnimator();
    }
    private ValueAnimator[] getTitleBlockAnimation(int effect){

        return mImageTitle.getEffectAnimation(effect);
    }
    private ValueAnimator showMaskAnimation(){
        return new ValueAnimator();
    }
    private ValueAnimator showMaskAnimation(int effect){
        return new ValueAnimator();
    }

    private void logViewsSizes(){
        if (mImage != null){
            Log.i(TAG, "logViewsSizes: Image: (" + mImage.getWidth() + " , " + mImage.getHeight() + ")");
        } else {
            Log.i(TAG, "logViewsSizes: Image is null");
        }
        if (mImageTitle != null){
            Log.i(TAG, "logViewsSizes: mImageTitle: (" + mImageTitle.getWidth() + " , " + mImageTitle.getHeight() + ")");
        } else {
            Log.i(TAG, "logViewsSizes: mImageTitle is null");
        }
        if (mImageMask != null){
            Log.i(TAG, "logViewsSizes: mImageMask: (" + mImageMask.getWidth() + " , " + mImageMask.getHeight() + ")");
        } else {
            Log.i(TAG, "logViewsSizes: mImageMask is null");
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
}
