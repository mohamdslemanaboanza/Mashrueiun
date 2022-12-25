package com.AbuAnzeh.mashruei.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.AbuAnzeh.mashruei.Models.StoreModel;
import com.AbuAnzeh.mashruei.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UpdateStoreActivity extends AppCompatActivity {



    private TextInputLayout textInputLayoutNameStore,textInputLayoutDeskStore,textInputLayoutPhoneStore,textInputLayoutPhoneUser,textInputLayoutLocationStore,textInputLayoutWorkTime;
    private EditText nameStore,description_store,PhoneNumberStore,PhoneNumberUser,LocationStore,WorkTime;
    private TextView header;
    private ImageView imgStore;
    private Button update_store;
    LinearLayout choose_location;
    private com.airbnb.lottie.LottieAnimationView animationView;
    private DatabaseReference databaseUsers,databaseStore;
    static int PReqCode = 1, REQUESCODE = 1;
    private Uri pickedImgUri=null;
    double latitude=0.0;
    double longitude=0.0;
    String addressStr;
    int PLACE_PICKER_REQUEST=2;
    private StorageReference ImagesStores;
    private StorageTask mUploadTask;


 String  nameStoreStr ,
    phoneStoreStr ,
    descriptionStoreStr ,
    workTimeStoreStr ,
    locationStoreStr ,
    imgUriStoreStr ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_store);

        header=findViewById(R.id.header);
        choose_location=findViewById(R.id.choose_location);
        textInputLayoutDeskStore=findViewById(R.id.textInputLayoutDeskStore);
        update_store=findViewById(R.id.create_store);
        animationView=findViewById(R.id.animationView);
        textInputLayoutNameStore=findViewById(R.id.textInputLayoutNameStore);
        textInputLayoutPhoneStore=findViewById(R.id.textInputLayoutPhoneStore);
        textInputLayoutWorkTime=findViewById(R.id.textInputLayoutWorkTime);
        nameStore=findViewById(R.id.nameStore);
        imgStore=findViewById(R.id.imgStore);
        PhoneNumberStore=findViewById(R.id.PhoneNumberStore);
        description_store=findViewById(R.id.description_store);
        textInputLayoutLocationStore=findViewById(R.id.textInputLayoutLocationStore);
        LocationStore=findViewById(R.id.LocationStore);
        WorkTime=findViewById(R.id.WorkTime);

        ImagesStores = FirebaseStorage.getInstance().getReference().child("ImagesStores");

        final SharedPreferences preferencesInfo = getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        String userId = preferencesInfo.getString("UserId", "");


        MapUtility.apiKey = getResources().getString(R.string.google_api_key);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        choose_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(UpdateStoreActivity.this, LocationPickerActivity.class);
                startActivityForResult(i, PLACE_PICKER_REQUEST);

            }
        });


        imgStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestForPermission();
            }
        });

        final SharedPreferences TypeStoreSharedPreferences = getSharedPreferences("TypeStore", Context.MODE_PRIVATE);
        String TypeStoreStr = TypeStoreSharedPreferences.getString("TypeStore", "");


        databaseStore = FirebaseDatabase.getInstance().getReference("Stores").child(TypeStoreStr).child(userId);

        databaseStore.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 nameStoreStr = dataSnapshot.child("nameStore").getValue(String.class);
                 phoneStoreStr = dataSnapshot.child("phoneStore").getValue(String.class);
                 descriptionStoreStr = dataSnapshot.child("descriptionStore").getValue(String.class);
                 workTimeStoreStr = dataSnapshot.child("workTimeStore").getValue(String.class);
                 locationStoreStr = dataSnapshot.child("locationStore").getValue(String.class);
                 imgUriStoreStr = dataSnapshot.child("imgUriStore").getValue(String.class);


                nameStore.setText(nameStoreStr);
                PhoneNumberStore.setText(phoneStoreStr);
                description_store.setText(descriptionStoreStr);
                WorkTime.setText(workTimeStoreStr);
                LocationStore.setText(locationStoreStr);
                Picasso.get().load(imgUriStoreStr).placeholder(R.drawable.logo).into(imgStore);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        update_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(nameStore.getText().toString())) {
                    textInputLayoutNameStore.setBoxStrokeColor(Color.RED);
                    nameStore.requestFocus();
                    return;
                }else if (TextUtils.isEmpty(description_store.getText().toString())){
                    textInputLayoutDeskStore.setBoxStrokeColor(Color.RED);
                    description_store.requestFocus();
                    return;
                }else if (TextUtils.isEmpty(PhoneNumberStore.getText().toString())){
                    textInputLayoutPhoneStore.setBoxStrokeColor(Color.RED);
                    PhoneNumberStore.requestFocus();
                    return;
                }else if (PhoneNumberStore.getText().length() > 10 || PhoneNumberStore.getText().length() < 10) {
                    textInputLayoutPhoneStore.setError("الرقم غير صحيح");
                    PhoneNumberStore.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(LocationStore.getText().toString())){
                    textInputLayoutLocationStore.setBoxStrokeColor(Color.RED);
                    LocationStore.requestFocus();
                    return;
                }else if (TextUtils.isEmpty(WorkTime.getText().toString())){
                    textInputLayoutWorkTime.setBoxStrokeColor(Color.RED);
                    WorkTime.requestFocus();
                    return;
                }


                if (pickedImgUri != null) {

                    animationView.setVisibility(View.VISIBLE);
                    final StorageReference fileReference = ImagesStores.child(System.currentTimeMillis()
                            + "." + getFileExtension(pickedImgUri));

                    mUploadTask = fileReference.putFile(pickedImgUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            HashMap<String,Object> store = new HashMap<>();
                                            store.put("nameStore",nameStore.getText().toString());
                                            store.put("descriptionStore",description_store.getText().toString());
                                            store.put("phoneStore",PhoneNumberStore.getText().toString());
                                            store.put("locationStore",LocationStore.getText().toString());
                                            store.put("workTimeStore",WorkTime.getText().toString());
                                            store.put("imgUriStore",uri.toString());


                                            databaseStore.updateChildren(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        animationView.setVisibility(View.GONE);

                                                        onBackPressed();
                                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                                                        Toast.makeText(UpdateStoreActivity.this, "تم تحديث معلومات المتجر", Toast.LENGTH_SHORT).show();

                                                    } else {

                                                        animationView.setVisibility(View.GONE);
                                                        Toast.makeText(UpdateStoreActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                                    }

                                                }
                                            });
                                        }
                                    });
                                }
                            });


                }else {
                    HashMap<String,Object> store = new HashMap<>();
                    store.put("nameStore",nameStore.getText().toString());
                    store.put("descriptionStore",description_store.getText().toString());
                    store.put("phoneStore",PhoneNumberStore.getText().toString());
                    store.put("locationStore",LocationStore.getText().toString());
                    store.put("workTimeStore",WorkTime.getText().toString());



                    databaseStore.updateChildren(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                animationView.setVisibility(View.GONE);

                                Toast.makeText(UpdateStoreActivity.this, "تم تحديث معلومات المتجر", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);



                            } else {

                                animationView.setVisibility(View.GONE);
                                Toast.makeText(UpdateStoreActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }




            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(UpdateStoreActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateStoreActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
                Toast.makeText(UpdateStoreActivity.this,"Please accept the required permission", Toast.LENGTH_SHORT).show();

            else
                ActivityCompat.requestPermissions(UpdateStoreActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
        } else
            openGallery();

    }

    private void openGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
        intentGallery.setType("image/*");
        intentGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(intentGallery, REQUESCODE);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR =getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            pickedImgUri = data.getData();
            imgStore.setImageURI(pickedImgUri);

        }
        else  if (requestCode == 2 ) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    String address = data.getStringExtra(MapUtility.ADDRESS);
                    double selectedLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    double selectedLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);

                    latitude=selectedLatitude;
                    longitude=selectedLongitude;

                    addressStr=address;
                    LocationStore.setText(address);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}