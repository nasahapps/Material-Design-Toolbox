package com.nasahapps.mdt.example.ui.components

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import com.nasahapps.mdt.components.StepperProgressLayout
import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.main.MainActivity
import com.nasahapps.mdt.example.ui.mock.MockViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_stepper_progress.*

/**
 * Created by Hakeem on 4/17/16.
 */
class StepperProgressFragment : ComponentFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_stepper_progress
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager?.adapter = MockViewPagerAdapter(fragmentManager)
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                layout?.progress = position + 1
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        layout?.maxProgress = MockViewPagerAdapter.MAX_COUNT
        layout?.progressType = arguments.getInt(EXTRA_TYPE)
        layout?.addOnStepperProgressListener(object : StepperProgressLayout.OnStepperProgressListener {
            override fun onStepSelected(position: Int) {
                viewPager?.setCurrentItem(position, true)
            }

            override fun onStepDeselected(position: Int) {

            }

            override fun onStepsFinished() {

            }
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setToolbarTitle("Steppers")
    }

    companion object {

        private val EXTRA_TYPE = "type"

        fun newInstance(@StepperProgressLayout.ProgressType type: Int): StepperProgressFragment {
            val args = Bundle()
            args.putInt(EXTRA_TYPE, type)

            val fragment = StepperProgressFragment()
            fragment.arguments = args

            return fragment
        }
    }
}
