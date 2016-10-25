package com.nasahapps.mdt.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nasahapps.mdt.R;

/**
 * Created by Hakeem on 10/24/16.
 */

public abstract class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mEmptyText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mdt_recycler_view_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(getLayoutManager());

        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        if (savedInstanceState == null) {
            setProgressVisible(true);
        }

        mEmptyText = (TextView) findViewById(R.id.emptyText);
    }

    /**
     * Gets the RecyclerView view widget
     */
    @Nullable
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * Gets the {@link RecyclerView.Adapter} associated with the attached RecyclerView
     */
    @Nullable
    public RecyclerView.Adapter getAdapter() {
        if (mRecyclerView != null) {
            return mRecyclerView.getAdapter();
        } else {
            return null;
        }
    }

    /**
     * Provide the adapter for the attached RecyclerView. If the adapter has an empty data set,
     * {@link #setEmptyTextVisible(boolean)} will automatically be called and the empty state
     * for this list will be shown.
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(adapter);
            setEmptyTextVisible(adapter.getItemCount() == 0);
        }
    }

    /**
     * Controls whether the loading ProgressBar is shown or not
     */
    public void setProgressVisible(boolean visible) {
        mProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * Controls whether the list is shown or not
     */
    public void setRecyclerViewVisible(boolean visible) {
        mRecyclerView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * Controls whether the empty state for this list should be shown or not
     */
    public void setEmptyTextVisible(boolean visible) {
        mEmptyText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * Sets the text to be shown if the list is empty
     */
    public void setEmptyText(CharSequence text) {
        mEmptyText.setText(text);
    }

    /**
     * Returns the {@link RecyclerView.LayoutManager} to be used by
     * the attached RecyclerView. This must be set in order for the RecyclerView to properly lay
     * out its children
     */
    @NonNull
    public abstract RecyclerView.LayoutManager getLayoutManager();
}
