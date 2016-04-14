package com.nasahapps.materialdesigntoolbox.example;

import android.os.Bundle;

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

}
