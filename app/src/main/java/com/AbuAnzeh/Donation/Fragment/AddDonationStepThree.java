package com.AbuAnzeh.Donation.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.Donation.HelpClasses.Constants;
import com.AbuAnzeh.Donation.Models.ModelDonation;
import com.AbuAnzeh.Donation.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class AddDonationStepThree extends Fragment  {


    //Declare
    private TextView hint,header;
    private Button publishing;
    double latitude;
    double longitude;
    int PLCEE_PICKER_REQUEST=1;
    private DatabaseReference Donations,Donors,MyDonations;
    private StorageReference DonationImges;
    private FirebaseAuth auth;
    private String Uid;
    private int NumberOfPathImg=0;
    private StorageTask mUploadTask;
    private Uri downloadUrl= Uri.parse(""), downloadUrl1= Uri.parse(""), downloadUrl3= Uri.parse("");
    private String full_name,phone_numbre,imgUri;

    public AddDonationStepThree() {
        // Required empty public constructor
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.step_three_donation, container, false);

        //init
        hint=view.findViewById(R.id.hint);
        header=view.findViewById(R.id.header);
        publishing=view.findViewById(R.id.publishing);

        Donors = FirebaseDatabase.getInstance().getReference("Donors");
        DonationImges = FirebaseStorage.getInstance().getReference().child("DonationImages");
        auth=FirebaseAuth.getInstance();
        Uid=auth.getUid();

        Donors.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                full_name=dataSnapshot.child(Uid).child("name").getValue(String.class);
                phone_numbre=dataSnapshot.child(Uid).child("phoneNumber").getValue(String.class);
                imgUri=dataSnapshot.child(Uid).child("imgUri").getValue(String.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        //تغير الخط
        publishing.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        hint.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        header.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()),PLCEE_PICKER_REQUEST);
                } catch (
                        GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (
                        GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }


        });

        SharedPreferences preferences = getActivity().getSharedPreferences("imges", MODE_PRIVATE);
        boolean yes =preferences.getBoolean("Type2", false);
        boolean no = preferences.getBoolean("Type1", true);


        if(yes==true )
        {
            Donations = FirebaseDatabase.getInstance().getReference("Food").child("Donations");
        }else
        {
            Donations = FirebaseDatabase.getInstance().getReference("NonFood").child("Donations");
        }

        MyDonations = FirebaseDatabase.getInstance().getReference("MyDonations").child(Uid);

        final String uri[] = new String[preferences.getInt("number", 0)];
        //عرض الصور الي رفعهم
        for (int i = 0; i < preferences.getInt("number", 0); i++) {
            uri[i] = preferences.getString("img" + i, "");
        }


        //بعرض البيانات الي كتبهم اذا رجع للصفحة
        final SharedPreferences DetailsPreferences = getActivity().getSharedPreferences("detalis", MODE_PRIVATE);
        final String  Details = DetailsPreferences.getString("Details", "");
        final String phone_number =DetailsPreferences.getString("Phone_number","");
        final String name_donation = DetailsPreferences.getString("name_donation","");
        final String city = DetailsPreferences.getString("City","");



        publishing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());






                for (int i = 0; i < uri.length; i++) {
                    if (uri[i] != null) {
                        final StorageReference fileReference = DonationImges.child(System.currentTimeMillis()
                                + "." + getFileExtension(Uri.fromFile(new File(uri[i]))));

                        final int finalI = i;
                        mUploadTask = fileReference.putFile(Uri.fromFile(new File(uri[i])))
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        final String key =Donations.push().getKey();

                                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uriImg) {
                                                //الحمدلله
                                                for (int i = 0; i < uri.length; i++) {
                                                    if (finalI == 0)
                                                        downloadUrl = uriImg;
                                                    if (finalI == 1)
                                                        downloadUrl1 = uriImg;
                                                    if (finalI == 2)
                                                        downloadUrl3 = uriImg;
                                                }
                                                NumberOfPathImg+=1;

                                                if (NumberOfPathImg == uri.length ) {

                                                    final ModelDonation donation=new ModelDonation(key,Uid,phone_number,full_name,imgUri,name_donation,currentDate,"متوفر",Details,downloadUrl.toString(),downloadUrl1.toString(),downloadUrl3.toString(),latitude+"",longitude+"",city);



                                                    Donations.child(key).setValue(donation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                            {
                                                                final SharedPreferences.Editor editor = getActivity().getSharedPreferences("imges", MODE_PRIVATE).edit();
                                                                final SharedPreferences.Editor DetailsPreferencesDelete = getActivity().getSharedPreferences("detalis", MODE_PRIVATE).edit();

                                                                editor.putString("img0","");
                                                                editor.putString("img1","");
                                                                editor.putString("img2","");
                                                                editor.putInt("number",0);
                                                                editor.apply();
                                                                DetailsPreferencesDelete.putString("Details","");
                                                                DetailsPreferencesDelete.putString("Phone_number","");
                                                                DetailsPreferencesDelete.putString("name_donation","");
                                                                DetailsPreferencesDelete.putString("City","");
                                                                DetailsPreferencesDelete.apply();

                                                                Toast.makeText(getActivity(), "تم نشر التبرع", Toast.LENGTH_SHORT).show();

                                                                MyDonations.child(key).setValue(donation);
                                                            }else {
                                                                Toast.makeText(getActivity(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                }


                                            }
                                        });


                                    }
                                });
                    } else {
                        Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
                    }
                }








            }
        });


        //الرجوع الى الخلف
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager=getActivity().getSupportFragmentManager();
                AddDonationStepTwo registerFragment=new AddDonationStepTwo();
                manager.beginTransaction()
                        .replace(R.id.contener_main,registerFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            }
        });
        return view;

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PLCEE_PICKER_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                Place place =PlacePicker.getPlace(data,getActivity());
                StringBuilder stringBuilder=new StringBuilder();
                latitude = Double.parseDouble(String.valueOf(place.getLatLng().latitude));
                longitude = Double.parseDouble(String.valueOf(place.getLatLng().longitude));



                //خطوط الطول و دوائر العرض
                stringBuilder.append("latitude : ");
                stringBuilder.append(latitude+"");
                stringBuilder.append("\n");
                stringBuilder.append("longitude");
                stringBuilder.append(longitude+"");
                stringBuilder.append("\n");

                hint.setText("تم اضافة الموقع "+"\n");




            }
        }
    }



    //داله للحصول على امتداد الصورة في حال تم اختيار الصورة من الاستيديو
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}


