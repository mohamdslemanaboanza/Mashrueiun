package com.AbuAnzeh.mashruei.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.mashruei.Activities.StoresActivity;
import com.AbuAnzeh.mashruei.Adpter.AdapterProducts;
import com.AbuAnzeh.mashruei.Adpter.AdapterStoreProduct;
import com.AbuAnzeh.mashruei.Adpter.AdapterStoreProductEdit;
import com.AbuAnzeh.mashruei.Adpter.custam_adpter_text_location_ser;
import com.AbuAnzeh.mashruei.Models.ModelProduct;
import com.AbuAnzeh.mashruei.Models.ModelProductTwo;
import com.AbuAnzeh.mashruei.Models.ProductModel;
import com.AbuAnzeh.mashruei.Models.custm_item_text;
import com.AbuAnzeh.mashruei.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProductStoreFragmentMain extends Fragment {



    RecyclerView myRecyclerView;
    private DatabaseReference StoresProduct,databaseStores;
    private ArrayList<ProductModel> mUploads;
    private EditText ser;
    private AdapterStoreProduct adapterProducts;


    public ProductStoreFragmentMain() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_store_main, container, false);

        mUploads=new ArrayList<>();

        myRecyclerView=view.findViewById(R.id.myRecyclerView);
        ser=view.findViewById(R.id.ser);
        myRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));



        SharedPreferences preferences = getActivity().getSharedPreferences("StoreType", Context.MODE_PRIVATE);
        String typeStore=preferences.getString("StoreType","");


        SharedPreferences  saveIdStore = getActivity().getSharedPreferences("saveIdStore",Context.MODE_PRIVATE);
        final String idStore = saveIdStore.getString("saveIdStore","");

        Log.d("StoreTypeProduct",typeStore);

        if (typeStore.equals("طعام")){
            typeStore = "Food";
        }else if (typeStore.equals("حلويات")){
            typeStore = "Candy";
        }else if (typeStore.equals("ملابس")){
            typeStore = "Clothes";
        }else if (typeStore.equals("البان و اجبان")){
            typeStore = "Dairies";
        }else if (typeStore.equals("حرف يدوية")){
            typeStore = "Handicraft";
        }else if (typeStore.equals("اثاثا منزلي")){
            typeStore = "HomeFurnishings";
        }else if (typeStore.equals("اخرى")){
            typeStore = "Other";
        }



        StoresProduct = FirebaseDatabase.getInstance().getReference("Stores").child(typeStore);

        StoresProduct.keepSynced(true);
        StoresProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    StoresProduct.child(idStore).child("Products").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                ProductModel dataSetFire = dataSnapshot1.getValue(ProductModel.class);

                                 if (dataSetFire.isActive == 1){
                                     mUploads.add(0,dataSetFire);
                                 }
                                 adapterProducts = new AdapterStoreProduct(getActivity(), mUploads);



                                myRecyclerView.setAdapter(adapterProducts);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


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

                    try {
                        adapterProducts.getFilter().filter(charSequence.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    adapterProducts.notifyDataSetChanged();
                } else {
                    adapterProducts.getFilter().filter(charSequence.toString());
                    adapterProducts.notifyDataSetChanged();
                    Log.d("NumberOfItems", adapterProducts.getItemCount() + "");


                }
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





        return view;
    }

}