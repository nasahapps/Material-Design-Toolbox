package com.nasahapps.mdt.bottomsheets;

import android.graphics.drawable.Drawable;

import com.nasahapps.mdt.TitleIconItem;

/**
 * Created by hhasan on 10/14/16.
 */

public class BottomSheetItem extends TitleIconItem {

    private int menuId;

    public BottomSheetItem(CharSequence text, Drawable icon) {
        super(text, icon);
    }

    BottomSheetItem(CharSequence text, Drawable icon, int menuId) {
        super(text, icon);
        this.menuId = menuId;
    }

    public int getMenuId() {
        return menuId;
    }
}
