package com.nasahapps.mdt.example.ui.style

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ListFragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.layout.ToolbarDifferentWidthsActivity
import com.nasahapps.mdt.example.ui.layout.ToolbarFullWidthDefaultHeightActivity
import com.nasahapps.mdt.example.ui.layout.ToolbarFullWidthExtendedHeightActivity
import com.nasahapps.mdt.example.ui.main.MainActivity

/**
 * Created by Hakeem on 4/13/16.
 */
class StructureListFragment : ListFragment() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1,
                arrayOf("Toolbar - full width, default height",
                        "Toolbar - full width, extended height",
                        "Toolbar - different widths (landscape recommended)",
                        "Toolbar - flexible/card",
                        "Toolbar - floating",
                        "Toolbar - detached",
                        "Toolbar - bottom-aligned",
                        "Toolbar - bottom shelf",
                        "App bar - light",
                        "App bar - dark",
                        "App bar - colored",
                        "App bar - transparent",
                        "App bar - single color",
                        "App bar - distinct color",
                        "App bar with menu",
                        "System bars - lean back",
                        "System bars - immersive",
                        "Status bar - dark translucent",
                        "Status bar - dark",
                        "Status bar - light translucent",
                        "Status bar - light",
                        "Navigation bar - light",
                        "Navigation bar - translucent",
                        "Navigation bar - translucent over image",
                        "Navigation bar - transparent",
                        "Navigation bar - transparent over image"))
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.setToolbarColor(ContextCompat.getColor(context,
                    R.color.mdt_pink_800))
            it.setStatusBarColor(ContextCompat.getColor(context,
                    R.color.mdt_pink_900))
            it.setToolbarTitleTextColor(Color.WHITE)
            it.setToolbarTitle("Layout - Structure")
            it.setToolbarVisible(true)
        }
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        (activity as? MainActivity)?.let {
            when (position) {
                0 -> startActivity(Intent(activity, ToolbarFullWidthDefaultHeightActivity::class.java))
                1 -> startActivity(Intent(activity, ToolbarFullWidthExtendedHeightActivity::class.java))
                2 -> startActivity(Intent(activity, ToolbarDifferentWidthsActivity::class.java))
            }
        }
    }
}
