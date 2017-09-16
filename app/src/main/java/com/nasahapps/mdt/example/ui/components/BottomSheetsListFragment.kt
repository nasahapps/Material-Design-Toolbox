package com.nasahapps.mdt.example.ui.components

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ListFragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.nasahapps.mdt.R
import com.nasahapps.mdt.bottomsheets.BottomSheetDialogBuilder
import com.nasahapps.mdt.bottomsheets.BottomSheetItem
import com.nasahapps.mdt.example.ui.main.MainActivity

/**
 * Created by Hakeem on 4/13/16.
 */
class BottomSheetsListFragment : ListFragment() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1,
                arrayOf("Simple List", "Simple List with Title", "Simple Grid", "Simple Grid with Title"))
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.setToolbarColor(ContextCompat.getColor(context,
                    R.color.mdt_indigo_500))
            it.setStatusBarColor(ContextCompat.getColor(context,
                    R.color.mdt_indigo_700))
            it.setToolbarTitleTextColor(Color.WHITE)
            it.setToolbarTitle("Bottom Sheets")
            it.setToolbarVisible(true)
        }
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        (activity as? MainActivity)?.let {
            val launcherIntent = Intent(Intent.ACTION_MAIN)
            launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            val appList = activity.packageManager.queryIntentActivities(launcherIntent, 0)
            val sheetItems = appList.map {
                BottomSheetItem(it.loadLabel(activity.packageManager), it.loadIcon(activity.packageManager))
            }
            when (position) {
                0 -> BottomSheetDialogBuilder(activity, false).setItems(sheetItems, null).show()
                1 -> BottomSheetDialogBuilder(activity, false).setTitle("Installed Apps").setItems(sheetItems, null).show()
                2 -> BottomSheetDialogBuilder(activity, true).setItems(sheetItems, null).show()
                3 -> BottomSheetDialogBuilder(activity, true).setTitle("Installed Apps").setItems(sheetItems, null).show()
                else -> {
                }
            }
        }
    }
}
