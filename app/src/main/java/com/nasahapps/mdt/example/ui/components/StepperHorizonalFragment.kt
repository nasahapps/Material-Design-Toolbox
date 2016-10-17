package com.nasahapps.mdt.example.ui.components

import android.os.Bundle
import android.view.View
import com.nasahapps.mdt.components.Stepper
import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_stepper_horizontal_layout.*

/**
 * Created by Hakeem on 4/17/16.
 */
class StepperHorizonalFragment : ComponentFragment() {

    internal var mCurrentStep: Int = 0
    internal var mMaxStep: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_stepper_horizontal_layout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMaxStep = layout.stepperCount

        button?.setOnClickListener { nextStep() }
    }

    private fun nextStep() {
        if (mCurrentStep >= mMaxStep) {
            // Reset back to step 1
            mCurrentStep = 0
            for (i in 0..layout.stepperCount - 1) {
                layout?.setStepperState(i, Stepper.INACTIVE)
            }

            layout?.setStepperState(mCurrentStep, Stepper.ACTIVE)
        } else {
            // Increment a step
            layout?.setStepperState(mCurrentStep, Stepper.COMPLETED)
            mCurrentStep++
            if (mCurrentStep < mMaxStep) {
                layout?.setStepperState(mCurrentStep, Stepper.ACTIVE)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setToolbarTitle("Steppers")
    }
}
