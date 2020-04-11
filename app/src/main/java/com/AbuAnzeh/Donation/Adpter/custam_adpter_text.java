package com.AbuAnzeh.Donation.Adpter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.AbuAnzeh.Donation.Models.custm_item_text;
import com.AbuAnzeh.Donation.R;

import java.util.ArrayList;

public class custam_adpter_text extends ArrayAdapter<custm_item_text>
{
    Context context;

    public custam_adpter_text(Context context, ArrayList<custm_item_text> custm_items) {
        super(context, 0, custm_items);
        this.context=context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return custmview(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return custmview(position, convertView, parent);
    }

    public View custmview(int position, View convertView, ViewGroup parent)
    {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.item_spainer_text,parent,false);

        }
        custm_item_text item=getItem(position);
        TextView ts=convertView.findViewById(R.id.txt_spiner);
        ts.setTypeface(Typeface.createFromAsset(context.getAssets(), "Omar-Regular-1.ttf"));

        if(item != null)
        {

            ts.setText(item.getSpinnertext());
        }
        return convertView;
    }
}
