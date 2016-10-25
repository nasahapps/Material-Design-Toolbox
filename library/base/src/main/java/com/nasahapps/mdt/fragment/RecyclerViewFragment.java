package com.nasahapps.mdt.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nasahapps.mdt.R;

/**
 * Created by Hakeem on 10/24/16.
 */

public abstract class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mdt_recycler_view_layout, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(getLayoutManager());

        return v;
    }

    @Nullable
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Nullable
    public RecyclerView.Adapter getAdapter() {
        if (mRecyclerView != null) {
            return mRecyclerView.getAdapter();
        } else {
            return null;
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(adapter);
        }
    }

    @NonNull
    public abstract RecyclerView.LayoutManager getLayoutManager();
}
