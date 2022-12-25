package com.AbuAnzeh.mashruei.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AbuAnzeh.mashruei.Activities.DetailsOrderActivity;
import com.AbuAnzeh.mashruei.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;


public class InfoStoreFragmentMain extends Fragment {




    private String nameStore,descriptionStore,phoneStore,locationStore,workTimeStore,imgUriStore,cityStore;
    private LinearLayout linearLayout4;
    private double latitude;
    private double longitude;
    private TextView nameStoreTextView,description_storeTextView,PhoneNumberStoreTextView,LocationStoreTextView,WorkTimeTextView,cityStoreTextView;
    private ImageView imgStore;
    private DatabaseReference databaseStore;


    public InfoStoreFragmentMain() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_store_main, container, false);




        nameStoreTextView=view.findViewById(R.id.nameStore);
        linearLayout4=view.findViewById(R.id.linearLayout4);

        description_storeTextView=view.findViewById(R.id.description_store);
        imgStore=view.findViewById(R.id.imgStore);
        PhoneNumberStoreTextView=view.findViewById(R.id.PhoneNumberStore);
        LocationStoreTextView=view.findViewById(R.id.LocationStore);
        WorkTimeTextView=view.findViewById(R.id.WorkTime);
        cityStoreTextView=view.findViewById(R.id.cityStore);






        SharedPreferences preferences = getActivity().getSharedPreferences("StoreType", Context.MODE_PRIVATE);
        String StoreType=preferences.getString("StoreType","");


        SharedPreferences  saveIdStore = getActivity().getSharedPreferences("saveIdStore",Context.MODE_PRIVATE);
        Log.d("saveIdStore",saveIdStore.getString("saveIdStore",""));

        if (StoreType.equals("طعام")){
            StoreType = "Food";
        }else if (StoreType.equals("حلويات")){
            StoreType = "Candy";
        }else if (StoreType.equals("ملابس")){
            StoreType = "Clothes";
        }else if (StoreType.equals("البان و اجبان")){
            StoreType = "Dairies";
        }else if (StoreType.equals("حرف يدوية")){
            StoreType = "Handicraft";
        }else if (StoreType.equals("اثاثا منزلي")){
            StoreType = "HomeFurnishings";
        }else if (StoreType.equals("اخرى")){
            StoreType = "Other";
        }
        databaseStore = FirebaseDatabase.getInstance().getReference("Stores").child(StoreType).child(saveIdStore.getString("saveIdStore",""));



        databaseStore.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameStore = dataSnapshot.child("nameStore").getValue(String.class);
                descriptionStore = dataSnapshot.child("descriptionStore").getValue(String.class);
                phoneStore = dataSnapshot.child("phoneStore").getValue(String.class);
                cityStore = dataSnapshot.child("cityStore").getValue(String.class);
                locationStore = dataSnapshot.child("locationStore").getValue(String.class);
                imgUriStore = dataSnapshot.child("imgUriStore").getValue(String.class);
                workTimeStore = dataSnapshot.child("workTimeStore").getValue(String.class);
                latitude = Double.parseDouble(String.valueOf(dataSnapshot.child("latitude").getValue(Double.class)));
                longitude = Double.parseDouble(String.valueOf(dataSnapshot.child("longitude").getValue(Double.class)));


                Picasso.get().load(imgUriStore).placeholder(R.drawable.logo).into(imgStore);
                nameStoreTextView.setText(nameStore);
                description_storeTextView.setText(descriptionStore);
                PhoneNumberStoreTextView.setText(phoneStore);
                WorkTimeTextView.setText(workTimeStore);
                LocationStoreTextView.setText(locationStore);
                cityStoreTextView.setText(cityStore);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        PhoneNumberStoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity) getActivity(),new String[]{Manifest.permission.CALL_PHONE},1);
                }
                else
                {
                    String dial="tel:"+phoneStore;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
            }
        });

        LocationStoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    getActivity().startActivity(mapIntent);


                }
            }
        });



        return view;
    }

}