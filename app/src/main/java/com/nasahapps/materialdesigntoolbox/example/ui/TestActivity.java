package com.nasahapps.materialdesigntoolbox.example.ui;

import com.nasahapps.materialdesigntoolbox.components.StepperLayout;
import com.nasahapps.materialdesigntoolbox.example.R;

import butterknife.Bind;
import butterknife.OnClick;

public class TestActivity extends BaseActivity {

    @Bind(R.id.stepperLayout)
    StepperLayout mStepperLayout;

    int mCurrentStep;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @OnClick(R.id.nextStep)
    public void nextStep() {
        if (mCurrentStep >= mStepperLayout.getStepperCount()) {
            mCurrentStep = 0;
            for (int i = 0; i < mStepperLayout.getStepperCount(); i++) {
                mStepperLayout.getStepper(i).setCompleted(false);
                mStepperLayout.getStepper(i).setActive(false);
            }
            mStepperLayout.getStepper(0).setActive(true);
            mStepperLayout.smoothScrollTo((int) mStepperLayout.getStepper(mCurrentStep).getX(),
                    (int) mStepperLayout.getStepper(mCurrentStep).getY());
            return;
        }

        mStepperLayout.getStepper(mCurrentStep).setCompleted(true);
        mCurrentStep++;
        if (mCurrentStep < mStepperLayout.getStepperCount()) {
            mStepperLayout.getStepper(mCurrentStep).setActive(true);
            mStepperLayout.smoothScrollTo((int) mStepperLayout.getStepper(mCurrentStep).getX(),
                    (int) mStepperLayout.getStepper(mCurrentStep).getY());
        }
    }

}
