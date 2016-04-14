package com.nasahapps.materialdesigntoolbox.example;

import android.view.View;

import com.nasahapps.materialdesigntoolbox.components.Tooltip;

import butterknife.OnClick;

public class TestActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @OnClick(R.id.button)
    public void showTooltip(View v) {
        Tooltip.makeTooltip(this, "Long Tooltip Here", Tooltip.LENGTH_SHORT, v).show();
    }
}
