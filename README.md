[![Build Status](https://travis-ci.org/habibi07/ImgEffects.svg?branch=master)](https://travis-ci.org/habibi07/ImgEffects)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ImgEffects-green.svg?style=true)](https://android-arsenal.com/details/1/3476)

# ImgEffects
Cool ImageVeiw effects

![](presentation_gif.gif)

## 1. Add library to project

Grab via Gradle, add to your denepndencies in module gradle:

```compile 'com.github.habibi07:ImgEffects:1.0.4' ```

## 2. Add View to layout first,
add xml namespace to access custom properties, add following line to your root viewgroup.

```xmlns:app='http://schemas.android.com/apk/res-auto' ```


#### 2.1. Setup ImageFrame.
```
  <com.kgkg.imagevieweffects.ImageFrame
    android:layout_width='wrap_content'
    android:layout_height="wrap_content"
    android:id="@+id/myImageFrame"
    app:mTitleBlockPosition="top"
    app:mImage="@drawable/hs">
  </com.kgkg.imagevieweffects.ImageFrame>
  ```

####2.2. Setup TitleBlock.
```
<com.kgkg.imagevieweffects.ImageMask
  android:id="@+id/titleMask"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  app:MaskEffect="slideIn"
  app:mEffectDuration="1000"
  app:mMaskColor="#FF303F9F"
  app:mMaskOpacity="0.5"/>
  ```

#### 2.3. Setup mask
```
<com.kgkg.imagevieweffects.ImageTitle
  android:id="@+id/titleBlock"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:text="Lorem ipsum"
  android:textAppearance="?android:attr/textAppearanceLarge"
  android:background="#15A98E"
  android:gravity="center"
  android:textColor="@android:color/white"
  android:padding="15dp"
  app:TitleEffect="ribbon"
  app:mEffectDuration="1000"
  app:mEffectDirection="left"/>
```
    
## 3. Set effect listener
```
IOnPlayEffect listener = new IOnPlayEffect() {
    @Override
    public void startEffect() {
        Log.i(TAG, "startEffect: ");
    }
    @Override
    public void endEffect() {
        Log.i(TAG, "endEffect: ");
    }
};

protected void onCreate(Bundle savedInstanceState) {
    myImageFrame = (ImageFrame) findViewById(R.id.myImageFrame);
    myImageFrame.setEffectListener(listener);
}  
```

###### To check other custom properties for view, simply read attrs file in ImgEffects module in resources
###### If you have any question, don't hesitate, write to me or simply, create an issue :)


## License
```

Copyright (C) 2016 Krzysztof Gregorowicz

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
