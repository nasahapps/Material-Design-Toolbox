package com.nasahapps.mdt.example.ui.components

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
class ListsListFragment : ListFragment() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1,
                arrayOf("Single Line - Text Only", "Single Line - Text with Icon", "Single Line - Text with Avatar",
                        "Single Line - Text with Icon and Avatar", "Two Line - Text Only",
                        "Two Line - Text with Icon", "Two Line - Text with Avatar",
                        "Two Line - Text with Icon and Avatar", "Three Line - Text Only",
                        "Three Line - Text with Icon", "Three Line - Text with Avatar",
                        "Three Line - Text with Icon and Avatar"))
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.setToolbarColor(ContextCompat.getColor(context,
                    R.color.mdt_indigo_500))
            it.setStatusBarColor(ContextCompat.getColor(context,
                    R.color.mdt_indigo_700))
            it.setToolbarTitleTextColor(Color.WHITE)
            it.setToolbarTitle("Lists")
            it.setToolbarVisible(true)
        }
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        (activity as? MainActivity)?.let {
            val fragment = when (position) {
                1 -> ListListFragment.newInstance(ListListFragment.Type.SINGLE_ITEM_ICON)
                2 -> ListListFragment.newInstance(ListListFragment.Type.SINGLE_ITEM_AVATAR)
                3 -> ListListFragment.newInstance(ListListFragment.Type.SINGLE_ITEM_AVATAR_ICON)
                else -> ListListFragment.newInstance(ListListFragment.Type.SINGLE_ITEM_TEXT)
            }

            it.startFragment(fragment)
        }
    }
}
