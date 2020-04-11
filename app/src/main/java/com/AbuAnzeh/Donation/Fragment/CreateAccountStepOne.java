package com.AbuAnzeh.Donation.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.Donation.Activities.IntroActivity;
import com.AbuAnzeh.Donation.R;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class CreateAccountStepOne extends Fragment {

    private TextView header,hint;
    private Button next;
    private ImageView img;
    static int PReqCode = 1, REQUESCODE = 1;
    private Uri  pickedImgUri;
    SharedPreferences.Editor editor;



    public CreateAccountStepOne() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.step_one_account, container, false);
        header=view.findViewById(R.id.header);
        hint=view.findViewById(R.id.hint);
        next=view.findViewById(R.id.next);
        img=view.findViewById(R.id.img);
        header.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        hint.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        next.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));




        SharedPreferences preferences = getActivity().getSharedPreferences("detalisUsre", MODE_PRIVATE);

        img.setImageURI(Uri.parse(preferences.getString("imgAcc","")));

        if (preferences.getString("imgAcc","").isEmpty())
            img.setImageResource(R.drawable.man);




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contener_account, new CreateAccountStepTwo())
                        .commit();
            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), IntroActivity.class));
                getActivity().finish();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (Build.VERSION.SDK_INT >= 22)
                    checkAndRequestForPermission();

                else
                    openGallery();

            }
        });

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (Build.VERSION.SDK_INT >= 22)
                    checkAndRequestForPermission();

                else
                    openGallery();

            }
        });





        return view;
    }


    private void openGallery() {

        //TODO: open gallery intent and wait for user to pick an image !

        Intent intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
        intentGallery.setType("image/*");
        intentGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(intentGallery, REQUESCODE);
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE))
                Toast.makeText(getActivity(),"Please accept the required permission", Toast.LENGTH_SHORT).show();

            else
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
        } else
            openGallery();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            editor  = getActivity().getSharedPreferences("detalisUsre", MODE_PRIVATE).edit();
            pickedImgUri = data.getData();
            img.setImageURI(pickedImgUri);
            editor.putString("imgAcc",pickedImgUri.toString());
            editor.apply();



        }
    }
}
