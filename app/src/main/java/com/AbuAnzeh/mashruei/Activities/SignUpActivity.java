package com.AbuAnzeh.mashruei.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.AbuAnzeh.mashruei.Adpter.custam_adpter_text_location;
import com.AbuAnzeh.mashruei.HelperClass.EntreCodeBottomSheet;
import com.AbuAnzeh.mashruei.HelperClass.ModelBottomSheet;
import com.AbuAnzeh.mashruei.Models.custm_item_text;
import com.AbuAnzeh.mashruei.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {


    private TextView skip;
    private ArrayList<custm_item_text> Governesses;
    private Spinner GovernessesSpinner;
    private TextView create_account,login;
    private EditText fname,lname,PhoneNumber,pass,confirmPass;
    private TextInputLayout textInputLayoutFname,textInputLayoutLname,textInputLayoutPhoneNumber,textInputLayoutPass,textInputLayoutConfirmPass;
    private String city="عمان";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        lname=findViewById(R.id.lname);
        confirmPass=findViewById(R.id.confirmPass);
        pass=findViewById(R.id.pass);
        fname=findViewById(R.id.fname);
        PhoneNumber=findViewById(R.id.PhoneNumber);
        login=findViewById(R.id.login);
        create_account=findViewById(R.id.create_account);
        textInputLayoutLname=findViewById(R.id.textInputLayoutLname);
        textInputLayoutConfirmPass=findViewById(R.id.textInputLayoutConfirmPass);
        textInputLayoutPass=findViewById(R.id.textInputLayoutPass);
        textInputLayoutPhoneNumber=findViewById(R.id.textInputLayoutPhoneNumber);
        skip=findViewById(R.id.skip);
        GovernessesSpinner=findViewById(R.id.GovernessesSpinner);
        textInputLayoutFname=findViewById(R.id.textInputLayoutFname);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LogInActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });




        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(fname.getText().toString())) {
                    textInputLayoutFname.setBoxStrokeColor(Color.RED);
                    fname.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(lname.getText().toString())) {
                    textInputLayoutLname.setBoxStrokeColor(Color.RED);
                    lname.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(PhoneNumber.getText().toString())) {
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
                }else if (pass.getText().length() < 8 ) {
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

                SharedPreferences preferencesInfo = getSharedPreferences("InfoUser",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencesInfo.edit();
                editor.putString("fname",fname.getText().toString());
                editor.putString("lname",lname.getText().toString());
                editor.putString("phone",PhoneNumber.getText().toString());
                editor.putString("pass",pass.getText().toString());
                editor.putString("city",city);
                editor.apply();

                EntreCodeBottomSheet bottomSheet = new EntreCodeBottomSheet();
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");




            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,DeskAppActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

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
                city = itemText.getSpinnertext();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
        Governesses.add(new custm_item_text("البحر الميت"));
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
}