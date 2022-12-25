package com.AbuAnzeh.mashruei.Adpter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AbuAnzeh.mashruei.Activities.StoresActivity;
import com.AbuAnzeh.mashruei.Models.DesignItemHome;
import com.AbuAnzeh.mashruei.R;

import java.util.ArrayList;

public class AdapterSectionTypes extends RecyclerView.Adapter<AdapterSectionTypes.viewItem> {

    private ArrayList<DesignItemHome> items;
    private Context context;
    String TitleCategory;


    public AdapterSectionTypes(Context context, ArrayList<DesignItemHome> items) {

        this.items = items;
        this.context = context;
    }

    class viewItem extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title;
        LinearLayout linearLayout;

        public viewItem(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageItem);
            title = itemView.findViewById(R.id.titleItem);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    @NonNull
    @Override
    public viewItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.design_item_home, viewGroup, false);


        return new viewItem(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull viewItem holder, final int position) {


        if (position == 0){
            holder.linearLayout.setBackgroundColor(context.getColor(R.color.colotOne));
        }else if (position == 1){
            holder.linearLayout.setBackgroundColor(context.getColor(R.color.colotTwo));
        }else if (position == 2){
            holder.linearLayout.setBackgroundColor(context.getColor(R.color.colotThree));
        }else if (position == 3){
            holder.linearLayout.setBackgroundColor(context.getColor(R.color.colotfour));
        }else if (position == 4){
            holder.linearLayout.setBackgroundColor(context.getColor(R.color.colotfive));
        }else if (position == 5){
            holder.linearLayout.setBackgroundColor(context.getColor(R.color.colotTwo));
        }
        holder.image.setImageResource(items.get(position).getImage());
        holder.title.setText(items.get(position).getTitle());



        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent go = new Intent(context, StoresActivity.class).putExtra("pos",position);
                context.startActivity(go);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);




            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
