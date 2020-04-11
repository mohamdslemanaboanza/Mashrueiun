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
import com.AbuAnzeh.Donation.Models.ModelDonation;
import com.AbuAnzeh.Donation.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdpterFoodItem extends RecyclerView.Adapter <AdpterFoodItem.ImageViewHolder>
{
    private Context context;
    private List<ModelDonation> mUploads;



    public AdpterFoodItem(Context context, List<ModelDonation> uploadList) {
        this.context = context;
        this.mUploads = uploadList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.design_item_donation, viewGroup, false);
        return new ImageViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder imageViewHolder, final int i) {
        final ModelDonation upload = mUploads.get(i);




        imageViewHolder.label.setTypeface(Typeface.createFromAsset(context.getAssets(), "Omar-Regular-1.ttf"));
        imageViewHolder.state.setTypeface(Typeface.createFromAsset(context.getAssets(), "Omar-Regular-1.ttf"));
        imageViewHolder.date.setTypeface(Typeface.createFromAsset(context.getAssets(), "Omar-Regular-1.ttf"));
        imageViewHolder.city.setTypeface(Typeface.createFromAsset(context.getAssets(), "Omar-Regular-1.ttf"));

        imageViewHolder.city.setText(upload.getCity());
        imageViewHolder.date.setText(upload.getDateDonation());
        imageViewHolder.label.setText(upload.getNameDonation());
        imageViewHolder.state.setText(upload.getStateDonation());

        Picasso.with(context).load(upload.getImage1()).placeholder(R.drawable.logo).error(R.drawable.logo).into(imageViewHolder.img_donation);

        imageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(context,DetailsDonationActivity.class);
                intent.putExtra("Name_P",upload.getNamePublisher());
                intent.putExtra("Id_P",upload.getIdPublisher());
                intent.putExtra("imgU",upload.getImgPublisher());
                intent.putExtra("Phone",upload.getPhone_number());
                intent.putExtra("idPost",upload.getId());
                intent.putExtra("nameDonation",upload.getNameDonation());
                intent.putExtra("DeDonation",upload.getDetailsDonation());
                intent.putExtra("Latitude",upload.getLatitude());
                intent.putExtra("Longitude",upload.getLongitude());
                intent.putExtra("Img1",upload.getImage1());
                intent.putExtra("Img2",upload.getImage2());
                intent.putExtra("Img3",upload.getImage3());
                intent.putExtra("city",upload.getCity());
                intent.putExtra("date",upload.getDateDonation());
                intent.putExtra("state",upload.getStateDonation());
                context.startActivity(intent);
            }
        });







    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

     public ImageView img_donation;
     public TextView date,label,city,state;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            img_donation=itemView.findViewById(R.id.img_donation);
            date=itemView.findViewById(R.id.date);
            label=itemView.findViewById(R.id.label);
            city=itemView.findViewById(R.id.city);
            state=itemView.findViewById(R.id.state);

        }
    }



}
