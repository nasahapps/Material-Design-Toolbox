package com.nasahapps.mdt.example.ui.layout

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.nasahapps.mdt.Utils
import com.nasahapps.mdt.example.R

/**
 * Created by Hakeem on 10/26/16.
 */
class AppBarColoredActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Title"
        supportActionBar?.setHomeAsUpIndicator(Utils.getTintedDrawableCompat(ContextCompat.getDrawable(this, R.drawable.ic_menu), Color.WHITE))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_two_items_overflow, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            for (i in 0..it.size() - 1) {
                it.getItem(i)?.icon = Utils.getTintedDrawableCompat(it.getItem(i)?.icon, Color.WHITE)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

}