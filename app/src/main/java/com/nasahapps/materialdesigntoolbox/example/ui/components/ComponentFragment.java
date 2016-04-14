package com.nasahapps.materialdesigntoolbox.example.ui.components;

import android.graphics.Color;

import com.nasahapps.materialdesigntoolbox.example.R;
import com.nasahapps.materialdesigntoolbox.example.ui.BaseFragment;
import com.nasahapps.materialdesigntoolbox.example.ui.main.MainActivity;

/**
 * Created by Hakeem on 4/13/16.
 */
public abstract class ComponentFragment extends BaseFragment {

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setToolbarColor(getColorInt(R.color.nh_indigo_500));
        ((MainActivity) getActivity()).setStatusBarColor(getColorInt(R.color.nh_indigo_700));
        ((MainActivity) getActivity()).setToolbarTitleTextColor(Color.WHITE);
    }
}
