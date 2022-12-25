package com.AbuAnzeh.mashruei.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.mashruei.Activities.CreateProductActivity;
import com.AbuAnzeh.mashruei.Activities.ProductActivity;
import com.AbuAnzeh.mashruei.Adpter.AdapterStoreProduct;
import com.AbuAnzeh.mashruei.Adpter.AdapterStoreProductEdit;
import com.AbuAnzeh.mashruei.Models.ModelProduct;
import com.AbuAnzeh.mashruei.Models.ProductModel;
import com.AbuAnzeh.mashruei.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ProductStoreFragment extends Fragment {



    Button create_product;
    RecyclerView myRecyclerView;


    private DatabaseReference StoresProduct,databaseStores;
    private ArrayList<ProductModel> mUploads;
    AdapterStoreProductEdit productEdit;
    private ImageView imageView4;
    private TextView textView9;


    public ProductStoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_store, container, false);
        create_product= view.findViewById(R.id.create_product);
        imageView4= view.findViewById(R.id.imageView4);
        textView9= view.findViewById(R.id.textView9);
        myRecyclerView= view.findViewById(R.id.recyclerView);

        final SharedPreferences preferencesInfo = getActivity().getSharedPreferences("InfoUser", MODE_PRIVATE);
        String userId = preferencesInfo.getString("UserId", "");

        myRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));


        final SharedPreferences TypeStoreSharedPreferences = getActivity().getSharedPreferences("TypeStore", Context.MODE_PRIVATE);
        String TypeStoreStr = TypeStoreSharedPreferences.getString("TypeStore", "");

        StoresProduct = FirebaseDatabase.getInstance().getReference("Stores").child(TypeStoreStr).child(userId).child("Products");

        SharedPreferences InfoStore=getActivity().getSharedPreferences("InfoStore",MODE_PRIVATE);
        boolean b  = InfoStore.getBoolean("Created",false);

        if (b){
            create_product.setVisibility(View.VISIBLE);
        }else {
            create_product.setVisibility(View.GONE);
        }

        create_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateProductActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });




        mUploads = new ArrayList<>();


        StoresProduct.keepSynced(true);
        StoresProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ProductModel dataSetFire = dataSnapshot1.getValue(ProductModel.class);

                        mUploads.add(0,dataSetFire);

                }

                productEdit = new AdapterStoreProductEdit(getActivity(), mUploads);

                if (productEdit.getItemCount() == 0)
                {
                    imageView4.setVisibility(View.VISIBLE);
                    textView9.setVisibility(View.VISIBLE);

                }

                myRecyclerView.setAdapter(productEdit);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }



}