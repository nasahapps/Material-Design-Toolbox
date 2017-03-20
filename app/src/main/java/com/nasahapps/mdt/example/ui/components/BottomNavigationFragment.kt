package com.nasahapps.mdt.example.ui.components

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.main.MainActivity
import com.nasahapps.mdt.example.ui.mock.MockFragment
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*

/**
 * Created by Hakeem on 4/13/16.
 */
class BottomNavigationFragment : ComponentFragment() {

    companion object {
        val EXTRA_NUM_TABS = "numTabs"
        val EXTRA_COLORED = "colored"
        private val BUNDLE_SELECTED_ITEM = "selectedItem"

        fun newInstance(numOfTabs: Int, colored: Boolean = false): BottomNavigationFragment {
            val args = Bundle()
            args.putInt(EXTRA_NUM_TABS, numOfTabs)
            args.putBoolean(EXTRA_COLORED, colored)
            val fragment = BottomNavigationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val COLOR_BGS = intArrayOf(R.drawable.bottom_nav_item_bg_cyan, R.drawable.bottom_nav_item_bg_purple,
            R.drawable.bottom_nav_item_bg_red, R.drawable.bottom_nav_item_bg_indigo, R.drawable.bottom_nav_item_bg_pink)

    private var mSelectedItem = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_bottom_navigation
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            mSelectedItem = it.getInt(BUNDLE_SELECTED_ITEM)
        }

        if (arguments.getBoolean(EXTRA_COLORED)) {
            setTabBackgroundColorForPosition(mSelectedItem)
            bottomNav?.itemIconTintList = ContextCompat.getColorStateList(context, R.color.color_bottom_nav_inverse)
            bottomNav?.itemTextColor = ContextCompat.getColorStateList(context, R.color.color_bottom_nav_inverse)
        }

        bottomNav?.setOnNavigationItemSelectedListener { item ->
            val position = when (item.itemId) {
                R.id.itemFive -> 4
                R.id.itemFour -> 3
                R.id.itemThree -> 2
                R.id.itemTwo -> 1
                else -> 0
            }
            mSelectedItem = position
            childFragmentManager.beginTransaction()
                    .replace(R.id.innerContainer, MockFragment.newInstance(position))
                    .commit()

            if (arguments.getBoolean(EXTRA_COLORED)) {
                setTabBackgroundColorForPosition(position)
            }

            true
        }

        val menuRes = when (arguments.getInt(EXTRA_NUM_TABS)) {
            5 -> R.menu.menu_bottom_nav_five_items
            4 -> R.menu.menu_bottom_nav_four_items
            else -> R.menu.menu_bottom_nav_three_items
        }
        bottomNav?.inflateMenu(menuRes)

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                    .add(R.id.innerContainer, MockFragment.newInstance(0))
                    .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(BUNDLE_SELECTED_ITEM, mSelectedItem)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setToolbarTitle("Bottom Navigation Bar")
    }

    private fun setTabBackgroundColorForPosition(position: Int) {
        bottomNav?.setBackgroundResource(COLOR_BGS[position])
    }
}
