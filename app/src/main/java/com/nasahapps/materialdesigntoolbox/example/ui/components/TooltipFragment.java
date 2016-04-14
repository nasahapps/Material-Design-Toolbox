package com.nasahapps.materialdesigntoolbox.example.ui.components;

import android.view.View;

import com.nasahapps.materialdesigntoolbox.components.Tooltip;
import com.nasahapps.materialdesigntoolbox.example.R;
import com.nasahapps.materialdesigntoolbox.example.ui.main.MainActivity;

import butterknife.OnClick;

/**
 * Created by Hakeem on 4/13/16.
 */
public class TooltipFragment extends ComponentFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tooltip;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setToolbarTitle("Tooltips");
    }

    @OnClick(R.id.button)
    public void showTooltip(View v) {
        Tooltip.makeTooltip(getActivity(), "Hello world!", Tooltip.LENGTH_SHORT, v).show();
    }
}
