package com.nasahapps.materialdesigntoolbox.example.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.nasahapps.materialdesigntoolbox.components.StepperProgressLayout;
import com.nasahapps.materialdesigntoolbox.example.R;
import com.nasahapps.materialdesigntoolbox.example.ui.adapter.MockViewPagerAdapter;

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

        mViewPager.setAdapter(new MockViewPagerAdapter(getSupportFragmentManager()));
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
        mLayout.addOnStepperProgressListener(new StepperProgressLayout.OnStepperProgressListener() {
            @Override
            public void onStepSelected(int position) {
                mViewPager.setCurrentItem(position, true);
            }

            @Override
            public void onStepDeselected(int position) {

            }

            @Override
            public void onStepsFinished() {

            }
        });
    }

}
