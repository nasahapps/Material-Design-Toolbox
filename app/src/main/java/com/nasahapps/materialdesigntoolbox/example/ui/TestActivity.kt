package com.nasahapps.materialdesigntoolbox.example.ui

import android.content.Intent
import android.os.Bundle
import com.nasahapps.materialdesigntoolbox.example.R
import com.nasahapps.mdt.bottomsheets.BottomSheetItem
import com.nasahapps.mdt.bottomsheets.BottomSheetUtils
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        button?.setOnClickListener {
            val launcherIntent = Intent(Intent.ACTION_MAIN)
            launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            val appList = packageManager.queryIntentActivities(launcherIntent, 0)
            val sheetItems = appList.map { BottomSheetItem(it.loadLabel(packageManager), it.loadIcon(packageManager)) }
            BottomSheetUtils.DialogBuilder(this, false)
                    .setTitle("Open in")
                    .setItems(R.menu.menu_bottom_sheet, null)
                    .show()
        }

        shareButton?.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
            i.type = "text/plain"
            startActivity(Intent.createChooser(i, "Send to..."))
        }
    }

}
