package com.nasahapps.mdt.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nasahapps.mdt.R;
import com.nasahapps.mdt.Utils;

import java.util.List;

/**
 * Created by Hakeem on 10/24/16.
 */

public class MultiLineAdapter extends RecyclerView.Adapter<MultiLineAdapter.ViewHolder> {

    private List<MultiLineItem> mList;
    private int mNumOfLines;

    public MultiLineAdapter(List<MultiLineItem> list, int numOfLines) {
        mList = list;
        mNumOfLines = numOfLines;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mdt_list_multi_line, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MultiLineItem item = mList.get(position);

        holder.primaryTextView.setText(item.text);
        holder.secondaryTextView.setText(item.secondaryText);
        holder.secondaryTextView.setMaxLines(mNumOfLines - 1);

        if (item.hasAvatar) {
            holder.avatar.setVisibility(View.VISIBLE);
            holder.startIcon.setVisibility(View.GONE);

            Bitmap bitmap = Utils.drawableToBitmap(item.startDrawable);
            if (bitmap != null) {
                RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(holder.itemView.getResources(),
                        bitmap);
                roundedDrawable.setCircular(true);
                holder.avatar.setImageDrawable(roundedDrawable);
            } else {
                holder.avatar.setImageDrawable(item.startDrawable);
            }
        } else {
            holder.avatar.setVisibility(View.GONE);
            if (item.startDrawable != null) {
                holder.startIcon.setVisibility(View.VISIBLE);
                holder.startIcon.setImageDrawable(item.startDrawable);
            } else {
                holder.startIcon.setVisibility(View.GONE);
            }
        }

        if (item.endDrawable != null) {
            holder.endIcon.setVisibility(View.VISIBLE);
            holder.endIcon.setImageDrawable(item.endDrawable);
        } else {
            holder.endIcon.setVisibility(View.GONE);
        }
    }

    public static class MultiLineItem extends SingleLineAdapter.SingleLineItem {
        private String secondaryText;

        public MultiLineItem(String primaryText, String secondaryText) {
            this(primaryText, secondaryText, null);
        }

        public MultiLineItem(String primaryText, String secondaryText, Drawable startDrawable) {
            this(primaryText, secondaryText, startDrawable, false);
        }

        public MultiLineItem(String primaryText, String secondaryText, Drawable startDrawable, boolean hasAvatar) {
            super(primaryText, startDrawable, hasAvatar);
            this.secondaryText = secondaryText;
        }

        public MultiLineItem(String primaryText, String secondaryText, Drawable startDrawable, Drawable endDrawable) {
            this(primaryText, secondaryText, startDrawable, true);
            this.endDrawable = endDrawable;
        }

        public String getSecondaryText() {
            return secondaryText;
        }

        public void setSecondaryText(String secondaryText) {
            this.secondaryText = secondaryText;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView primaryTextView, secondaryTextView;
        ImageView startIcon, avatar, endIcon;

        ViewHolder(View v) {
            super(v);
            primaryTextView = (TextView) v.findViewById(R.id.primaryText);
            secondaryTextView = (TextView) v.findViewById(R.id.secondaryText);
            startIcon = (ImageView) v.findViewById(R.id.startIcon);
            avatar = (ImageView) v.findViewById(R.id.avatar);
            endIcon = (ImageView) v.findViewById(R.id.endIcon);
        }

    }

}
