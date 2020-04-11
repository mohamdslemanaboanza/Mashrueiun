    package com.AbuAnzeh.Donation.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.AbuAnzeh.Donation.Activities.AlbumSelectActivity;
import com.AbuAnzeh.Donation.R;
import com.AbuAnzeh.Donation.HelpClasses.Constants;
import com.AbuAnzeh.Donation.Models.Image;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

    /**
 * A simple {@link Fragment} subclass.
 */
public class AddDonationStepOne extends Fragment {


        //Declare
        private ArrayList<Image> images;
        private LinearLayout lnrImages;
        private ImageView AddPhoto;
        private TextView hint, number_of_img, hint1, type_of_donation, header;
        private Button next;
        private RadioButton yes, no;


        public AddDonationStepOne() {
            // Required empty public constructor
        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.step_one_donation, container, false);

            //init
            hint = view.findViewById(R.id.hint);
            header = view.findViewById(R.id.header);
            next = view.findViewById(R.id.next);
            number_of_img = view.findViewById(R.id.number_of_img);
            hint1 = view.findViewById(R.id.hint1);
            yes = view.findViewById(R.id.yes);
            no = view.findViewById(R.id.no);
            type_of_donation = view.findViewById(R.id.type_of_donation);
            lnrImages = view.findViewById(R.id.lnrImages);
            AddPhoto = view.findViewById(R.id.AddPhoto);
            images = new ArrayList<>();

            //تغيير الخط
            hint.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
            next.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
            yes.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
            no.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
            hint1.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
            number_of_img.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
            type_of_donation.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));


            //هون بدي اضل عارض الصورة اذا قرر الرجوع من الخطوة الثانيه للاولى او من الثالثه  للاولى
            SharedPreferences preferences = getActivity().getSharedPreferences("imges", MODE_PRIVATE);
            final SharedPreferences.Editor editor = getActivity().getSharedPreferences("imges", MODE_PRIVATE).edit();


            //بدي اعرف شو نوع التبرع
            no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b)
                        yes.setChecked(false);
                    editor.putBoolean("Type2", false);
                    editor.putBoolean("Type1", true);
                    editor.apply();
                }
            });

            //بدي اعرف شو نوع التبرع
            yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b)
                        no.setChecked(false);
                    editor.putBoolean("Type2", true);
                    editor.putBoolean("Type1", false);
                    editor.apply();
                }
            });


            //بدي اعرف كم صورة خزن
            final String uri[] = new String[preferences.getInt("number", 0)];


            //عرض الصور الي رفعهم
            for (int i = 0; i < preferences.getInt("number", 0); i++) {
                uri[i] = preferences.getString("img" + i, "");
                Bitmap YourBitmap = BitmapFactory.decodeFile(uri[i]);
                ImageView imageView;
                View v = getLayoutInflater().inflate(R.layout.img_design, null);
                imageView = v.findViewById(R.id.imageView);
                number_of_img.setText(preferences.getInt("number", 0) + "/3");
                imageView.setImageBitmap(YourBitmap);
                imageView.setAdjustViewBounds(true);
                if (v.getParent() != null) {
                    ((ViewGroup) v.getParent()).removeView(v);
                }
                lnrImages.addView(v);
            }


            //هون كمان بدي اعرض الو شو اختار لما يرجع للخطوة الاولى
            yes.setChecked(preferences.getBoolean("Type2", false));
            no.setChecked(preferences.getBoolean("Type1", false));


            //استدعاء دالة فتح الاستيديو من خلال الضغط على الصورة
            AddPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    images = new ArrayList<>();
                    Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
                    intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 3);
                    startActivityForResult(intent, Constants.REQUEST_CODE);
                }
            });


            //اذا ضغط على التالي
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //هون لازم يختار صروة وحده على الاقل

                    if (images.size() == 0 && uri.length == 0) {
                        Snackbar.make(view, "يجب أختيار صورة واحدة على الاقل", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else if (!yes.isChecked() & !no.isChecked()) {
                        Snackbar.make(view, "يجب تحديد نوع التبرع", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {

                        //اذا رفع صورة وحدة على الاقل بروح للخطوة الثانية
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contener_main, new AddDonationStepTwo()).commit();
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        AddDonationStepTwo registerFragment = new AddDonationStepTwo();
                        manager.beginTransaction()
                                .replace(R.id.contener_main, registerFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                    }

                }
            });

            return view;
        }


        //داله لعرض الصورة والحصول على بيانات الصورة
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
                for (int i = 0; i < images.size(); i++) {

                    Bitmap YourBitmap = BitmapFactory.decodeFile(images.get(i).path);
                    ImageView imageView;

                    View v = getLayoutInflater().inflate(R.layout.img_design, null);
                    imageView = v.findViewById(R.id.imageView);


                    //هون بدي اخزن الصور الي رفعهم عشان اذا رجع يشوفهم
                    final SharedPreferences.Editor editor = getActivity().getSharedPreferences("imges", MODE_PRIVATE).edit();
                    //بخزن عدد الصور
                    number_of_img.setText(images.size() + "/3");

                    if (i == 0) {
                        editor.putString("img0", images.get(i).path);
                        editor.apply();
                    }
                    if (i == 1) {
                        editor.putString("img1", images.get(i).path);
                        editor.apply();
                    }
                    if (i == 2) {
                        editor.putString("img2", images.get(i).path);
                        editor.apply();

                    }
                    editor.putInt("number", images.size());
                    editor.apply();


                    imageView.setImageBitmap(YourBitmap);
                    imageView.setAdjustViewBounds(true);

                    if (v.getParent() != null) {
                        ((ViewGroup) v.getParent()).removeView(v);
                    }
                    lnrImages.addView(v);
                }

            }
        }
    }
