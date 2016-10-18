package com.nasahapps.mdt.example.ui

import android.os.Bundle
import com.nasahapps.mdt.FabAnimationHelper
import com.nasahapps.mdt.example.R
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : BaseActivity() {

    lateinit var mHelper: FabAnimationHelper
    var mAnimated = false

    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHelper = FabAnimationHelper(fab)

        button?.setOnClickListener {
            if (mAnimated) {
                mHelper.animateFromView(bottomView)
            } else {
                mHelper.animateToView(bottomView)
            }
            mAnimated = !mAnimated
        }
    }

}
