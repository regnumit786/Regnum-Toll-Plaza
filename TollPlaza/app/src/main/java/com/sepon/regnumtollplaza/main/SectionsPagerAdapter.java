package com.sepon.regnumtollplaza.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sepon.regnumtollplaza.R;
import com.sepon.regnumtollplaza.fragment.CharsindorGraph;
import com.sepon.regnumtollplaza.fragment.Previous_fragment;
import com.sepon.regnumtollplaza.fragment.Today_Fragment;
import com.sepon.regnumtollplaza.fragment.VipPass_fragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Today_Fragment();
            case 1:
                return new Previous_fragment();
            case 2:
                return new VipPass_fragment();
            case 3:
                return new CharsindorGraph();
            default:
                return null;
        }
       // return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }
}