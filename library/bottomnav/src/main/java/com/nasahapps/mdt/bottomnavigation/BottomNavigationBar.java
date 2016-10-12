package com.nasahapps.mdt.bottomnavigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nasahapps.materialdesigntoolbox.Utils;
import com.nasahapps.materialdesigntoolbox.behavior.ScrollDownBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Bottom navigation bars make it easy to explore and switch between top-level views in a single tap.</br>
 *
 * Tapping on a bottom navigation icon should take you directly to the associated view or refreshes
 * the currently active view.</br>
 *
 * Bottom navigation is primarily for use on mobile, though for tablets, it is possible to use this class
 * for side navigation.</br>
 *
 * <a href="https://material.google.com/components/bottom-navigation.html">https://material.google.com/components/bottom-navigation.html</a>
 */

@CoordinatorLayout.DefaultBehavior(ScrollDownBehavior.class)
public class BottomNavigationBar extends RelativeLayout implements View.OnClickListener, ViewGroup.OnHierarchyChangeListener {

    private int mSelectedTabPosition;
    private int mActiveColor, mInactiveColor;
    private List<OnTabSelectedListener> mListeners = new ArrayList<>();
    private int[] mBackgroundColorList = new int[1];
    private LinearLayout mTabLayout;
    private View mBackgroundLayer1, mBackgroundLayer2;
    private boolean mDarkTheme;

    public BottomNavigationBar(Context context) {
        super(context);
        init(null);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.selectedTabPosition = mSelectedTabPosition;
        ss.activeColor = mActiveColor;
        ss.inactiveColor = mInactiveColor;
        ss.backgroundColorList = mBackgroundColorList;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mSelectedTabPosition = ss.selectedTabPosition;
        mActiveColor = ss.activeColor;
        mInactiveColor = ss.inactiveColor;
        mBackgroundColorList = ss.backgroundColorList;

        configure();
    }

    private void init(AttributeSet attrs) {
        setClipChildren(false);
        setClipToPadding(false);

        mTabLayout = new LinearLayout(getContext());
        int tabId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tabId = View.generateViewId();
        } else {
            tabId = new Random().nextInt(Integer.MAX_VALUE);
        }
        mTabLayout.setId(tabId);

        if (getBackground() != null && getBackground() instanceof ColorDrawable) {
            mBackgroundColorList = new int[1];
            mBackgroundColorList[0] = ((ColorDrawable) getBackground()).getColor();
        }

        mBackgroundLayer1 = new View(getContext());
        mBackgroundLayer2 = new View(getContext());
        if (mSelectedTabPosition < mBackgroundColorList.length) {
            mBackgroundLayer1.setBackgroundColor(mBackgroundColorList[mSelectedTabPosition]);
            mBackgroundLayer2.setBackgroundColor(mBackgroundColorList[mSelectedTabPosition]);
        }
        LayoutParams layerLp1 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layerLp1.addRule(ALIGN_LEFT, tabId);
        layerLp1.addRule(ALIGN_TOP, tabId);
        layerLp1.addRule(ALIGN_RIGHT, tabId);
        layerLp1.addRule(ALIGN_BOTTOM, tabId);
        // We could've used new LayoutParams(RelativeLayout.LayoutParams) constructor but it's only for Kitkat and above,
        // so instead of writing a case statement for that, might as well just rewrite all this code :/
        LayoutParams layerLp2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layerLp2.addRule(ALIGN_LEFT, tabId);
        layerLp2.addRule(ALIGN_TOP, tabId);
        layerLp2.addRule(ALIGN_RIGHT, tabId);
        layerLp2.addRule(ALIGN_BOTTOM, tabId);
        addView(mBackgroundLayer2, 0, layerLp2);
        addView(mBackgroundLayer1, 0, layerLp1);

        if (attrs != null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.BottomNavigationBar, 0, 0);
            try {
                mDarkTheme = ta.getBoolean(R.styleable.BottomNavigationBar_bottomNavigationDarkTheme, false);

                mActiveColor = ta.getColor(R.styleable.BottomNavigationBar_bottomNavigationActiveTint, 0);
                if (mActiveColor == 0) {
                    mActiveColor = Utils.getColorFromAttribute(getContext(), R.attr.colorPrimary);
                }

                mInactiveColor = ta.getColor(R.styleable.BottomNavigationBar_bottomNavigationInactiveTint, 0);
                if (mInactiveColor == 0) {
                    mInactiveColor = Utils.getColorFromAttribute(getContext(),
                            mDarkTheme ? android.R.attr.textColorSecondaryInverse : android.R.attr.textColorSecondary);
                }

                int orientation = ta.getInt(R.styleable.BottomNavigationBar_bottomNavigationOrientation, LinearLayout.HORIZONTAL);
                mTabLayout.setOrientation(orientation);
            } finally {
                ta.recycle();
            }
        }

        LayoutParams tabLp;
        if (getTabLayoutOrientation() == LinearLayout.HORIZONTAL) {
            // Only set elevation if tabs are horizontal
            ViewCompat.setElevation(this, getResources().getDimensionPixelSize(R.dimen.bottom_nav_elevation));
            // And have its gravity be CENTER_HORIZONTAL
            mTabLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            // and its layout params will be width=MATCH_PARENT, height=WRAP_CONTENT
            tabLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            // Have tabs be aligned to the top
            mTabLayout.setGravity(Gravity.TOP);
            // Layout params will be width=WRAP_CONTENT, height=MATCH_PARENT
            tabLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        addView(mTabLayout, tabLp);
    }

    private void configure() {
        int tabHeight, activeTabWidth, inactiveTabWidth;
        if (getTabLayoutOrientation() == LinearLayout.HORIZONTAL) {
            if (getTabCount() <= 3) {
                // Fixed tabs
                // Each tab will have the same width and height
                tabHeight = getResources().getDimensionPixelSize(R.dimen.bottom_nav_tab_height);
                activeTabWidth = Utils.getScreenWidth(getContext()) / getTabCount();
                // Make sure width is between 80dp and 168dp
                activeTabWidth = Math.max(activeTabWidth, getResources().getDimensionPixelSize(R.dimen.bottom_nav_tab_fixed_minimum_width));
                activeTabWidth = Math.min(activeTabWidth, getResources().getDimensionPixelSize(R.dimen.bottom_nav_tab_fixed_maximum_width));
                inactiveTabWidth = activeTabWidth;
            } else {
                // Shifting tabs
                tabHeight = getResources().getDimensionPixelSize(R.dimen.bottom_nav_tab_height);
                int[] array = new int[2];
                calculateShiftingTabWidths(array);
                activeTabWidth = array[0];
                inactiveTabWidth = array[1];
            }
        } else {
            // Tab width/height is constant
            tabHeight = activeTabWidth = inactiveTabWidth = getResources().getDimensionPixelSize(R.dimen.bottom_nav_tab_width_height_vertical);
        }

        for (int i = 0; i < getTabCount(); i++) {
            Tab tab = getTabAt(i);
            tab.setLayoutParams(new LinearLayout.LayoutParams(i == mSelectedTabPosition ? activeTabWidth : inactiveTabWidth, tabHeight));
            TabStyle style;
            if (getTabLayoutOrientation() == LinearLayout.VERTICAL) {
                style = TabStyle.VERTICAL;
            } else if (getTabCount() <= 3) {
                style = TabStyle.FIXED;
            } else {
                style = TabStyle.SHIFTING;
            }
            tab.setTabStyle(style);
            tab.setActive(i == mSelectedTabPosition);
            tab.setTag(i);
            tab.setOnClickListener(this);
        }

        if (mSelectedTabPosition < mBackgroundColorList.length) {
            int beforeColor = getBackground() != null ? ((ColorDrawable) getBackground()).getColor() : Color.WHITE;
            int afterColor = mBackgroundColorList[mSelectedTabPosition];
            animateBackgroundColor(beforeColor, afterColor);
        }

        invalidate();
    }

    /**
     * Calculates the width of both the active and inactive tab in SHIFTING mode.
     *
     * @param array an array of size 2 to put values in, where the first value is the active tab's width and
     *              the second value is the inactive tabs' widths
     */
    private void calculateShiftingTabWidths(int[] array) {
        if (array.length < 2) {
            throw new RuntimeException("Array must be of at least size 2!");
        }

        int minActiveWidth = getResources().getDimensionPixelSize(R.dimen.bottom_nav_tab_shifting_active_minimum_width);
        int maxActiveWidth = getResources().getDimensionPixelSize(R.dimen.bottom_nav_tab_shifting_active_maximum_width);
        int minInactiveWidth = getResources().getDimensionPixelSize(R.dimen.bottom_nav_tab_shifting_inactive_minimum_width);
        int maxInactiveWidth = getResources().getDimensionPixelSize(R.dimen.bottom_nav_tab_shifting_inactive_maximum_width);

        // First, assume all inactive tab widths is the minimum (56dp)
        int inactiveTabWidth = minInactiveWidth;
        // Then take the difference between those widths totaled and the overall screen width, and this
        // is the initial active tab's width
        int activeTabWidth = Utils.getScreenWidth(getContext()) - inactiveTabWidth * (getTabCount() - 1);

        // If the active tab width is less than its min (96dp), bring it up to its min
        if (activeTabWidth < minActiveWidth) {
            activeTabWidth = minActiveWidth;
        } else if (activeTabWidth > maxActiveWidth) {
            // If the active tab width is more than its max (168dp), trim it down to max, and split the difference
            // amongst the inactive tabs
            int difference = activeTabWidth - maxActiveWidth;
            activeTabWidth = maxActiveWidth;
            inactiveTabWidth += (difference / (getTabCount() - 1));
            // But also make sure the inactive tab width is no more than its max (96dp)
            inactiveTabWidth = Math.max(inactiveTabWidth, minInactiveWidth);
            inactiveTabWidth = Math.min(inactiveTabWidth, maxInactiveWidth);
        }

        array[0] = activeTabWidth;
        array[1] = inactiveTabWidth;
    }

    private void animateBackgroundColor(int before, final int after) {
        if (mBackgroundLayer1 != null && mBackgroundLayer2 != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    && mBackgroundLayer1.isAttachedToWindow() && mBackgroundLayer2.isAttachedToWindow()) {
                // Circular reveal layer 2, then when done, make layer 1 match
                mBackgroundLayer2.setBackgroundColor(after);
                Tab selectedTab = getTabAt(mSelectedTabPosition);
                int centerX = selectedTab.getLeft() + (selectedTab.getWidth() / 2);
                int centerY = selectedTab.getTop() + (selectedTab.getHeight() / 2);
                float finalRadius = getTabLayoutOrientation() == LinearLayout.HORIZONTAL
                        ? mBackgroundLayer1.getWidth() * 1.3f : mBackgroundLayer1.getHeight() * 1.3f;
                Animator anim = ViewAnimationUtils.createCircularReveal(mBackgroundLayer2, centerX, centerY,
                        0f, finalRadius);
                anim.setInterpolator(new FastOutSlowInInterpolator());
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mBackgroundLayer1.setBackgroundColor(after);
                    }
                });
                anim.start();
            } else {
                ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), before, after);
                anim.setInterpolator(new FastOutSlowInInterpolator());
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mBackgroundLayer1.setBackgroundColor((int) animation.getAnimatedValue());
                        mBackgroundLayer2.setBackgroundColor((int) animation.getAnimatedValue());
                    }
                });
                anim.start();
            }
        }
    }

    public void setDarkTheme(boolean darkTheme) {
        mDarkTheme = darkTheme;

        // If no inactive tint was set, set it
        int secondaryColor = Utils.getColorFromAttribute(getContext(), android.R.attr.textColorSecondary);
        int secondaryInverseColor = Utils.getColorFromAttribute(getContext(), android.R.attr.textColorSecondaryInverse);
        if (mInactiveColor == secondaryColor || mInactiveColor == secondaryInverseColor) {
            mInactiveColor = Utils.getColorFromAttribute(getContext(),
                    mDarkTheme ? android.R.attr.textColorSecondaryInverse : android.R.attr.textColorSecondary);
        }

        configure();
    }

    /**
     * Returns the number of tabs
     */
    public int getTabCount() {
        return mTabLayout.getChildCount();
    }

    /**
     * Returns the tab at the given position
     */
    public Tab getTabAt(int position) {
        return (Tab) mTabLayout.getChildAt(position);
    }

    /**
     * Creates a new tab with the given text and icon
     *
     * @return the newly created tab
     */
    public Tab newTab(CharSequence text, Drawable icon) {
        Tab tab = new Tab(getContext());
        tab.setIcon(icon);
        tab.setText(text);
        return tab;
    }

    /**
     * Adds the given tab to this layout
     * @throws RuntimeException if the total number of tabs exceeds 5
     */
    public void addTab(Tab tab) {
        if (getTabCount() >= 5) {
            // Max limit is 5 tabs
            throw new RuntimeException("Max number of tabs per guidelines is 5!");
        }

        mTabLayout.addView(tab);
        configure();
    }

    /**
     * Returns the color tint for the selected tab
     */
    @ColorInt
    public int getActiveColor() {
        return mActiveColor;
    }

    /**
     * Sets the color tint for the selected tab
     */
    public void setActiveColor(@ColorInt int activeColor) {
        mActiveColor = activeColor;
        // And set the active color for each tab
        for (int i = 0; i < getTabCount(); i++) {
            getTabAt(i).configure();
        }
    }

    /**
     * Returns the color tint for unselected tabs
     */
    @ColorInt
    public int getInactiveColor() {
        return mInactiveColor;
    }

    /**
     * Sets the color tint for unselected tabs
     * @param inactiveColor
     */
    public void setInactiveColor(@ColorInt int inactiveColor) {
        mInactiveColor = inactiveColor;
        // And set the inactive color for each tab
        for (int i = 0; i < getTabCount(); i++) {
            getTabAt(i).configure();
        }
    }

    /**
     * Adds a listener for when tabs are selected, reselected, or deselected
     */
    public void addOnTabSelectedListener(OnTabSelectedListener listener) {
        if (listener != null) {
            mListeners.add(listener);
        }
    }

    /**
     * Removes the given tab listener from this layout
     */
    public void removeOnTabSelectedListener(OnTabSelectedListener listener) {
        if (listener != null) {
            mListeners.remove(listener);
        }
    }

    /**
     * Sets the background color to be shown for each tab, where the first color is
     * for the first tab, second color for the second tab, etc.
     */
    public void setBackgroundColors(@ColorInt int... colors) {
        mBackgroundColorList = colors;
        configure();
    }

    /**
     * Sets the background color to be shown for each tab, where the first color is
     * for the first tab, second color for the second tab, etc.
     */
    public void setBackgroundColorResources(@ColorRes int... colors) {
        mBackgroundColorList = new int[colors.length];
        for (int i = 0; i < colors.length; i++) {
            mBackgroundColorList[i] = ContextCompat.getColor(getContext(), colors[i]);
        }
        configure();
    }

    private void setAllTabsInactive() {
        for (int i = 0; i < getTabCount(); i++) {
            getTabAt(i).setActive(false);
        }
    }

    /**
     * Returns the position of the currently selected tab
     */
    public int getSelectedTabPosition() {
        return mSelectedTabPosition;
    }

    /**
     * Sets the tab at the given position to be the "selected" tab
     */
    public void setSelectedTabPosition(int selectedTabPosition) {
        if (selectedTabPosition == mSelectedTabPosition) {
            for (OnTabSelectedListener listener : mListeners) {
                listener.onTabReselected(selectedTabPosition);
            }
        } else {
            int oldPosition = mSelectedTabPosition;
            mSelectedTabPosition = selectedTabPosition;
            for (OnTabSelectedListener listener : mListeners) {
                listener.onTabUnselected(oldPosition);
                listener.onTabSelected(mSelectedTabPosition);
            }
        }

        setAllTabsInactive();
        getTabAt(selectedTabPosition).setActive(true);
        configure();
    }

    @LinearLayoutCompat.OrientationMode
    private int getTabLayoutOrientation() {
        return mTabLayout.getOrientation();
    }

    // On Click listener for tabs
    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        setSelectedTabPosition(position);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        WindowInsets newInsets = insets.replaceSystemWindowInsets(0, 0, 0, 0);
        mTabLayout.setPadding(insets.getSystemWindowInsetLeft(), mTabLayout.getPaddingTop(), insets.getSystemWindowInsetRight(),
                insets.getSystemWindowInsetBottom());
        return super.onApplyWindowInsets(newInsets);
    }

    // Adding a hierarchy change listener to all our parents for listening for potential Snackbars
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getTabLayoutOrientation() == LinearLayout.HORIZONTAL) {
            ViewParent parent = getParent();
            while (parent != null) {
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).setOnHierarchyChangeListener(this);
                    parent = parent.getParent();
                } else {
                    parent = null;
                }
            }
        }
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        if (parent instanceof FrameLayout || parent instanceof RelativeLayout) {
            // If the view added was a snackbar, re-align it so it doesn't cover this bottom nav
            if (child instanceof Snackbar.SnackbarLayout) {
                ViewGroup viewGroup = (ViewGroup) parent;
                // But first make sure the two are siblings
                if (parent != getParent()) {
                    // These aren't siblings, bring Snackbar down to our level
                    viewGroup.removeView(child);
                    ((ViewGroup) getParent()).addView(child);
                }

                if (getParent() instanceof FrameLayout) {
                    // Adjust the padding to it ends up behind and above the bottom nav
                    child.setPadding(child.getPaddingLeft(), child.getPaddingTop(), child.getPaddingRight(), getHeight());
                    ((FrameLayout.LayoutParams) child.getLayoutParams()).gravity = Gravity.BOTTOM;
                } else {
                    // Just lay it out above the bottom nav
                    // First make sure this view has an id
                    int id = getId();
                    if (id == NO_ID) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            id = View.generateViewId();
                        } else {
                            id = new Random().nextInt(Integer.MAX_VALUE);
                        }
                        setId(id);
                    }
                    ((LayoutParams) child.getLayoutParams()).addRule(RelativeLayout.ABOVE, id);
                }

                getParent().bringChildToFront(this);
            }
        }
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        // no-op
    }

    private enum TabStyle {
        FIXED,
        SHIFTING,
        VERTICAL
    }

    public interface OnTabSelectedListener {
        /**
         * Called when a previously unselected tab is selected
         * @param position the position of the selected tab
         */
        void onTabSelected(int position);

        /**
         * Called when the previously selected tab is unselected
         * @param position the position of the unselected tab
         */
        void onTabUnselected(int position);

        /**
         * Called when the previously selected tab is reselected
         * @param position the position of the reselected tab
         */
        void onTabReselected(int position);
    }

    static class SavedState extends BaseSavedState {

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int selectedTabPosition, activeColor, inactiveColor;
        int[] backgroundColorList;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            selectedTabPosition = in.readInt();
            activeColor = in.readInt();
            inactiveColor = in.readInt();
            in.readIntArray(backgroundColorList);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(selectedTabPosition);
            out.writeInt(activeColor);
            out.writeInt(inactiveColor);
            out.writeIntArray(backgroundColorList);
        }
    }

    public class Tab extends LinearLayout {

        private AppCompatImageView mIcon;
        private AppCompatTextView mText;
        private TabStyle mTabStyle = TabStyle.FIXED;
        private boolean mIsActive = false;

        public Tab(Context context) {
            super(context);
            init();
        }

        public Tab(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            setOrientation(VERTICAL);
            setGravity(Gravity.CENTER);
            setBackground(Utils.getDrawableFromAttribute(getContext(), R.attr.selectableItemBackground));

            mIcon = new AppCompatImageView(getContext());
            int iconSize = getResources().getDimensionPixelSize(R.dimen.bottom_nav_tab_icon_size);
            addView(mIcon, new LayoutParams(iconSize, iconSize));

            mText = new AppCompatTextView(getContext());
            mText.setMaxLines(1);
            mText.setTextColor(mInactiveColor);
            addView(mText, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            configure();
        }

        private void configure() {
            // Set padding
            if (mTabStyle == TabStyle.FIXED) {
                setPadding(getResources().getDimensionPixelSize(R.dimen.bottom_nav_tab_fixed_left_right_padding), 0,
                        getResources().getDimensionPixelSize(R.dimen.bottom_nav_tab_fixed_left_right_padding), 0);
            } else {
                setPadding(0, 0, 0, 0);
            }

            // Set text size
            if (mTabStyle != TabStyle.VERTICAL) {
                float beforeTextSize = mText.getTextSize();
                float afterTextSize;
                if (mTabStyle == TabStyle.FIXED) {
                    afterTextSize = getResources().getDimensionPixelSize(mIsActive ? R.dimen.bottom_nav_tab_fixed_active_text_size
                            : R.dimen.bottom_nav_tab_fixed_inactive_text_size);
                } else {
                    afterTextSize = getResources().getDimensionPixelSize(R.dimen.bottom_nav_tab_shifting_text_size);
                }
                animateTextSize(beforeTextSize, afterTextSize);
            }

            // Set icon/text color
            int beforeColor = mText.getCurrentTextColor();
            int afterColor = mIsActive ? mActiveColor : mInactiveColor;
            animateIconColor(beforeColor, afterColor);
            if (mTabStyle != TabStyle.VERTICAL) {
                animateTextColor(beforeColor, afterColor);
            }

            // Originally tried to use the support transitions library, had too many UI bugs
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionSet set = new TransitionSet();
                set.addTransition(new ChangeBounds().excludeTarget(mText, true));
                set.addTransition(new Fade());
                TransitionManager.beginDelayedTransition(this, set);
            }

            // Hide the text if SHIFTING and not active, or if VERTICAL
            if (mTabStyle == TabStyle.VERTICAL || (mTabStyle == TabStyle.SHIFTING && !mIsActive)) {
                mText.setVisibility(GONE);
            } else {
                mText.setVisibility(VISIBLE);
            }

            invalidate();
        }

        private void animateTextSize(float before, float after) {
            ValueAnimator anim = ValueAnimator.ofFloat(before, after);
            anim.setInterpolator(new FastOutSlowInInterpolator());
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) animation.getAnimatedValue());
                }
            });
            anim.start();
        }

        private void animateTextColor(int before, int after) {
            ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), before, after);
            anim.setInterpolator(new FastOutSlowInInterpolator());
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mText.setTextColor((int) animation.getAnimatedValue());
                }
            });
            anim.start();
        }

        private void animateIconColor(int before, int after) {
            ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), before, after);
            anim.setInterpolator(new FastOutSlowInInterpolator());
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mIcon.setImageTintList(ColorStateList.valueOf((int) animation.getAnimatedValue()));
                    } else if (mIcon.getDrawable() != null) {
                        mIcon.setImageDrawable(Utils.getTintedDrawableCompat(mIcon.getDrawable(), (int) animation.getAnimatedValue()));
                    }
                }
            });
            anim.start();
        }

        /**
         * Returns if this tab is the currently selected tab
         */
        public boolean isActive() {
            return mIsActive;
        }

        /**
         * Sets this tab as the currently selected tab
         */
        public void setActive(boolean active) {
            mIsActive = active;
            configure();
        }

        /**
         * Gets the {@link TabStyle} of this tab
         * @return one of {@link TabStyle#FIXED}, {@link TabStyle#SHIFTING}, or {@link TabStyle#VERTICAL}
         */
        public TabStyle getTabStyle() {
            return mTabStyle;
        }

        /**
         * Sets the {@link TabStyle} of this tab
         */
        public void setTabStyle(TabStyle tabStyle) {
            mTabStyle = tabStyle;
            configure();
        }

        /**
         * Sets this tab's icon
         */
        public void setIcon(@DrawableRes int res) {
            mIcon.setImageResource(res);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mIcon.setImageTintList(ColorStateList.valueOf(mIsActive ? mActiveColor : mInactiveColor));
            } else if (mIcon.getDrawable() != null) {
                mIcon.setImageDrawable(Utils.getTintedDrawableCompat(mIcon.getDrawable(), mIsActive ? mActiveColor : mInactiveColor));
            }
        }

        /**
         * Sets this tab's icon
         */
        public void setIcon(Drawable drawable) {
            mIcon.setImageDrawable(drawable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mIcon.setImageTintList(ColorStateList.valueOf(mIsActive ? mActiveColor : mInactiveColor));
            } else if (mIcon.getDrawable() != null) {
                mIcon.setImageDrawable(Utils.getTintedDrawableCompat(mIcon.getDrawable(), mIsActive ? mActiveColor : mInactiveColor));
            }
        }

        /**
         * Sets this tab's text
         */
        public void setText(CharSequence text) {
            mText.setText(text);
        }

        /**
         * Sets this tab's text
         */
        public void setText(@StringRes int res) {
            mText.setText(res);
        }
    }
}
