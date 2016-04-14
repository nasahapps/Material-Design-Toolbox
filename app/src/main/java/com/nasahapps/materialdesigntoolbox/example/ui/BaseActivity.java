package com.nasahapps.materialdesigntoolbox.example.ui;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import icepick.Icepick;

/**
 * Created by Hakeem on 4/13/16.
 */
public abstract class BaseActivity extends com.nasahapps.nasahutils.app.BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @ColorInt
    protected int getColorInt(@ColorRes int colorRes) {
        return ContextCompat.getColor(this, colorRes);
    }

}
