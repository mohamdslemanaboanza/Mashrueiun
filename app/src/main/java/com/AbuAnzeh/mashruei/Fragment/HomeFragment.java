package com.AbuAnzeh.mashruei.Fragment;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.AbuAnzeh.mashruei.Activities.DetailsActivity;
import com.AbuAnzeh.mashruei.Activities.MyAccountActivity;
import com.AbuAnzeh.mashruei.Activities.NotificationActivity;
import com.AbuAnzeh.mashruei.Activities.SearchProductActivity;
import com.AbuAnzeh.mashruei.Activities.SignUpActivity;
import com.AbuAnzeh.mashruei.Adpter.AdapterCenter;
import com.AbuAnzeh.mashruei.Adpter.AdapterPost;
import com.AbuAnzeh.mashruei.Adpter.AdapterSectionTypes;
import com.AbuAnzeh.mashruei.Adpter.AdapterMostWanted;
import com.AbuAnzeh.mashruei.Models.DesignItemHome;
import com.AbuAnzeh.mashruei.Models.ModelCenter;
import com.AbuAnzeh.mashruei.Models.ModelPost;
import com.AbuAnzeh.mashruei.Models.StoreModel;
import com.AbuAnzeh.mashruei.R;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {




    private RecyclerView recyclerViewTypeDonations,RecyclerViewCharities,RecyclerViewMyPosts,RecyclerViewCenters;
    private ShimmerFrameLayout shimmerFrameLayout,shimmerFrameLayout2,shimmerFrameLayout3,shimmerFrameLayout4;
    private ImageView account;
    private GridLayout notification;
    private List<StoreModel> mUploads;
    private ArrayList<ModelPost> posts;
    private ArrayList<ModelCenter> centers;
    private DatabaseReference databaseStores,databaseReferencePosts,databaseReferenceCenters,referenceNotifications,referenceUserNotifications;
    private FusedLocationProviderClient mFusedLocationClient;
    double longitude;
    double latiude;
    int number;
    private EditText serach_product;
    StoreModel store;
    private DatabaseReference databaseUsers;
    private TextView textView7,numberTextView;
    AdapterPost adapterPost;
    ArrayList<String> arrayList;
    public HomeFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        referenceNotifications= FirebaseDatabase.getInstance().getReference("AppNotifications");
        referenceUserNotifications= FirebaseDatabase.getInstance().getReference("UserNotification");
        databaseStores = FirebaseDatabase.getInstance().getReference("Stores");
        databaseReferencePosts= FirebaseDatabase.getInstance().getReference("Posts");
        databaseReferenceCenters= FirebaseDatabase.getInstance().getReference("Centers");

        textView7=view.findViewById(R.id.textView7);
        numberTextView=view.findViewById(R.id.numberTextView);
        shimmerFrameLayout=view.findViewById(R.id.shimmerFrameLayout);
        shimmerFrameLayout3=view.findViewById(R.id.shimmerFrameLayout3);
        shimmerFrameLayout2=view.findViewById(R.id.shimmerFrameLayout2);
        shimmerFrameLayout4=view.findViewById(R.id.shimmerFrameLayout4);
        RecyclerViewCenters=view.findViewById(R.id.RecyclerViewCenters);
        centers=new ArrayList<>();

        SharedPreferences preferencesInfo = getActivity().getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        final String userId = preferencesInfo.getString("UserId", "");

        SharedPreferences loginOrSign = getActivity().getSharedPreferences("LoginOrSign", Context.MODE_PRIVATE);
        final boolean successLogin = loginOrSign.getBoolean("success", false);

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fname = dataSnapshot.child("fname").getValue(String.class);
                String lname = dataSnapshot.child("lname").getValue(String.class);

                if (lname != null) {
                    textView7.setText(fname + " " + lname + " مرحبا" + "\n" + "نتمنى انك في افضل حال اليوم.");
                }else {
                        lname = "";
                        fname = "";
                    textView7.setText(fname + " " + lname + " مرحبا" + "\n" + "نتمنى انك في افضل حال اليوم.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        arrayList=new ArrayList<>();
        mUploads=new ArrayList<>();
        posts=new ArrayList<>();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());


        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission
            requestPremation();

        } else {
            // already permission granted

            OnLocationUpdatedListener tt = new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(Location location) {
                    latiude = location.getLatitude();
                    longitude = location.getLongitude();
                    databaseStores.keepSynced(true);
                    databaseStores.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                Log.d("KeyStore",dataSnapshot1.getKey());
                                databaseStores.child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                            store  = dataSnapshot2.getValue(StoreModel.class);


                                            getDistance(latiude,longitude,store.getLatitude(),store.getLongitude(),store);


                                        }

                                        final AdapterMostWanted adpterInstitutions = new AdapterMostWanted(getContext(), mUploads);
                                        RecyclerViewCharities.setVisibility(View.VISIBLE);
                                        shimmerFrameLayout3.setVisibility(View.GONE);
                                        RecyclerViewCharities.setAdapter(adpterInstitutions);

                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            };
            SmartLocation.with(requireActivity()).location().oneFix().start(tt);
        }

        recyclerViewTypeDonations=view.findViewById(R.id.RecyclerViewTypeDonations);
        notification=view.findViewById(R.id.notification);
        serach_product=view.findViewById(R.id.serach_product);
        RecyclerViewMyPosts=view.findViewById(R.id.RecyclerViewMyPosts);
        RecyclerViewCharities=view.findViewById(R.id.RecyclerViewCharities);
        account=view.findViewById(R.id.account);


        recyclerViewTypeDonations.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        RecyclerViewCenters.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

        AdapterSectionTypes itemHome = new AdapterSectionTypes(getContext(), preparyArray());
        shimmerFrameLayout2.setVisibility(View.GONE);
        recyclerViewTypeDonations.setVisibility(View.VISIBLE);
        recyclerViewTypeDonations.setAdapter(itemHome);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);


        RecyclerViewCharities.setLayoutManager(linearLayoutManager);


        //The LinearSnapHelper will snap the center of the target child view to the center of the attached RecyclerView , it's optional if you want , you can use it
        final LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(RecyclerViewMyPosts);


        referenceNotifications.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 number = (int)dataSnapshot.getChildrenCount();

                 Log.d("NumberMM",number+"");

                referenceUserNotifications.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        number += (int)dataSnapshot.getChildrenCount();


                        if (number==0) {
                            numberTextView.setVisibility(View.GONE);
                        }else {
                            numberTextView.setText(number + "");
                            numberTextView.setVisibility(View.VISIBLE);
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReferencePosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ModelPost post = snapshot.getValue(ModelPost.class);
                    posts.add(0,post);

                }

                RecyclerViewMyPosts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
                 adapterPost = new AdapterPost(getContext(), posts);
                RecyclerViewMyPosts.setVisibility(View.VISIBLE);
                 shimmerFrameLayout.setVisibility(View.GONE);
                RecyclerViewMyPosts.setAdapter(adapterPost);


                final int speedScroll = 2000;
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    int count = 0;
                    boolean flag = true;
                    @Override
                    public void run() {
                        if(count < adapterPost.getItemCount()){
                            if(count==adapterPost.getItemCount()-1){
                                flag = false;
                            }else if(count == 0){
                                flag = true;
                            }
                            if(flag) count++;
                            else count--;

                            RecyclerViewMyPosts.smoothScrollToPosition(count);
                            handler.postDelayed(this,speedScroll);
                        }
                    }
                };

                handler.postDelayed(runnable,speedScroll);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });











        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (successLogin){
                    startActivity(new Intent(getActivity(), MyAccountActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else {
                    if (successLogin) {
                        startActivity(new Intent(getActivity(), NotificationActivity.class));
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }else {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dilog_login);
                        dialog.setCancelable(true);

                        Window window = dialog.getWindow();
                        WindowManager.LayoutParams wlp = window.getAttributes();

                        wlp.gravity = Gravity.CENTER;
                        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                        window.setAttributes(wlp);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        TextView skip = dialog.findViewById(R.id.notNow);
                        Button go = dialog.findViewById(R.id.go);


                        skip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();
                            }
                        });

                        go.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();
                                startActivity(new Intent(getActivity(), SignUpActivity.class));
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                            }
                        });

                        dialog.show();
                    }
                }

            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (successLogin) {
                    startActivity(new Intent(getActivity(), NotificationActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dilog_login);
                    dialog.setCancelable(true);

                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();

                    wlp.gravity = Gravity.CENTER;
                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                    window.setAttributes(wlp);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    TextView skip = dialog.findViewById(R.id.notNow);
                    Button go = dialog.findViewById(R.id.go);


                    skip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                        }
                    });

                    go.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                            startActivity(new Intent(getActivity(), SignUpActivity.class));
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                        }
                    });

                    dialog.show();
                }
            }
        });

        serach_product.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH){

                    getActivity().startActivity(new Intent(getActivity(), SearchProductActivity.class).putExtra("KeyWord",serach_product.getText().toString()));

                    return true;

                }

                return false;
            }
        });

        databaseReferenceCenters.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    ModelCenter center = snapshot.getValue(ModelCenter.class);
                    centers.add(center);

                }
                AdapterCenter adapterCenter =new AdapterCenter(getActivity(),centers);
                RecyclerViewCenters.setVisibility(View.VISIBLE);
                shimmerFrameLayout4.setVisibility(View.GONE);
                RecyclerViewCenters.setAdapter(adapterCenter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private ArrayList<DesignItemHome> preparyArray() {

        ArrayList<DesignItemHome> list = new ArrayList<>();

        DesignItemHome designItem = new DesignItemHome();


        designItem = new DesignItemHome();
        designItem.setImage(R.drawable.icone_food);
        designItem.setTitle("طعام");
        list.add(designItem);

        designItem = new DesignItemHome();
        designItem.setImage(R.drawable.cake);
        designItem.setTitle("حلويات");
        list.add(designItem);

        designItem = new DesignItemHome();
        designItem.setImage(R.drawable.dairy);
        designItem.setTitle("البان و اجبان");
        list.add(designItem);

        designItem = new DesignItemHome();
        designItem.setImage(R.drawable.donateclothes);
        designItem.setTitle("ملابس");
        list.add(designItem);


        designItem = new DesignItemHome();
        designItem.setImage(R.drawable.handicrafts);
        designItem.setTitle("حرف يدوية");
        list.add(designItem);

        designItem = new DesignItemHome();
        designItem.setImage(R.drawable.donatefurniture);
        designItem.setTitle("أثاث منزلي");
        list.add(designItem);


        designItem = new DesignItemHome();
        designItem.setImage(R.drawable.c);
        designItem.setTitle("أخرى");
        list.add(designItem);

        return list;

    }


    private double getDistance(double fromLat, double fromLon, double toLat, double toLon,StoreModel store){
        double radius = 6371;   // Earth radius in km
        double deltaLat = Math.toRadians(toLat - fromLat);
        double deltaLon = Math.toRadians(toLon - fromLon);
        double lat1 = Math.toRadians(fromLat);
        double lat2 = Math.toRadians(toLat);
        double aVal = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                Math.sin(deltaLon/2) * Math.sin(deltaLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double cVal = 2*Math.atan2(Math.sqrt(aVal), Math.sqrt(1-aVal));

        double distance = radius*cVal;
        Log.d("distance","radius * angle = " +distance);


        //اذا اقل من 20 كيلو متر معناتو قريبه علي
        if (distance<=20 && store.isThereAreItems())
        {
            mUploads.add(0,store);
        }

        return distance;
    }




    private void requestPremation() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
    }




}