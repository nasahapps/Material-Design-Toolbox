package com.nasahapps.mdt.example.ui.layout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.nasahapps.mdt.example.R

/**
 * Created by Hakeem on 10/26/16.
 */
class AppBarLightActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Title"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_two_items_overflow, menu)
        return super.onCreateOptionsMenu(menu)
    }

}