package com.nasahapps.mdt.example.ui.components

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.nasahapps.mdt.adapter.MultiLineAdapter
import com.nasahapps.mdt.adapter.SingleLineAdapter
import com.nasahapps.mdt.app.RecyclerViewFragment
import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.main.MainActivity

/**
 * Created by Hakeem on 4/13/16.
 */
class ListListFragment : RecyclerViewFragment() {

    enum class Type {
        SINGLE_ITEM_TEXT,
        SINGLE_ITEM_ICON,
        SINGLE_ITEM_AVATAR,
        SINGLE_ITEM_AVATAR_ICON,
        TWO_ITEM_TEXT,
        TWO_ITEM_ICON,
        TWO_ITEM_AVATAR,
        TWO_ITEM_AVATAR_ICON,
        THREE_ITEM_TEXT,
        THREE_ITEM_ICON,
        THREE_ITEM_AVATAR,
        THREE_ITEM_AVATAR_ICON
    }

    companion object {
        private val EXTRA_TYPE = "type"

        fun newInstance(type: Type): ListListFragment {
            val args = Bundle()
            args.putSerializable(EXTRA_TYPE, type)
            val fragment = ListListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(activity)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = arguments.getSerializable(EXTRA_TYPE) as Type
        when (type) {
            Type.SINGLE_ITEM_ICON -> {
                val list = emptyList<SingleLineAdapter.SingleLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(SingleLineAdapter.SingleLineItem("Item $i", ContextCompat.getDrawable(activity, R.drawable.ic_call)))
                }
                adapter = SingleLineAdapter(list)
            }
            Type.SINGLE_ITEM_AVATAR -> {
                val list = emptyList<SingleLineAdapter.SingleLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(SingleLineAdapter.SingleLineItem("Item $i", ContextCompat.getDrawable(activity, R.drawable.avatar), true))
                }
                adapter = SingleLineAdapter(list)
            }
            Type.SINGLE_ITEM_AVATAR_ICON -> {
                val list = emptyList<SingleLineAdapter.SingleLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(SingleLineAdapter.SingleLineItem("Item $i", ContextCompat.getDrawable(activity, R.drawable.avatar),
                            ContextCompat.getDrawable(activity, R.drawable.ic_call)))
                }
                adapter = SingleLineAdapter(list)
            }
            Type.TWO_ITEM_TEXT -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum)))
                }
                adapter = MultiLineAdapter(list, 2)
            }
            Type.TWO_ITEM_ICON -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum), ContextCompat.getDrawable(activity, R.drawable.ic_call)))
                }
                adapter = MultiLineAdapter(list, 2)
            }
            Type.TWO_ITEM_AVATAR -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum), ContextCompat.getDrawable(activity, R.drawable.avatar), true))
                }
                adapter = MultiLineAdapter(list, 2)
            }
            Type.TWO_ITEM_AVATAR_ICON -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum), ContextCompat.getDrawable(activity, R.drawable.avatar),
                            ContextCompat.getDrawable(activity, R.drawable.ic_call)))
                }
                adapter = MultiLineAdapter(list, 2)
            }
            Type.THREE_ITEM_TEXT -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum)))
                }
                adapter = MultiLineAdapter(list, 3)
            }
            Type.THREE_ITEM_ICON -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum), ContextCompat.getDrawable(activity, R.drawable.ic_call)))
                }
                adapter = MultiLineAdapter(list, 3)
            }
            Type.THREE_ITEM_AVATAR -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum), ContextCompat.getDrawable(activity, R.drawable.avatar), true))
                }
                adapter = MultiLineAdapter(list, 3)
            }
            Type.THREE_ITEM_AVATAR_ICON -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum), ContextCompat.getDrawable(activity, R.drawable.avatar),
                            ContextCompat.getDrawable(activity, R.drawable.ic_call)))
                }
                adapter = MultiLineAdapter(list, 3)
            }
            else -> {
                val list = emptyList<SingleLineAdapter.SingleLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(SingleLineAdapter.SingleLineItem("Item $i"))
                }
                adapter = SingleLineAdapter(list)
            }
        }

        recyclerView?.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        setProgressVisible(false)
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.setToolbarColor(ContextCompat.getColor(activity, R.color.mdt_indigo_500))
            it.setStatusBarColor(ContextCompat.getColor(activity, R.color.mdt_indigo_700))
            it.setToolbarTitleTextColor(Color.WHITE)
            val title = when (arguments.getSerializable(EXTRA_TYPE) as Type) {
                Type.SINGLE_ITEM_TEXT -> "Single Item - Text Only"
                Type.SINGLE_ITEM_ICON -> "Single Item - Text with Icon"
                Type.SINGLE_ITEM_AVATAR -> "Single Item - Text with Avatar"
                Type.SINGLE_ITEM_AVATAR_ICON -> "Single Item - Text with Icon and Avatar"
                Type.TWO_ITEM_TEXT -> "Two Item - Text Only"
                Type.TWO_ITEM_ICON -> "Two Item - Text with Icon"
                Type.TWO_ITEM_AVATAR -> "Two Item - Text with Avatar"
                Type.TWO_ITEM_AVATAR_ICON -> "Two Item - Text with Icon and Avatar"
                Type.THREE_ITEM_TEXT -> "Three Item - Text Only"
                Type.THREE_ITEM_ICON -> "Three Item - Text with Icon"
                Type.THREE_ITEM_AVATAR -> "Three Item - Text with Avatar"
                Type.THREE_ITEM_AVATAR_ICON -> "Three Item - Text with Icon and Avatar"
                else -> "Lists"
            }
            (activity as MainActivity).setToolbarTitle(title)
            it.setToolbarVisible(true)
        }
    }
}
