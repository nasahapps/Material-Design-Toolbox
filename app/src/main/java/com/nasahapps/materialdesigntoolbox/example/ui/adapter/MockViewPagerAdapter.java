package com.nasahapps.materialdesigntoolbox.example.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nasahapps.materialdesigntoolbox.example.ui.mock.MockFragment;

/**
 * Created by Hakeem on 4/17/16.
 */
public class MockViewPagerAdapter extends FragmentPagerAdapter {

    public static final int MAX_COUNT = 6;

    public MockViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MockFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return MAX_COUNT;
    }
}
