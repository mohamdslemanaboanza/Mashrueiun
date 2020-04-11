package com.AbuAnzeh.Donation.Fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AbuAnzeh.Donation.Adpter.AdpterFoodItem;
import com.AbuAnzeh.Donation.Adpter.AdpterNews;
import com.AbuAnzeh.Donation.Models.ModelDonation;
import com.AbuAnzeh.Donation.Models.News;
import com.AbuAnzeh.Donation.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView rec;
    EditText ser;
    RecyclerView recyclerView;
    DatabaseReference Donations;
    private List<ModelDonation> mUploads;
    AdpterFoodItem adpterFoodItem;
    Animation l , r;
    ProgressBar progressBar;
    private ArrayList<News> news_tape;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rec = view. findViewById(R.id.myRecyclerView);
        ser = view. findViewById(R.id.ser);
        progressBar = view. findViewById(R.id.progressBar);
        Donations = FirebaseDatabase.getInstance().getReference("Food").child("Donations");

        news_tape=new ArrayList<>();



        mUploads = new ArrayList<>();




        Donations.keepSynced(true);
        Donations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    ModelDonation dataSetFire=dataSnapshot1.getValue(ModelDonation.class);
                    mUploads.add(dataSetFire);




                }
                adpterFoodItem =new AdpterFoodItem(getActivity(),mUploads);
                rec.setAdapter(adpterFoodItem);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        recyclerView=view.findViewById(R.id.RecyclerView);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec.setLayoutManager(new LinearLayoutManager(getActivity()));


        ser.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        final AnimationSet s = new AnimationSet(false);//false means don't share interpolators
        l=AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_right);
        r=AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_left);
        s.addAnimation(l);
        s.addAnimation(r);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        final AdpterNews adpterNews=new AdpterNews(getActivity(),news());
        recyclerView.setAdapter(adpterNews);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            int count=adpterNews.getItemCount();

            @Override
            public void run() {
                recyclerView.scrollToPosition(count);

                count-=1;
                recyclerView.startAnimation(r);






                handler.postDelayed(this, 7000);
            }
        }, 7000);








        return view;
    }




    private ArrayList<News> news()
    {
        news_tape=new ArrayList<>();


        News news=new News();
        news.setCommit("الله يجزيكم الخير");
        news_tape.add(news);


        news=new News();
        news.setCommit("تطبيق رائع");
        news_tape.add(news);


        news=new News();
        news.setCommit("شكرا الكم");
        news_tape.add(news);


        news=new News();
        news.setCommit("الله يجزيكم الخير");
        news_tape.add(news);






        return news_tape;
    }

}
