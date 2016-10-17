package com.nasahapps.mdt.example.ui

import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat

/**
 * Created by Hakeem on 4/13/16.
 */
abstract class BaseActivity : com.nasahapps.nasahutils.app.BaseActivity() {

    @ColorInt
    fun getColorInt(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

}
