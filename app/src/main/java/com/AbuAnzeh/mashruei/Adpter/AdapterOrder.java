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

import com.AbuAnzeh.mashruei.Activities.DetailsOrderActivity;
import com.AbuAnzeh.mashruei.Models.ModelProduct;
import com.AbuAnzeh.mashruei.Models.ModelProductTwo;
import com.AbuAnzeh.mashruei.R;

import java.util.ArrayList;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.viewItem> {

    private ArrayList<ModelProductTwo> items;
    private Context context;
    String TitleCategory;


    public AdapterOrder(Context context, ArrayList<ModelProductTwo> items) {

        this.items = items;
        this.context = context;
    }

    class viewItem extends RecyclerView.ViewHolder{



        public viewItem(@NonNull View itemView) {
            super(itemView);



        }
    }

    @NonNull
    @Override
    public viewItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_cart, viewGroup, false);


        return new viewItem(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull viewItem holder, final int position) {



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, DetailsOrderActivity.class));
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
