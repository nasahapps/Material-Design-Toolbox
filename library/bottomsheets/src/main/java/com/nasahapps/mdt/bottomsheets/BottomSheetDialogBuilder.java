package com.nasahapps.mdt.bottomsheets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ColorInt;
import android.support.annotation.MenuRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nasahapps.mdt.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakeem on 10/17/16.
 */
public class BottomSheetDialogBuilder {
    private BottomSheetDialog mDialog;
    private boolean mIsGrid;
    private int mIconTint;

    public BottomSheetDialogBuilder(Context c, boolean isGrid) {
        mDialog = new BottomSheetDialog(c);
        View view = LayoutInflater.from(c).inflate(R.layout.layout_bottom_sheet_list, null);
        mDialog.setContentView(view);

        mIsGrid = isGrid;
        // Set RecyclerView LayoutManager depending on if user wants a grid or not
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bottomSheetRecyclerView);
        int columns;
        if (isGrid) {
            // 4 columns for large tablets
            if (Utils.isLargeTablet(c)) {
                columns = 4;
            } else {
                // 4 columns in landscape, 3 in portrait
                columns = Utils.isPortrait(c) ? 3 : 4;
            }
        } else {
            // 2 columns for tablets
            if (Utils.isTablet(c)) {
                columns = 2;
            } else {
                // 2 columns in landscape, 1 in portrait
                columns = Utils.isPortrait(c) ? 1 : 2;
            }
        }
        recyclerView.setLayoutManager(new GridLayoutManager(c, columns));
        recyclerView.addOnScrollListener(new OnBottomSheetScrollListener());

        // Add top padding if grid
        if (isGrid) {
            recyclerView.setPadding(recyclerView.getPaddingLeft(), c.getResources().getDimensionPixelSize(R.dimen.bottom_sheet_grid_top_padding),
                    recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
        }

        // Adjust side padding for tablets
        int sidePadding = Utils.isTablet(c) ? Utils.getScreenWidth(c) / 6 : 0;
        View dialogViewParent = (View) mDialog.findViewById(R.id.bottomSheetLayout).getParent().getParent();
        dialogViewParent.setPadding(sidePadding, dialogViewParent.getPaddingTop(), sidePadding,
                dialogViewParent.getPaddingBottom());
    }

    public BottomSheetDialogBuilder setTitle(CharSequence title) {
        ((TextView) mDialog.findViewById(R.id.title)).setText(title);
        return this;
    }

//        public DialogBuilder setAdapter(BottomSheetAdapter adapter) {
//            RecyclerView recyclerView = (RecyclerView) mDialog.findViewById(R.id.bottomSheetRecyclerView);
//            if (adapter instanceof BottomSheetListAdapter) {
//                ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(Utils.isPortrait(mDialog.getContext()) ? 3 : 4);
//                mIsGrid = false;
//            } else if (adapter instanceof BottomSheetGridAdapter) {
//                ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(Utils.isPortrait(mDialog.getContext()) ? 1 : 2);
//                mIsGrid = true;
//            } else {
//                throw new RuntimeException("Adapter must be either BottomSheetListAdapter or BottomSheetGridAdapter!");
//            }
//
//            adapter.setIconTint(mIconTint);
//            recyclerView.setAdapter(adapter);
//            return this;
//        }

    public BottomSheetDialogBuilder setCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
        return this;
    }

    public BottomSheetDialogBuilder setCanceledOnTouchOutside(boolean cancelable) {
        mDialog.setCanceledOnTouchOutside(cancelable);
        return this;
    }

    public BottomSheetDialogBuilder setOnCancelListener(DialogInterface.OnCancelListener listener) {
        mDialog.setOnCancelListener(listener);
        return this;
    }

    public BottomSheetDialogBuilder setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mDialog.setOnDismissListener(listener);
        return this;
    }

    public BottomSheetDialogBuilder setOnKeyListener(DialogInterface.OnKeyListener listener) {
        mDialog.setOnKeyListener(listener);
        return this;
    }

    public BottomSheetDialogBuilder setItems(@MenuRes int menuRes, BottomSheetAdapter.OnClickListener listener) {
        PopupMenu dummyMenu = new PopupMenu(mDialog.getContext(), null);
        Menu menu = dummyMenu.getMenu();
        new MenuInflater(mDialog.getContext()).inflate(menuRes, menu);
        List<BottomSheetItem> items = new ArrayList<>();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isEnabled()) {
                items.add(new BottomSheetItem(item.getTitle(), item.getIcon(), item.getItemId()));
            }
        }

        return setItems(items, listener);
    }

    public BottomSheetDialogBuilder setItems(List<BottomSheetItem> items, BottomSheetAdapter.OnClickListener listener) {
        BottomSheetAdapter adapter = mIsGrid ? new BottomSheetGridAdapter(items, mDialog, listener)
                : new BottomSheetListAdapter(items, mDialog, listener);
        adapter.setIconTint(mIconTint);
        ((RecyclerView) mDialog.findViewById(R.id.bottomSheetRecyclerView)).setAdapter(adapter);
        return this;
    }

    public BottomSheetDialogBuilder setIconTint(@ColorInt int tint) {
        mIconTint = tint;
        BottomSheetAdapter adapter = (BottomSheetAdapter) ((RecyclerView) mDialog.findViewById(R.id.bottomSheetRecyclerView)).getAdapter();
        if (adapter != null) {
            adapter.setIconTint(mIconTint);
        }
        return this;
    }

    public BottomSheetDialog create() {
        checkTitleValidity();
        return mDialog;
    }

    public BottomSheetDialog show() {
        checkTitleValidity();
        mDialog.show();
        return mDialog;
    }

    private void checkTitleValidity() {
        TextView title = (TextView) mDialog.findViewById(R.id.title);
        title.setVisibility(TextUtils.isEmpty(title.getText()) ? View.GONE : View.VISIBLE);
    }

    private static class OnBottomSheetScrollListener extends RecyclerView.OnScrollListener {

        private int mScrollY;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mScrollY += dy;
            mScrollY = Math.max(0, mScrollY);
            mScrollY = Math.min(recyclerView.getHeight(), mScrollY);
            setTitleElevationEnabled(recyclerView, mScrollY > 0);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        private void setTitleElevationEnabled(RecyclerView recyclerView, boolean enabled) {
            if (recyclerView.getParent() != null && recyclerView.getParent() instanceof ViewGroup) {
                ViewGroup parent = (ViewGroup) recyclerView.getParent();
                View title = parent.findViewById(R.id.title);
                if (title != null && title.getVisibility() == View.VISIBLE) {
                    ViewCompat.setElevation(title, enabled ? Utils.dpToPixel(title.getContext(), 2) : 0);
                }
            }
        }
    }
}
