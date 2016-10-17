package com.nasahapps.mdt.example.ui.main

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.v4.app.Fragment
import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.container, MainFragment()).commit()
        }
    }

    fun startFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarColor(@ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
        }
    }

    fun setToolbarColor(@ColorInt color: Int) {
        toolbar?.setBackgroundColor(color)
    }

    fun setToolbarTitleTextColor(@ColorInt color: Int) {
        toolbar?.setTitleTextColor(color)
    }

    fun setToolbarTitle(text: CharSequence) {
        toolbar?.title = text
    }
}
