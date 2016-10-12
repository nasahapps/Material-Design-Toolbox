package com.nasahapps.materialdesigntoolbox.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by hhasan on 10/13/16.
 */

public class ScrollDownBehavior extends CoordinatorLayout.Behavior {

    private boolean mScrollEnabled = true;
    private OnAppBarOffsetChangedListener mOnAppBarOffsetChangedListener;
    private boolean mIsScrolling;
    // Keep a reference to the "snap" animation, so we can easily cancel it when scrolling
    // Else there would be a slight delay in the actual scrolling
    private ViewPropertyAnimator mSnapAnimator;

    public ScrollDownBehavior() {
        super();
    }

    public ScrollDownBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ViewPropertyAnimator scrollView(View v, int dy, boolean shouldSnap) {
        v.clearAnimation();

        float transY = v.getTranslationY() + dy;
        if (transY > v.getHeight()) {
            transY = v.getHeight();
        } else if (transY < 0) {
            transY = 0;
        }
        v.setTranslationY(transY);

        // Snap when scrolling stops
        if (shouldSnap) {
            if (transY < v.getHeight() / 2) {
                // Snap up
                return v.animate().translationY(0);
            } else {
                // Snap down
                return v.animate().translationY(v.getHeight());
            }
        }

        return null;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        mIsScrolling = true;
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        mIsScrolling = false;
        if (mScrollEnabled) {
            mSnapAnimator = scrollView(child, 0, !mIsScrolling);
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (mScrollEnabled) {
            if (mSnapAnimator != null) {
                mSnapAnimator.cancel();
            }
            scrollView(child, dyConsumed, !mIsScrolling);
        }
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        mScrollEnabled = scrollEnabled;
        if (mOnAppBarOffsetChangedListener != null) {
            mOnAppBarOffsetChangedListener.setScrollEnabled(scrollEnabled);
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        // If this behavior is used in accordance with an AppBarLayout, we need to also scroll when that
        // AppBarLayout's offset changes
        if (dependency instanceof AppBarLayout && mOnAppBarOffsetChangedListener == null) {
            mOnAppBarOffsetChangedListener = new OnAppBarOffsetChangedListener(child);
            ((AppBarLayout) dependency).addOnOffsetChangedListener(mOnAppBarOffsetChangedListener);
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    private static class OnAppBarOffsetChangedListener implements AppBarLayout.OnOffsetChangedListener {

        private int mLastVerticalOffset;
        private View mView;
        private boolean mScrollEnabled = true;

        public OnAppBarOffsetChangedListener(View v) {
            mView = v;
        }

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            int dy = mLastVerticalOffset - verticalOffset;
            mLastVerticalOffset = verticalOffset;
            if (mScrollEnabled) {
                scrollView(mView, dy, false);
            }
        }

        public void setScrollEnabled(boolean scrollEnabled) {
            mScrollEnabled = scrollEnabled;
        }
    }

}
