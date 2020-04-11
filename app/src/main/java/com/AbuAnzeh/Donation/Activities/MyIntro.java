package com.AbuAnzeh.Donation.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import com.AbuAnzeh.Donation.R;
import com.AbuAnzeh.Donation.HelpClasses.AppIntroSampleSlider;
import com.github.paolorotolo.appintro.AppIntro;


public class MyIntro extends AppIntro {

    boolean b;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void init(Bundle savedInstanceState) {



        //اذا كبس زر عرظ الشرائه من القائمة الجانبية
        SharedPreferences Again = getSharedPreferences("Again", MODE_PRIVATE);
        b =Again.getBoolean("Again",false);






        //اضافة الصفحات التي يتم اظهارها
        addSlide(AppIntroSampleSlider.newInstance(R.layout.app_intro1));
        addSlide(AppIntroSampleSlider.newInstance(R.layout.app_intro2));
        addSlide(AppIntroSampleSlider.newInstance(R.layout.app_intro3));
        addSlide(AppIntroSampleSlider.newInstance(R.layout.app_intro4));


        // أظهار البار
        showStatusBar(true);
        // أظهار زر التخطي
        showSkipButton(true);



        //بغير الكلام عشان بدي اعرف دخل التطبيق من اول مرة ولا بدو عرضها كمان مرة
        if(b){
            setDoneText("انهاء");
            setSkipText("تخطي");
        }else {

            setDoneText("أبدأ");
            setSkipText("تخطي");
        }




        setDepthAnimation();
    }

    @Override
    public void onSkipPressed() {
        if(b)
        {

            //اذا كبس تخطي وهو عمل عرض الشرائح من القائمة الجانبيه
            //هون اذا كبس على زر التخطي
            SharedPreferences.Editor editor = getSharedPreferences("Again", Context.MODE_PRIVATE).edit();
            editor.putBoolean("Again",false);
            editor.apply();
            onBackPressed();

        }else
        {

            //هون اذا كبس على زر التخطي
            SharedPreferences.Editor editor = getSharedPreferences("Done", Context.MODE_PRIVATE).edit();
            editor.putBoolean("sDone",false);
            editor.apply();

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }


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
        if(b)
        {
            SharedPreferences.Editor editor = getSharedPreferences("Again", Context.MODE_PRIVATE).edit();
            editor.putBoolean("Again",false);
            editor.apply();

            onBackPressed();
        }else
            {
                //هون اذا كبس على زر التخطي
                SharedPreferences.Editor editor = getSharedPreferences("Done", Context.MODE_PRIVATE).edit();
                editor.putBoolean("sDone",false);
                editor.apply();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }

    }

    @Override
    public void onSlideChanged() {
        // Do something here when slide is changed
    }


}