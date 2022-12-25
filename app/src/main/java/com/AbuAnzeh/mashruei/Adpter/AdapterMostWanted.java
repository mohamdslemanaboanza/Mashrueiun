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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AbuAnzeh.mashruei.Activities.ProductActivity;
import com.AbuAnzeh.mashruei.Models.ModelMostWanted;
import com.AbuAnzeh.mashruei.Models.StoreModel;
import com.AbuAnzeh.mashruei.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterMostWanted extends RecyclerView.Adapter <AdapterMostWanted.ImageViewHolder> {
    private Context context;
    private List<StoreModel> mUploads;




    public AdapterMostWanted(Context context, List<StoreModel> uploadList) {
        this.context = context;
        this.mUploads = uploadList;



    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.design_inn, viewGroup, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder imageViewHolder, final int i) {
        final StoreModel upload = mUploads.get(i);




        imageViewHolder.name.setText(upload.getNameStore());
        imageViewHolder.typesStore.setText(upload.getTypeStore());
        Picasso.get().load(upload.getImgUriStore()).placeholder(R.drawable.logo).into(imageViewHolder.img);

        imageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences saveIdStore = context.getSharedPreferences("saveIdStore",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = saveIdStore.edit();
                editor.putString("saveIdStore",upload.getKeyUser());
                editor.apply();

                SharedPreferences preferences = context.getSharedPreferences("StoreType", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = preferences.edit();
                editor1.putString("StoreType",upload.getTypeStore());
                editor1.apply();


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

        public TextView name,typesStore;
        public ImageView img;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameCharities);
            typesStore = itemView.findViewById(R.id.TypesStore);
            img = itemView.findViewById(R.id.imageCharities);



        }
    }



}
