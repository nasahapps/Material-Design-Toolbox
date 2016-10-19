package com.nasahapps.mdt;

import android.graphics.drawable.Drawable;

/**
 * Created by hhasan on 10/19/16.
 */

public class TitleIconItem {

    private CharSequence text;
    private Drawable icon;

    private TitleIconItem() {
    }

    public TitleIconItem(CharSequence text, Drawable icon) {
        this.text = text;
        this.icon = icon;
    }

    public CharSequence getText() {
        return text;
    }

    public Drawable getIcon() {
        return icon;
    }

}
