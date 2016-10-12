package com.nasahapps.materialdesigntoolbox.example.ui.components

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.nasahapps.materialdesigntoolbox.example.R
import com.nasahapps.materialdesigntoolbox.example.ui.main.MainActivity
import com.nasahapps.materialdesigntoolbox.example.ui.mock.MockFragment
import com.nasahapps.mdt.bottomnavigation.BottomNavigationBar
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*

/**
 * Created by Hakeem on 4/13/16.
 */
class BottomNavigationFragment : ComponentFragment() {

    companion object {
        val EXTRA_NUM_TABS = "numTabs"
        val EXTRA_COLORED = "colored"

        fun newInstance(numOfTabs: Int, colored: Boolean = false): BottomNavigationFragment {
            val args = Bundle()
            args.putInt(EXTRA_NUM_TABS, numOfTabs)
            args.putBoolean(EXTRA_COLORED, colored)
            val fragment = BottomNavigationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    val TITLES = arrayOf("Recents", "Favorites", "Nearby", "Music", "Movies")
    val ICONS = intArrayOf(R.drawable.ic_na_test_history, R.drawable.ic_na_test_favorite,
            R.drawable.ic_na_test_location, R.drawable.ic_na_test_music, R.drawable.ic_na_test_movie)
    val COLORS = intArrayOf(R.color.nh_cyan_500, R.color.nh_purple_500, R.color.nh_red_500,
            R.color.nh_indigo_500, R.color.nh_pink_500)

    override fun getLayoutId(): Int {
        return R.layout.fragment_bottom_navigation
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i in 0..arguments.getInt(EXTRA_NUM_TABS) - 1) {
            bottomNav?.addTab(bottomNav?.newTab(TITLES[i], ContextCompat.getDrawable(activity, ICONS[i])))
        }

        if (arguments.getBoolean(EXTRA_COLORED)) {
            val newArray = IntArray(arguments.getInt(EXTRA_NUM_TABS))
            for (i in 0..arguments.getInt(EXTRA_NUM_TABS) - 1) {
                newArray[i] = COLORS[i]
            }
            bottomNav?.setBackgroundColorResources(*newArray)
            bottomNav?.activeColor = Color.WHITE
            bottomNav?.setDarkTheme(true)
        }

        bottomNav?.addOnTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                childFragmentManager.beginTransaction()
                        .replace(R.id.innerContainer, MockFragment.newInstance(position))
                        .commit()
            }

            override fun onTabUnselected(position: Int) {
            }

            override fun onTabReselected(position: Int) {
            }
        })

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                    .add(R.id.innerContainer, MockFragment.newInstance(0))
                    .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setToolbarTitle("Bottom Navigation Bar")
    }
}
