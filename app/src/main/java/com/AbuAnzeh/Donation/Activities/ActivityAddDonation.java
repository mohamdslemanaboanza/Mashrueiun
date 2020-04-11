package com.AbuAnzeh.Donation.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.AbuAnzeh.Donation.Fragment.AddDonationStepOne;
import com.AbuAnzeh.Donation.R;

public class ActivityAddDonation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donation);



        getSupportFragmentManager().beginTransaction().replace(R.id.contener, new AddDonationStepOne())
                .commit();



    }
}
