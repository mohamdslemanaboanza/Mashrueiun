package com.AbuAnzeh.mashruei.Adpter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AbuAnzeh.mashruei.Activities.DetailsActivity;
import com.AbuAnzeh.mashruei.Activities.ProductActivity;
import com.AbuAnzeh.mashruei.Models.ModelProduct;
import com.AbuAnzeh.mashruei.Models.ProductModel;
import com.AbuAnzeh.mashruei.Models.StoreModel;
import com.AbuAnzeh.mashruei.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterStoreProduct extends RecyclerView.Adapter<AdapterStoreProduct.viewItem>  implements Filterable {

    private ArrayList<ProductModel> items;
    private Context context;
    ArrayList<ProductModel> getDataAdapterFull;




    public AdapterStoreProduct(Context context, ArrayList<ProductModel> items) {

        this.items = items;
        this.context = context;
        getDataAdapterFull= new ArrayList<>(items);
        Log.d("context",context.toString());
    }

    class viewItem extends RecyclerView.ViewHolder{

        public ImageView img_product;
        public TextView nameProduct,priceProduct;
        public com.omega_r.libs.OmegaCenterIconButton   add_to_card;


        public viewItem(@NonNull View itemView) {
            super(itemView);

            img_product=itemView.findViewById(R.id.img_product);
            nameProduct=itemView.findViewById(R.id.nameProduct);
            priceProduct=itemView.findViewById(R.id.priceProduct);
            add_to_card=itemView.findViewById(R.id.add_to_card);

        }
    }

    @NonNull
    @Override
    public viewItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_store, viewGroup, false);
        return new viewItem(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull viewItem holder, final int position) {

        final ProductModel product = items.get(position);

        Picasso.get().load(product.getImageScreen()).placeholder(R.drawable.logo).into(holder.img_product);
        holder.nameProduct.setText(product.getNameProduct());
        holder.priceProduct.setText(product.getPriceProduct()+" د.أ ");



        if (context.toString().contains("SearchProductActivity")){
            holder.add_to_card.setText("الذهاب الى المتجر");
        }else {

        }

        holder.add_to_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context.toString().contains("SearchProductActivity")){
                    SharedPreferences preferences = context.getSharedPreferences("StoreType", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("StoreType",product.getTypeStore());
                    editor.apply();

                    context.startActivity(new Intent(context, ProductActivity.class));

                }else {

                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DetailsActivity.class);
                intent.putExtra("nameProduct",product.getNameProduct());
                intent.putExtra("detailsProduct",product.getDeskProduct());
                intent.putExtra("priceProduct",product.getPriceProduct());
                intent.putExtra("max",product.getMaxQuantity());
                intent.putExtra("min",product.getMinQuantity());
                intent.putExtra("flag",true);
                intent.putExtra("idProduct",product.getIdProduct());



                context.startActivity(intent);
            }
        });

    }


    @Override
    public Filter getFilter() {
        return Exsablelist;
    }

    private Filter Exsablelist = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ProductModel> filthredlsit = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filthredlsit.addAll(getDataAdapterFull);
            } else {
                String fp = constraint.toString().toLowerCase().trim();
                for (ProductModel item : getDataAdapterFull) {
                    if (item.getNameProduct().toLowerCase().contains(fp)) {
                        filthredlsit.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filthredlsit;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            items.clear();
            items.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



    @Override
    public int getItemCount() {
        return items.size();
    }
}
