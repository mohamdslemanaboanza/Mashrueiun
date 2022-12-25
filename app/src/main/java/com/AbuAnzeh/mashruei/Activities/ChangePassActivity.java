package com.AbuAnzeh.mashruei.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.mashruei.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ChangePassActivity extends AppCompatActivity {


    private TextView header;
    private EditText confirmPass,pass;
    private Button changePass;
    private TextInputLayout textInputLayoutConfirmPass,textInputLayoutPass;
    private DatabaseReference databaseUsers;
    private com.airbnb.lottie.LottieAnimationView animationView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);


        header=findViewById(R.id.header);
        pass=findViewById(R.id.pass);
        confirmPass=findViewById(R.id.confirmPass);
        changePass=findViewById(R.id.changePass);
        animationView=findViewById(R.id.animationView);
        textInputLayoutConfirmPass=findViewById(R.id.textInputLayoutConfirmPass);
        textInputLayoutPass=findViewById(R.id.textInputLayoutPass);



        databaseUsers = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getText().length() < 8 ) {
                    textInputLayoutPass.setError("كلمة المرور صغيرة جدا");
                    pass.requestFocus();
                    return;
                }else if (TextUtils.isEmpty(confirmPass.getText().toString())) {
                    textInputLayoutConfirmPass.setBoxStrokeColor(Color.RED);
                    confirmPass.requestFocus();
                    return;
                }else if (!confirmPass.getText().toString().equals(pass.getText().toString())) {
                    textInputLayoutConfirmPass.setBoxStrokeColor(Color.RED);
                    textInputLayoutPass.setBoxStrokeColor(Color.RED);
                    textInputLayoutConfirmPass.setError("كلمة المرور غير متطابقة");
                    return;
                }

                animationView.setVisibility(View.VISIBLE);

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("pass",pass.getText().toString());

                databaseUsers.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            onBackPressed();
                            Toast.makeText(ChangePassActivity.this,"تم تغيير كلمة المرور بنجاح", Toast.LENGTH_SHORT).show();
                        } else {
                            animationView.setVisibility(View.INVISIBLE);
                            Toast.makeText(ChangePassActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}