package com.nasahapps.materialdesigntoolbox.example.ui.components;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nasahapps.materialdesigntoolbox.components.StepperProgressLayout;
import com.nasahapps.materialdesigntoolbox.example.R;
import com.nasahapps.materialdesigntoolbox.example.ui.main.MainActivity;

/**
 * Created by Hakeem on 4/13/16.
 */
public class StepperListFragment extends ListFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                new String[]{
                        "Stepper Horizontal Layout",
                        "Stepper Progress w/Text",
                        "Stepper Progress w/Dots",
                        "Stepper Progress w/Bar"
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setToolbarColor(ContextCompat.getColor(getContext(),
                R.color.nh_indigo_500));
        ((MainActivity) getActivity()).setStatusBarColor(ContextCompat.getColor(getContext(),
                R.color.nh_indigo_700));
        ((MainActivity) getActivity()).setToolbarTitleTextColor(Color.WHITE);
        ((MainActivity) getActivity()).setToolbarTitle("Steppers");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        switch (position) {
            case 0:
                ((MainActivity) getActivity()).startFragment(new StepperHorizonalFragment());
                break;
            case 1:
                ((MainActivity) getActivity()).startFragment(StepperProgressFragment.newInstance(StepperProgressLayout.TYPE_TEXT));
                break;
            case 2:
                ((MainActivity) getActivity()).startFragment(StepperProgressFragment.newInstance(StepperProgressLayout.TYPE_DOTS));
                break;
            case 3:
                ((MainActivity) getActivity()).startFragment(StepperProgressFragment.newInstance(StepperProgressLayout.TYPE_BAR));
                break;
        }
    }
}
