package com.nasahapps.materialdesigntoolbox.example.ui.components;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.nasahapps.materialdesigntoolbox.components.HorizontalStepperLayout;
import com.nasahapps.materialdesigntoolbox.components.Stepper;
import com.nasahapps.materialdesigntoolbox.example.R;
import com.nasahapps.materialdesigntoolbox.example.ui.main.MainActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Hakeem on 4/17/16.
 */
public class StepperHorizonalFragment extends ComponentFragment {

    @Bind(R.id.layout)
    HorizontalStepperLayout mStepperLayout;

    int mCurrentStep, mMaxStep;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_stepper_horizontal_layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMaxStep = mStepperLayout.getStepperCount();
    }

    @OnClick(R.id.button)
    public void nextStep() {
        if (mCurrentStep >= mMaxStep) {
            // Reset back to step 1
            mCurrentStep = 0;
            for (int i = 0; i < mStepperLayout.getStepperCount(); i++) {
                mStepperLayout.setStepperState(i, Stepper.INACTIVE);
            }

            mStepperLayout.setStepperState(mCurrentStep, Stepper.ACTIVE);
        } else {
            // Increment a step
            mStepperLayout.setStepperState(mCurrentStep, Stepper.COMPLETED);
            mCurrentStep++;
            if (mCurrentStep < mMaxStep) {
                mStepperLayout.setStepperState(mCurrentStep, Stepper.ACTIVE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setToolbarTitle("Steppers");
    }
}
