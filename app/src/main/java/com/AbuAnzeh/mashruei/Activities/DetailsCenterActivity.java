package com.AbuAnzeh.mashruei.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.AbuAnzeh.mashruei.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsCenterActivity extends AppCompatActivity implements OnMapReadyCallback {


    TextView desk,nameTxt,location;
    private GoogleMap mMap;
    ImageView make_phone,face;
    private static final int REQUEST_CALL=1;
    ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_center);
        desk=findViewById(R.id.desk);
        nameTxt=findViewById(R.id.name);
        logo=findViewById(R.id.logo);
        make_phone=findViewById(R.id.make_phone);
        location=findViewById(R.id.location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        String name=getIntent().getExtras().getString("Name");
        String deskStr=getIntent().getExtras().getString("Deck");
        final String phoneStr=getIntent().getExtras().getString("phone");
        final String url=getIntent().getExtras().getString("img");
        final String locationStr=getIntent().getExtras().getString("location");

        location.setText(locationStr);
        nameTxt.setText(name);
        desk.setText(deskStr);


        Picasso.get().load(url).placeholder(R.drawable.logo).into(logo);




        make_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(DetailsCenterActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity) DetailsCenterActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                }
                else
                {
                    String dial="tel:"+phoneStr;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
            }
        });



    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double lat=getIntent().getExtras().getDouble("lat");
        double lon=getIntent().getExtras().getDouble("lon");

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(lat, lon);

        mMap.addMarker(new MarkerOptions().position(sydney).title("موقع"));
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
