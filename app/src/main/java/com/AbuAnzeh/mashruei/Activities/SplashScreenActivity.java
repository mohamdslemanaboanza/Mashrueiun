package com.AbuAnzeh.mashruei.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.AbuAnzeh.mashruei.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView img;
    Animation animation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_splach_screen);

        img = findViewById(R.id.img);
        animation=AnimationUtils.loadAnimation(this,R.anim.animation);
        img.startAnimation(animation);



        SharedPreferences preferencesInfo = getSharedPreferences("LoginOrSign", Context.MODE_PRIVATE);
        boolean successLogin = preferencesInfo.getBoolean("success", false);

        Log.d("successLogin",successLogin+"");
        if (successLogin) {
            startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
            finish();
        }



    }

    public void go(View view) {


        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


    }


}
