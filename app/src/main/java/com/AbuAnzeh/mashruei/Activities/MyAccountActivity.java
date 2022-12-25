package com.AbuAnzeh.mashruei.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.mashruei.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyAccountActivity extends AppCompatActivity {

    private TextView header;
    private LinearLayout updateInfo,changePass;
    private DatabaseReference databaseUsers;
    private ImageView img_user;
    private String fname,lname,phone,createAt,authKey,pass,city,imgUri,typeUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info_user);

        updateInfo=findViewById(R.id.updateInfo);
        changePass=findViewById(R.id.changePass);
        header=findViewById(R.id.header);
        img_user=findViewById(R.id.img_user);

        SharedPreferences preferencesInfo = getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        String userId = preferencesInfo.getString("UserId", "");

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 imgUri = dataSnapshot.child("imgUri").getValue(String.class);
                 fname = dataSnapshot.child("fname").getValue(String.class);
                 lname = dataSnapshot.child("lname").getValue(String.class);
                 phone = dataSnapshot.child("phone").getValue(String.class);
                 city = dataSnapshot.child("city").getValue(String.class);
                authKey = dataSnapshot.child("authKey").getValue(String.class);
                pass = dataSnapshot.child("pass").getValue(String.class);
                createAt = dataSnapshot.child("createAt").getValue(String.class);
                typeUser = dataSnapshot.child("typeUser").getValue(String.class);
                Picasso.get().load(imgUri).placeholder(R.drawable.logo).into(img_user);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyAccountActivity.this,UpdateInfoActivity.class)
                .putExtra("city",city)
                        .putExtra("fname",fname)
                        .putExtra("lname",lname)
                        .putExtra("phone",phone)
                        .putExtra("createAt",createAt)
                        .putExtra("imgUri",imgUri)
                        .putExtra("authKey",authKey)
                        .putExtra("pass",pass)
                );

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyAccountActivity.this,ChangePassActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}