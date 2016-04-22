package com.nasahapps.materialdesigntoolbox.components;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.nasahapps.materialdesigntoolbox.R;
import com.nasahapps.materialdesigntoolbox.Utils;

import java.util.ArrayList;

/**
 * Created by Hakeem on 3/14/16.
 * </p>
 * A {@link FrameLayout} made easy to explore and switch between top-level views in a single tap.
 * </br>
 * <a href="http://www.google.com/design/spec/components/bottom-navigation.html">http://www.google.com/design/spec/components/bottom-navigation.html</a>
 */
@CoordinatorLayout.DefaultBehavior(BottomNavigationBar.ScrollingViewBehavior.class)
public class BottomNavigationBar extends FrameLayout implements BottomNavigationTab.OnTabSelectedListener {

    public static final int INVALID_TAB_POSITION = -1;
    static final long ANIM_DURATION = 250;
    static final Interpolator ANIM_INTERPOLATOR = new FastOutSlowInInterpolator();

    @ColorInt
    private int mAccentColor;
    private int mCurrentTab;
    private boolean mUsesDarkTheme, mInitialized;
    private ArrayList<BottomNavigationTab> mTabs = new ArrayList<>();
    private BottomNavigationTab.OnTabSelectedListener mOnTabSelectedListener;
    @ColorInt
    private int[] mBackgroundColors;
    private LinearLayout mTabLayout;
//    private View mCircularRevealBackground, mCircularRevealForeground;

    public BottomNavigationBar(Context context) {
        super(context);
        initView(null);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
//        mCircularRevealBackground = new View(getContext());
//        addView(mCircularRevealBackground, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//
//        mCircularRevealForeground = new View(getContext());
//        addView(mCircularRevealForeground, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));

        mTabLayout = new LinearLayout(getContext());
        mTabLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTabLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        ViewCompat.setElevation(this, getResources().getDimensionPixelSize(R.dimen.na_bottom_nav_elevation));
        addView(mTabLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        if (attrs != null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.BottomNavigationBar, 0, 0);

            try {
                mUsesDarkTheme = ta.getBoolean(R.styleable.BottomNavigationBar_bottomNavDarkTheme, false);

                mAccentColor = mUsesDarkTheme ? Color.WHITE
                        : ta.getColor(R.styleable.BottomNavigationBar_bottomNavAccentColor, 0);
                if (mAccentColor == 0) {
                    mAccentColor = Utils.getColorFromAttribute(getContext(), R.attr.colorAccent);
                }

//                if (getBackground() instanceof ColorDrawable) {
//                    int backgroundColor = ((ColorDrawable) getBackground()).getColor();
//                    mCircularRevealBackground.setBackgroundColor(backgroundColor);
//                } else {
//                    mCircularRevealBackground.setBackgroundColor(Color.WHITE);
//                }
            } finally {
                ta.recycle();
            }
        }
    }

    public void addTab(BottomNavigationTab tab) {
        addTab(tab, mTabs.size());
    }

    public void addTab(BottomNavigationTab tab, boolean setSelected) {
        addTab(tab, mTabs.size(), setSelected);
    }

    public void addTab(BottomNavigationTab tab, int position) {
        addTab(tab, position, false);
    }

    public void addTab(BottomNavigationTab tab, int position, boolean setSelected) {
        checkTabCount();
        mTabs.add(position, tab);
        if (setSelected || getTabCount() == 1) {
            setTabSelected(position);
        }

        boolean fixed = getTabCount() <= 3;
        mTabLayout.addView(tab, new LinearLayout.LayoutParams(0,
                getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_height)));
        for (BottomNavigationTab tab1 : mTabs) {
            tab1.adjustForFixedMode(fixed);
        }
    }

    public BottomNavigationTab newTab() {
        return new BottomNavigationTab(getContext());
    }

    private void checkTabCount() {
        if (getTabCount() > 5) {
            throw new RuntimeException("You can only have a max of 5 tabs! If you want more, it's " +
                    "best to use a navigation drawer.");
        }
    }

    public void setTabSelected(int position) {
        if (getSelectedTabPosition() != position) {
            for (BottomNavigationTab tab : mTabs) {
                tab.setSelected(false);
            }

            mCurrentTab = position;
            mTabs.get(position).setSelected(true);

            if (mUsesDarkTheme && mBackgroundColors != null) {
                changeBackgroundColor(mCurrentTab);
            }
        }
    }

    public int getSelectedTabPosition() {
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i).isSelected()) {
                return i;
            }
        }

        return INVALID_TAB_POSITION;
    }

    public BottomNavigationTab getTabAt(int position) {
        return mTabs.get(position);
    }

    public int getTabCount() {
        return mTabs.size();
    }

    public void removeAllTabs() {
        mTabs = new ArrayList<>();
        mTabLayout.removeAllViews();
    }

    public void removeTab(BottomNavigationTab tab) {
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i).equals(tab)) {
                mTabLayout.removeView(mTabs.get(i));
                mTabs.remove(i);

                if (getSelectedTabPosition() == INVALID_TAB_POSITION) {
                    setTabSelected(0);
                }

                break;
            }
        }

        for (BottomNavigationTab tab1 : mTabs) {
            tab1.adjustForFixedMode(mTabs.size() <= 3);
        }
    }

    public void removeTabAt(int position) {
        mTabLayout.removeView(mTabs.get(position));
        mTabs.remove(position);

        if (getSelectedTabPosition() == INVALID_TAB_POSITION) {
            setTabSelected(0);
        }

        for (BottomNavigationTab tab1 : mTabs) {
            tab1.adjustForFixedMode(mTabs.size() <= 3);
        }
    }

    public void setUsesDarkTheme(boolean usesDarkTheme) {
        mUsesDarkTheme = usesDarkTheme;
        for (BottomNavigationTab tab : mTabs) {
            tab.adjustForDarkTheme();
        }
    }

    public void setAccentColor(int accentColor) {
        mAccentColor = accentColor;
    }

    @Override
    public void setBackgroundColor(int color) {
//        mCircularRevealBackground.setBackgroundColor(color);
        super.setBackgroundColor(color);
        setAccentColor(Color.WHITE);
        setUsesDarkTheme(true);
    }

    public void setOnTabSelectedListener(BottomNavigationTab.OnTabSelectedListener listener) {
        mOnTabSelectedListener = listener;
    }

    public void setBackgroundColors(int[] backgroundColors) {
        if (backgroundColors.length != mTabs.size()) {
            throw new IllegalArgumentException("Background colors array length must match the number of tabs!");
        }

        mBackgroundColors = backgroundColors;
        changeBackgroundColor(getSelectedTabPosition());
    }

    @SuppressWarnings("NewApi")
    private void changeBackgroundColor(final int position) {
//        if (mInitialized) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                // Do a circular view animation
//                mCircularRevealForeground.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        int[] absPosition = new int[2];
//                        mTabs.get(position).getLocationOnScreen(absPosition);
//                        int cx = absPosition[0] + (mTabs.get(position).getWidth() / 2);
//                        int cy = mTabs.get(position).getHeight() / 2;
//                        int endRadius = (int) (getMeasuredWidth() * 1.1);
//                        mCircularRevealForeground.setBackgroundColor(mBackgroundColors[position]);
//                        Animator anim = ViewAnimationUtils.createCircularReveal(mCircularRevealForeground,
//                                cx, cy, 0, endRadius);
//                        anim.addListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                mCircularRevealBackground.setBackgroundColor(mBackgroundColors[position]);
//                            }
//                        });
//                        anim.setDuration(ANIM_DURATION * 3);
//                        anim.setInterpolator(ANIM_INTERPOLATOR);
//                        anim.start();
//                    }
//                });
//            } else {
//                // Do a simple color fade
//                int startingColor;
//                if (getBackground() instanceof ColorDrawable) {
//                    startingColor = ((ColorDrawable) getBackground()).getColor();
//                } else {
//                    startingColor = Color.WHITE;
//                }
//
//                ValueAnimator backgroundAnim = ValueAnimator.ofObject(new ArgbEvaluator(),
//                        startingColor, mBackgroundColors[position]);
//                backgroundAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        setBackgroundColor((int) animation.getAnimatedValue());
//                    }
//                });
//                backgroundAnim.setDuration(ANIM_DURATION);
//                backgroundAnim.setInterpolator(ANIM_INTERPOLATOR);
//                backgroundAnim.start();
//            }
//        } else {
//            mInitialized = true;
//            setBackgroundColor(mBackgroundColors[position]);
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (BottomNavigationTab tab : mTabs) {
            tab.adjustTabWidth(getTabCount() <= 3);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
        lp.height += insets.getSystemWindowInsetBottom();
        setLayoutParams(lp);
        return super.onApplyWindowInsets(insets);
    }

    @Override
    public void addView(View child) {
        if (child instanceof BottomNavigationTab) {
            mTabLayout.addView(child);
            mTabs.add((BottomNavigationTab) child);
            checkTabCount();
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof BottomNavigationTab) {
            mTabLayout.addView(child, index, params);
            mTabs.add((BottomNavigationTab) child);
            checkTabCount();
        } else {
            super.addView(child, index, params);
        }
    }

    @Override
    public void addView(View child, int width, int height) {
        if (child instanceof BottomNavigationTab) {
            mTabLayout.addView(child, width, height);
            mTabs.add((BottomNavigationTab) child);
            checkTabCount();
        } else {
            super.addView(child, width, height);
        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (child instanceof BottomNavigationTab) {
            mTabLayout.addView(child, params);
            mTabs.add((BottomNavigationTab) child);
            checkTabCount();
        } else {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(View child, int index) {
        if (child instanceof BottomNavigationTab) {
            mTabLayout.addView(child, index);
            mTabs.add((BottomNavigationTab) child);
            checkTabCount();
        } else {
            super.addView(child, index);
        }
    }

    @Override
    public void onTabReselected(BottomNavigationTab tab, int position) {
        if (mOnTabSelectedListener != null) {
            mOnTabSelectedListener.onTabReselected(tab, position);
        }
    }

    @Override
    public void onTabSelected(BottomNavigationTab tab, int position) {
        if (mOnTabSelectedListener != null) {
            mOnTabSelectedListener.onTabSelected(tab, position);
        }

        setTabSelected(position);
    }

    public static class ScrollingViewBehavior extends CoordinatorLayout.Behavior<View> {

        View mChild;
        int mTranslationY;

        public ScrollingViewBehavior() {
            super();
        }

        public ScrollingViewBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
            return dependency instanceof ScrollingView;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
            mChild = child;
            if (dependency instanceof NestedScrollView) {
                ((NestedScrollView) dependency).setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                        Log.d("BottomNav", "scrollY: " + scrollY + ", oldScrollY: " + oldScrollY);
                    }
                });
            } else if (dependency instanceof RecyclerView) {
                ((RecyclerView) dependency).addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int oldTranslationY = mTranslationY;
                        mTranslationY = (int) mChild.getTranslationY() + dy;
                        mTranslationY = Math.min(mTranslationY, mChild.getHeight());
                        mTranslationY = Math.max(mTranslationY, 0);
                        if (mTranslationY != oldTranslationY) {
                            mChild.setTranslationY(mTranslationY);
                        }
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            if (mTranslationY < mChild.getHeight() / 2) {
                                mChild.animate().translationY(0);
                            } else {
                                mChild.animate().translationY(mChild.getHeight());
                            }
                        }
                    }
                });
            }

            return super.onDependentViewChanged(parent, child, dependency);
        }
    }

}
