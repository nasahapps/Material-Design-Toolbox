package com.nasahapps.mdt.example.ui.components

import android.graphics.Color

import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.BaseFragment
import com.nasahapps.mdt.example.ui.main.MainActivity

/**
 * Created by Hakeem on 4/13/16.
 */
abstract class ComponentFragment : BaseFragment() {

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.setToolbarColor(getColorInt(R.color.mdt_indigo_500))
            it.setStatusBarColor(getColorInt(R.color.mdt_indigo_700))
            it.setToolbarTitleTextColor(Color.WHITE)
        }
    }
}
