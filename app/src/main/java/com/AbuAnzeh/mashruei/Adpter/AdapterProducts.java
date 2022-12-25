package com.AbuAnzeh.mashruei.Adpter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AbuAnzeh.mashruei.Activities.ProductActivity;
import com.AbuAnzeh.mashruei.Activities.StoresActivity;
import com.AbuAnzeh.mashruei.Models.ModelProductTwo;
import com.AbuAnzeh.mashruei.Models.ProductModel;
import com.AbuAnzeh.mashruei.Models.StoreModel;
import com.AbuAnzeh.mashruei.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterProducts extends RecyclerView.Adapter <AdapterProducts.ImageViewHolder>implements Filterable
{
    private Context context;
    private List<StoreModel> mUploads;
    private List<ProductModel> ProductModel;
    ArrayList<StoreModel> getDataAdapterFull;

    public AdapterProducts(Context storesActivity, List<StoreModel> mUploads,List<ProductModel> productModels) {
        this.context = storesActivity;
        this.mUploads = mUploads;
        this.ProductModel = productModels;
        getDataAdapterFull= new ArrayList<>(mUploads);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.design_item, viewGroup, false);
        return new ImageViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int i) {






        final StoreModel store = mUploads.get(i);


        Picasso.get().load(store.getImgUriStore()).placeholder(R.drawable.logo).into(holder.img_store);
        holder.location.setText(store.getLocationStore());
        holder.name.setText(store.getNameStore());


        holder.myRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
        AdapterStore itemHome = new AdapterStore(context, ProductModel);
        holder.myRecyclerView.setAdapter(itemHome);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences  saveIdStore = context.getSharedPreferences("saveIdStore",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = saveIdStore.edit();
                editor.putString("saveIdStore",store.getKeyUser());
                editor.putString("phoneStore",store.getPhoneStore());
                editor.apply();
                context.startActivity(new Intent(context, ProductActivity.class));
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });




    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

     public ImageView img_store;
     public TextView location,name;
     public RecyclerView myRecyclerView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            img_store=itemView.findViewById(R.id.img_user);
            location=itemView.findViewById(R.id.location);
            name=itemView.findViewById(R.id.name);
            myRecyclerView=itemView.findViewById(R.id.myRecyclerView);



        }
    }

    @Override
    public Filter getFilter() {
        return Exsablelist;
    }

    private Filter Exsablelist = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<StoreModel> filthredlsit = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filthredlsit.addAll(getDataAdapterFull);
            } else {
                String fp = constraint.toString().toLowerCase().trim();
                for (StoreModel item : getDataAdapterFull) {
                    if (item.getNameStore().toLowerCase().contains(fp)) {
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

            mUploads.clear();
            mUploads.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public int numberOfItems(){
        return mUploads.size();
    }


}
