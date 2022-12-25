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

import com.AbuAnzeh.mashruei.Activities.DetailsPostActivity;
import com.AbuAnzeh.mashruei.Models.ModelPost;
import com.AbuAnzeh.mashruei.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.viewItem> {

    private ArrayList<ModelPost> items;
    private Context context;
    String TitleCategory;


    public AdapterPost(Context context, ArrayList<ModelPost> items) {

        this.items = items;
        this.context = context;
    }

    class viewItem extends RecyclerView.ViewHolder{


        public TextView namePost,deskPost;
        public ImageView imagePost;
        public viewItem(@NonNull View itemView) {
            super(itemView);

            namePost=itemView.findViewById(R.id.namePost);
            deskPost=itemView.findViewById(R.id.deskPost);
            imagePost=itemView.findViewById(R.id.imagePost);



        }
    }

    @NonNull
    @Override
    public viewItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_post_home_fragment, viewGroup, false);


        return new viewItem(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull viewItem holder, final int position) {


        final ModelPost post = items.get(position);

        holder.deskPost.setText(post.getDeckPost());
        holder.namePost.setText(post.getNamePost());

        Picasso.get().load(post.getUriImagePost()).placeholder(R.drawable.logo).into(holder.imagePost);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, DetailsPostActivity.class)
                .putExtra("name",post.getNamePost()).putExtra("deck",post.getDeckPost()).putExtra("img",post.getUriImagePost()));
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });



    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
