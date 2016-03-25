package com.kgkg.imagevieweffectexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kgkg.imagevieweffects.ImageViewEffects;

public class MainActivity extends AppCompatActivity {

    private ImageViewEffects myCustomImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myCustomImageView = (ImageViewEffects) findViewById(R.id.myCustomImageView);
        myCustomImageView.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myCustomImageView.onClickCall();
        }
    };
}
