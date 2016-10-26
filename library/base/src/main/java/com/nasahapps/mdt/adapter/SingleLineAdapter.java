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
    private OnItemClickListener mListener;

    public SingleLineAdapter(List<SingleLineItem> list, OnItemClickListener listener) {
        mList = list;
        mListener = listener;
    }

    public void setListener(OnItemClickListener listener) {
        mListener = listener;
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

        holder.textView.setText(item.primaryText);
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
        protected CharSequence primaryText;
        protected Drawable startDrawable;
        protected Drawable endDrawable;
        protected boolean hasAvatar;

        /**
         * Creates a SingleLineItem with just text
         */
        public SingleLineItem(CharSequence primaryText) {
            this(primaryText, null);
        }

        /**
         * Creates a SingleLineItem with text and an icon that is start/left-aligned
         */
        public SingleLineItem(CharSequence primaryText, Drawable startDrawable) {
            this(primaryText, startDrawable, false);
        }

        /**
         * Creates a SingleLineItem with text and an avatar-sized image that is start/left-aligned
         */
        public SingleLineItem(CharSequence primaryText, Drawable startDrawable, boolean hasAvatar) {
            this.primaryText = primaryText;
            this.startDrawable = startDrawable;
            this.hasAvatar = hasAvatar;
        }

        /**
         * Creates a SingleLineItem with text, a start/left-aligned avatar-sized image, and a
         * end/right-aligned icon.
         */
        public SingleLineItem(CharSequence primaryText, Drawable avatar, Drawable endDrawable) {
            this(primaryText, avatar, true);
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

        public CharSequence getPrimaryText() {
            return primaryText;
        }

        public void setPrimaryText(CharSequence primaryText) {
            this.primaryText = primaryText;
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

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
        }

    }

}
