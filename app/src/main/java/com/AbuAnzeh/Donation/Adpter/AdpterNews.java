package com.AbuAnzeh.Donation.Adpter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AbuAnzeh.Donation.Models.News;
import com.AbuAnzeh.Donation.R;

import java.util.List;

public class AdpterNews extends RecyclerView.Adapter <AdpterNews.ImageViewHolder>
{
    private Context context;
    private List<News> mUploads;



    public AdpterNews(Context context, List<News> uploadList) {
        this.context = context;
        this.mUploads = uploadList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.news_taps, viewGroup, false);
        return new ImageViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder imageViewHolder, final int i) {
        News upload = mUploads.get(i);




        imageViewHolder.commit.setTypeface(Typeface.createFromAsset(context.getAssets(), "Omar-Regular-1.ttf"));
        imageViewHolder.commit.setText(upload.getCommit());









    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {


     public TextView commit;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            commit=itemView.findViewById(R.id.commit);

        }
    }



}
