package com.nasahapps.mdt.example.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.mock.MockRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = MockRecyclerViewAdapter()
    }

}
