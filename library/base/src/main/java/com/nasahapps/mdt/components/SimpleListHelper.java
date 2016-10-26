package com.nasahapps.mdt.components;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nasahapps.mdt.listener.OnItemClickListener;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by hhasan on 10/26/16.
 */

public class SimpleListHelper {

    private WeakReference<RecyclerView> mRecyclerView;
    private ListType mType;

    public <T> SimpleListHelper(RecyclerView recyclerView, ListType type, List<T> items,
                                @Nullable OnItemClickListener listener) {
        this(recyclerView, type, items, false, LinearLayoutManager.VERTICAL, false, listener);
    }

    public <T> SimpleListHelper(RecyclerView recyclerView, ListType type, List<T> items, boolean useDividers,
                                @Nullable OnItemClickListener listener) {
        this(recyclerView, type, items, useDividers, LinearLayoutManager.VERTICAL, false, listener);
    }

    public <T> SimpleListHelper(RecyclerView recyclerView, ListType type, List<T> items, int layoutOrientation,
                                boolean reverseLayout, @Nullable OnItemClickListener listener) {
        this(recyclerView, type, items, false, layoutOrientation, reverseLayout, listener);
    }

    public <T> SimpleListHelper(RecyclerView recyclerView, ListType type, List<T> items, boolean useDividers,
                                int layoutOrientation, boolean reverseLayout, @Nullable OnItemClickListener listener) {
        mRecyclerView = new WeakReference<>(recyclerView);
        mType = type;

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), layoutOrientation, reverseLayout));

    }

    public enum ListType {
        SINGLE_LINE_TEXT_ONLY,
        SINGLE_LINE_TEXT_ONLY_DENSE,
        SINGLE_LINE_AVATAR,
        SINGLE_LINE_AVATAR_DENSE,
        SINGLE_LINE_AVATAR_END_ICON,
        SINGLE_LINE_AVATAR_END_ICON_DENSE,
        SINGLE_LINE_CHECKBOX,
        SINGLE_LINE_CHECKBOX_DENSE,
        SINGLE_LINE_START_ICON,
        SINGLE_LINE_START_ICON_DENSE,
        SINGLE_LINE_SWITCH,
        SINGLE_LINE_SWITCH_DENSE,
        MULTI_LINE_TEXT_ONLY,
        MULTI_LINE_TEXT_ONLY_DENSE,
        MULTI_LINE_AVATAR,
        MULTI_LINE_AVATAR_DENSE,
        MULTI_LINE_AVATAR_END_ICON,
        MULTI_LINE_AVATAR_END_ICON_DENSE,
        MULTI_LINE_CHECKBOX,
        MULTI_LINE_CHECKBOX_DENSE,
        MULTI_LINE_START_ICON,
        MULTI_LINE_START_ICON_DENSE,
        MULTI_LINE_SWITCH,
        MULTI_LINE_SWITCH_DENSE
    }

}
