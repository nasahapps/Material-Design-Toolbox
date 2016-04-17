package com.nasahapps.materialdesigntoolbox.example.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.nasahapps.materialdesigntoolbox.components.OnStepperProgressListener;
import com.nasahapps.materialdesigntoolbox.components.StepperProgressLayout;
import com.nasahapps.materialdesigntoolbox.example.R;
import com.nasahapps.materialdesigntoolbox.example.ui.mock.MockFragment;

import butterknife.Bind;

public class TestActivity extends BaseActivity {

    @Bind(R.id.viewPager)
    ViewPager mViewPager;
    @Bind(R.id.layout)
    StepperProgressLayout mLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mLayout.setProgress(position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mLayout.addOnStepperProgressListener(new OnStepperProgressListener() {
            @Override
            public void onStepSelected(int position) {
                mViewPager.setCurrentItem(position, true);
            }

            @Override
            public void onStepDeselected(int position) {

            }

            @Override
            public void onStepCompleted(int position) {

            }

            @Override
            public void onStepsFinished() {

            }
        });
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new MockFragment();
        }

        @Override
        public int getCount() {
            return 8;
        }
    }

}
