package com.nasahapps.materialdesigntoolbox.example.ui.mock

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by hhasan on 10/13/16.
 */
class MockRecyclerViewAdapter : RecyclerView.Adapter<MockRecyclerViewAdapter.ViewHolder>() {

    override fun getItemCount() = 100

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder.itemView as? TextView)?.text = "Item $position"
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(v)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

}