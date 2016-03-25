package com.kgkg.imagevieweffects;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Krzysiek on 2016-03-23.
 */
public class ImageViewEffects extends FrameLayout {

/*    <enum name="none" value="0" />
    <enum name="fade" value="1" />
    <enum name="slide" value="2" />
    <enum name="move" value="3" />
    <enum name="uncover" value="4" />
    <enum name="corner" value="5" />
    <enum name="ribbon" value="6" />*/

    public static String TAG = "kgkg";

    private ImageView ivImage;
    private TextView tvTitle;

    private String mTitle;
    private int mImage;
    private int mEffect;
    private IOnShowEffect OnShowEffectListener;

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
        } finally {
            a.recycle();
        }

        if (mImage != 0 && ivImage != null)
            ivImage.setImageResource(mImage);
        if (mTitle.length() > 0 && tvTitle != null)
            tvTitle.setText(mTitle);
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
    }

    private void log(String tekst){
        Log.i(TAG, tekst);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        log("onDraw");
    }

    public void onClickCall(){
        log("wybrano efekt nr: " + mEffect);
    }

    @Override
    public boolean performClick() {
        if (super.performClick()){
            log("listener assigned");
        } else {
            log("no listener");
        }

        return super.performClick();
    }

    public void setOnShowEffectListener(IOnShowEffect onShowEffectListener) {
        OnShowEffectListener = onShowEffectListener;
    }
}
