package com.nasahapps.mdt.fabutils;

import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.widget.LinearLayout;

import com.nasahapps.mdt.TitleIconItem;
import com.nasahapps.mdt.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhasan on 10/19/16.
 */

public class FabMenu {

    private WeakReference<FloatingActionButton> mFab;
    private WeakReference<LinearLayout> mLayout = new WeakReference<>(null);
    private List<TitleIconItem> mItems = new ArrayList<>();
    private int mMiniFabSize, mMargin;
    private boolean mIsShowing;

    private FabMenu(FloatingActionButton fab) {
        mFab = new WeakReference<>(fab);
        mMiniFabSize = Utils.dpToPixel(fab.getContext(), 40);
        mMargin = Utils.dpToPixel(fab.getContext(), 16);
    }

    public void show() {
        if (mFab.get() != null) {
            if (!mIsShowing) {
                mIsShowing = true;
                FloatingActionButton fab = mFab.get();

                LinearLayout layout = new LinearLayout(fab.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                mLayout = new WeakReference<>(layout);
                for (TitleIconItem item : mItems) {
                    FloatingActionButton miniFab = new FloatingActionButton(fab.getContext());
                    miniFab.setImageDrawable(item.getIcon());
                    layout.addView(miniFab);
                }
            }
        }
    }

    public void hide() {
        if (mFab.get() != null) {
            if (mIsShowing) {
                mIsShowing = false;
            }
        }
    }

    private List<TitleIconItem> getItems() {
        return mItems;
    }

    private WeakReference<FloatingActionButton> getFab() {
        return mFab;
    }

    public boolean isShowing() {
        return mIsShowing;
    }

    public static class Builder {

        private FabMenu mFabMenu;

        public Builder(FloatingActionButton fab) {
            mFabMenu = new FabMenu(fab);
        }

        public Builder withMenuItem(Drawable icon) {
            mFabMenu.getItems().add(new TitleIconItem(null, icon));
            return this;
        }

        public FabMenu build() {
            return mFabMenu;
        }

    }
}
