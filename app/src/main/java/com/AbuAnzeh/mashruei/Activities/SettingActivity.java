package com.AbuAnzeh.mashruei.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.AbuAnzeh.mashruei.R;

public class SettingActivity extends AppCompatActivity {


    private TextView header;
    private Switch notificationEnabled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        header=findViewById(R.id.header);
        notificationEnabled=findViewById(R.id.notificationEnabled);


        final SharedPreferences preferencesNotificationEnabled  = getSharedPreferences("preferencesNotificationEnabled",MODE_PRIVATE);
        boolean NotificationEnabled = preferencesNotificationEnabled.getBoolean("NotificationEnabled",true);

        if (NotificationEnabled){
            notificationEnabled.setChecked(true);
        }else {
            notificationEnabled.setChecked(false);
        }
        notificationEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                SharedPreferences.Editor editor=preferencesNotificationEnabled.edit();

                Log.d("NotificationEnabled",b+"");
                if (b){
                    editor.putBoolean("NotificationEnabled",true);
                }else {
                    editor.putBoolean("NotificationEnabled",false);
                }

                editor.apply();
            }
        });
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}