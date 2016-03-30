package com.kgkg.imagevieweffects;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Krzysiek on 2016-03-30.
 */
public class ImageFrame extends FrameLayout {
    private ImageTitle mImageTitle;
    private ImageMask mImageMask;

    public ImageFrame(Context context) {
        super(context);
    }

    public ImageFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
