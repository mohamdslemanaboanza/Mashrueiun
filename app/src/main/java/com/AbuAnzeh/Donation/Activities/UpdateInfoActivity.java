package com.AbuAnzeh.Donation.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.Donation.Adpter.custam_adpter_text;
import com.AbuAnzeh.Donation.HelpClasses.CheckConnecationInternet;
import com.AbuAnzeh.Donation.Models.UserInfo;
import com.AbuAnzeh.Donation.Models.custm_item_text;
import com.AbuAnzeh.Donation.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UpdateInfoActivity extends AppCompatActivity {

    private TextView header, title_name, title_phone, title_city;
    private ImageView img_user;
    private TextInputLayout textInputLayoutName, textInputLayoutPhone;
    private EditText name, phone_number;
    private Spinner city;
    private Button update;
    private DatabaseReference Donors;
    private StorageReference UserDonors;
    private FirebaseAuth firebaseAuth;
    private CheckConnecationInternet internet;
    private String full_name, phone_numbre, imgUri, CityOfUser, pass, email;
    private ArrayList<custm_item_text> custm_D;
    private String CitySpinner;
    private StorageTask mUploadTask;
    static int PReqCode = 1, REQUESCODE = 1;
    private Uri  pickedImgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        header = findViewById(R.id.header);
        img_user = findViewById(R.id.img_user);
        title_name = findViewById(R.id.title_name);
        phone_number = findViewById(R.id.phone_number);
        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        title_city = findViewById(R.id.title_city);
        textInputLayoutPhone = findViewById(R.id.textInputLayoutPhone);
        city = findViewById(R.id.city);
        update = findViewById(R.id.update);
        name = findViewById(R.id.name);
        title_phone = findViewById(R.id.title_phone);
        internet = new CheckConnecationInternet(UpdateInfoActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        Donors = FirebaseDatabase.getInstance().getReference("Donors");
        UserDonors = FirebaseStorage.getInstance().getReference().child("UserDonors");


        header.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        title_name.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        phone_number.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        textInputLayoutName.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        textInputLayoutPhone.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        textInputLayoutPhone.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        update.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        name.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        title_phone.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        title_city.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 22)
                    checkAndRequestForPermission();

                else
                    openGallery();
            }
        });

        custm_D = getCustmListP();
        custam_adpter_text ustamAdabterCustam_adpter_text = new custam_adpter_text(this, custm_D);
        if (city != null) {
            city.setAdapter(ustamAdabterCustam_adpter_text);
        }

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                custm_item_text itemText = (custm_item_text) parent.getSelectedItem();
                CitySpinner = itemText.getSpinnertext();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (internet.isConnected()) {
            final String id = firebaseAuth.getUid();
            Donors.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    full_name = dataSnapshot.child(id).child("name").getValue(String.class);
                    phone_numbre = dataSnapshot.child(id).child("phoneNumber").getValue(String.class);
                    imgUri = dataSnapshot.child(id).child("imgUri").getValue(String.class);
                    CityOfUser = dataSnapshot.child(id).child("city").getValue(String.class);
                    pass = dataSnapshot.child(id).child("pass").getValue(String.class);
                    email = dataSnapshot.child(id).child("userName").getValue(String.class);

                    if (CityOfUser.equals("عمان")) {
                        city.setSelection(1);
                    } else if (CityOfUser.equals("الزرقاء")) {
                        city.setSelection(2);
                    } else if (CityOfUser.equals("الطفيلة")) {
                        city.setSelection(3);
                    } else if (CityOfUser.equals("الكرك")) {
                        city.setSelection(4);
                    } else if (CityOfUser.equals("اربد")) {
                        city.setSelection(5);
                    } else if (CityOfUser.equals("معان")) {
                        city.setSelection(6);
                    } else if (CityOfUser.equals("العقبة")) {
                        city.setSelection(7);
                    } else if (CityOfUser.equals("السلط")) {
                        city.setSelection(8);
                    } else if (CityOfUser.equals("البحر الميت")) {
                        city.setSelection(9);
                    } else if (CityOfUser.equals("مادبا")) {
                        city.setSelection(11);
                    } else if (CityOfUser.equals("جرش")) {
                        city.setSelection(12);
                    } else if (CityOfUser.equals("عجلون")) {
                        city.setSelection(13);
                    } else if (CityOfUser.equals("المفرق")) {
                        city.setSelection(14);
                    }
                    Picasso.with(UpdateInfoActivity.this).load(imgUri).placeholder(R.drawable.logo).error(R.drawable.logo).into(img_user);
                    name.setText(full_name);
                    phone_number.setText(phone_numbre);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(UpdateInfoActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(UpdateInfoActivity.this, "تحقق من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
        }



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(name.getText()))
                {
                    textInputLayoutName.setHelperText("مطلوب");
                    return;
                }else if(TextUtils.isEmpty(phone_number.getText()))
                {
                    textInputLayoutPhone.setHelperText("مطلوب");
                    return;
                }else if(phone_number.getText().toString().length()>10 && phone_number.getText().toString().length()<10)
                {
                    textInputLayoutPhone.setHelperText("رقم الهاتف غير صالح");
                    return;
                }else if (CitySpinner.equals("اختيار المحافظة"))
                {
                    Toast.makeText(UpdateInfoActivity.this, "يجب أختيار المحافظة", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (internet.isConnected()) {

                    if(pickedImgUri != null)
                    {
                        final StorageReference fileReference = UserDonors.child(System.currentTimeMillis()
                                + "." + getFileExtension(pickedImgUri));


                        mUploadTask = fileReference.putFile(pickedImgUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {


                                                UserInfo info = new UserInfo(firebaseAuth.getUid(), CitySpinner, phone_number.getText().toString(), uri.toString(), pass, email, name.getText().toString());
                                                Donors.child(firebaseAuth.getUid()).setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(UpdateInfoActivity.this, "تم تحديث البيانات", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(UpdateInfoActivity.this, "حدث خطأ ما" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });


                                    }
                                });

                    }
                    else
                        {
                            UserInfo info = new UserInfo(firebaseAuth.getUid(), CitySpinner, phone_number.getText().toString(), imgUri, pass, email, name.getText().toString());
                            Donors.child(firebaseAuth.getUid()).setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UpdateInfoActivity.this, "تم تحديث البيانات", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(UpdateInfoActivity.this, "حدث خطأ ما" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                } else {
                    Toast.makeText(UpdateInfoActivity.this, "تحقق من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private ArrayList<custm_item_text> getCustmListP() {
        custm_D = new ArrayList<>();
        custm_D.add(new custm_item_text("اختيار المحافظة"));
        custm_D.add(new custm_item_text("عمان"));
        custm_D.add(new custm_item_text("الزرقاء"));
        custm_D.add(new custm_item_text("الطفيلة"));
        custm_D.add(new custm_item_text("الكرك"));
        custm_D.add(new custm_item_text("اربد"));
        custm_D.add(new custm_item_text("معان"));
        custm_D.add(new custm_item_text("العقبة"));
        custm_D.add(new custm_item_text("السلط"));
        custm_D.add(new custm_item_text("البحر الميت"));
        custm_D.add(new custm_item_text("مادبا"));
        custm_D.add(new custm_item_text("جرش"));
        custm_D.add(new custm_item_text("عجلون"));
        custm_D.add(new custm_item_text("المفرق"));

        return custm_D;


    }

    private void openGallery() {

        //TODO: open gallery intent and wait for user to pick an image !

        Intent intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
        intentGallery.setType("image/*");
        intentGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(intentGallery, REQUESCODE);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(UpdateInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
                Toast.makeText(UpdateInfoActivity.this, "Please accept the required permission", Toast.LENGTH_SHORT).show();

            else
                ActivityCompat.requestPermissions(UpdateInfoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
        } else
            openGallery();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            pickedImgUri = data.getData();
            img_user.setImageURI(pickedImgUri);

        }
    }
}