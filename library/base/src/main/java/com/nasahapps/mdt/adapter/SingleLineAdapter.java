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

public class SingleLineAdapter extends RecyclerView.Adapter<SingleLineAdapter.ViewHolder> {

    private List<SingleLineItem> mList;

    public SingleLineAdapter(List<SingleLineItem> list) {
        mList = list;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mdt_list_single_line, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SingleLineItem item = mList.get(position);

        holder.textView.setText(item.text);
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

    public static class SingleLineItem {
        private String text;
        private Drawable startDrawable;
        private Drawable endDrawable;
        private boolean hasAvatar;

        public SingleLineItem(String text) {
            this(text, null);
        }

        public SingleLineItem(String text, Drawable startDrawable) {
            this(text, startDrawable, false);
        }

        public SingleLineItem(String text, Drawable startDrawable, boolean hasAvatar) {
            this.text = text;
            this.startDrawable = startDrawable;
            this.hasAvatar = hasAvatar;
        }

        public SingleLineItem(String text, Drawable startDrawable, Drawable endDrawable) {
            this(text, startDrawable, true);
            this.endDrawable = endDrawable;
        }

        public Drawable getEndDrawable() {
            return endDrawable;
        }

        public void setEndDrawable(Drawable endDrawable) {
            this.endDrawable = endDrawable;
        }

        public boolean isHasAvatar() {
            return hasAvatar;
        }

        public void setHasAvatar(boolean hasAvatar) {
            this.hasAvatar = hasAvatar;
        }

        public Drawable getStartDrawable() {
            return startDrawable;
        }

        public void setStartDrawable(Drawable startDrawable) {
            this.startDrawable = startDrawable;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView startIcon, avatar, endIcon;

        ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.text);
            startIcon = (ImageView) v.findViewById(R.id.startIcon);
            avatar = (ImageView) v.findViewById(R.id.avatar);
            endIcon = (ImageView) v.findViewById(R.id.endIcon);
        }

    }

}
