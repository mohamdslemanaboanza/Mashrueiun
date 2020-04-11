package com.AbuAnzeh.Donation.Fragment;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.Donation.Activities.MainActivity;
import com.AbuAnzeh.Donation.Adpter.custam_adpter_text;
import com.AbuAnzeh.Donation.Models.UserInfo;
import com.AbuAnzeh.Donation.Models.custm_item_text;
import com.AbuAnzeh.Donation.R;
import com.AbuAnzeh.Donation.HelpClasses.CheckConnecationInternet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountStepThree extends Fragment {


    private TextView t5, t6, header;
    private EditText phone;
    Spinner city;
    ArrayList<custm_item_text> custm_D;
    String CitySpinner;
    private Button create_account,check;
    FirebaseAuth firebaseAuth;
    DatabaseReference Donors;
    CheckConnecationInternet internet;
    private StorageTask mUploadTask;
    StorageReference UserDonors;
    SharedPreferences.Editor editor;


    private TextInputLayout textInputLayout1;

    public CreateAccountStepThree() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.step_three_account, container, false);

        editor = getActivity().getSharedPreferences("detalisUsre", MODE_PRIVATE).edit();

        t5 = view.findViewById(R.id.t5);
        t6 = view.findViewById(R.id.t6);
        header = view.findViewById(R.id.header);
        check = view.findViewById(R.id.check);
        phone = view.findViewById(R.id.phone_number);
        city = view.findViewById(R.id.city);
        create_account = view.findViewById(R.id.create_account);
        textInputLayout1 = view.findViewById(R.id.textInputLayout1);

        Donors = FirebaseDatabase.getInstance().getReference("Donors");
        UserDonors = FirebaseStorage.getInstance().getReference().child("UserDonors");
        internet = new CheckConnecationInternet(getActivity());


        check.setEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();



        t5.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        t6.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        header.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        phone.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        check.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));

        create_account.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));

        textInputLayout1.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));

        final SharedPreferences.Editor editor = getActivity().getSharedPreferences("CityPhone", MODE_PRIVATE).edit();
        SharedPreferences preferences = getActivity().getSharedPreferences("CityPhone", MODE_PRIVATE);


        phone.setText(preferences.getString("Phone", ""));

        int pos = preferences.getInt("CitySpinner", 0);
        city.setSelection(pos);


        custm_D = getCustmListP();
        custam_adpter_text ustamAdabterCustam_adpter_text = new custam_adpter_text(getActivity(), custm_D);
        if (city != null) {
            city.setAdapter(ustamAdabterCustam_adpter_text);
        }

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                custm_item_text itemText = (custm_item_text) parent.getSelectedItem();
                CitySpinner = itemText.getSpinnertext();
                editor.putInt("CitySpinner", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (TextUtils.isEmpty(phone.getText())) {
                    textInputLayout1.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                    textInputLayout1.setHelperText("مطلوب");
                    return;
                } else if (phone.getText().length() < 10 || phone.getText().length() > 10) {
                    textInputLayout1.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                    textInputLayout1.setHelperText("رقم الهاتف غير صالح");
                    return;
                } else if (CitySpinner.equals("اختيار المحافظة")) {
                    Snackbar.make(view, "يجب أختيار المحافظة", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }


                SharedPreferences preferences = getActivity().getSharedPreferences("detalisUsre", MODE_PRIVATE);
                final String Pass = preferences.getString("Pass", "");
                final String UserName = preferences.getString("NameUser", "");


                if (internet.isConnected()) {
                    firebaseAuth.createUserWithEmailAndPassword(UserName, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final FirebaseUser user = firebaseAuth.getCurrentUser();

                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "تم أرسال رابط التاكيد على البريد الالكتروني", Toast.LENGTH_SHORT).show();
                                            check.setEnabled(true);
                                            check.setVisibility(View.VISIBLE);

                                        } else {
                                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });





                            } else {

                                Snackbar.make(view, "اسم المستخدم موجود بالفعل استخدم اسم مستخدم آخر ", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    });
                } else {
                    Snackbar.make(view, "تحقق من اتصالك بالانترنت", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });




        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                CreateAccountStepTwo registerFragment = new CreateAccountStepTwo();
                manager.beginTransaction()
                        .replace(R.id.contener_account, registerFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            }
        });


        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editor.putString("Phone", charSequence.toString());
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Task<Void> user = firebaseAuth.getCurrentUser().reload();


                user.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       FirebaseUser   user_email = firebaseAuth.getCurrentUser();
                        boolean useremailveri = user_email.isEmailVerified();


                        final FirebaseUser user=firebaseAuth.getCurrentUser();


                        if (useremailveri)
                        {
                            SharedPreferences preferences = getActivity().getSharedPreferences("detalisUsre", MODE_PRIVATE);


                            final String City = CitySpinner;
                            final String PhoneNumber = phone.getText().toString();
                            final Uri imgUri = Uri.parse(preferences.getString("imgAcc", ""));
                            final String Pass = preferences.getString("Pass", "");
                            final String UserName = preferences.getString("NameUser", "");
                            final String Name = preferences.getString("Name", "");



                            final StorageReference fileReference = UserDonors.child(System.currentTimeMillis()
                                    + "." + getFileExtension(imgUri));


                            mUploadTask = fileReference.putFile(imgUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(final Uri uri) {

                                                    final UserInfo userInfo = new UserInfo(firebaseAuth.getUid(), City, PhoneNumber, uri.toString(), Pass, UserName, Name);


                                                    Donors.child(user.getUid()).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                editor.putString("Name", "");
                                                                editor.putString("imgAcc", "");
                                                                editor.putString("NameUser", "");
                                                                editor.putString("Pass", "");
                                                                editor.putString("Phone", "");
                                                                editor.putInt("CitySpinner", 0);
                                                                editor.apply();

                                                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("Success", Context.MODE_PRIVATE).edit();
                                                                editor.putBoolean("Success", true);
                                                                editor.apply();


                                                                startActivity(new Intent(getActivity(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                                                getActivity().finish();
                                                            } else {
                                                                Toast.makeText(getContext(), "حدث خطأ ما", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                }
                                            });


                                        }
                                    });


                        }else
                            {
                                Toast.makeText(getActivity(), "لم يتم التحقق من الحساب ", Toast.LENGTH_SHORT).show();
                            }

                    }
                });


            }
        });

        return view;
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

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


}
