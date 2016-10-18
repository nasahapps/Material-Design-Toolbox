package com.nasahapps.mdt.example.ui

import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

/**
 * Created by Hakeem on 4/13/16.
 */
abstract class BaseActivity : AppCompatActivity() {

    @LayoutRes
    protected open fun getLayoutId() = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getLayoutId() != 0) {
            setContentView(getLayoutId())
        }
    }

    @ColorInt
    fun getColorInt(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

}
