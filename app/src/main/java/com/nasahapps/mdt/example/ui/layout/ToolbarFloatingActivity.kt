package com.nasahapps.mdt.example.ui.layout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nasahapps.mdt.example.R
import kotlinx.android.synthetic.main.activity_toolbar_floating.*

/**
 * Created by hhasan on 10/26/16.
 */
class ToolbarFloatingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_floating)
        toolbar?.inflateMenu(R.menu.menu_toolbar_one_item_map_overflow)
    }

}