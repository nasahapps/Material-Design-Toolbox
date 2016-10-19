package com.nasahapps.mdt.example.ui.style

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nasahapps.mdt.Utils
import com.nasahapps.mdt.example.R
import com.nasahapps.mdt.example.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_color.*

class ColorFragment : BaseFragment() {

    companion object {
        private val EXTRA_ARRAY = "array"
        private val EXTRA_TITLE = "title"

        fun newInstance(array: IntArray, title: String): ColorFragment {
            val args = Bundle()
            args.putIntArray(EXTRA_ARRAY, array)
            args.putString(EXTRA_TITLE, title)
            val fragment = ColorFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_color

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = ColorListAdapter(arguments.getIntArray(EXTRA_ARRAY), arguments.getString(EXTRA_TITLE))
    }

    class ColorListAdapter(val colorArray: IntArray, val colorName: String) : RecyclerView.Adapter<ColorListAdapter.ViewHolder>() {

        override fun getItemCount() = colorArray.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val color = ContextCompat.getColor(holder.itemView.context, colorArray[position])
            holder.itemView?.setBackgroundColor(color)
            (holder.itemView as? TextView)?.text = "$colorName ${getColorNumber(position)}"
            (holder.itemView as? TextView)?.setTextColor(if (Utils.shouldUseWhiteText(color)) Color.WHITE else Color.BLACK)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent?.context)?.inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(v)
        }

        inner class ViewHolder(v: View?) : RecyclerView.ViewHolder(v)

        private fun getColorNumber(position: Int) = when (position) {
            1 -> "100"
            2 -> "200"
            3 -> "300"
            4 -> "400"
            5 -> "500"
            6 -> "600"
            7 -> "700"
            8 -> "800"
            9 -> "900"
            10 -> "A100"
            11 -> "A200"
            12 -> "A400"
            13 -> "A700"
            else -> "50"
        }

    }
}