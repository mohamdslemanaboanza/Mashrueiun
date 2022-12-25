package com.AbuAnzeh.mashruei.Adpter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AbuAnzeh.mashruei.Activities.DetailsCenterActivity;
import com.AbuAnzeh.mashruei.Activities.ProductActivity;
import com.AbuAnzeh.mashruei.Models.ModelCenter;
import com.AbuAnzeh.mashruei.Models.StoreModel;
import com.AbuAnzeh.mashruei.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCenter extends RecyclerView.Adapter <AdapterCenter.ImageViewHolder> {
    private Context context;
    private List<ModelCenter> mUploads;




    public AdapterCenter(Context context, List<ModelCenter> uploadList) {
        this.context = context;
        this.mUploads = uploadList;



    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.item_center, viewGroup, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder imageViewHolder, final int i) {
        final ModelCenter upload = mUploads.get(i);




        imageViewHolder.name.setText(upload.getName());
        Picasso.get().load(upload.getVideo()).placeholder(R.drawable.logo).into(imageViewHolder.img);

        imageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                Intent intent = new Intent(context,DetailsCenterActivity.class);
                intent.putExtra("Name",upload.getName());
                intent.putExtra("Deck",upload.getDesk());
                intent.putExtra("img",upload.getVideo());
                intent.putExtra("lat",upload.getLat());
                intent.putExtra("lon",upload.getLon());
                intent.putExtra("location",upload.getCity());
                intent.putExtra("phone",upload.getPhoneNumber());
                context.startActivity(intent);


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
            name = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.image);



        }
    }



}
