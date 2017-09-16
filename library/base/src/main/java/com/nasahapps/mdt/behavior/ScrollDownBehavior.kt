package com.nasahapps.mdt.behavior

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator

/**
 * Created by hhasan on 10/13/16.
 */

class ScrollDownBehavior<V : View> : CoordinatorLayout.Behavior<V> {

    private var mScrollEnabled = true
    private var mOnAppBarOffsetChangedListener: OnAppBarOffsetChangedListener? = null
    private var mIsScrolling: Boolean = false
    // Keep a reference to the "snap" animation, so we can easily cancel it when scrolling
    // Else there would be a slight delay in the actual scrolling
    private var mSnapAnimator: ViewPropertyAnimator? = null

    constructor() : super()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        mIsScrolling = true
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, type: Int) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        mIsScrolling = false
        if (mScrollEnabled) {
            mSnapAnimator = scrollView(child, 0, !mIsScrolling)
        }
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        if (mScrollEnabled) {
            if (mSnapAnimator != null) {
                mSnapAnimator!!.cancel()
            }
            scrollView(child, dyConsumed, !mIsScrolling)
        }
    }

    fun setScrollEnabled(scrollEnabled: Boolean) {
        mScrollEnabled = scrollEnabled
        if (mOnAppBarOffsetChangedListener != null) {
            mOnAppBarOffsetChangedListener!!.setScrollEnabled(scrollEnabled)
        }
    }

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: V, dependency: View?): Boolean {
        // If this behavior is used in accordance with an AppBarLayout, we need to also scroll when that
        // AppBarLayout's offset changes
        if (dependency is AppBarLayout && mOnAppBarOffsetChangedListener == null) {
            mOnAppBarOffsetChangedListener = OnAppBarOffsetChangedListener(child)
            dependency.addOnOffsetChangedListener(mOnAppBarOffsetChangedListener)
        }
        return super.layoutDependsOn(parent, child, dependency)
    }

    private class OnAppBarOffsetChangedListener(private val mView: View) : AppBarLayout.OnOffsetChangedListener {

        private var mLastVerticalOffset: Int = 0
        private var mScrollEnabled = true

        override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
            val dy = mLastVerticalOffset - verticalOffset
            mLastVerticalOffset = verticalOffset
            if (mScrollEnabled) {
                scrollView(mView, dy, false)
            }
        }

        fun setScrollEnabled(scrollEnabled: Boolean) {
            mScrollEnabled = scrollEnabled
        }
    }

    companion object {
        @JvmStatic
        fun scrollView(v: View, dy: Int, shouldSnap: Boolean): ViewPropertyAnimator? {
            v.clearAnimation()

            var transY = v.translationY + dy
            if (transY > v.height) {
                transY = v.height.toFloat()
            } else if (transY < 0) {
                transY = 0f
            }
            v.translationY = transY

            // Snap when scrolling stops
            return if (shouldSnap) {
                if (transY < v.height / 2) {
                    // Snap up
                    v.animate().translationY(0f)
                } else {
                    // Snap down
                    v.animate().translationY(v.height.toFloat())
                }
            } else null
        }
    }

}
