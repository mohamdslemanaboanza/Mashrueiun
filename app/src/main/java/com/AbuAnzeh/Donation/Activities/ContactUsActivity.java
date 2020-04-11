package com.AbuAnzeh.Donation.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.AbuAnzeh.Donation.R;

public class ContactUsActivity extends AppCompatActivity {


    //Declare
    private Button send;
    private EditText text;
    private TextView header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        //init
        text=findViewById(R.id.detalis);
        send=findViewById(R.id.send);
        header=findViewById(R.id.header);


        //تغيير الخط
        header.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        send.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        text.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));

        //الرجوع الى الخلف
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
}
