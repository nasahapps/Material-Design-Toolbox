package com.nasahapps.mdt.example.ui.layout

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nasahapps.mdt.Utils
import com.nasahapps.mdt.example.R
import kotlinx.android.synthetic.main.activity_toolbar_different_widths.*

/**
 * Created by hhasan on 10/26/16.
 */
class ToolbarDifferentWidthsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_different_widths)
        leftToolbar?.inflateMenu(R.menu.menu_toolbar_one_item)
        rightToolbar?.inflateMenu(R.menu.menu_toolbar_one_item_overflow)
        rightToolbar?.menu?.let {
            for (i in 0..it.size() - 1) {
                it.getItem(i).icon = Utils.getTintedDrawableCompat(it.getItem(i).icon, Color.WHITE)
            }
        }
    }

}