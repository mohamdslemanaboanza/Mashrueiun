package com.AbuAnzeh.mashruei.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.AbuAnzeh.mashruei.R;
import com.squareup.picasso.Picasso;

public class DetailsPostActivity extends AppCompatActivity {


    ImageView imagePost;
    TextView namePost,deskPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_post);

        imagePost=findViewById(R.id.imagePost);
        namePost=findViewById(R.id.namePost);
        deskPost=findViewById(R.id.deskPost);


        String name=getIntent().getExtras().getString("name");





        String deck=getIntent().getExtras().getString("deck");
        String imgUri=getIntent().getExtras().getString("img");

        Picasso.get().load(imgUri).placeholder(R.drawable.logo).into(imagePost);
        namePost.setText(name);
        deskPost.setText(deck);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}