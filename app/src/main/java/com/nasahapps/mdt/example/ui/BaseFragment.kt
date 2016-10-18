package com.nasahapps.mdt.example.ui

import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nasahapps.mdt.example.ui.main.MainActivity

/**
 * Created by Hakeem on 4/13/16.
 */
abstract class BaseFragment : Fragment() {

    @LayoutRes
    protected open fun getLayoutId() = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = if (getLayoutId() != 0) {
            inflater?.inflate(getLayoutId(), container, false)
        } else {
            null
        }

        return v
    }

    @ColorInt
    protected fun getColorInt(@ColorRes colorRes: Int) = (activity as? MainActivity)?.getColorInt(colorRes) ?: Color.BLACK

}
