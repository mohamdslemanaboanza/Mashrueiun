package com.AbuAnzeh.mashruei.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AbuAnzeh.mashruei.Models.Image;
import com.AbuAnzeh.mashruei.Models.ModelProduct;
import com.AbuAnzeh.mashruei.Models.ProductModel;
import com.AbuAnzeh.mashruei.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterStore extends RecyclerView.Adapter<AdapterStore.viewItem> {

    private List<ProductModel> items;
    private Context context;



    public AdapterStore(Context context, List<ProductModel> items) {

        this.items = items;
        this.context = context;
    }

    class viewItem extends RecyclerView.ViewHolder{


        public ImageView img_product;
        public TextView nameCharities;


        public viewItem(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            nameCharities = itemView.findViewById(R.id.nameCharities);


        }
    }

    @NonNull
    @Override
    public viewItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.design_store_home, viewGroup, false);


        return new viewItem(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull viewItem holder, final int position) {


        ProductModel product = items.get(position);


        if (context.toString().contains("StoresActivity"))
            holder.nameCharities.setVisibility(View.GONE);

            holder.nameCharities.setText(product.getNameProduct());
        Picasso.get().load(product.getImageScreen()).placeholder(R.drawable.logo).into(holder.img_product);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
