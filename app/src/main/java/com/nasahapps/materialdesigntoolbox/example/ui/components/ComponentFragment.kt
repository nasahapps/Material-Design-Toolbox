package com.nasahapps.materialdesigntoolbox.example.ui.components

import android.graphics.Color

import com.nasahapps.materialdesigntoolbox.example.R
import com.nasahapps.materialdesigntoolbox.example.ui.BaseFragment
import com.nasahapps.materialdesigntoolbox.example.ui.main.MainActivity

/**
 * Created by Hakeem on 4/13/16.
 */
abstract class ComponentFragment : BaseFragment() {

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.setToolbarColor(getColorInt(R.color.nh_indigo_500))
            it.setStatusBarColor(getColorInt(R.color.nh_indigo_700))
            it.setToolbarTitleTextColor(Color.WHITE)
        }
    }
}
