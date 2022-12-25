package com.AbuAnzeh.mashruei.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.AbuAnzeh.mashruei.Adpter.AdapterNotifications;
import com.AbuAnzeh.mashruei.Models.ModelNotifications;
import com.AbuAnzeh.mashruei.Models.ModelProductTwo;
import com.AbuAnzeh.mashruei.Models.Modelnotice;
import com.AbuAnzeh.mashruei.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private TextView header,textView4;
    private RecyclerView mRecyclerView;
    private ArrayList<Modelnotice> mUploads;
    private DatabaseReference referenceNotifications,databaseReference;
    private Button back;
    private ImageView imageView9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        header=findViewById(R.id.header);
        mRecyclerView=findViewById(R.id.RecyclerView);
        back=findViewById(R.id.back);
        textView4=findViewById(R.id.textView9);
        imageView9=findViewById(R.id.imageView4);
        mUploads=new ArrayList<>();

        SharedPreferences preferencesInfo = getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        final String userId = preferencesInfo.getString("UserId", "");
        referenceNotifications= FirebaseDatabase.getInstance().getReference("AppNotifications");
        databaseReference= FirebaseDatabase.getInstance().getReference("UserNotification").child(userId);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageView9.setVisibility(View.GONE);
        textView4.setVisibility(View.GONE);




        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Modelnotice notice =snapshot.getValue(Modelnotice.class);
                    mUploads.add(0,notice);
                }


                referenceNotifications.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Modelnotice notice =snapshot.getValue(Modelnotice.class);
                            mUploads.add(0,notice);
                        }


                        AdapterNotifications notifications = new AdapterNotifications(NotificationActivity.this,mUploads);
                        mRecyclerView.setAdapter(notifications);

                        if (notifications.getItemCount() ==0){
                            imageView9.setVisibility(View.VISIBLE);
                            textView4.setVisibility(View.VISIBLE);
                        }else {
                            back.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
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