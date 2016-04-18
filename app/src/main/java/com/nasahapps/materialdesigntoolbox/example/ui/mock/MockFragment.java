package com.nasahapps.materialdesigntoolbox.example.ui.mock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nasahapps.materialdesigntoolbox.example.ui.BaseFragment;

/**
 * Created by Hakeem on 4/17/16.
 */
public class MockFragment extends BaseFragment {

    private static final String EXTRA_PAGE = "page";

    public static MockFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_PAGE, page);

        MockFragment fragment = new MockFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView v = new TextView(getContext());
        TextViewCompat.setTextAppearance(v, android.support.v7.appcompat.R.style.TextAppearance_AppCompat);
        v.setText("Page " + getArguments().getInt(EXTRA_PAGE));
        v.setGravity(Gravity.CENTER);
        return v;
    }
}
