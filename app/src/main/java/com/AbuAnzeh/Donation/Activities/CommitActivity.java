package com.AbuAnzeh.Donation.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.Donation.Adpter.AdpterCommit;
import com.AbuAnzeh.Donation.Adpter.AdpterFoodItem;
import com.AbuAnzeh.Donation.Models.ModelCommit;
import com.AbuAnzeh.Donation.Models.ModelDonation;
import com.AbuAnzeh.Donation.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommitActivity extends AppCompatActivity {

    private EditText commit;
    private TextView header,nothing;
    private ImageView Send,nothing_img;
    private DatabaseReference commitDatabase;
    private RecyclerView rec;
    private AdpterCommit adpterCommit;
    private List<ModelCommit> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit);
        header=findViewById(R.id.header);
        Send=findViewById(R.id.send);
        commit=findViewById(R.id.commit);
        rec=findViewById(R.id.rec);
        nothing_img=findViewById(R.id.nothing_img);
        nothing=findViewById(R.id.nothing);



        String idPost= getIntent().getExtras().getString("idPost");
        final String img= getIntent().getExtras().getString("img");
        Send.setEnabled(false);


        commitDatabase = FirebaseDatabase.getInstance().getReference("commitDatabase").child(idPost);

        header.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));
        commit.setTypeface(Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf"));

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mUploads = new ArrayList<>();


        rec.setLayoutManager(new LinearLayoutManager(this));


        items();



        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = commitDatabase.push().getKey();
                ModelCommit modelCommit=new ModelCommit(key,commit.getText().toString(),img);
                commitDatabase.child(key).setValue(modelCommit).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            items();
                            commit.setText("");
                        }
                    }
                });

            }
        });


        commit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length() == 0) {

                    Send.setAlpha((float) 0.5);
                    Send.setEnabled(false);
                } else {

                    Send.setAlpha((float) 0.9);
                    Send.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void items()

    {

        mUploads=new ArrayList<>();

        commitDatabase.keepSynced(true);
        commitDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    ModelCommit dataSetFire=dataSnapshot1.getValue(ModelCommit.class);
                    mUploads.add(dataSetFire);




                }
                adpterCommit  =new AdpterCommit(CommitActivity.this,mUploads);
                rec.setAdapter(adpterCommit);
                nothing.setVisibility(View.INVISIBLE);
                nothing_img.setVisibility(View.INVISIBLE);

                if(adpterCommit.getItemCount()==0)
                {
                    nothing.setVisibility(View.INVISIBLE);
                    nothing_img.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(CommitActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
