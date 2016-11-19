package com.nasahapps.mdt.example.ui

import android.os.Bundle
import com.nasahapps.mdt.example.R
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chip?.setOnClickListener { }
    }

}
