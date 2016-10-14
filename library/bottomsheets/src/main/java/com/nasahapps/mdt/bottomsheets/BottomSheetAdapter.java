package com.nasahapps.mdt.bottomsheets;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nasahapps.materialdesigntoolbox.Utils;

import java.util.List;

/**
 * Created by hhasan on 10/14/16.
 */

abstract class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ViewHolder> {

    private List<BottomSheetItem> mItems;
    private DialogInterface mDialog;
    private OnClickListener mListener;
    private int mIconTint;

    private BottomSheetAdapter() {
    }

    BottomSheetAdapter(List<BottomSheetItem> items, DialogInterface dialog, OnClickListener listener) {
        mItems = items;
        mDialog = dialog;
        mListener = listener;
    }

    void setIconTint(int iconTint) {
        mIconTint = iconTint;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BottomSheetItem item = mItems.get(position);
        holder.text.setText(item.getText());
        Drawable icon = item.getIcon();
        if (icon != null && mIconTint != 0) {
            icon = Utils.getTintedDrawableCompat(icon, mIconTint);
        }
        holder.icon.setImageDrawable(icon);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface OnClickListener {
        void onClick(DialogInterface dialog, int position, int id);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView icon;

        public ViewHolder(View v) {
            super(v);

            text = (TextView) v.findViewById(R.id.text);
            icon = (ImageView) v.findViewById(R.id.icon);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick(mDialog, getAdapterPosition(), mItems.get(getAdapterPosition()).getMenuId());
                    }
                }
            });
        }

    }

}
