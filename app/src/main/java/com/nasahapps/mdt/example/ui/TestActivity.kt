package com.nasahapps.mdt.example.ui

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import com.nasahapps.mdt.example.R

class TestActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById(android.R.id.content).setOnClickListener {
            CustomFragment().show(supportFragmentManager, "TAG")
        }
    }

    /**
     * Called when the bottom sheet changes its state
     *
     * The newState is one of the following:
     * BottomSheetBehavior.STATE_COLLAPSED: bottomSheet is collapsed
     * BottomSheetBehavior.STATE_DRAGGING: bottomSheet is being dragged
     * BottomSheetBehavior.STATE_EXPANDED: bottomSheet is expanded
     * BottomSheetBehavior.STATE_HIDDEN: bottomSheet is hidden
     * BottomSheetBehavior.STATE_SETTLING: bottomSheet is settling
     */
    class CustomFragment : BottomSheetDialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialog = super.onCreateDialog(savedInstanceState)
            dialog.setContentView(R.layout.layout_test)
            return dialog
        }
    }

}
