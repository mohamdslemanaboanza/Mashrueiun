package com.AbuAnzeh.mashruei.HelperClass;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.AbuAnzeh.mashruei.Activities.DeskAppActivity;
import com.AbuAnzeh.mashruei.Activities.MainActivity;
import com.AbuAnzeh.mashruei.Models.UserModel;
import com.AbuAnzeh.mashruei.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class EntreCodeBottomSheet extends BottomSheetDialogFragment {


    private EditText numOne, numTwo, numThree, numFoure, numFive, numSex;
    private TextView confirmation_code, PhoneNumber, resMessage;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseUsers;
    private String verificationId, code;

    private String fname;
    private String lname;
    private String phone;
    private String pass;
    private String city;
    private String imgUri;
    private com.airbnb.lottie.LottieAnimationView animationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.enter_code, container, false);

        mAuth = FirebaseAuth.getInstance();
        confirmation_code = v.findViewById(R.id.confirmation_code);
        PhoneNumber = v.findViewById(R.id.PhoneNumber);
        resMessage = v.findViewById(R.id.resMessage);
        numOne = v.findViewById(R.id.numOne);
        numTwo = v.findViewById(R.id.numTwo);
        numThree = v.findViewById(R.id.numThree);
        numFoure = v.findViewById(R.id.numFour);
        numFive = v.findViewById(R.id.numFive);
        numSex = v.findViewById(R.id.numSex);
        animationView = v.findViewById(R.id.animationView);

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");


        SharedPreferences preferencesInfo = getActivity().getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        fname = preferencesInfo.getString("fname", "");
        lname = preferencesInfo.getString("lname", "");
        phone = preferencesInfo.getString("phone", "");
        pass = preferencesInfo.getString("pass", "");
        city = preferencesInfo.getString("city", "");
        imgUri = preferencesInfo.getString("imgUri", "https://firebasestorage.googleapis.com/v0/b/mashruei-1b951.appspot.com/o/logo.png?alt=media&token=5f9102d5-882c-4cbd-8fca-6ee2416d20e6");
        PhoneNumber.setText("الرجاء ادخال رمز التحقق الذي تم ارساله على الرقم : " + "+962" + phone.substring(1));

        sendVerificationCode("+962" + phone.substring(1));
        confirmation_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCode();
                animationView.setVisibility(View.VISIBLE);
                animationView.playAnimation();


            }
        });

        resMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode("+962" + phone.substring(1));
            }
        });

        numOne.requestFocus();
        numOne.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    numTwo.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        numTwo.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    numThree.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });
        numThree.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    numFoure.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });

        numFoure.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    numFive.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });

        numFive.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {

                    if (s.length() == 1) {
                        numSex.requestFocus();
                    }


                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });

        numSex.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {

                    if (s.length() == 1) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }


                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });


        return v;
    }

    private void sendVerificationCode(String Number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(Number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(requireActivity())
                        .setCallbacks(mCallBack)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            Toast.makeText(getActivity(), "تم ارسال رمز التحقق", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            code = phoneAuthCredential.getSmsCode();

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }
    };

    private void verifyCode() {


        String codeFromUser = numOne.getText().toString() + numTwo.getText().toString() + numThree.getText().toString() + numFoure.getText().toString() + numFive.getText().toString() + numSex.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, codeFromUser);
        sginInwithcredential(credential);


    }

    private void sginInwithcredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);

                    String key = mAuth.getUid();

                    UserModel user = new UserModel(fname, lname, phone, pass, city, imgUri, mAuth.getUid(), formattedDate, "");

                    databaseUsers.child(key).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            animationView.setVisibility(View.INVISIBLE);

                            SharedPreferences preferencesInfo = getActivity().getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferencesInfo.edit();
                            editor.putString("UserId", mAuth.getUid());
                            editor.apply();

                            final Dialog dialog = new Dialog(getActivity());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dilog_success);
                            dialog.setCancelable(true);

                            Window window = dialog.getWindow();
                            WindowManager.LayoutParams wlp = window.getAttributes();

                            wlp.gravity = Gravity.CENTER;
                            wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                            window.setAttributes(wlp);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            TextView skip = dialog.findViewById(R.id.notNow);
                            Button go = dialog.findViewById(R.id.go);


                            skip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SharedPreferences preferencesInfo = getActivity().getSharedPreferences("LoginOrSign", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferencesInfo.edit();
                                    editor.putBoolean("success", true);
                                    editor.apply();
                                    startActivity(new Intent(getActivity(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    getActivity().finish();
                                }
                            });

                            go.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SharedPreferences preferencesInfo = getActivity().getSharedPreferences("LoginOrSign", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferencesInfo.edit();
                                    editor.putBoolean("success", true);
                                    editor.apply();
                                    startActivity(new Intent(getActivity(), DeskAppActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    getActivity().finish();

                                }
                            });


                            dialog.show();
                        }
                    });


                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(getActivity(), "الرمز المدخل خاطئ", Toast.LENGTH_SHORT).show();
                        animationView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

}