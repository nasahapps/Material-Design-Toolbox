package com.nasahapps.mdt.example.ui.mock

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by Hakeem on 4/17/16.
 */
class MockViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return MockFragment.newInstance(position + 1)
    }

    override fun getCount(): Int {
        return MAX_COUNT
    }

    companion object {
        val MAX_COUNT = 6
    }
}
