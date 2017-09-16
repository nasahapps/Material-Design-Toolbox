package com.nasahapps.mdt.bottomsheets

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by hhasan on 10/14/16.
 */

class BottomSheetGridAdapter(items: List<BottomSheetItem>, dialog: DialogInterface,
                             listener: BottomSheetAdapter.OnClickListener?) : BottomSheetAdapter(items, dialog, listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.mdt_grid_bottom_sheet_item, parent, false)
        return BottomSheetAdapter.ViewHolder(v)
    }
}
