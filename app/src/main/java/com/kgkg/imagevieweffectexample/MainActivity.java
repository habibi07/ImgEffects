package com.kgkg.imagevieweffectexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.kgkg.imagevieweffects.IOnPlayEffect;
import com.kgkg.imagevieweffects.ImageFrame;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "kgkg";
    private ImageFrame myImageFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myImageFrame = (ImageFrame) findViewById(R.id.myImageFrame);
        //myImageFrame.setOnClickListener(listener);
        myImageFrame.setEffectListener(callback);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myImageFrame.showTitleBlockEffect();
        }
    };

    IOnPlayEffect callback = new IOnPlayEffect() {
        @Override
        public void startEffect() {


        }

        @Override
        public void endEffect() {

        }
    };

}
