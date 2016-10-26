package com.nasahapps.mdt.example.ui.style

import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.main.MainActivity

/**
 * Created by Hakeem on 4/13/16.
 */
class TypographyFragment : StyleFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_typography
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setToolbarTitle("Typography")
    }
}
