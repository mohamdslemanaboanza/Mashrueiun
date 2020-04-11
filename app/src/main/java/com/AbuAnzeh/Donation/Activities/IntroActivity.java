package com.AbuAnzeh.Donation.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.Donation.Models.UserInfo;
import com.AbuAnzeh.Donation.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class IntroActivity extends AppCompatActivity {


    //Declare
    private Button login;
    private TextView label, label2, desk, AlreadyHaveAccount;
    private DatabaseReference Donors;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private Animation animation;
    private Button loginButton, create_account;
    private static final String TAG = "FACELOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.into_activity);

        //Initialize
        login = findViewById(R.id.login);
        label = findViewById(R.id.label);
        label2 = findViewById(R.id.label2);
        desk = findViewById(R.id.desk);
        AlreadyHaveAccount = findViewById(R.id.AlreadyHaveAccount);
        create_account = findViewById(R.id.create_account);
        loginButton = findViewById(R.id.buttonFacebookLogin);


        //animation
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);

        //بدء الايمنشن
        desk.startAnimation(animation);

        //تغيير نوع الاخطاء
        label2.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        create_account.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        loginButton.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        label.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        desk.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        AlreadyHaveAccount.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        login.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));

        //path الداتا بالفيربيس
        mAuth = FirebaseAuth.getInstance();
        Donors = FirebaseDatabase.getInstance().getReference("Donors");

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        //انشاء حساب
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this, RegistrationActivity.class));
            }
        });


        //اذا بدو يسجل دخول
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));


            }
        });


        //بنحط خط تحت كلمة اهدار
        SpannableString ss = new SpannableString(desk.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {

            }
        };
        ss.setSpan(clickableSpan, 16, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        desk.setText(ss);
        desk.setMovementMethod(LinkMovementMethod.getInstance());


        //لوجن ان باستخدام الفيسبوك
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginButton.setEnabled(false);
                LoginManager.getInstance().logInWithReadPermissions(IntroActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override //اذا نجح تسجيل دخول
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());

                    }

                    //اذا كنسل
                    @Override
                    public void onCancel() {


                    }

                    //اذا فشل
                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(IntroActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    //هون بدي اخذ البيانات اذا تم تسجيل دخول
    private void handleFacebookAccessToken(final AccessToken token) {


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            final FirebaseUser user = mAuth.getCurrentUser();


                            //بدي اخذ بيانات الفيس بوك
                            GraphRequest request = GraphRequest.newMeRequest(
                                    token,
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            // Application code
                                            try {
                                                Log.i("Response", response.toString());

                                                final String email = response.getJSONObject().getString("email");
                                                final String firstName = response.getJSONObject().getString("first_name");
                                                final String lastName = response.getJSONObject().getString("last_name");
                                                final String Uri = "https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?type=large";


                                                UserInfo info = new UserInfo(user.getUid(), "اضغط لأضافة المحافظة", "أضغط لأضافة رقم هاتف", Uri, " ", email, firstName + " " + lastName);


                                                //برفع البيانات للفيربيس
                                                Donors.child(user.getUid()).setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        //هون بعرف انو تم تسجيل دخول
                                                        SharedPreferences.Editor editor = getSharedPreferences("Success", Context.MODE_PRIVATE).edit();
                                                        editor.putBoolean("Success", true);
                                                        editor.apply();


                                                        startActivity(new Intent(IntroActivity.this, MainActivity.class));
                                                        finish();
                                                    }
                                                });


                                                //اذا صار خطا
                                            } catch (JSONException e) {
                                                Toast.makeText(IntroActivity.this, "error", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,email,first_name,last_name,picture");
                            request.setParameters(parameters);
                            request.executeAsync();
                            loginButton.setEnabled(true);


                            //اذا صار خطا
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(IntroActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();


                        }

                    }
                });
    }


}