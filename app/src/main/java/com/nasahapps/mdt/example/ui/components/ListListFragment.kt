package com.nasahapps.mdt.example.ui.components

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.nasahapps.mdt.adapter.MultiLineAdapter
import com.nasahapps.mdt.adapter.OnItemClickListener
import com.nasahapps.mdt.adapter.SingleLineAdapter
import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * Created by Hakeem on 4/13/16.
 */
class ListListFragment : ComponentFragment(), OnItemClickListener {

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

    override fun getLayoutId() = R.layout.fragment_list

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = arguments.getSerializable(EXTRA_TYPE) as Type
        val adapter = when (type) {
            Type.SINGLE_ITEM_ICON -> {
                val list = emptyList<SingleLineAdapter.SingleLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(SingleLineAdapter.SingleLineItem("Item $i", ContextCompat.getDrawable(activity, R.drawable.ic_call)))
                }
                SingleLineAdapter(list, this)
            }
            Type.SINGLE_ITEM_AVATAR -> {
                val list = emptyList<SingleLineAdapter.SingleLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(SingleLineAdapter.SingleLineItem("Item $i", ContextCompat.getDrawable(activity, R.drawable.avatar), true))
                }
                SingleLineAdapter(list, this)
            }
            Type.SINGLE_ITEM_AVATAR_ICON -> {
                val list = emptyList<SingleLineAdapter.SingleLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(SingleLineAdapter.SingleLineItem("Item $i", ContextCompat.getDrawable(activity, R.drawable.avatar),
                            ContextCompat.getDrawable(activity, R.drawable.ic_call)))
                }
                SingleLineAdapter(list, this)
            }
            Type.TWO_ITEM_TEXT -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum)))
                }
                MultiLineAdapter(list, 2, this)
            }
            Type.TWO_ITEM_ICON -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum), ContextCompat.getDrawable(activity, R.drawable.ic_call)))
                }
                MultiLineAdapter(list, 2, this)
            }
            Type.TWO_ITEM_AVATAR -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum), ContextCompat.getDrawable(activity, R.drawable.avatar), true))
                }
                MultiLineAdapter(list, 2, this)
            }
            Type.TWO_ITEM_AVATAR_ICON -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum), ContextCompat.getDrawable(activity, R.drawable.avatar),
                            ContextCompat.getDrawable(activity, R.drawable.ic_call)))
                }
                MultiLineAdapter(list, 2, this)
            }
            Type.THREE_ITEM_TEXT -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum)))
                }
                MultiLineAdapter(list, 3, this)
            }
            Type.THREE_ITEM_ICON -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum), ContextCompat.getDrawable(activity, R.drawable.ic_call)))
                }
                MultiLineAdapter(list, 3, this)
            }
            Type.THREE_ITEM_AVATAR -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum), ContextCompat.getDrawable(activity, R.drawable.avatar), true))
                }
                MultiLineAdapter(list, 3, this)
            }
            Type.THREE_ITEM_AVATAR_ICON -> {
                val list = emptyList<MultiLineAdapter.MultiLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(MultiLineAdapter.MultiLineItem("Item $i", getString(R.string.lorem_ipsum), ContextCompat.getDrawable(activity, R.drawable.avatar),
                            ContextCompat.getDrawable(activity, R.drawable.ic_call)))
                }
                MultiLineAdapter(list, 3, this)
            }
            else -> {
                val list = emptyList<SingleLineAdapter.SingleLineItem>().toMutableList()
                for (i in 0..100) {
                    list.add(SingleLineAdapter.SingleLineItem("Item $i"))
                }
                SingleLineAdapter(list, this)
            }
        }

        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = adapter
        recyclerView?.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
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
        }
    }

    override fun onItemClick(v: View?, position: Int) {

    }
}
