package com.AbuAnzeh.Donation.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.Donation.Models.UserInfo;
import com.AbuAnzeh.Donation.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {


    //Declare
    private TextView hint_user,hint_pass,forget_pass;
    private Button buttonFacebookLogin,login;
    private EditText email,pass;
    private TextInputLayout textInputLayoutEmail,textInputLayoutPass;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference Donors;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize
        buttonFacebookLogin=findViewById(R.id.buttonFacebookLogin);
        email=findViewById(R.id.user_name);
        pass=findViewById(R.id.pass);
        login=findViewById(R.id.login);
        textInputLayoutEmail=findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPass=findViewById(R.id.textInputLayoutPass);
        hint_user=findViewById(R.id.hint_user);
        hint_pass=findViewById(R.id.hint_pass);
        forget_pass=findViewById(R.id.forget_pass);
        firebaseAuth=FirebaseAuth.getInstance();
        Donors = FirebaseDatabase.getInstance().getReference("Donors");



        //تغيير نوع الخط
        pass.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        email.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        buttonFacebookLogin.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        login.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        hint_pass.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        hint_user.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        forget_pass.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));

        //خط تحت كلمة هل نسيت كلمة المرور
        forget_pass.setPaintFlags(forget_pass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);



        //اذا كبس على نسيت كلمة المرور
        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //اذا كان الايميل مش مكتوب بشكل صحيح
                if(!isValidEmailId(email.getText().toString().trim())) {

                    //بطلع الو ملاحظة
                    textInputLayoutEmail.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                    textInputLayoutEmail.setHelperText("البريدالالكتروني غير صالح");
                    return;

                }

                //هون بدنا نتاكد ان المستخدم اكد ايميلو من خلال الرابط الي بعتنها بالتسجيل (انشاء حساب)
                //اذا  اكد الايميل
                if(firebaseAuth.getCurrentUser().isEmailVerified()){
                    //ببعت رابط تغيير الباسوورد الى الايميل الي اكدو بصفحة التسجيل
                    firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //اذا تمت عملية ارسال رابط تغيير الباسوورد بنجاح
                            if(task.isSuccessful())
                            {

                                Toast.makeText(LoginActivity.this, "تم أرسال رابط اعادة تعيين كلمة المرور الى البريد الكتروني ", Toast.LENGTH_SHORT).show();

                            }
                            //اذا ما تمت العملية بنجاح
                            else
                            {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage()+"حدذ خطا ما ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                //اذا ما اكد الايميل الخاص فيه
                else
                    {
                        textInputLayoutEmail.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                        textInputLayoutEmail.setHelperText("لم يتم التحقق من البريد الالكتروني الخاص بك يرجى التاكد من البريد الالكتروني ");
                    }
            }
        });







        //اذا بلش كتابة بمحي الملاحظة تعت مطلوب الي بطلعها اذا كبس لوج ان وهو مش كاتب ايشي
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //بمحي الملاحظة
                textInputLayoutEmail.setHelperText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //اذا بلش كتابة الباسوورد بطلع الو حجم الي كتابو على 6
                textInputLayoutPass.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.b)));
                textInputLayoutPass.setHelperText(charSequence.toString().length()+"/6");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        //اذا كبس على زر تسجيل دخول
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //اذا كان الايميل او الباسوورد او اذا كان الايميل مش مكتوب صح او اذا كان الباس اقل من 6 بطلع الو ملاحظة
                if (TextUtils.isEmpty(email.getText())) {
                    textInputLayoutEmail.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                    textInputLayoutEmail.setHelperText("مطلوب");
                    return;
                } else if (pass.getText().toString().length() < 6) {
                    textInputLayoutPass.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                    textInputLayoutPass.setHelperText("يجب أن تتكون من 6 من أرقام");
                    return;

                } else if (!isValidEmailId(email.getText().toString().trim())) {
                    textInputLayoutEmail.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                    textInputLayoutEmail.setHelperText("البريدالالكتروني غير صالح");
                    return;


                }



                Donors.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            UserInfo userInfo= dataSnapshot1.getValue(com.AbuAnzeh.Donation.Models.UserInfo.class);


                            if(userInfo.getPass().equals(pass.getText().toString()) && userInfo.getUserName().equals(email.getText().toString()))
                            {

                                //بشيل الفرغات من الايميل اذا كتب فرغات اول ايشي او اخر ايشب=ي من الايميل
                                firebaseAuth.signInWithEmailAndPassword(email.getText().toString().replaceAll("\\s", ""), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        //اذا تمت عملية الدخول بنقلو الى الصفحة الرئيسية
                                        if (task.isSuccessful()) {
                                            updateUI();
                                        }

                                        //اذا المعلومات خطأ
                                        else {
                                            //اذا لم يتم تحقق من البريد الالكتروني
                                            textInputLayoutEmail.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                                            textInputLayoutEmail.setHelperText("لم يتم التحقق من البريد الالكتروني الخاص بك يرجى أدخال بريد الكتروني صالح الخاص بك");

                                        }
                                    }
                                });

                                return;
                            }else
                            {
                                Toast.makeText(LoginActivity.this, "المعلومات المدخلة خاطئة", Toast.LENGTH_SHORT).show();


                            }




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                }
        });


        //اذا كبس على تسجيل دخول باستخدام فيس بوك
        buttonFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //بجيب المستخدم الحالي
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                //اذا كان عامل انشاء حساب باستخدام فيس بوك
                if (currentUser != null) {
                    updateUI();


                }
                //اذا لم تقم بانشاء حساب
                else
                {
                    Toast.makeText(LoginActivity.this, "لم تقم بانشاء حساب ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    //اذا نجح بتسجيل دخول
    private void updateUI() {

        //اذا نجح بتسجيل دخول
        SharedPreferences.Editor editor = getSharedPreferences("Success", Context.MODE_PRIVATE).edit();
        editor.putBoolean("Success",true);
        editor.apply();

        //روح على الصفحة الرئيسية
        startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();

    }


    //التأكد من البريد الالكتروني مكتوب بشكل صحيح
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}
