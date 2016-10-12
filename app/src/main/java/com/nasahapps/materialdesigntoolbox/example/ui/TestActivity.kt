package com.nasahapps.materialdesigntoolbox.example.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.nasahapps.materialdesigntoolbox.example.R
import com.nasahapps.materialdesigntoolbox.example.ui.mock.MockRecyclerViewAdapter
import com.nasahapps.mdt.bottomnavigation.BottomNavigationBar
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = MockRecyclerViewAdapter()

        bottomNav?.addTab(bottomNav?.newTab("Recents", ContextCompat.getDrawable(this, R.drawable.ic_na_test_history)))
        bottomNav?.addTab(bottomNav?.newTab("Favorites", ContextCompat.getDrawable(this, R.drawable.ic_na_test_favorite)))
        bottomNav?.addTab(bottomNav?.newTab("Nearby", ContextCompat.getDrawable(this, R.drawable.ic_na_test_location)))
        bottomNav?.addTab(bottomNav?.newTab("Music", ContextCompat.getDrawable(this, R.drawable.ic_na_test_music)))
        bottomNav?.addTab(bottomNav?.newTab("Movies", ContextCompat.getDrawable(this, R.drawable.ic_na_test_movie)))
//        bottomNav?.setBackgroundColorResources(R.color.nh_cyan_500, R.color.nh_indigo_500, R.color.nh_red_500, R.color.nh_pink_500, R.color.nh_purple_500)
        bottomNav?.addOnTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                Log.d("Test", "Position $position selected")
            }

            override fun onTabUnselected(position: Int) {
                Log.d("Test", "Position $position unselected")
            }

            override fun onTabReselected(position: Int) {
                Log.d("Test", "Position $position reselected")
            }
        })

        button?.setOnClickListener {
            Snackbar.make(findViewById(android.R.id.content), "Snackbar!", Snackbar.LENGTH_SHORT).show()
        }
    }

}
