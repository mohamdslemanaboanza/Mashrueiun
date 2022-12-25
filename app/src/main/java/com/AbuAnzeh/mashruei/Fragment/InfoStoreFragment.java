package com.AbuAnzeh.mashruei.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.mashruei.Activities.CreateStoreActivity;
import com.AbuAnzeh.mashruei.Activities.UpdateStoreActivity;
import com.AbuAnzeh.mashruei.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;


public class InfoStoreFragment extends Fragment {


    private Button create_store_button,edit;
    private DatabaseReference databaseStore;
    private String nameStore,descriptionStore,phoneStore,locationStore,workTimeStore,imgUriStore;
    private int isActiveStore;
    private LinearLayout linearLayout4;
    private ImageView imgStore,imageHint;
    private TextView hint,nameStoreTextView,description_storeTextView,PhoneNumberStoreTextView,LocationStoreTextView,WorkTimeTextView,stutasStore;


    public InfoStoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_info_store, container, false);
        create_store_button=view.findViewById(R.id.create_store);
        hint=view.findViewById(R.id.textView9);
        nameStoreTextView=view.findViewById(R.id.nameStore);
        edit=view.findViewById(R.id.edit);
        imgStore=view.findViewById(R.id.imgStore);
        imageHint=view.findViewById(R.id.imageView4);
        linearLayout4=view.findViewById(R.id.linearLayout4);
        description_storeTextView=view.findViewById(R.id.description_store);
        PhoneNumberStoreTextView=view.findViewById(R.id.PhoneNumberStore);
        LocationStoreTextView=view.findViewById(R.id.LocationStore);
        WorkTimeTextView=view.findViewById(R.id.WorkTime);
        stutasStore=view.findViewById(R.id.stutasStore);

        final SharedPreferences preferencesInfo = getActivity().getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        String userId = preferencesInfo.getString("UserId", "");



        final SharedPreferences TypeStoreSharedPreferences = getActivity().getSharedPreferences("TypeStore", Context.MODE_PRIVATE);
        String TypeStoreStr = TypeStoreSharedPreferences.getString("TypeStore", "");


        databaseStore = FirebaseDatabase.getInstance().getReference("Stores").child(TypeStoreStr).child(userId);



        databaseStore.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameStore = dataSnapshot.child("nameStore").getValue(String.class);
                descriptionStore = dataSnapshot.child("descriptionStore").getValue(String.class);
                phoneStore = dataSnapshot.child("phoneStore").getValue(String.class);
                locationStore = dataSnapshot.child("locationStore").getValue(String.class);
                imgUriStore = dataSnapshot.child("imgUriStore").getValue(String.class);
                workTimeStore = dataSnapshot.child("workTimeStore").getValue(String.class);


                try {
                    isActiveStore = Integer.parseInt(String.valueOf(dataSnapshot.child("isActiveStore").getValue()));

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                Picasso.get().load(imgUriStore).placeholder(R.drawable.logo).into(imgStore);
                nameStoreTextView.setText(nameStore);
                description_storeTextView.setText(descriptionStore);
                PhoneNumberStoreTextView.setText(phoneStore);
                WorkTimeTextView.setText(workTimeStore);
                LocationStoreTextView.setText(locationStore);


                if (isActiveStore == 0){
                    stutasStore.setText("قيد المراجعة");
                }else if (isActiveStore == 1 ){
                    stutasStore.setText("المتجر فعّال");
                    stutasStore.setTextColor(Color.GREEN);
                }else {
                    stutasStore.setText("تم رفض الطلب");
                    stutasStore.setTextColor(Color.RED);
                }


                if (phoneStore == null ){
                    linearLayout4.setVisibility(View.GONE);
                    edit.setVisibility(View.GONE);

                    create_store_button.setVisibility(View.VISIBLE);
                    hint.setVisibility(View.VISIBLE);
                    imageHint.setVisibility(View.VISIBLE);


                }else {
                    edit.setVisibility(View.VISIBLE);

                    create_store_button.setVisibility(View.GONE);
                    hint.setVisibility(View.GONE);
                    imageHint.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        create_store_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateStoreActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UpdateStoreActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        return view;
    }
}