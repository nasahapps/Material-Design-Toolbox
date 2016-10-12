package com.nasahapps.materialdesigntoolbox.example.ui.components

import android.os.Bundle
import android.view.View
import com.nasahapps.materialdesigntoolbox.components.Tooltip
import com.nasahapps.materialdesigntoolbox.example.R
import com.nasahapps.materialdesigntoolbox.example.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_tooltip.*

/**
 * Created by Hakeem on 4/13/16.
 */
class TooltipFragment : ComponentFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_tooltip
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button?.setOnClickListener { showTooltip(it) }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setToolbarTitle("Tooltips")
    }

    private fun showTooltip(v: View) {
        Tooltip.makeTooltip(activity, "Hello world!", Tooltip.LENGTH_SHORT, v).show()
    }
}
