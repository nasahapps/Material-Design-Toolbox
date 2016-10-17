package com.nasahapps.mdt.example.ui.mock

import android.os.Bundle
import android.support.v4.widget.TextViewCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.nasahapps.mdt.example.ui.BaseFragment

/**
 * Created by Hakeem on 4/17/16.
 */
class MockFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = TextView(context)
        TextViewCompat.setTextAppearance(v, android.support.v7.appcompat.R.style.TextAppearance_AppCompat)
        v.text = "Page ${arguments.getInt(EXTRA_PAGE)}"
        v.gravity = Gravity.CENTER
        return v
    }

    companion object {

        private val EXTRA_PAGE = "page"

        fun newInstance(page: Int): MockFragment {
            val args = Bundle()
            args.putInt(EXTRA_PAGE, page)

            val fragment = MockFragment()
            fragment.arguments = args

            return fragment
        }
    }
}
