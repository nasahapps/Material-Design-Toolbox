package com.nasahapps.materialdesigntoolbox.example.ui.mock;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nasahapps.materialdesigntoolbox.example.ui.BaseFragment;

import java.util.Random;

/**
 * Created by Hakeem on 4/17/16.
 */
public class MockFragment extends BaseFragment {

    private static final int[] RANDOM_COLORS = {
            Color.BLACK,
            Color.BLUE,
            Color.CYAN,
            Color.DKGRAY,
            Color.LTGRAY,
            Color.GRAY,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.YELLOW,
            Color.WHITE
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = new View(getContext());
        v.setBackgroundColor(RANDOM_COLORS[new Random().nextInt(RANDOM_COLORS.length)]);
        return v;
    }
}
