package com.kgkg.imagevieweffects;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Krzysiek on 2016-03-23.
 */
public class ImageViewEffects extends FrameLayout {

    public static String TAG = "kgkg";

    private ImageView ivImage;
    private TextView tvTitle;
    private LinearLayout titleBlock;
    private LinearLayout titleMask;
    private ViewGroup.LayoutParams titleMaskParams;
    private ViewGroup.LayoutParams titleBlockParams;

    private String mTitle;
    private int mImage;
    private int mEffect;
    private int mTitleHeight;
    private int mTitleBackColor;
    private int mTitleFontColor;
    private boolean isTitleMaskSet = false;
    private boolean isTitleBlockSet = false;
    private boolean isEffectToggled = false;
    private float mMaskOpacity;
    private int mMaskColor;
    private int mTitleBlockPosition;
    private int mEffectDuration;


    private IOnPlayEffect OnPlayEffectListener;

    public ImageViewEffects(Context context) {
        super(context);
        init();
    }

    public ImageViewEffects(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageViewEffects, 0, 0);
        try {
            mTitle = a.getString(R.styleable.ImageViewEffects_mTitle);
            mImage = a.getResourceId(R.styleable.ImageViewEffects_mImage, 0);
            mEffect = a.getInt(R.styleable.ImageViewEffects_mEffect, 0);
            mTitleHeight = a.getInteger(R.styleable.ImageViewEffects_mTitleHeight, 0);
            mTitleBackColor = a.getColor(R.styleable.ImageViewEffects_mTtitleBackColor,
                    getResources().getColor(android.R.color.black));
            mTitleFontColor = a.getColor(R.styleable.ImageViewEffects_mTtitleFontColor,
                    getResources().getColor(android.R.color.white));
            mMaskOpacity = a.getFloat(R.styleable.ImageViewEffects_mMaskOpacity, 0.5f);
            mMaskColor = a.getColor(R.styleable.ImageViewEffects_mMaskColor,
                    getResources().getColor(android.R.color.black));
            mTitleBlockPosition = a.getInt(R.styleable.ImageViewEffects_mTitleBlockPosition, 0);
            mEffectDuration = a.getInt(R.styleable.ImageViewEffects_mEffectDuration, 1000);
        } finally {
            a.recycle();
        }

        if (mImage != 0 && ivImage != null)
            ivImage.setImageResource(mImage);
        if (mTitle.length() > 0 && tvTitle != null)
        {
            tvTitle.setTextColor(mTitleFontColor);
            tvTitle.setText(mTitle);
        }
        if (titleBlock != null)
        {
            titleBlockParams = titleBlock.getLayoutParams();
            titleBlock.setBackgroundColor(mTitleBackColor);
        }
        if (titleMask != null){
            titleMaskParams = titleMask.getLayoutParams();
            titleMask.setBackgroundColor(mMaskColor);
            titleMask.setAlpha(mMaskOpacity);
        }



    }

    public ImageViewEffects(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.imageview_layout, this, true);

        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        ivImage = (ImageView) this.findViewById(R.id.ivImage);
        titleMask = (LinearLayout) this.findViewById(R.id.titleMask);
        titleBlock = (LinearLayout) this.findViewById(R.id.titleBlock);

    }

    private void l(String tekst){
        Log.i(TAG, tekst);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setOnShowEffectListener(IOnPlayEffect onPlayEffectListener) {
        OnPlayEffectListener = onPlayEffectListener;
    }

    public void playEffect(){
        playEffect(mEffect);
    }

    public void playEffect(int effect){

        if (OnPlayEffectListener != null){
            OnPlayEffectListener.startEffect();
        }
        if (mEffect != effect)
            mEffect = effect;

        switch (mEffect){
            case 0:
                Log.i(TAG, "playEffect: No effect specified");
                break;
            case 1:
                fade();
                break;
            case 2:
                slide();
                break;
            case 3:
                move();
                break;
            case 4:
                uncover();
                break;
            case 5:
                corner();
                break;
            case 6:
                ribbon();
                break;
            default:
                Log.i(TAG, "playEffect: no effect for int=" + effect);
                break;
        }

        if (isEffectToggled)
            isEffectToggled = false;
        else
            isEffectToggled = true;
    }

    private void fade(){
        Log.i(TAG, "fade: isEffectToggled: " + isEffectToggled);
        if (isEffectToggled)
            fadeOut();
        else
            fadeIn();
    }
    private void slide(){
        Log.i(TAG, "slide: ");
    }
    private void move(){
        Log.i(TAG, "move: ");
    }
    private void uncover(){
        Log.i(TAG, "uncover: ");
    }
    private void corner(){
        Log.i(TAG, "corner: ");
    }
    private void ribbon(){
        Log.i(TAG, "ribbon: ");
    }

    private void fadeIn(){
        titleBlock.setVisibility(View.VISIBLE);
        titleMask.setVisibility(View.VISIBLE);
    }
    private void fadeOut(){
        titleBlock.setVisibility(View.GONE);
        titleMask.setVisibility(View.GONE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isTitleBlockSet = false;
        isTitleMaskSet = false;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        ViewTreeObserver vto = ivImage.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isTitleMaskSet && ivImage.getHeight() > 0) {
                    titleMaskParams.height = ivImage.getHeight();
                    titleMaskParams.width = ivImage.getWidth();
                    titleMask.setLayoutParams(titleMaskParams);
                    isTitleMaskSet = true;
                }
                if (!isTitleBlockSet && ivImage.getHeight() > 0) {
                    // FIXME: 2016-03-25 Else is never triggerd, change condition
                    if (tvTitle.getHeight() < mTitleHeight) {
                        titleBlockParams = titleBlock.getLayoutParams();
                        titleBlockParams.height = mTitleHeight;
                        titleBlockParams.width = ivImage.getWidth();
                        titleBlock.setLayoutParams(titleBlockParams);
                    } else {
                        mTitleHeight = tvTitle.getHeight();
                    }
                    Log.i(TAG, "onGlobalLayout: titleBlockParams height: " + titleBlockParams.height);
                    Log.i(TAG, "onGlobalLayout: min titleBlock height: " + titleBlock);
                    int top;
                    switch (mTitleBlockPosition) {
                        case 0:
                            break;
                        case 1:
                            top = ivImage.getHeight() / 2 - mTitleHeight / 2;
                            titleBlock.setY(top);
                            break;
                        case 2:
                            top = ivImage.getHeight() - mTitleHeight;
                            titleBlock.setY(top);
                            break;
                        default:
                            break;
                    }
                    isTitleBlockSet = true;
                }
            }
        });
    }
}
