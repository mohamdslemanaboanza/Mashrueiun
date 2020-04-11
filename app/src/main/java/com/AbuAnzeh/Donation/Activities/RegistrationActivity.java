package com.AbuAnzeh.Donation.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.AbuAnzeh.Donation.Fragment.CreateAccountStepOne;
import com.AbuAnzeh.Donation.R;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        getSupportFragmentManager().beginTransaction().replace(R.id.contener_account, new CreateAccountStepOne())
                .commit();




    }
}
