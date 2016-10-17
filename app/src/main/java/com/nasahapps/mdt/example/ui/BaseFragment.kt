package com.nasahapps.mdt.example.ui

import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import com.nasahapps.mdt.example.ui.main.MainActivity

/**
 * Created by Hakeem on 4/13/16.
 */
abstract class BaseFragment : com.nasahapps.nasahutils.app.BaseFragment() {

    @ColorInt
    protected fun getColorInt(@ColorRes colorRes: Int) = (activity as? MainActivity)?.getColorInt(colorRes) ?: Color.BLACK

}
