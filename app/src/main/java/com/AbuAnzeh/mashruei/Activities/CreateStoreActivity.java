package com.AbuAnzeh.mashruei.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.AbuAnzeh.mashruei.Adpter.Adapter_choose_type_store;
import com.AbuAnzeh.mashruei.Models.StoreModel;
import com.AbuAnzeh.mashruei.Models.custm_item_text;
import com.AbuAnzeh.mashruei.R;
import com.google.android.gms.maps.model.LatLng;
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
import com.shivtechs.maplocationpicker.MapUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CreateStoreActivity extends AppCompatActivity {


    private TextView header;
    private ImageView imgStore;
    static int PReqCode = 1, REQUESCODE = 1;
    int PLACE_PICKER_REQUEST = 2;
    private Uri pickedImgUri = null;
    private TextInputLayout textInputLayoutNameStore, textInputLayoutDeskStore, textInputLayoutPhoneStore, textInputLayoutPhoneUser, textInputLayoutLocationStore, textInputLayoutWorkTime;
    private EditText nameStore, description_store, PhoneNumberStore, PhoneNumberUser, LocationStore, WorkTime;
    private Button create_store;
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    String addressStr, phone, typeStore;
    LinearLayout choose_location;
    private DatabaseReference databaseUsers, databaseStore;
    private StorageTask mUploadTask;
    private StorageReference ImagesStores;
    private String fname, lname, authKey, city, token;
    private com.airbnb.lottie.LottieAnimationView animationView;
    private Spinner TypesStoresSpinner;
    private ArrayList<custm_item_text> types;


    @Override
    protected void onResume() {
        super.onResume();
        try {
            LocationStore.setText(getAreaInfo(new LatLng(latitude, longitude)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getAreaInfo(LatLng latLng) throws IOException {

        Geocoder gcd = new Geocoder(this, Locale.forLanguageTag("ar"));
        List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);

        if (addresses.isEmpty()) {
            return "Location UnKnown";
        } else {
            return addresses.get(0).getAddressLine(0);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store);


        final SharedPreferences preferencesInfo = getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        final String userId = preferencesInfo.getString("UserId", "");

        databaseStore = FirebaseDatabase.getInstance().getReference("Stores");
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        ImagesStores = FirebaseStorage.getInstance().getReference().child("ImagesStores");


        header = findViewById(R.id.header);
        TypesStoresSpinner = findViewById(R.id.TypesStoresSpinner);
        choose_location = findViewById(R.id.choose_location);
        textInputLayoutDeskStore = findViewById(R.id.textInputLayoutDeskStore);
        create_store = findViewById(R.id.create_store);
        animationView = findViewById(R.id.animationView);
        textInputLayoutNameStore = findViewById(R.id.textInputLayoutNameStore);
        textInputLayoutPhoneStore = findViewById(R.id.textInputLayoutPhoneStore);
        textInputLayoutWorkTime = findViewById(R.id.textInputLayoutWorkTime);
        nameStore = findViewById(R.id.nameStore);
        imgStore = findViewById(R.id.imgStore);
        PhoneNumberStore = findViewById(R.id.PhoneNumberStore);
        description_store = findViewById(R.id.description_store);
        textInputLayoutLocationStore = findViewById(R.id.textInputLayoutLocationStore);
        LocationStore = findViewById(R.id.LocationStore);
        WorkTime = findViewById(R.id.WorkTime);


        types = getTypes();
        Adapter_choose_type_store ustamAdabterCustam_adpter_text = new Adapter_choose_type_store(this, types);
        if (TypesStoresSpinner != null) {
            TypesStoresSpinner.setAdapter(ustamAdabterCustam_adpter_text);
        }

        TypesStoresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                custm_item_text itemText = (custm_item_text) adapterView.getSelectedItem();
                typeStore = itemText.getSpinnertext();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                phone = dataSnapshot.child("phone").getValue(String.class);
                fname = dataSnapshot.child("fname").getValue(String.class);
                lname = dataSnapshot.child("lname").getValue(String.class);
                city = dataSnapshot.child("city").getValue(String.class);
                authKey = dataSnapshot.child("authKey").getValue(String.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        MapUtility.apiKey = getResources().getString(R.string.google_api_key);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        choose_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(CreateStoreActivity.this, LocationPickerActivity.class);
                startActivity(i);
            }
        });

        create_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(nameStore.getText().toString())) {
                    textInputLayoutNameStore.setBoxStrokeColor(Color.RED);
                    nameStore.requestFocus();
                    showSoftKeyBoard(nameStore);
                    return;
                } else if (TextUtils.isEmpty(description_store.getText().toString())) {
                    textInputLayoutDeskStore.setBoxStrokeColor(Color.RED);
                    description_store.requestFocus();
                    showSoftKeyBoard(description_store);
                    return;
                } else if (TextUtils.isEmpty(PhoneNumberStore.getText().toString())) {
                    textInputLayoutPhoneStore.setBoxStrokeColor(Color.RED);
                    PhoneNumberStore.requestFocus();
                    showSoftKeyBoard(PhoneNumberStore);
                    return;
                } else if (PhoneNumberStore.getText().length() > 10 || PhoneNumberStore.getText().length() < 10) {
                    textInputLayoutPhoneStore.setError("الرقم غير صحيح");
                    PhoneNumberStore.requestFocus();
                    showSoftKeyBoard(PhoneNumberStore);
                    return;
                } else if (TextUtils.isEmpty(LocationStore.getText().toString())) {
                    textInputLayoutLocationStore.setBoxStrokeColor(Color.RED);
                    LocationStore.requestFocus();
                    showSoftKeyBoard(LocationStore);
                    return;
                } else if (TextUtils.isEmpty(WorkTime.getText().toString())) {
                    textInputLayoutWorkTime.setBoxStrokeColor(Color.RED);
                    WorkTime.requestFocus();
                    showSoftKeyBoard(WorkTime);
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


                                            StoreModel store = new StoreModel(nameStore.getText().toString()
                                                    , description_store.getText().toString(), PhoneNumberStore.getText().toString(),
                                                    WorkTime.getText().toString(), 0, uri.toString(), city, fname + " " + lname, phone, authKey, false, latitude, longitude, LocationStore.getText().toString(), typeStore, token);

                                            if (typeStore.equals("طعام")) {
                                                typeStore = "Food";
                                            } else if (typeStore.equals("حلويات")) {
                                                typeStore = "Candy";
                                            } else if (typeStore.equals("ملابس")) {
                                                typeStore = "Clothes";
                                            } else if (typeStore.equals("البان و اجبان")) {
                                                typeStore = "Dairies";
                                            } else if (typeStore.equals("حرف يدوية")) {
                                                typeStore = "Handicraft";
                                            } else if (typeStore.equals("اثاثا منزلي")) {
                                                typeStore = "HomeFurnishings";
                                            } else if (typeStore.equals("اخرى")) {
                                                typeStore = "Other";
                                            }


                                            SharedPreferences preferences = getSharedPreferences("TypeStore", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("TypeStore", typeStore);
                                            editor.putString("nameStore", nameStore.getText().toString());
                                            editor.putString("locationStore", LocationStore.getText().toString());
                                            editor.putString("imageStore", uri.toString());
                                            editor.putString("idStore", userId);
                                            editor.apply();


                                            databaseStore.child(typeStore).child(userId).setValue(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        animationView.setVisibility(View.GONE);


                                                        HashMap<String, Object> hashMap = new HashMap<>();
                                                        hashMap.put("typeUser", typeStore);
                                                        databaseUsers.updateChildren(hashMap);
                                                        final Dialog dialog = new Dialog(CreateStoreActivity.this);
                                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                        dialog.setContentView(R.layout.dilog_success);
                                                        dialog.setCancelable(true);

                                                        SharedPreferences InfoStore = getSharedPreferences("InfoStore", MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = InfoStore.edit();
                                                        editor.putBoolean("Created", true);
                                                        editor.apply();


                                                        Window window = dialog.getWindow();
                                                        WindowManager.LayoutParams wlp = window.getAttributes();

                                                        wlp.gravity = Gravity.CENTER;
                                                        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                                                        window.setAttributes(wlp);
                                                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                                        TextView skip = dialog.findViewById(R.id.notNow);
                                                        TextView hint = dialog.findViewById(R.id.hint);
                                                        Button go = dialog.findViewById(R.id.go);

                                                        go.setVisibility(View.GONE);
                                                        skip.setText("اغلاق");

                                                        skip.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                onBackPressed();
                                                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                                            }
                                                        });

                                                        hint.setText("تهانينا ! \n لقد تم انشاء متجر منزلي خاص بك سوف يتم مراجعة طلبك من قبل الفرق المختص وسيتم ارسال اشعار لك في حال تمت الموافقة على انشاء المتجر \n استعد لنشر منتجاتك المنزلية ...");

                                                        dialog.show();


                                                    } else {

                                                        animationView.setVisibility(View.GONE);
                                                        Toast.makeText(CreateStoreActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                                    }

                                                }
                                            });
                                        }
                                    });
                                }
                            });


                } else {
                    StoreModel store = new StoreModel(nameStore.getText().toString()
                            , description_store.getText().toString(), PhoneNumberStore.getText().toString(),
                            WorkTime.getText().toString(), 0, "https://firebasestorage.googleapis.com/v0/b/mashruei-1b951.appspot.com/o/logo.png?alt=media&token=5f9102d5-882c-4cbd-8fca-6ee2416d20e6", city, fname + " " + lname, phone, authKey, false, latitude, longitude, LocationStore.getText().toString()
                            , typeStore, token);

                    if (typeStore.equals("طعام")) {
                        typeStore = "Food";
                    } else if (typeStore.equals("حلويات")) {
                        typeStore = "Candy";
                    } else if (typeStore.equals("ملابس")) {
                        typeStore = "Clothes";
                    } else if (typeStore.equals("البان و اجبان")) {
                        typeStore = "Dairies";
                    } else if (typeStore.equals("حرف يدوية")) {
                        typeStore = "Handicraft";
                    } else if (typeStore.equals("اثاثا منزلي")) {
                        typeStore = "HomeFurnishings";
                    } else if (typeStore.equals("اخرى")) {
                        typeStore = "Other";
                    }

                    SharedPreferences preferences = getSharedPreferences("TypeStore", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("TypeStore", typeStore);
                    editor.putString("nameStore", nameStore.getText().toString());
                    editor.putString("locationStore", LocationStore.getText().toString());
                    editor.putString("imageStore", "https://firebasestorage.googleapis.com/v0/b/mashruei-1b951.appspot.com/o/logo.png?alt=media&token=5f9102d5-882c-4cbd-8fca-6ee2416d20e6");
                    editor.putString("idStore", userId);
                    editor.apply();

                    databaseStore.child(typeStore).child(userId).setValue(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                animationView.setVisibility(View.GONE);
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("typeUser", typeStore);
                                databaseUsers.updateChildren(hashMap);

                                final Dialog dialog = new Dialog(CreateStoreActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dilog_success);
                                dialog.setCancelable(true);

                                SharedPreferences InfoStore = getSharedPreferences("InfoStore", MODE_PRIVATE);
                                SharedPreferences.Editor editor = InfoStore.edit();
                                editor.putBoolean("Created", true);
                                editor.apply();
                                Window window = dialog.getWindow();
                                WindowManager.LayoutParams wlp = window.getAttributes();

                                wlp.gravity = Gravity.CENTER;
                                wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                                window.setAttributes(wlp);
                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                TextView skip = dialog.findViewById(R.id.notNow);
                                TextView hint = dialog.findViewById(R.id.hint);
                                Button go = dialog.findViewById(R.id.go);

                                go.setVisibility(View.GONE);
                                skip.setText("اغلاق");

                                skip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onBackPressed();
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    }
                                });

                                hint.setText("تهانينا ! \n لقد تم انشاء متجر منزلي خاص بك سوف يتم مراجعة طلبك من قبل الفرق المختص وسيتم ارسال اشعار لك في حال تمت الموافقة على انشاء المتجر \n استعد لنشر منتجاتك المنزلية ...");

                                dialog.show();


                            } else {

                                animationView.setVisibility(View.GONE);
                                Toast.makeText(CreateStoreActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }
        });


        imgStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestForPermission();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(CreateStoreActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(CreateStoreActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
                Toast.makeText(CreateStoreActivity.this, "Please accept the required permission", Toast.LENGTH_SHORT).show();

            else
                ActivityCompat.requestPermissions(CreateStoreActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
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
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            pickedImgUri = data.getData();
            imgStore.setImageURI(pickedImgUri);

        } else if (requestCode == 2) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    String address = data.getStringExtra(MapUtility.ADDRESS);
                    double selectedLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    double selectedLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);

                    latitude = selectedLatitude;
                    longitude = selectedLongitude;

                    addressStr = address;
                    LocationStore.setText(address);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private ArrayList<custm_item_text> getTypes() {
        types = new ArrayList<>();
        types.add(new custm_item_text("أختر نوع منتجاتك المنزلية", R.drawable.c));
        types.add(new custm_item_text("طعام", R.drawable.icone_food));
        types.add(new custm_item_text("حلويات", R.drawable.cake));
        types.add(new custm_item_text("البان و اجبان", R.drawable.dairy));
        types.add(new custm_item_text("ملابس", R.drawable.donateclothes));
        types.add(new custm_item_text("حرف يدوية", R.drawable.handicrafts));
        types.add(new custm_item_text("اثاثا منزلي", R.drawable.donatefurniture));
        types.add(new custm_item_text("أخرى", R.drawable.c));

        return types;


    }

    // code to show soft keyboard
    private void showSoftKeyBoard(EditText editBox) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        editBox.requestFocus();
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}