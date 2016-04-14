package com.nasahapps.materialdesigntoolbox.example.ui;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.nasahapps.materialdesigntoolbox.example.ui.main.MainActivity;

import icepick.Icepick;

/**
 * Created by Hakeem on 4/13/16.
 */
public abstract class BaseFragment extends com.nasahapps.nasahutils.app.BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @ColorInt
    protected int getColorInt(@ColorRes int colorRes) {
        return ((MainActivity) getActivity()).getColorInt(colorRes);
    }

}
