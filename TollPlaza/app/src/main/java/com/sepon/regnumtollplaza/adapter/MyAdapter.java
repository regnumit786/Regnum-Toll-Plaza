package com.sepon.regnumtollplaza.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sepon.regnumtollplaza.fragment.chittagong.Graph_fragment;
import com.sepon.regnumtollplaza.fragment.chittagong.Regular_fragment;
import com.sepon.regnumtollplaza.fragment.chittagong.Today_Chittagong_fragment;

public class MyAdapter  extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs){
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Today_Chittagong_fragment homeFragment = new Today_Chittagong_fragment();
               
                return homeFragment;
            case 1:
                Regular_fragment sportFragment = new Regular_fragment();
                return sportFragment;

            case 2:
                Graph_fragment graph = new Graph_fragment();
                return graph;

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
