package com.nasahapps.materialdesigntoolbox.example.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nasahapps.materialdesigntoolbox.example.R;
import com.nasahapps.materialdesigntoolbox.example.ui.components.TooltipFragment;

/**
 * Created by Hakeem on 4/13/16.
 */
public class MainFragment extends ListFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                new String[]{
                        "Components"
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setToolbarColor(ContextCompat.getColor(getContext(), R.color.nh_cyan_500));
        ((MainActivity) getActivity()).setStatusBarColor(ContextCompat.getColor(getContext(), R.color.nh_cyan_700));
        ((MainActivity) getActivity()).setToolbarTitleTextColor(Color.BLACK);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.app_name));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        switch (position) {
            case 0:
                ((MainActivity) getActivity()).startFragment(new TooltipFragment());
                break;
        }
    }
}
