package com.AbuAnzeh.mashruei.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.mashruei.Adpter.custam_adpter_text_location;
import com.AbuAnzeh.mashruei.Models.UserModel;
import com.AbuAnzeh.mashruei.Models.custm_item_text;
import com.AbuAnzeh.mashruei.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateInfoActivity extends AppCompatActivity {

    private ArrayList<custm_item_text> Governesses;
    private Spinner GovernessesSpinner;
    private TextView header,createAt;
    private TextInputLayout textInputLayoutFname,textInputLayoutLname;
    private EditText fname,lname,PhoneNumber;
    private ImageView img_user;
    private Button updateInfo;
    static int PReqCode = 1, REQUESCODE = 1;
    private Uri pickedImgUri=null;
    private String cityStr;
    private DatabaseReference databaseUsers;
    private com.airbnb.lottie.LottieAnimationView animationView ;
    private StorageTask mUploadTask;
    private StorageReference ImagesUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        SharedPreferences preferencesInfo = getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        String userId = preferencesInfo.getString("UserId", "");

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        ImagesUsers = FirebaseStorage.getInstance().getReference().child("ImagesUsers");

        header=findViewById(R.id.header);
        fname=findViewById(R.id.fname);
        PhoneNumber=findViewById(R.id.PhoneNumber);
        lname=findViewById(R.id.lname);
        animationView=findViewById(R.id.animationView);
        createAt=findViewById(R.id.textView8);
        img_user=findViewById(R.id.imageView3);
        updateInfo=findViewById(R.id.updateInfo);
        GovernessesSpinner=findViewById(R.id.GovernessesSpinner);
        textInputLayoutFname=findViewById(R.id.textInputLayoutFname);
        textInputLayoutLname=findViewById(R.id.textInputLayoutLname);



        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        final String imgUri=getIntent().getExtras().getString("imgUri");
        Picasso.get().load(imgUri).placeholder(R.drawable.logo).into(img_user);


        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 22)
                    checkAndRequestForPermission();

                else
                    openGallery();
            }
        });



        final String authKey=getIntent().getExtras().getString("authKey");


        String fnameStr=getIntent().getExtras().getString("fname");
        fname.setText(fnameStr);

        String lnameStr=getIntent().getExtras().getString("lname");
        lname.setText(lnameStr);


        final String phone=getIntent().getExtras().getString("phone");
        PhoneNumber.setText(phone);

        String createAtStr=getIntent().getExtras().getString("createAt");
        createAt.setText("اخر تحديث "+createAtStr);



        String city=getIntent().getExtras().getString("city");
        final String pass=getIntent().getExtras().getString("pass");
        final String typeUser=getIntent().getExtras().getString("typeUser");


        Governesses = GetGovernorate();
        custam_adpter_text_location ustamAdabterCustam_adpter_text=new custam_adpter_text_location(this, Governesses);
        if(GovernessesSpinner != null)
        {
            GovernessesSpinner.setAdapter(ustamAdabterCustam_adpter_text);
        }

        GovernessesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                custm_item_text itemText = (custm_item_text) adapterView.getSelectedItem();
                cityStr = itemText.getSpinnertext();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (city.equals("عمان")) {
            GovernessesSpinner.setSelection(0);
        } else if (city.equals("الزرقاء")) {
            GovernessesSpinner.setSelection(1);
        } else if (city.equals("الطفيلة")) {
            GovernessesSpinner.setSelection(2);
        } else if (city.equals("الكرك")) {
            GovernessesSpinner.setSelection(3);
        } else if (city.equals("اربد")) {
            GovernessesSpinner.setSelection(4);
        } else if (city.equals("معان")) {
            GovernessesSpinner.setSelection(5);
        } else if (city.equals("العقبة")) {
            GovernessesSpinner.setSelection(6);
        } else if (city.equals("السلط")) {
            GovernessesSpinner.setSelection(7);
        }  else if (city.equals("مادبا")) {
            GovernessesSpinner.setSelection(8);
        } else if (city.equals("جرش")) {
            GovernessesSpinner.setSelection(9);
        } else if (city.equals("عجلون")) {
            GovernessesSpinner.setSelection(10);
        } else if (city.equals("المفرق")) {
            GovernessesSpinner.setSelection(11);
        }
        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                final String formattedDate = df.format(c);

                if (TextUtils.isEmpty(fname.getText().toString())) {
                    textInputLayoutFname.setBoxStrokeColor(Color.RED);
                    fname.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(lname.getText().toString())) {
                    textInputLayoutLname.setBoxStrokeColor(Color.RED);
                    lname.requestFocus();
                    return;
                }

               if (pickedImgUri == null){

                   animationView.setVisibility(View.VISIBLE);

                   UserModel user =new UserModel(fname.getText().toString(),lname.getText().toString(),PhoneNumber.getText().toString(),pass,cityStr,imgUri,authKey,formattedDate,"");

                   databaseUsers.child(authKey).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){
                               onBackPressed();
                               Toast.makeText(UpdateInfoActivity.this, "تم التحديث المعلومات بنجاح", Toast.LENGTH_SHORT).show();

                           }else {

                               Toast.makeText(UpdateInfoActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();


                           }
                           animationView.setVisibility(View.INVISIBLE);
                       }
                   });


               }else {
                   animationView.setVisibility(View.VISIBLE);
                   final StorageReference fileReference = ImagesUsers.child(System.currentTimeMillis()
                           + "." + getFileExtension(pickedImgUri));

                   mUploadTask = fileReference.putFile(pickedImgUri)
                           .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                               @Override
                               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                   fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                           UserModel user =new UserModel(fname.getText().toString(),lname.getText().toString(),PhoneNumber.getText().toString(),pass,cityStr,uri.toString(),authKey,formattedDate,typeUser);
                                           databaseUsers.child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if (task.isSuccessful()){
                                                       onBackPressed();
                                                       Toast.makeText(UpdateInfoActivity.this, "تم التحديث المعلومات بنجاح", Toast.LENGTH_SHORT).show();

                                                   }else {

                                                       Toast.makeText(UpdateInfoActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                                   }
                                                   animationView.setVisibility(View.INVISIBLE);
                                               }
                                           });
                                       }
                                   });
                               }
                           });

               }

            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR =getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    //بضيف المدن
    private ArrayList<custm_item_text> GetGovernorate()
    {
        Governesses =new ArrayList<>();
        Governesses.add(new custm_item_text("عمان"));
        Governesses.add(new custm_item_text("الزرقاء"));
        Governesses.add(new custm_item_text("الطفيلة"));
        Governesses.add(new custm_item_text("الكرك"));
        Governesses.add(new custm_item_text("اربد"));
        Governesses.add(new custm_item_text("معان"));
        Governesses.add(new custm_item_text("العقبة"));
        Governesses.add(new custm_item_text("السلط"));
        Governesses.add(new custm_item_text("مادبا"));
        Governesses.add(new custm_item_text("جرش"));
        Governesses.add(new custm_item_text("عجلون"));
        Governesses.add(new custm_item_text("المفرق"));

        return Governesses;


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(UpdateInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
                Toast.makeText(UpdateInfoActivity.this,"Please accept the required permission", Toast.LENGTH_SHORT).show();

            else
                ActivityCompat.requestPermissions(UpdateInfoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
        } else
            openGallery();

    }

    private void openGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
        intentGallery.setType("image/*");
        intentGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(intentGallery, REQUESCODE);
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