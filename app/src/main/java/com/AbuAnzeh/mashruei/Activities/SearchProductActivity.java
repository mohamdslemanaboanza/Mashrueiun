package com.AbuAnzeh.mashruei.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.mashruei.Adpter.AdapterProducts;
import com.AbuAnzeh.mashruei.Adpter.AdapterStoreProduct;
import com.AbuAnzeh.mashruei.Models.ProductModel;
import com.AbuAnzeh.mashruei.Models.StoreModel;
import com.AbuAnzeh.mashruei.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchProductActivity extends AppCompatActivity {

    private DatabaseReference databaseStores;
    private ArrayList<ProductModel>  productModels;
    private RecyclerView myRecyclerView;
    private AdapterStoreProduct adapterProducts;
    private ImageView imageView4;
    private TextView textView9,header;
    private String KeyWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        imageView4=findViewById(R.id.imageView4);
        myRecyclerView=findViewById(R.id.myRecyclerView);
        textView9=findViewById(R.id.textView9);
        header=findViewById(R.id.header);

        KeyWord=getIntent().getExtras().getString("KeyWord");


        databaseStores = FirebaseDatabase.getInstance().getReference("Stores");
        myRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        productModels=new ArrayList<>();

        databaseStores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    databaseStores.child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                databaseStores.child(dataSnapshot1.getKey()).child(dataSnapshot2.getKey()).child("Products").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                            ProductModel dataSetFire = dataSnapshot1.getValue(ProductModel.class);
                                            if (dataSetFire.getNameProduct().contains(KeyWord)){
                                                productModels.add(0,dataSetFire);
                                            }


                                            adapterProducts = new AdapterStoreProduct(SearchProductActivity.this, productModels);
                                            myRecyclerView.setAdapter(adapterProducts);
                                        }

                                        if (adapterProducts != null) {
                                            if (adapterProducts.getItemCount() == 0) {
                                                textView9.setVisibility(View.VISIBLE);
                                                imageView4.setVisibility(View.VISIBLE);
                                            } else {
                                                textView9.setVisibility(View.GONE);
                                                imageView4.setVisibility(View.GONE);
                                            }
                                        }
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



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchProductActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}