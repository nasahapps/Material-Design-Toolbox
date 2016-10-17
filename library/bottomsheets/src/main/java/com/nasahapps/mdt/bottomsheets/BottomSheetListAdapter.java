package com.nasahapps.mdt.bottomsheets;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by hhasan on 10/14/16.
 */

class BottomSheetListAdapter extends BottomSheetAdapter {

    public BottomSheetListAdapter(List<BottomSheetItem> items, DialogInterface dialog, OnClickListener listener) {
        super(items, dialog, listener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mdt_list_bottom_sheet_item, parent, false);
        return new ViewHolder(v);
    }
}
