package com.nasahapps.materialdesigntoolbox.example.ui.components

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ListFragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

import com.nasahapps.materialdesigntoolbox.components.StepperProgressLayout
import com.nasahapps.materialdesigntoolbox.example.R
import com.nasahapps.materialdesigntoolbox.example.ui.main.MainActivity

/**
 * Created by Hakeem on 4/13/16.
 */
class StepperListFragment : ListFragment() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1,
                arrayOf("Stepper Horizontal Layout", "Stepper Progress w/Text", "Stepper Progress w/Dots", "Stepper Progress w/Bar"))
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.setToolbarColor(ContextCompat.getColor(context,
                    R.color.mdt_indigo_500))
            it.setStatusBarColor(ContextCompat.getColor(context,
                    R.color.mdt_indigo_700))
            it.setToolbarTitleTextColor(Color.WHITE)
            it.setToolbarTitle("Steppers")
        }
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        (activity as? MainActivity)?.let {
            when (position) {
                0 -> it.startFragment(StepperHorizonalFragment())
                1 -> it.startFragment(StepperProgressFragment.newInstance(StepperProgressLayout.TYPE_TEXT))
                2 -> it.startFragment(StepperProgressFragment.newInstance(StepperProgressLayout.TYPE_DOTS))
                3 -> it.startFragment(StepperProgressFragment.newInstance(StepperProgressLayout.TYPE_BAR))
            }
        }
    }
}
