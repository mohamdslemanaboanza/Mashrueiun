package com.AbuAnzeh.mashruei.Adpter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AbuAnzeh.mashruei.Activities.CreateProductActivity;
import com.AbuAnzeh.mashruei.Activities.DetailsActivity;
import com.AbuAnzeh.mashruei.Activities.UpdateProductActivity;
import com.AbuAnzeh.mashruei.Models.ModelProduct;
import com.AbuAnzeh.mashruei.Models.ProductModel;
import com.AbuAnzeh.mashruei.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterStoreProductEdit extends RecyclerView.Adapter<AdapterStoreProductEdit.viewItem> {

    private ArrayList<ProductModel> items;
    private Context context;



    public AdapterStoreProductEdit(Context context, ArrayList<ProductModel> items) {

        this.items = items;
        this.context = context;
    }

    class viewItem extends RecyclerView.ViewHolder{


        public ImageView img_product;
        public TextView nameProduct,priceProduct;

        public viewItem(@NonNull View itemView) {
            super(itemView);

            img_product= itemView.findViewById(R.id.img_product);
            priceProduct= itemView.findViewById(R.id.priceProduct);
            nameProduct= itemView.findViewById(R.id.nameProduct);


        }
    }

    @NonNull
    @Override
    public viewItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_store_edit, viewGroup, false);

        return new viewItem(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull viewItem holder, final int position) {

        final ProductModel product = items.get(position);

        holder.nameProduct.setText(product.getNameProduct());
        holder.priceProduct.setText(product.getPriceProduct()+" د.أ ");
        Picasso.get().load(product.getImageScreen()).placeholder(R.drawable.logo).into(holder.img_product);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateProductActivity.class);

                intent.putExtra("name",product.getNameProduct());
                intent.putExtra("desk",product.getDeskProduct());
                intent.putExtra("max",product.getMaxQuantity());
                intent.putExtra("min",product.getMinQuantity());
                intent.putExtra("price",product.getPriceProduct());
                intent.putExtra("id",product.getIdProduct());
                intent.putExtra("quantity",product.getQuantityProduct());
                context.startActivity(intent);

                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });



    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
