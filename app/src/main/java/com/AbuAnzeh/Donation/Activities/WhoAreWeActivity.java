package com.AbuAnzeh.Donation.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.AbuAnzeh.Donation.R;

public class WhoAreWeActivity extends AppCompatActivity {

    //Declare
    private TextView header,description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_are_we);

        //Declare
        header=findViewById(R.id.header);
        description=findViewById(R.id.description);


        //تغيير الخط
        header.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        description.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));

        //رجوع للصفحة السابقة
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        //تغيير لون الخط لبعض اجزاء النص
        SpannableString ss=new SpannableString(description.getText());
        ForegroundColorSpan spanColor=new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
        ForegroundColorSpan spanColor2=new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
        ss.setSpan(spanColor,6,12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(spanColor2,190,347,SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        description.setText(ss);




    }
}
