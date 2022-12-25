package com.AbuAnzeh.mashruei.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.AbuAnzeh.mashruei.Models.UserModel;
import com.AbuAnzeh.mashruei.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {


    TextView create_account,skip;
    Button logIn;
    private TextInputLayout textInputLayoutPass,textInputLayoutPhoneNumber;
    private EditText pass,PhoneNumber;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        create_account=findViewById(R.id.create_account);
        skip=findViewById(R.id.skip);
        logIn=findViewById(R.id.LogIn);
        textInputLayoutPass=findViewById(R.id.textInputLayoutPass);
        textInputLayoutPhoneNumber=findViewById(R.id.textInputLayoutPhoneNumber);
        pass=findViewById(R.id.Pass);
        PhoneNumber=findViewById(R.id.PhoneNumber);

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        SpannableString ss = new SpannableString(create_account.getText().toString());

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(PhoneNumber.getText().toString())) {
                    textInputLayoutPhoneNumber.setBoxStrokeColor(Color.RED);
                    PhoneNumber.requestFocus();
                    return;
                } else if (PhoneNumber.getText().length() > 10 || PhoneNumber.getText().length() < 10) {
                    textInputLayoutPhoneNumber.setError("الرقم غير صحيح");
                    return;
                } else if (TextUtils.isEmpty(pass.getText())) {
                    textInputLayoutPass.setBoxStrokeColor(Color.RED);
                    pass.requestFocus();
                    return;
                }


                databaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            UserModel userInfo= dataSnapshot1.getValue(UserModel.class);


                            if(userInfo.getPass().equals(pass.getText().toString()) && userInfo.getPhone().equals(PhoneNumber.getText().toString()))
                            {

                                SharedPreferences preferencesInfoLogin = getSharedPreferences("LoginOrSign",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorLogin = preferencesInfoLogin.edit();
                                editorLogin.putBoolean("success",true);
                                editorLogin.apply();

                                SharedPreferences TypeStoreSharedPreferences = getSharedPreferences("TypeStore", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorTypeStore= TypeStoreSharedPreferences.edit();
                                editorTypeStore.putString("TypeStore",userInfo.getTypeUser());
                                editorTypeStore.apply();

                                SharedPreferences preferencesInfo = getSharedPreferences("InfoUser",MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferencesInfo.edit();
                                editor.putString("UserId",userInfo.getAuthKey());
                                editor.apply();
                                startActivity(new Intent(LogInActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                return;
                            }




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this,MainActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });



        ForegroundColorSpan fcsRed = new ForegroundColorSpan(getColor(R.color.colorPrimaryDark));

        ss.setSpan(fcsRed, 15, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        create_account.setText(ss);

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this,SignUpActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}