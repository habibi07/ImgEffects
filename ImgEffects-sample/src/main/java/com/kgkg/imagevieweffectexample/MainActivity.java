package com.kgkg.imagevieweffectexample;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.kgkg.imagevieweffects.IOnPlayEffect;
import com.kgkg.imagevieweffects.ImageFrame;
import com.kgkg.imagevieweffects.ImageMask;
import com.kgkg.imagevieweffects.ImageTitle;

public class MainActivity extends Activity {

    public static String TAG = "kgkg";
    private ImageFrame imageFrame;
    private ImageTitle imageTitle;
    private ImageMask imageMask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        imageFrame = (ImageFrame) findViewById(R.id.myImageFrame);
        imageTitle = (ImageTitle) findViewById(R.id.titleBlock);
        imageMask = (ImageMask) findViewById(R.id.titleMask);

        imageFrame.setEffectListener(callback);
    }

    IOnPlayEffect callback = new IOnPlayEffect() {
        @Override
        public void startEffect() {

        }

        @Override
        public void endEffect() {

        }
    };


    //Ribbon and SlideIn
    public void button(View v){
        Log.i(TAG, "button: Ribbon and SlideIn");
        imageTitle.setEffect(6);
        imageMask.setEffect(5);
    }
    //Corner
    public void button2(View v){
        Log.i(TAG, "button1: Corner");
        imageTitle.setEffect(5);
        imageMask.setEffect(5);
    }
    //Uncover and slideTop
    public void button3(View v){
        Log.i(TAG, "button1: Uncover and slideTop");
        imageFrame.setTitleBlockPosition(2);
        imageTitle.setEffect(4);
        imageMask.setEffect(1);
    }
    //move and slideBottom
    public void button4(View v){
        Log.i(TAG, "button1: move and slideBottom");
        imageFrame.setTitleBlockPosition(1);
        imageTitle.setEffect(3);
        imageMask.setEffect(2);

    }
    //slide right and slideLeft
    public void button5(View v){
        imageTitle.setEffectDirection(1);
        imageTitle.setEffect(2);
        imageMask.setEffect(4);
        imageFrame.setTitleBlockPosition(0);
    }
    //fade and fade
    public void button6(View v){
        Log.i(TAG, "button1: fade and fade");
        imageFrame.setTitleBlockPosition(2);
        imageTitle.setEffect(1);
        imageMask.setEffect(6);
    }
    //slide middle
    public void button7(View v){
        Log.i(TAG, "button1: slide middle");
        imageFrame.setTitleBlockPosition(1);
        imageTitle.setEffect(5);
        imageMask.setEffect(4);
    }

}
