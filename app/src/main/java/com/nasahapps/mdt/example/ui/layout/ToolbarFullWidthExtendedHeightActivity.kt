package com.nasahapps.mdt.example.ui.layout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nasahapps.mdt.example.R
import kotlinx.android.synthetic.main.activity_toolbar_full_width_default_height.*

/**
 * Created by hhasan on 10/26/16.
 */
class ToolbarFullWidthExtendedHeightActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_full_width_extended_height)
        toolbar?.inflateMenu(R.menu.menu_toolbar_three_items_overflow)
    }

}