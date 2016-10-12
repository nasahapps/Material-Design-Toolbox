package com.nasahapps.materialdesigntoolbox.example.ui.components

import android.view.View
import butterknife.OnClick
import com.nasahapps.materialdesigntoolbox.components.Tooltip
import com.nasahapps.materialdesigntoolbox.example.R
import com.nasahapps.materialdesigntoolbox.example.ui.main.MainActivity

/**
 * Created by Hakeem on 4/13/16.
 */
class TooltipFragment : ComponentFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_tooltip
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setToolbarTitle("Tooltips")
    }

    @OnClick(R.id.button)
    fun showTooltip(v: View) {
        Tooltip.makeTooltip(activity, "Hello world!", Tooltip.LENGTH_SHORT, v).show()
    }
}
