package com.nasahapps.mdt.example.ui.style

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ListFragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.main.MainActivity

/**
 * Created by Hakeem on 4/13/16.
 */
class StyleListFragment : ListFragment() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1,
                arrayOf("Color"))
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.setToolbarColor(ContextCompat.getColor(context,
                    R.color.mdt_teal_500))
            it.setStatusBarColor(ContextCompat.getColor(context,
                    R.color.mdt_teal_700))
            it.setToolbarTitleTextColor(Color.WHITE)
            it.setToolbarTitle("Style")
            it.setToolbarVisible(true)
        }
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        (activity as? MainActivity)?.let {
            when (position) {
                0 -> it.startFragment(ColorViewPagerFragment())
            }
        }
    }
}
