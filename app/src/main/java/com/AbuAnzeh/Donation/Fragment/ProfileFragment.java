package com.AbuAnzeh.Donation.Fragment;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.Donation.Activities.UpdateInfoActivity;
import com.AbuAnzeh.Donation.Adpter.AdpterFoodItem;
import com.AbuAnzeh.Donation.HelpClasses.CheckConnecationInternet;
import com.AbuAnzeh.Donation.Models.ModelDonation;
import com.AbuAnzeh.Donation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {




    //Declare
    private TextView name,phone_number,city,nothing;
    private DatabaseReference Donors;
    private FirebaseAuth firebaseAuth;
    private AdpterFoodItem adpterFoodItem;
    private List<ModelDonation> mUploads;
    private RecyclerView rec;
    private String full_name,phone_numbre,imgUri,CityOfUser;
    private ImageView img_user,nothing_img;
    private CheckConnecationInternet internet;
    private DatabaseReference MyDonations;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_profile, container, false);

        //init
        name=view.findViewById(R.id.name);
        phone_number=view.findViewById(R.id.phone_number);
        city=view.findViewById(R.id.city);
        img_user=view.findViewById(R.id.img_user);
        rec=view.findViewById(R.id.rec);
        nothing_img=view.findViewById(R.id.nothing_img);
        nothing=view.findViewById(R.id.nothing);


        firebaseAuth=FirebaseAuth.getInstance();
        internet=new CheckConnecationInternet(getActivity());
        Donors = FirebaseDatabase.getInstance().getReference("Donors");
        MyDonations = FirebaseDatabase.getInstance().getReference("MyDonations").child(firebaseAuth.getUid());

        mUploads = new ArrayList<>();


        rec.setLayoutManager(new LinearLayoutManager(getActivity()));



        MyDonations.keepSynced(true);
        MyDonations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    ModelDonation dataSetFire=dataSnapshot1.getValue(ModelDonation.class);
                    mUploads.add(dataSetFire);




                }
                adpterFoodItem =new AdpterFoodItem(getActivity(),mUploads);
                rec.setAdapter(adpterFoodItem);
                nothing.setVisibility(View.INVISIBLE);
                nothing_img.setVisibility(View.INVISIBLE);

                if(adpterFoodItem.getItemCount()==0)
                {
                    nothing.setVisibility(View.INVISIBLE);
                    nothing_img.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        //تغيير الخط
        city.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        phone_number.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        name.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        nothing.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));

        phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUI();
            }
        });

        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUI();
            }
        });

        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUI();
            }
        });

        if(firebaseAuth.getUid() == null)
        {
            Toast.makeText(getContext(), "لم يتم أنشاء حساب", Toast.LENGTH_SHORT).show();
        }else
            {
                if(internet.isConnected())
                {
                    final String id=firebaseAuth.getUid();
                    Donors.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            full_name=dataSnapshot.child(id).child("name").getValue(String.class);
                            phone_numbre=dataSnapshot.child(id).child("phoneNumber").getValue(String.class);
                            imgUri=dataSnapshot.child(id).child("imgUri").getValue(String.class);
                            CityOfUser=dataSnapshot.child(id).child("city").getValue(String.class);

                            Picasso.with(getActivity()).load(imgUri).placeholder(R.drawable.logo).error(R.drawable.logo).into(img_user);
                            name.setText(full_name);
                            phone_number.setText(phone_numbre);
                            city.setText(CityOfUser);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                    {
                        Toast.makeText(getActivity(), "تحقق من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
                    }
            }



        return view;
    }

    public void UpdateUI()
    {
        startActivity(new Intent(getActivity(),UpdateInfoActivity.class));
    }
}
