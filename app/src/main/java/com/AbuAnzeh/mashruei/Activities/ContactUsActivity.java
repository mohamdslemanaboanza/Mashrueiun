package com.AbuAnzeh.mashruei.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.mashruei.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactUsActivity extends AppCompatActivity {


    //Declare
    private Button send;
    private EditText text;
    private TextView header;
    private ImageView what,email,link;
    private DatabaseReference massege,databaseUsers;
    private TextInputLayout textInputLayoutMessage;
    private String fname,lname,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        //init
        text=findViewById(R.id.text);
        send=findViewById(R.id.send);
        header=findViewById(R.id.header);
        what=findViewById(R.id.what);
        link=findViewById(R.id.link);
        email=findViewById(R.id.email);
        textInputLayoutMessage=findViewById(R.id.textInputLayoutMessage);
        massege= FirebaseDatabase.getInstance().getReference("Messages");

        SharedPreferences preferencesInfo = getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        String userId = preferencesInfo.getString("UserId", "");
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fname = dataSnapshot.child("fname").getValue(String.class);
                lname = dataSnapshot.child("lname").getValue(String.class);
                phone = dataSnapshot.child("phone").getValue(String.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        what.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String smsNumber = "962789045025";
                try {
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "السلام عليكم !");
                    sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                } catch(Exception e) {
                    Toast.makeText(ContactUsActivity.this, "Error\n" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String   Email="mabnoaza@gmail.com";
                String[] a=Email.split(",");
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL,a);
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent,"اختر التطبيق"));

            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.linkedin.com/in/mohemmad-abuanzeh-920815148/"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });


        //تغيير الخط
        header.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        send.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        text.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));

        //الرجوع الى الخلف
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(text.getText()))
                {
                    textInputLayoutMessage.setBoxStrokeColor(Color.RED);
                    text.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    text.requestFocus();
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }else
                    {
                        String fullName="الاسم : "+fname+" "+lname;
                        String key=massege.push().getKey();
                        massege.child(key).setValue(text.getText().toString()+"\n"+fullName+"\n"+"رقم الهاتف : "+phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ContactUsActivity.this, "تم أرسال الرسالة شكرا لك", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
