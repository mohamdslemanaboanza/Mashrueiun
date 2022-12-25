package com.AbuAnzeh.mashruei.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.mashruei.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class DetailsOrderActivity extends AppCompatActivity {


    private TextView header,nameProduct,deskProduct,priceProduct,quantityProduct,hint,name,phoneNumber,city,location;
    private com.omega_r.libs.OmegaCenterIconButton makePhone,delete;
    private static final int REQUEST_CALL=1;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_order);

        header=findViewById(R.id.header);
        nameProduct=findViewById(R.id.nameProduct);
        deskProduct=findViewById(R.id.deskProduct);
        priceProduct=findViewById(R.id.priceProduct);
        hint=findViewById(R.id.hint);
        makePhone=findViewById(R.id.makePhone);
        delete=findViewById(R.id.delete);
        name=findViewById(R.id.name);
        phoneNumber=findViewById(R.id.phoneNumber);
        city=findViewById(R.id.city);
        location=findViewById(R.id.location);
        quantityProduct=findViewById(R.id.quantityProduct);

        SharedPreferences preferencesInfo = getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        final String userId = preferencesInfo.getString("UserId", "");

        String idProduct=getIntent().getExtras().getString("idProduct");
        databaseReference= FirebaseDatabase.getInstance().getReference("Order").child(userId).child(idProduct);

        String nameProductStr=getIntent().getExtras().getString("nameProduct");
        nameProduct.setText(nameProductStr);

        String detailsProductStr=getIntent().getExtras().getString("detailsProduct");
        deskProduct.setText(detailsProductStr);

        String priceProductStr=getIntent().getExtras().getString("priceProduct");
        priceProduct.setText(priceProductStr+" د.أ ");

        String QuantityStr=getIntent().getExtras().getString("Quantity");
        quantityProduct.setText(QuantityStr);

        String hintStr=getIntent().getExtras().getString("hint");
        hint.setText("ملاحظات : "+hintStr);


        String nameStr=getIntent().getExtras().getString("name");
        name.setText("الاسم : "+nameStr);


        final String phoneNumberStr=getIntent().getExtras().getString("phoneNumber");
        phoneNumber.setText("رقم الهاتف : "+phoneNumberStr);


        String cityStr=getIntent().getExtras().getString("city");
        city.setText("المدينة : "+cityStr);


        String locationStr=getIntent().getExtras().getString("location");
        location.setText(locationStr);


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final double latitude=getIntent().getExtras().getDouble("latitude");
        final double longitude=getIntent().getExtras().getDouble("longitude");

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });


        makePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(DetailsOrderActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity) DetailsOrderActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                }
                else
                {
                    String dial="tel:"+phoneNumberStr;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(DetailsOrderActivity.this, "تم حذف الطلب", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }else {
                            Toast.makeText(DetailsOrderActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}