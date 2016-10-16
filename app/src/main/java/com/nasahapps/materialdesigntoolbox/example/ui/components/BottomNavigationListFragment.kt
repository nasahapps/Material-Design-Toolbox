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
class BottomNavigationListFragment : ListFragment() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1,
                arrayOf("Three Tabs", "Four Tabs", "Five Tabs", "Three Tabs Colored",
                        "Four Tabs Colored", "Five Tabs Colored"))
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.setToolbarColor(ContextCompat.getColor(context,
                    R.color.mdt_indigo_500))
            it.setStatusBarColor(ContextCompat.getColor(context,
                    R.color.mdt_indigo_700))
            it.setToolbarTitleTextColor(Color.WHITE)
            it.setToolbarTitle("Bottom Navigation")
        }
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        (activity as? MainActivity)?.let {
            val fragment = when (position) {
                0 -> BottomNavigationFragment.newInstance(3)
                1 -> BottomNavigationFragment.newInstance(4)
                2 -> BottomNavigationFragment.newInstance(5)
                3 -> BottomNavigationFragment.newInstance(3, true)
                4 -> BottomNavigationFragment.newInstance(4, true)
                5 -> BottomNavigationFragment.newInstance(5, true)
                else -> BottomNavigationFragment.newInstance(3)
            }

            it.startFragment(fragment)
        }
    }
}
