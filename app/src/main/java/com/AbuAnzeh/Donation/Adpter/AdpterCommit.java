package com.AbuAnzeh.Donation.Adpter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AbuAnzeh.Donation.Activities.DetailsDonationActivity;
import com.AbuAnzeh.Donation.Models.ModelCommit;
import com.AbuAnzeh.Donation.Models.ModelDonation;
import com.AbuAnzeh.Donation.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdpterCommit extends RecyclerView.Adapter <AdpterCommit.ImageViewHolder>
{
    private Context context;
    private List<ModelCommit> mUploads;



    public AdpterCommit(Context context, List<ModelCommit> uploadList) {
        this.context = context;
        this.mUploads = uploadList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.design_commit, viewGroup, false);
        return new ImageViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder imageViewHolder, final int i) {
        final ModelCommit upload = mUploads.get(i);


        imageViewHolder.commit.setText(upload.getCommit());

        imageViewHolder.commit.setTypeface(Typeface.createFromAsset(context.getAssets(), "Omar-Regular-1.ttf"));



        Picasso.with(context).load(upload.getImg()).placeholder(R.drawable.logo).error(R.drawable.logo).into(imageViewHolder.user_img);










    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {


        ImageView user_img;
        TextView commit;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            user_img=itemView.findViewById(R.id.user_img);
            commit=itemView.findViewById(R.id.commit);




        }
    }



}
