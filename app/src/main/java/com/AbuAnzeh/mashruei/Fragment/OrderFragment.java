package com.AbuAnzeh.mashruei.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.mashruei.Activities.DetailsActivity;
import com.AbuAnzeh.mashruei.Activities.DetailsOrderActivity;
import com.AbuAnzeh.mashruei.Adpter.AdapterOrder;
import com.AbuAnzeh.mashruei.Models.ModelProductTwo;
import com.AbuAnzeh.mashruei.Models.ProductModel;
import com.AbuAnzeh.mashruei.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment {



    RecyclerView orderRecyclerView;
    DatabaseReference databaseOrder;
    ImageView imageView4;
    TextView textView9;
    ArrayList<ProductModel>  listOrder;

    private String userId;

    public OrderFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        orderRecyclerView=view.findViewById(R.id.orderRecyclerView);
        imageView4=view.findViewById(R.id.imageView4);
        textView9=view.findViewById(R.id.textView9);

        textView9.setVisibility(View.GONE);
        imageView4.setVisibility(View.GONE);

        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listOrder=new ArrayList<>();
        SharedPreferences preferencesInfo = getActivity().getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
          userId = preferencesInfo.getString("UserId", "");



        databaseOrder= FirebaseDatabase.getInstance().getReference("Order").child(userId);

        databaseOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    ProductModel product = dataSnapshot1.getValue(ProductModel.class);
                    listOrder.add(product);
                }

                AdapterOrder adapterCart = new AdapterOrder(getActivity(),listOrder);
                orderRecyclerView.setAdapter(adapterCart);

                if( listOrder.size() == 0){
                    textView9.setVisibility(View.VISIBLE);
                    imageView4.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


    public  class AdapterOrder extends RecyclerView.Adapter <AdapterOrder.ImageViewHolder>
    {
        private Context context;
        private List<ProductModel> mUploads;
        Activity mActivity;
        DatabaseReference databaseReference;

        public AdapterOrder(Context context, List<ProductModel> uploadList) {
            this.context = context;
            this.mUploads = uploadList;





            databaseReference= FirebaseDatabase.getInstance().getReference("Order").child(userId);

        }

        @NonNull
        @Override
        public AdapterOrder.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


            View view = LayoutInflater.from(context).inflate(R.layout.item_cart, viewGroup, false);
            return new AdapterOrder.ImageViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull  final  ImageViewHolder holder, final int i) {
            final ProductModel upload = mUploads.get(i);

            Picasso.get().load(upload.getImageScreen()).placeholder(R.drawable.logo).into(holder.img_product);
            holder.nameProduct.setText(upload.getNameProduct());
            holder.quantityProduct.setText("الكمية : "+upload.getQuantityProduct());
            holder.priceProduct.setText(upload.getPriceProduct()+"د.أ ");


            Log.d("context",context.toString());


                holder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(context, DetailsOrderActivity.class);
                    intent.putExtra("nameProduct",upload.getNameProduct());
                    intent.putExtra("detailsProduct",upload.getDeskProduct());
                    intent.putExtra("priceProduct",upload.getPriceProduct());
                    intent.putExtra("Quantity",upload.getQuantityProduct());
                    intent.putExtra("hint",upload.getHintProduct());
                    intent.putExtra("name",upload.getNameUser());
                    intent.putExtra("phoneNumber",upload.getPhoneNumber());
                    intent.putExtra("city",upload.getCity());
                    intent.putExtra("location",upload.getLocation());
                    intent.putExtra("latitude",upload.getLat());
                    intent.putExtra("longitude",upload.getLon());
                    intent.putExtra("idProduct",upload.getIdProduct());


                    context.startActivity(intent);
                });



                holder.nameProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseReference .child(upload.idProduct).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    mUploads.remove(i);
                                    notifyDataSetChanged();
                                }else {
                                    Toast.makeText(context, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
        }

        @Override
        public int getItemCount() {
            return mUploads.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {



            public RoundedImageView img_product;
            public TextView nameProduct,quantityProduct,priceProduct,nameStore;
            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                img_product=itemView.findViewById(R.id.img_product);
                nameProduct=itemView.findViewById(R.id.nameProduct);
                quantityProduct=itemView.findViewById(R.id.quantityProduct);
                priceProduct=itemView.findViewById(R.id.priceProduct);
                nameStore=itemView.findViewById(R.id.nameStore);


            }
        }




    }



}