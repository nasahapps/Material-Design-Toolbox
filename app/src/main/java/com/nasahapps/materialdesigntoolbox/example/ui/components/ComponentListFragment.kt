package com.nasahapps.materialdesigntoolbox.example.ui.components

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ListFragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

import com.nasahapps.materialdesigntoolbox.example.R
import com.nasahapps.materialdesigntoolbox.example.ui.main.MainActivity

/**
 * Created by Hakeem on 4/13/16.
 */
class ComponentListFragment : ListFragment() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1,
                arrayOf("Bottom Navigation", "Bottom Sheets", "Buttons", "Steppers", "Tooltips"))
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.setToolbarColor(ContextCompat.getColor(context,
                    R.color.mdt_indigo_500))
            it.setStatusBarColor(ContextCompat.getColor(context,
                    R.color.mdt_indigo_700))
            it.setToolbarTitleTextColor(Color.WHITE)
            it.setToolbarTitle("Components")
        }
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        (activity as? MainActivity)?.let {
            when (position) {
                0 -> it.startFragment(BottomNavigationListFragment())
                1 -> it.startFragment(BottomSheetsListFragment())
                2 -> it.startFragment(ButtonFragment())
                3 -> it.startFragment(StepperListFragment())
                4 -> it.startFragment(TooltipFragment())
            }
        }
    }
}
