package com.nasahapps.mdt.bottomsheets

import android.content.Context
import android.content.DialogInterface
import android.support.annotation.ColorInt
import android.support.annotation.MenuRes
import android.support.design.widget.BottomSheetDialog
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nasahapps.mdt.Utils
import java.util.ArrayList

/**
 * Created by Hakeem on 10/17/16.
 */
class BottomSheetDialogBuilder(c: Context, private val mIsGrid: Boolean) {

    private val mDialog = BottomSheetDialog(c)
    private var mIconTint: Int = 0

    init {
        val view = LayoutInflater.from(c).inflate(R.layout.mdt_layout_bottom_sheet_list, null)
        mDialog.setContentView(view)
        // Set RecyclerView LayoutManager depending on if user wants a grid or not
        val recyclerView = view.findViewById<View>(R.id.bottomSheetRecyclerView) as RecyclerView
        val columns: Int
        if (mIsGrid) {
            // 4 columns for large tablets
            if (Utils.isLargeTablet(c)) {
                columns = 4
            } else {
                // 4 columns in landscape, 3 in portrait
                columns = if (Utils.isPortrait(c)) 3 else 4
            }
        } else {
            // 2 columns for tablets
            if (Utils.isTablet(c)) {
                columns = 2
            } else {
                // 2 columns in landscape, 1 in portrait
                columns = if (Utils.isPortrait(c)) 1 else 2
            }
        }
        recyclerView.layoutManager = GridLayoutManager(c, columns)
        recyclerView.addOnScrollListener(OnBottomSheetScrollListener())

        // Add top padding if grid
        if (mIsGrid) {
            recyclerView.setPadding(recyclerView.paddingLeft, c.resources.getDimensionPixelSize(R.dimen.mdt_bottom_sheet_grid_top_padding),
                    recyclerView.paddingRight, recyclerView.paddingBottom)
        }

        // Adjust side padding for tablets
        val sidePadding = if (Utils.isTablet(c)) Utils.getScreenWidth(c) / 6 else 0
        val dialogViewParent = mDialog.findViewById<View>(R.id.bottomSheetLayout)!!.parent.parent as View
        dialogViewParent.setPadding(sidePadding, dialogViewParent.paddingTop, sidePadding,
                dialogViewParent.paddingBottom)
    }

    fun setTitle(title: CharSequence): BottomSheetDialogBuilder {
        (mDialog.findViewById<View>(R.id.title) as TextView).text = title
        return this
    }

    //        public DialogBuilder setAdapter(BottomSheetAdapter adapter) {
    //            RecyclerView recyclerView = (RecyclerView) mDialog.findViewById(R.id.bottomSheetRecyclerView);
    //            if (adapter instanceof BottomSheetListAdapter) {
    //                ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(Utils.isPortrait(mDialog.getContext()) ? 3 : 4);
    //                mIsGrid = false;
    //            } else if (adapter instanceof BottomSheetGridAdapter) {
    //                ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(Utils.isPortrait(mDialog.getContext()) ? 1 : 2);
    //                mIsGrid = true;
    //            } else {
    //                throw new RuntimeException("Adapter must be either BottomSheetListAdapter or BottomSheetGridAdapter!");
    //            }
    //
    //            adapter.setIconTint(mIconTint);
    //            recyclerView.setAdapter(adapter);
    //            return this;
    //        }

    fun setCancelable(cancelable: Boolean): BottomSheetDialogBuilder {
        mDialog.setCancelable(cancelable)
        return this
    }

    fun setCanceledOnTouchOutside(cancelable: Boolean): BottomSheetDialogBuilder {
        mDialog.setCanceledOnTouchOutside(cancelable)
        return this
    }

    fun setOnCancelListener(listener: DialogInterface.OnCancelListener): BottomSheetDialogBuilder {
        mDialog.setOnCancelListener(listener)
        return this
    }

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener): BottomSheetDialogBuilder {
        mDialog.setOnDismissListener(listener)
        return this
    }

    fun setOnKeyListener(listener: DialogInterface.OnKeyListener): BottomSheetDialogBuilder {
        mDialog.setOnKeyListener(listener)
        return this
    }

    fun setItems(@MenuRes menuRes: Int, listener: BottomSheetAdapter.OnClickListener?): BottomSheetDialogBuilder {
        val dummyMenu = PopupMenu(mDialog.context, null!!)
        val menu = dummyMenu.menu
        MenuInflater(mDialog.context).inflate(menuRes, menu)
        val items = ArrayList<BottomSheetItem>()
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item.isEnabled) {
                items.add(BottomSheetItem(item.title, item.icon, item.itemId))
            }
        }

        return setItems(items, listener)
    }

    fun setItems(items: List<BottomSheetItem>, listener: BottomSheetAdapter.OnClickListener?): BottomSheetDialogBuilder {
        val adapter = if (mIsGrid)
            BottomSheetGridAdapter(items, mDialog, listener)
        else
            BottomSheetListAdapter(items, mDialog, listener)
        adapter.setIconTint(mIconTint)
        (mDialog.findViewById<View>(R.id.bottomSheetRecyclerView) as RecyclerView).adapter = adapter
        return this
    }

    fun setIconTint(@ColorInt tint: Int): BottomSheetDialogBuilder {
        mIconTint = tint
        val adapter = (mDialog.findViewById<View>(R.id.bottomSheetRecyclerView) as RecyclerView).adapter as BottomSheetAdapter
        adapter.setIconTint(mIconTint)
        return this
    }

    fun create(): BottomSheetDialog {
        checkTitleValidity()
        return mDialog
    }

    fun show(): BottomSheetDialog {
        checkTitleValidity()
        mDialog.show()
        return mDialog
    }

    private fun checkTitleValidity() {
        val title = mDialog.findViewById<View>(R.id.title) as TextView?
        title!!.visibility = if (TextUtils.isEmpty(title.text)) View.GONE else View.VISIBLE
    }

    private class OnBottomSheetScrollListener : RecyclerView.OnScrollListener() {

        private var mScrollY: Int = 0

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            mScrollY += dy
            mScrollY = Math.max(0, mScrollY)
            mScrollY = Math.min(recyclerView!!.height, mScrollY)
            setTitleElevationEnabled(recyclerView, mScrollY > 0)
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        private fun setTitleElevationEnabled(recyclerView: RecyclerView, enabled: Boolean) {
            if (recyclerView.parent is ViewGroup) {
                val parent = recyclerView.parent as ViewGroup
                val title = parent.findViewById<View>(R.id.title)
                if (title != null && title.visibility == View.VISIBLE) {
                    ViewCompat.setElevation(title, (if (enabled) Utils.dpToPixel(title.context, 2) else 0).toFloat())
                }
            }
        }
    }
}
