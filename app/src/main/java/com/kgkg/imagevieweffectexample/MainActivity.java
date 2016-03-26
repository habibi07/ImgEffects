package com.kgkg.imagevieweffectexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kgkg.imagevieweffects.IOnPlayEffect;
import com.kgkg.imagevieweffects.ImageViewEffects;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "kgkg";
    private ImageViewEffects myCustomImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myCustomImageView = (ImageViewEffects) findViewById(R.id.myCustomImageView);
        myCustomImageView.setOnClickListener(listener);
        myCustomImageView.setOnShowEffectListener(imgViewListener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myCustomImageView.playEffect();
        }
    };

    IOnPlayEffect imgViewListener = new IOnPlayEffect() {
        @Override
        public void startEffect() {
            //Log.i(TAG, "startEffect: ");
        }

        @Override
        public void endEffect() {
            //Log.i(TAG, "endEffect: ");
        }
    };
}
