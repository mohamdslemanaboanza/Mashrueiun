package com.AbuAnzeh.mashruei.Adpter;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.AbuAnzeh.mashruei.Fragment.InfoStoreFragment;
import com.AbuAnzeh.mashruei.Fragment.ProductStoreFragment;

public class MyAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                InfoStoreFragment infoStoreFragment = new InfoStoreFragment();
                return infoStoreFragment;
            case 1:
                ProductStoreFragment productStoreFragment = new ProductStoreFragment();
                return productStoreFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}