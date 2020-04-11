package com.AbuAnzeh.Donation.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.AbuAnzeh.Donation.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_screen);


        //اذا تمت مشاهدة شرح التطبيق الي همه 4 سلايدات
        SharedPreferences preferences = getSharedPreferences("Done", MODE_PRIVATE);
        boolean b =preferences.getBoolean("sDone",true);


        //اذا تم تسجيل دخول بنجاح
        SharedPreferences Success = getSharedPreferences("Success", MODE_PRIVATE);
        boolean s =Success.getBoolean("Success",false);


        final Intent  SplashScreen =new Intent(SplashScreenActivity.this,MyIntro.class);
        final Intent  MainActivity =new Intent(SplashScreenActivity.this,MainActivity.class);
        final Intent  IntroActivity=new Intent(SplashScreenActivity.this,MainActivity.class);






        if(s)
            b=false;



        if(b)
        {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(SplashScreen);
                    finish();

                }
            }, 3000);
        }else  if(s)
            {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(MainActivity);
                        finish();

                    }
                }, 3000);
            }
        else

            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(IntroActivity);
                        finish();

                    }
                }, 3000);
            }

    }
}
