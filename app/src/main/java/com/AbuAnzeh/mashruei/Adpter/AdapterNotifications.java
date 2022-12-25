package com.AbuAnzeh.mashruei.Adpter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AbuAnzeh.mashruei.Models.ModelMostWanted;
import com.AbuAnzeh.mashruei.Models.ModelNotifications;
import com.AbuAnzeh.mashruei.Models.Modelnotice;
import com.AbuAnzeh.mashruei.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterNotifications extends RecyclerView.Adapter <AdapterNotifications.ImageViewHolder>  {
    private Context context;
    private List<Modelnotice> mUploads;



    public AdapterNotifications(Context context, List<Modelnotice> modelNotifications) {
        this.context = context;
        this.mUploads = modelNotifications;


    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, viewGroup, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int i) {
        final Modelnotice upload = mUploads.get(i);

        holder.name.setText(upload.getName());
        holder.deck.setText(upload.getDeck());





    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {


        public TextView name,deck;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            deck=itemView.findViewById(R.id.desk);




        }
    }



}
