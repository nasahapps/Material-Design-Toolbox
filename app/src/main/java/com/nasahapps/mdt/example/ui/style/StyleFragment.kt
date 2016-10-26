package com.nasahapps.mdt.example.ui.style

import android.graphics.Color

import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.BaseFragment
import com.nasahapps.mdt.example.ui.main.MainActivity

/**
 * Created by Hakeem on 4/13/16.
 */
abstract class StyleFragment : BaseFragment() {

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.setToolbarColor(getColorInt(R.color.mdt_teal_500))
            it.setStatusBarColor(getColorInt(R.color.mdt_teal_700))
            it.setToolbarTitleTextColor(Color.WHITE)
            it.setToolbarVisible(true)
        }
    }
}
