package com.AbuAnzeh.Donation.Adpter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.AbuAnzeh.Donation.R;
import com.squareup.picasso.Picasso;


public class ImageAdapterView extends PagerAdapter
{
    private Context context;
    private Uri imgUri[];


    public ImageAdapterView(Context context, Uri[] imgUri) {
        this.context = context;
        this.imgUri = imgUri;
    }

    @Override
    public int getCount() {
        return imgUri.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view==((ImageView)object);

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView=new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context).load(imgUri[position]).placeholder(R.drawable.logo).error(R.drawable.logo).into(imageView);
        ((ViewPager)container).addView(imageView,0);

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                context.startActivity(new Intent(context, FullImagesActivity.class));
//
//            }
//        });

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((ImageView)object);
    }
}
