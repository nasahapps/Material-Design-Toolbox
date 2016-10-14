package com.nasahapps.mdt.bottomsheets;

import android.graphics.drawable.Drawable;

/**
 * Created by hhasan on 10/14/16.
 */

public class BottomSheetItem {

    private CharSequence text;
    private Drawable icon;
    private int menuId;

    public BottomSheetItem(CharSequence text, Drawable icon) {
        this.text = text;
        this.icon = icon;
    }

    BottomSheetItem(CharSequence text, Drawable icon, int menuId) {
        this.icon = icon;
        this.menuId = menuId;
        this.text = text;
    }

    public CharSequence getText() {
        return text;
    }

    public Drawable getIcon() {
        return icon;
    }

    public int getMenuId() {
        return menuId;
    }
}
