package com.AbuAnzeh.mashruei.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.AbuAnzeh.mashruei.HelperClass.AppIntroSampleSlider;
import com.AbuAnzeh.mashruei.R;
import com.github.paolorotolo.appintro.AppIntro;


public class MyIntro extends AppIntro {

    boolean b;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void init(Bundle savedInstanceState) {




        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);





        //اضافة الصفحات التي يتم اظهارها
        addSlide(AppIntroSampleSlider.newInstance(R.layout.app_intro1));
        addSlide(AppIntroSampleSlider.newInstance(R.layout.app_intro2));
        addSlide(AppIntroSampleSlider.newInstance(R.layout.app_intro3));
        addSlide(AppIntroSampleSlider.newInstance(R.layout.app_intro4));


        // أظهار البار
        showStatusBar(true);
        // أظهار زر التخطي
        showSkipButton(true);

        setDoneText("أنهاء");
        setSkipText("تخطي");



        setDepthAnimation();
    }

    @Override
    public void onSkipPressed() {
        SharedPreferences.Editor editor = getSharedPreferences("reading", Context.MODE_PRIVATE).edit();
        editor.putBoolean("readingSlide",false);
        editor.apply();

        startActivity(new Intent(MyIntro.this,MainActivity.class));
        finish();

    }

    @Override
    public void onNextPressed() {
        // Do something here when users click or tap on Next button.



    }

    @Override
    public void onDonePressed() {


        // Do something here when users click or tap tap on Done button.

        //اذا كبس تخطي وهو عمل عرض الشرائح من القائمة الجانبيه
        //وبرجعو على الصفحة الرئيسية

            SharedPreferences.Editor editor = getSharedPreferences("Again", Context.MODE_PRIVATE).edit();
            editor.putBoolean("Again",false);
            editor.apply();

        startActivity(new Intent(MyIntro.this,MainActivity.class));
        finish();



    }

    @Override
    public void onSlideChanged() {
        // Do something here when slide is changed
    }


}