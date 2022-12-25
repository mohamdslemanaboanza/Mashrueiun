package com.AbuAnzeh.mashruei.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;


import com.AbuAnzeh.mashruei.Adpter.ViewPagerAdapter;
import com.AbuAnzeh.mashruei.R;

import java.util.TimerTask;

public class DeskAppActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private int dotscount;
    private ImageView[] dots;
    private String[] texts;
    private ViewPagerAdapter adpterRec;
    private LinearLayout sliderDotspanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desk_app);
        sliderDotspanel = findViewById(R.id.SliderDots);
        viewPager=findViewById(R.id.viewPager);

        texts = new String[]{
                "خدمة حكومية لادارة و دعم المشاريع الخاصة بالعائلات",
                "تطبيق مشروعي هو تطبيق يعمل على الدعم المشارع الصغيرة للاسر من خلال نشر المنتجات التي تقوم الاسرة بإنتاجها منزليا  بالتطبيق بعد موافقة الجهات المختصة بذلك و الوصول الى اكبر شريحة من المجتمع الاردني",
                "نشر المنتجات منزلية الصُنع مثل : الصابون و الخبز العربي و الزيت و الحلويات و الملابس .. الخ",
                "يتم طلب انشاء متجر لنشر المنتجات المنزلية ويتم الموافقة على انشاء متجر من قبل وزارة التنمية الاجتماعية",

        };

        adpterRec=new ViewPagerAdapter(this,texts);
        viewPager.setAdapter(adpterRec);

        dotscount = 4;
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(DeskAppActivity.this, R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(DeskAppActivity.this, R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    public void skip(View view) {

        startActivity(new Intent(DeskAppActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(0);
                    }

                }
            });


        }
    }
}