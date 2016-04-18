package com.nasahapps.materialdesigntoolbox.example.ui.components;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.nasahapps.materialdesigntoolbox.components.OnStepperProgressListener;
import com.nasahapps.materialdesigntoolbox.components.StepperProgressLayout;
import com.nasahapps.materialdesigntoolbox.example.R;
import com.nasahapps.materialdesigntoolbox.example.ui.adapter.MockViewPagerAdapter;
import com.nasahapps.materialdesigntoolbox.example.ui.main.MainActivity;

import butterknife.Bind;

/**
 * Created by Hakeem on 4/17/16.
 */
public class StepperProgressFragment extends ComponentFragment {

    private static final String EXTRA_TYPE = "type";

    @Bind(R.id.viewPager)
    ViewPager mViewPager;
    @Bind(R.id.layout)
    StepperProgressLayout mLayout;

    public static StepperProgressFragment newInstance(@StepperProgressLayout.ProgressType int type) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_TYPE, type);

        StepperProgressFragment fragment = new StepperProgressFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_stepper_progress;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager.setAdapter(new MockViewPagerAdapter(getFragmentManager()));
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
        mLayout.setMaxProgress(MockViewPagerAdapter.MAX_COUNT);
        mLayout.setProgressType(getArguments().getInt(EXTRA_TYPE));
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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setToolbarTitle("Steppers");
    }
}
