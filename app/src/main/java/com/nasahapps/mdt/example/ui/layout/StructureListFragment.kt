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
import com.nasahapps.mdt.example.ui.layout.*
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
                        "Toolbar - flexible/card (landscape recommended)",
                        "Toolbar - floating",
                        "App bar - light",
                        "App bar - dark",
                        "App bar - colored",
                        "App bar - transparent",
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
                3 -> startActivity(Intent(activity, ToolbarFlexibleCardActivity::class.java))
                4 -> startActivity(Intent(activity, ToolbarFloatingActivity::class.java))
                5 -> startActivity(Intent(activity, AppBarLightActivity::class.java))
                6 -> startActivity(Intent(activity, AppBarDarkActivity::class.java))
                7 -> startActivity(Intent(activity, AppBarColoredActivity::class.java))
                8 -> startActivity(Intent(activity, AppBarTransparentActivity::class.java))
                9 -> startActivity(Intent(activity, AppBarMenuActivity::class.java))
            }
        }
    }
}
