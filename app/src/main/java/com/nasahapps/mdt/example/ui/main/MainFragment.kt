package com.nasahapps.mdt.example.ui.main

import android.os.Bundle
import android.support.v4.app.ListFragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.components.ComponentListFragment

/**
 * Created by Hakeem on 4/13/16.
 */
class MainFragment : ListFragment() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1,
                arrayOf("Components"))
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setToolbarColor(ContextCompat.getColor(context, R.color.mdt_cyan_500))
        (activity as MainActivity).setStatusBarColor(ContextCompat.getColor(context, R.color.mdt_cyan_700))
        (activity as MainActivity).setToolbarTitleTextColor(ContextCompat.getColor(context, R.color.mdt_black_87))
        (activity as MainActivity).setToolbarTitle(getString(R.string.app_name))
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        when (position) {
            0 -> (activity as MainActivity).startFragment(ComponentListFragment())
        }
    }
}
