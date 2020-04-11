package com.AbuAnzeh.Donation.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.Donation.Adpter.ImageAdapterView;
import com.AbuAnzeh.Donation.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class DetailsDonationActivity extends AppCompatActivity  implements OnMapReadyCallback {

    private TextView header,user_name,added,city,detalis,commit,likes,hint_de;
    boolean flag=true;    private CircleIndicator indicator;
    private static final Integer[] XMEN=new Integer[3];
    private ViewPager viewPager;
    private int size=3;
    private ImageView img,make_phone;
    private Uri[] uris;
    private String DeDonation;
    private DatabaseReference commitDatabase,likesDatabase;
    private GoogleMap mMap;
    private ArrayList<Integer> XMENArray = new ArrayList<>();
    private static final int REQUEST_CALL=1;
    double latitude;
    double longitude;






    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setContentView(R.layout.activity_details_donation);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        header=findViewById(R.id.header);
        user_name=findViewById(R.id.user_name);
        added=findViewById(R.id.added);
        city=findViewById(R.id.city);
        detalis=findViewById(R.id.detalis);
        hint_de=findViewById(R.id.hint_de);
        commit=findViewById(R.id.commit);
        likes=findViewById(R.id.likes);
        make_phone=findViewById(R.id.make_phone);
        img=findViewById(R.id.img);
        viewPager=findViewById(R.id.viewPager);
        indicator  = findViewById(R.id.indicator);

        final String idPost=getIntent().getExtras().getString("idPost");


        commitDatabase = FirebaseDatabase.getInstance().getReference("commitDatabase").child(idPost);
        likesDatabase = FirebaseDatabase.getInstance().getReference("likesDatabase").child(idPost);






        header.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        hint_de.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        user_name.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        added.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        city.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        detalis.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        commit.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        likes.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));




        String nameDonation=getIntent().getExtras().getString("nameDonation");
        DeDonation=getIntent().getExtras().getString("DeDonation");
        String state=getIntent().getExtras().getString("state");
        final String imgU=getIntent().getExtras().getString("imgU");
        String cityD=getIntent().getExtras().getString("city");
        String Name_P=getIntent().getExtras().getString("Name_P");
        final String Phone=getIntent().getExtras().getString("Phone");

        header.setText(nameDonation);
        detalis.setText(DeDonation);
        added.setText(state);
        user_name.setText(Name_P);
        city.setText(cityD);

        Picasso.with(this).load(imgU).placeholder(R.drawable.logo).error(R.drawable.logo).into(img);

            uris=new Uri[size];
            uris[0]=Uri.parse(getIntent().getExtras().getString("Img1"));
            uris[1]=Uri.parse(getIntent().getExtras().getString("Img2"));
            uris[2]=Uri.parse(getIntent().getExtras().getString("Img3"));
        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);



        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailsDonationActivity.this,CommitActivity.class).putExtra("idPost",idPost).putExtra("img",imgU));
            }
        });




        make_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(DetailsDonationActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity) DetailsDonationActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                }
                else
                {
                    String dial="tel:"+Phone;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
            }
        });

        commitDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commit.setText(dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        likesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        viewPager.setAdapter(new ImageAdapterView(DetailsDonationActivity.this,uris));

        indicator.setViewPager(viewPager);




        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final String key=commitDatabase.push().getKey();

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int num = Integer.parseInt(commit.getText().toString());

                if (flag)
                {
                    likes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_red, 0, 0, 0);
                    flag=false;


                            likesDatabase.child(key).setValue(num+1);


                }else
                    {
                        likes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border_black_24dp, 0, 0, 0);
                        flag=true;
                        likesDatabase.child(key).removeValue();

                    }
            }
        });
    }

    public void share(View view) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        try {

            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, DeDonation+"\n"+"https://play.google.com/store/apps/details?id=com.AbuAnzeh.Donation"+"\n تطبيق نِعمة \n");
            intent.putExtra(Intent.EXTRA_TITLE, "تطبيق نِعمة");


            startActivity(Intent.createChooser(intent, "مشاركة التبرع عبر"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(DetailsDonationActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        latitude = getIntent().getExtras().getDouble("Latitude");
        longitude = getIntent().getExtras().getDouble("Longitude");

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(sydney).title("الموقع"));
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));

    }
}
