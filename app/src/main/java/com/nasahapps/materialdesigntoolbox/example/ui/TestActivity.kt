package com.nasahapps.materialdesigntoolbox.example.ui

import android.os.Bundle
import android.support.v4.view.ViewPager
import com.nasahapps.materialdesigntoolbox.components.StepperProgressLayout
import com.nasahapps.materialdesigntoolbox.example.R
import com.nasahapps.materialdesigntoolbox.example.ui.adapter.MockViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_stepper_progress.*

class TestActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewPager?.adapter = MockViewPagerAdapter(supportFragmentManager)
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                layout?.progress = position + 1
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
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

}
