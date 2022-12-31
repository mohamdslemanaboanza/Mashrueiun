package com.AbuAnzeh.mashruei.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.mashruei.Adpter.AdapterProducts;
import com.AbuAnzeh.mashruei.Adpter.AdapterStoreProductEdit;
import com.AbuAnzeh.mashruei.Adpter.custam_adpter_text_location;
import com.AbuAnzeh.mashruei.Adpter.custam_adpter_text_location_ser;
import com.AbuAnzeh.mashruei.Models.ModelProductTwo;
import com.AbuAnzeh.mashruei.Models.ModelProduct;
import com.AbuAnzeh.mashruei.Models.ProductModel;
import com.AbuAnzeh.mashruei.Models.StoreModel;
import com.AbuAnzeh.mashruei.Models.custm_item_text;
import com.AbuAnzeh.mashruei.R;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StoresActivity extends AppCompatActivity {


    private RecyclerView myRecyclerView;
    private List<StoreModel> mUploads;
    private List<ProductModel> productModels;
    private LottieAnimationView animationView;
    private EditText ser;
    private TextView header;
    private ImageView serach;
    private Spinner GovernessesSpinner;
    private ArrayList<custm_item_text> Governesses;
    private DatabaseReference databaseStores;
    private ImageView imageView4;
    private TextView textView9;

    private String CitySpinner;
    AdapterProducts adapterProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        myRecyclerView=findViewById(R.id.myRecyclerView);
        ser=findViewById(R.id.ser);
        header=findViewById(R.id.header);
        serach=findViewById(R.id.serach);
        animationView=findViewById(R.id.animationView);
        imageView4=findViewById(R.id.imageView4);
        textView9=findViewById(R.id.textView9);

        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));





        String typeStore = "";
        int pos = getIntent().getExtras().getInt("pos");

        SharedPreferences preferences = getSharedPreferences("StoreType",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        if (pos == 0){
            typeStore = "Food";
        }else if (pos == 1){
            typeStore = "Candy";
        }
        else if (pos == 2){
            typeStore = "Dairies";
        } else if (pos == 3){
            typeStore = "Clothes";
        }else if (pos == 4){
            typeStore = "Handicraft";
        }else if (pos == 5){
            typeStore = "HomeFurnishings";
        }else if (pos == 6){
            typeStore = "Other";
        }
        editor.putString("StoreType",typeStore);
        editor.apply();

        Log.d("StoreType",typeStore);
        databaseStores = FirebaseDatabase.getInstance().getReference("Stores").child(typeStore);
        mUploads=new ArrayList<>();
        productModels=new ArrayList<>();
        databaseStores.keepSynced(true);




        ser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                adapterProducts.notifyDataSetChanged();
//                if (adapterProducts.getItemCount() == 0){
//                    imageView4.setVisibility(View.VISIBLE);
//                    textView9.setVisibility(View.VISIBLE);
//                }else {
//                    imageView4.setVisibility(View.GONE);
//                    textView9.setVisibility(View.GONE);
//                }


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    imageView4.setVisibility(View.GONE);
                    textView9.setVisibility(View.GONE);
                    //                    adapterProducts.getFilter().filter(charSequence.toString());
//                    adapterProducts.notifyDataSetChanged();
                } else {
                    adapterProducts.getFilter().filter(charSequence.toString());
                    adapterProducts.notifyDataSetChanged();
                    Log.d("NumberOfItems", adapterProducts.getItemCount() + "");
                    if (adapterProducts.getItemCount() == 0) {
                        imageView4.setVisibility(View.VISIBLE);
                        textView9.setVisibility(View.VISIBLE);
                    } else {
                        imageView4.setVisibility(View.GONE);
                        textView9.setVisibility(View.GONE);
                    }
                }
                animationView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                adapterProducts.notifyDataSetChanged();
//                if (adapterProducts.getItemCount() == 0){
//                    imageView4.setVisibility(View.VISIBLE);
//                    textView9.setVisibility(View.VISIBLE);
//                }else {
//                    imageView4.setVisibility(View.GONE);
//                    textView9.setVisibility(View.GONE);
//                }

            }
        });



        GovernessesSpinner=findViewById(R.id.GovernessesSpinner);
        Governesses = GetGovernorate();
        custam_adpter_text_location_ser ustamAdabterCustam_adpter_text=new custam_adpter_text_location_ser(this, Governesses);
        if(GovernessesSpinner != null)
        {
            GovernessesSpinner.setAdapter(ustamAdabterCustam_adpter_text);
        }


        assert GovernessesSpinner != null;
        GovernessesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                custm_item_text itemText = (custm_item_text) adapterView.getSelectedItem();
                CitySpinner = itemText.getSpinnertext();
                mUploads=new ArrayList<>();
                productModels=new ArrayList<>();

                databaseStores.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        dataSnapshot.hasChildren();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            final StoreModel store = dataSnapshot1.getValue(StoreModel.class);
                            if (CitySpinner.equals("الكل") && store.getIsActiveStore() == 1 && store.isThereAreItems()){
                                mUploads.add(0,store);
                            }
                           else if (store.getCityStore().equals(CitySpinner) && store.getIsActiveStore() == 1 && store.isThereAreItems())
                                mUploads.add(0,store);


                            databaseStores.child(dataSnapshot1.getKey()).child("Products").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                        ProductModel dataSetFire = dataSnapshot1.getValue(ProductModel.class);

                                        if (CitySpinner.equals("الكل") && dataSetFire.getIsActive()==1){
                                            productModels.add(0,dataSetFire);
                                        }
                                        else if (store.getCityStore().equals(CitySpinner)&& dataSetFire.getIsActive()==1)
                                            productModels.add(0, dataSetFire);


                                    }

                                    adapterProducts = new AdapterProducts(StoresActivity.this, mUploads, productModels);
                                    myRecyclerView.setAdapter(adapterProducts);


                                    if (adapterProducts != null) {
                                        if (adapterProducts.getItemCount() == 0) {
                                            imageView4.setVisibility(View.VISIBLE);
                                            textView9.setVisibility(View.VISIBLE);
                                        } else {
                                            imageView4.setVisibility(View.GONE);
                                            textView9.setVisibility(View.GONE);
                                        }
                                    }else {
                                        imageView4.setVisibility(View.VISIBLE);
                                        textView9.setVisibility(View.VISIBLE);
                                    }
                                    animationView.setVisibility(View.GONE);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }


                            });



                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(StoresActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        serach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ser.setVisibility(View.VISIBLE);
                ser.setFocusable(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });






    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    //بضيف المدن
    private ArrayList<custm_item_text> GetGovernorate()
    {
        Governesses =new ArrayList<>();
        Governesses.add(new custm_item_text("الكل"));
        Governesses.add(new custm_item_text("عمان"));
        Governesses.add(new custm_item_text("الزرقاء"));
        Governesses.add(new custm_item_text("الطفيلة"));
        Governesses.add(new custm_item_text("الكرك"));
        Governesses.add(new custm_item_text("اربد"));
        Governesses.add(new custm_item_text("معان"));
        Governesses.add(new custm_item_text("العقبة"));
        Governesses.add(new custm_item_text("السلط"));
        Governesses.add(new custm_item_text("مادبا"));
        Governesses.add(new custm_item_text("جرش"));
        Governesses.add(new custm_item_text("عجلون"));
        Governesses.add(new custm_item_text("المفرق"));

        return Governesses;


    }


}
