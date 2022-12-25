package com.AbuAnzeh.mashruei.Adpter;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.AbuAnzeh.mashruei.Fragment.InfoStoreFragment;
import com.AbuAnzeh.mashruei.Fragment.InfoStoreFragmentMain;
import com.AbuAnzeh.mashruei.Fragment.ProductStoreFragment;
import com.AbuAnzeh.mashruei.Fragment.ProductStoreFragmentMain;

public class MyAdapterStore extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public MyAdapterStore(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ProductStoreFragmentMain productStoreFragment = new ProductStoreFragmentMain();
                return productStoreFragment;
            case 1:
                InfoStoreFragmentMain infoStoreFragment = new InfoStoreFragmentMain();
                return infoStoreFragment;
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