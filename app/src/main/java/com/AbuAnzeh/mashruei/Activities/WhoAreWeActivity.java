package com.AbuAnzeh.mashruei.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.AbuAnzeh.mashruei.R;

public class WhoAreWeActivity extends AppCompatActivity {

    //Declare
    private TextView header, description;
    private Button share;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_are_we);

        //Declare
        header = findViewById(R.id.header);
        share = findViewById(R.id.share);
        description = findViewById(R.id.description);


//        List<SlideModel> slideModels=new ArrayList<>();
//        slideModels.add(new SlideModel(R.drawable.imgtwo));
//        slideModels.add(new SlideModel(R.drawable.imgone));
//        slideModels.add(new SlideModel(R.drawable.imgfour));
//        slideModels.add(new SlideModel(R.drawable.min));


//        viewPager.setImageList(slideModels,true);
        int flag = getIntent().getExtras().getInt("flag");

        if (flag == 0) {

        } else {
            share.setVisibility(View.GONE);
            header.setText("طريقة الاستخدام");
            description.setText(getString(R.string.howUseApp));
        }


        //رجوع للصفحة السابقة
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    public void share_app(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "نِعمة");
        String v = "https://play.google.com/store/apps/details?id=com.AbuAnzeh.Donation\n\n";
        intent.putExtra(Intent.EXTRA_TEXT, v);
        startActivity(Intent.createChooser(intent, "مشاركة عبر"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
