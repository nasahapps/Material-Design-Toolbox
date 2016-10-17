package com.nasahapps.materialdesigntoolbox.example.ui.components

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.nasahapps.materialdesigntoolbox.example.R
import com.nasahapps.materialdesigntoolbox.example.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_buttons.*

/**
 * Created by Hakeem on 4/13/16.
 */
class ButtonFragment : ComponentFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_buttons
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, arrayOf("Item 1", "Item 2", "Item 3", "Item 4"))
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = spinnerAdapter
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setToolbarTitle("Buttons")
    }
}
