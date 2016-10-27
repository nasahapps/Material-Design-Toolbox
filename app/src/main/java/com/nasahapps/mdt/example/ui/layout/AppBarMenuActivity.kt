package com.nasahapps.mdt.example.ui.layout

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.TextViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.nasahapps.mdt.Utils
import com.nasahapps.mdt.example.R
import kotlinx.android.synthetic.main.activity_app_bar_menu.*

/**
 * Created by Hakeem on 10/26/16.
 */
class AppBarMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar_menu)
        toolbar?.navigationIcon = Utils.getTintedDrawableCompat(toolbar?.navigationIcon, Color.WHITE)
        toolbar?.inflateMenu(R.menu.menu_toolbar_two_items_overflow)
        toolbar?.menu?.let {
            for (i in 0..it.size() - 1) {
                it.getItem(i).icon = Utils.getTintedDrawableCompat(it.getItem(i).icon, Color.WHITE)
            }
        }

        val adapter = CustomArrayAdapter(this, android.R.layout.simple_spinner_item, arrayOf("All", "Family", "Friends", "Coworkers"))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = adapter
    }

    class CustomArrayAdapter(c: Context, layoutRes: Int, items: Array<String>) : ArrayAdapter<String>(c, layoutRes, items) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val v = super.getView(position, convertView, parent)
            (v as? TextView)?.let {
                TextViewCompat.setTextAppearance(it, R.style.TextAppearance_AppCompat_Title)
                it.setTextColor(Color.WHITE)
            }

            return v
        }
    }

}