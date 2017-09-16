package com.nasahapps.mdt.bottomsheets

import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.nasahapps.mdt.Utils

/**
 * Created by hhasan on 10/14/16.
 */

abstract class BottomSheetAdapter constructor(private val mItems: List<BottomSheetItem>,
                                              private val mDialog: DialogInterface,
                                              private val mListener: OnClickListener?) : RecyclerView.Adapter<BottomSheetAdapter.ViewHolder>() {

    private var mIconTint: Int = 0

    fun setIconTint(iconTint: Int) {
        mIconTint = iconTint
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.text.text = item.text
        var icon = item.icon
        if (icon != null && mIconTint != 0) {
            icon = Utils.getTintedDrawable(icon, mIconTint)
        }
        holder.icon.setImageDrawable(icon)

        holder.itemView.setOnClickListener {
            mListener?.onClick(mDialog, holder.adapterPosition, mItems[holder.adapterPosition].menuId)
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    interface OnClickListener {
        fun onClick(dialog: DialogInterface, position: Int, id: Int)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var text = v.findViewById<TextView>(R.id.text)
        var icon = v.findViewById<ImageView>(R.id.icon)

    }

}
