package com.nasahapps.materialdesigntoolbox.components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class BottomNavigationBar extends FrameLayout {

    public static final int INVALID_TAB_POSITION = -1;
    private static final long ANIM_DURATION = 250;
    private static final Interpolator ANIM_INTERPOLATOR = new FastOutSlowInInterpolator();

    @ColorInt
    private int mAccentColor;
    private int mCurrentTab;
    private boolean mUsesDarkTheme, mInitialized;
    private ArrayList<Tab> mTabs = new ArrayList<>();
    private OnTabSelectedListener mOnTabSelectedListener;
    @ColorInt
    private int[] mBackgroundColors;
    private LinearLayout mTabLayout;
    private View mCircularRevealBackground, mCircularRevealForeground;

    public BottomNavigationBar(Context context) {
        super(context);
        initView(null);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        mCircularRevealBackground = new View(getContext());
        addView(mCircularRevealBackground, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mCircularRevealForeground = new View(getContext());
        addView(mCircularRevealForeground, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mTabLayout = new LinearLayout(getContext());
        mTabLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTabLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        ViewCompat.setElevation(this, getResources().getDimensionPixelSize(R.dimen.na_bottom_nav_elevation));
        addView(mTabLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        if (attrs != null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.BottomNavigationBar, 0, 0);

            try {
                mUsesDarkTheme = ta.getBoolean(R.styleable.BottomNavigationBar_bottomNavDarkTheme, false);

                mAccentColor = mUsesDarkTheme ? Color.WHITE
                        : ta.getColor(R.styleable.BottomNavigationBar_bottomNavAccentColor, 0);
                if (mAccentColor == 0) {
                    TypedValue tv = new TypedValue();
                    getContext().getTheme().resolveAttribute(R.attr.colorAccent, tv, true);
                    mAccentColor = tv.data;
                }

                if (getBackground() instanceof ColorDrawable) {
                    int backgroundColor = ((ColorDrawable) getBackground()).getColor();
                    mCircularRevealBackground.setBackgroundColor(backgroundColor);
                } else {
                    mCircularRevealBackground.setBackgroundColor(Color.WHITE);
                }
            } finally {
                ta.recycle();
            }

            if (isInEditMode()) {
                addTab(newTab().setText("Recents").setIcon(R.drawable.ic_na_test_history));
                addTab(newTab().setText("Favorites").setIcon(R.drawable.ic_na_test_favorite));
                addTab(newTab().setText("Nearby").setIcon(R.drawable.ic_na_test_location));
                addTab(newTab().setText("Movies").setIcon(R.drawable.ic_na_test_movie));
                addTab(newTab().setText("Music").setIcon(R.drawable.ic_na_test_music));
            }
        }
    }

    public void addTab(Tab tab) {
        addTab(tab, mTabs.size());
    }

    public void addTab(Tab tab, boolean setSelected) {
        addTab(tab, mTabs.size(), setSelected);
    }

    public void addTab(Tab tab, int position) {
        addTab(tab, position, false);
    }

    public void addTab(Tab tab, int position, boolean setSelected) {
        checkTabCount();
        mTabs.add(position, tab);
        if (setSelected || getTabCount() == 1) {
            setTabSelected(position);
        }

        boolean fixed = getTabCount() <= 3;
        mTabLayout.addView(tab.getView(), new LinearLayout.LayoutParams(0,
                getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_height)));
        for (Tab tab1 : mTabs) {
            tab1.adjustForFixedMode(fixed);
        }
    }

    public Tab newTab() {
        return new Tab(getContext(), getTabCount() < 3);
    }

    private void checkTabCount() {
        if (getTabCount() >= 5) {
            throw new RuntimeException("You can only have a max of 5 tabs! If you want more, it's " +
                    "best to use a navigation drawer.");
        }
    }

    public void setTabSelected(int position) {
        if (getSelectedTabPosition() != position) {
            for (Tab tab : mTabs) {
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

    public Tab getTabAt(int position) {
        return mTabs.get(position);
    }

    public int getTabCount() {
        return mTabs.size();
    }

    public void removeAllTabs() {
        mTabs = new ArrayList<>();
        mTabLayout.removeAllViews();
    }

    public void removeTab(Tab tab) {
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i).equals(tab)) {
                mTabLayout.removeView(mTabs.get(i).getView());
                mTabs.remove(i);

                if (getSelectedTabPosition() == INVALID_TAB_POSITION) {
                    setTabSelected(0);
                }

                break;
            }
        }

        for (Tab tab1 : mTabs) {
            tab1.adjustForFixedMode(mTabs.size() <= 3);
        }
    }

    public void removeTabAt(int position) {
        mTabLayout.removeView(mTabs.get(position).getView());
        mTabs.remove(position);

        if (getSelectedTabPosition() == INVALID_TAB_POSITION) {
            setTabSelected(0);
        }

        for (Tab tab1 : mTabs) {
            tab1.adjustForFixedMode(mTabs.size() <= 3);
        }
    }

    public void setUsesDarkTheme(boolean usesDarkTheme) {
        mUsesDarkTheme = usesDarkTheme;
        for (Tab tab : mTabs) {
            tab.adjustForDarkTheme();
        }
    }

    public void setAccentColor(int accentColor) {
        mAccentColor = accentColor;
    }

    @Override
    public void setBackgroundColor(int color) {
        mCircularRevealBackground.setBackgroundColor(color);
        setAccentColor(Color.WHITE);
        setUsesDarkTheme(true);
    }

    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
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
        if (mInitialized) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Do a circular view animation
                mCircularRevealForeground.post(new Runnable() {
                    @Override
                    public void run() {
                        int[] absPosition = new int[2];
                        mTabs.get(position).getView().getLocationOnScreen(absPosition);
                        int cx = absPosition[0] + (mTabs.get(position).getView().getWidth() / 2);
                        int cy = mTabs.get(position).getView().getHeight() / 2;
                        int endRadius = (int) (getMeasuredWidth() * 1.1);
                        mCircularRevealForeground.setBackgroundColor(mBackgroundColors[position]);
                        Animator anim = ViewAnimationUtils.createCircularReveal(mCircularRevealForeground,
                                cx, cy, 0, endRadius);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mCircularRevealBackground.setBackgroundColor(mBackgroundColors[position]);
                            }
                        });
                        anim.setDuration(ANIM_DURATION * 3);
                        anim.setInterpolator(ANIM_INTERPOLATOR);
                        anim.start();
                    }
                });
            } else {
                // Do a simple color fade
                int startingColor;
                if (getBackground() instanceof ColorDrawable) {
                    startingColor = ((ColorDrawable) getBackground()).getColor();
                } else {
                    startingColor = Color.WHITE;
                }

                ValueAnimator backgroundAnim = ValueAnimator.ofObject(new ArgbEvaluator(),
                        startingColor, mBackgroundColors[position]);
                backgroundAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        setBackgroundColor((int) animation.getAnimatedValue());
                    }
                });
                backgroundAnim.setDuration(ANIM_DURATION);
                backgroundAnim.setInterpolator(ANIM_INTERPOLATOR);
                backgroundAnim.start();
            }
        } else {
            mInitialized = true;
            setBackgroundColor(mBackgroundColors[position]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (Tab tab : mTabs) {
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

    public interface OnTabSelectedListener {
        void onTabSelected(Tab tab, int position);

        void onTabReselected(Tab tab, int position);

        void onTabUnselected(Tab tab, int position);
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
                        Log.d("BottomNav", "scrollY: " + scrollY + ", oldScrollY: " + oldScrollY);
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

    public class Tab {

        private LinearLayout mView;
        private boolean mIsSelected, mIsFixed;
        private Drawable mOriginalDrawable;
        private int mOriginalDrawableResource;

        private Tab(Context c, boolean isFixed) {
            mIsFixed = isFixed;

            mView = new LinearLayout(c);
            TypedValue typedValue = new TypedValue();
            c.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
            mView.setBackgroundResource(typedValue.resourceId);
            mView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (((ViewGroup) v.getParent())).indexOfChild(v);
                    if (mOnTabSelectedListener != null) {
                        if (mIsSelected) {
                            // This tab has been reselected
                            mOnTabSelectedListener.onTabReselected(Tab.this, position);
                        } else {
                            // New tab selected
                            mOnTabSelectedListener.onTabSelected(Tab.this, position);
                            mOnTabSelectedListener.onTabUnselected(mTabs.get(mCurrentTab), mCurrentTab);
                        }
                    }

                    setTabSelected(position);
                }
            });
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                // Pre-Kitkat, we'll just use the standard LayoutTransition APIs
                // but post-Kitkat, we'll use the new Transition APIs for layout adjustments
                mView.setLayoutTransition(new LayoutTransition());
            }
            mView.setOrientation(LinearLayout.VERTICAL);
            mView.setGravity(Gravity.CENTER);

            adjustMinimumWidth(mIsFixed);

            int iconSize = c.getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_icon_size);
            ImageView iv = new ImageView(c);
            iv.setLayoutParams(new LayoutParams(iconSize, iconSize));
            iv.setAlpha(Utils.getFloatResource(c, mUsesDarkTheme ? R.dimen.na_inactive_opacity_dark : R.dimen.na_inactive_opacity));
            mView.addView(iv);

            TextView tv = new TextView(c);
            tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setSingleLine();
            tv.setGravity(Gravity.CENTER);
            float textSize;
            if (mIsFixed) {
                textSize = c.getResources().getDimension(mIsSelected ? R.dimen.na_fixed_bottom_nav_text_size_active
                        : R.dimen.na_fixed_bottom_nav_text_size_inactive);
            } else {
                textSize = c.getResources().getDimension(R.dimen.na_shifting_bottom_nav_text_size);
            }
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv.setTextColor(ContextCompat.getColor(c, mUsesDarkTheme ? R.color.bottom_bar_inactive_tint_dark
                    : R.color.bottom_bar_inactive_tint));
            mView.addView(tv);
            if (!mIsFixed) {
                tv.setVisibility(mIsSelected ? VISIBLE : GONE);
            }
        }

        private void adjustMinimumWidth(boolean fixed) {
            if (fixed) {
                mView.setMinimumWidth(mView.getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_tab_min_width));
            } else {
                mView.setMinimumWidth(mView.getResources().getDimensionPixelSize(mIsSelected ? R.dimen.na_shifting_bottom_nav_tab_min_width_active
                        : R.dimen.na_shifting_bottom_nav_tab_min_width_inactive));
            }
        }

        public boolean isFixed() {
            return mIsFixed;
        }

        public Tab setFixed(boolean fixed) {
            mIsFixed = fixed;
            return this;
        }

        public boolean isSelected() {
            return mIsSelected;
        }

        public Tab setSelected(boolean selected) {
            boolean wasPreviouslySelected = mIsSelected;
            mIsSelected = selected;

            // Animate changes in paddingTop and textSize, as well as tint color
            animateTextSize(selected, mIsFixed);
            animateImageAlpha(selected);
            animateTextColor(selected, mIsFixed);
            // We'll only animate image tint if the selection state of this tab has changed
            if (wasPreviouslySelected != selected) {
                animateImageTint(selected);
            }

            return this;
        }

        private Animator setupAnimator(Animator anim) {
            anim.setDuration(ANIM_DURATION);
            anim.setInterpolator(ANIM_INTERPOLATOR);
            return anim;
        }

        public void adjustForDarkTheme() {
            beginDelayedTransition();

            if (mOriginalDrawable != null) {
                setIcon(mOriginalDrawable);
            } else if (mOriginalDrawableResource != 0) {
                setIcon(mOriginalDrawableResource);
            }

            float alpha;
            if (mIsSelected) {
                alpha = 1f;
            } else {
                alpha = Utils.getFloatResource(mView.getContext(),
                        mUsesDarkTheme ? R.dimen.na_inactive_opacity_dark : R.dimen.na_inactive_opacity);
            }
            mView.getChildAt(0).setAlpha(alpha);

            int textColor;
            if (mUsesDarkTheme) {
                textColor = mIsSelected ? Color.WHITE : ContextCompat.getColor(mView.getContext(),
                        R.color.bottom_bar_inactive_tint_dark);
            } else {
                textColor = mIsSelected ? mAccentColor : ContextCompat.getColor(mView.getContext(),
                        R.color.bottom_bar_inactive_tint);
            }
            // noinspection ResourceType
            ((TextView) mView.getChildAt(1)).setTextColor(textColor);
        }

        public void adjustForFixedMode(boolean fixed) {
            if (mIsFixed != fixed) {
                // Readjust
                mIsFixed = fixed;
                adjustMinimumWidth(fixed);
                setSelected(mIsSelected);
            }
        }

        public void adjustTabWidth(boolean fixed) {
            beginDelayedTransition();

            int fullWidth = ((View) mView.getParent()).getMeasuredWidth();
            int tabWidth;
            // For these tabs to have the "suggested" width (particularly for shifting tabs),
            // the absolute minimum width of the entire bottom nav bar must be at least 352dp for 5 tabs
            // (288 for 4 tabs)
            // Any smaller and we can't apply the suggested tab width
            if ((getTabCount() == 4 && Utils.pixelToDp(mView.getContext(), fullWidth) <= 288)
                    || (getTabCount() == 5 && Utils.pixelToDp(mView.getContext(), fullWidth) <= 352)) {
                tabWidth = fullWidth / getTabCount();
            } else {
                if (fixed) {
                    tabWidth = Math.max(fullWidth / getTabCount(), mView.getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_tab_min_width));
                    tabWidth = Math.min(tabWidth, mView.getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_tab_max_width));
                } else {
                    tabWidth = mIsSelected ? calculateActiveTabWidth() : calculateInactiveTabWidth();
                }
            }

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mView.getLayoutParams();
            lp.width = tabWidth;
            mView.setLayoutParams(lp);
        }

        private int calculateActiveTabWidth() {
            int fullWidth = ((View) mView.getParent()).getMeasuredWidth();
            int inactiveMinWidth = mView.getResources().getDimensionPixelSize(R.dimen.na_shifting_bottom_nav_tab_min_width_inactive);
            int activeWidth = fullWidth - ((getTabCount() - 1) * inactiveMinWidth);
            activeWidth = Math.max(activeWidth, mView.getResources().getDimensionPixelSize(R.dimen.na_shifting_bottom_nav_tab_min_width_active));
            activeWidth = Math.min(activeWidth, mView.getResources().getDimensionPixelSize(R.dimen.na_shifting_bottom_nav_tab_max_width_active));
            return activeWidth;
        }

        private int calculateInactiveTabWidth() {
            int activeWidth = calculateActiveTabWidth();
            int fullWidth = ((View) mView.getParent()).getMeasuredWidth();
            int inactiveWidth = ((fullWidth - activeWidth) / (getTabCount() - 1));
            inactiveWidth = Math.max(inactiveWidth, mView.getResources().getDimensionPixelSize(R.dimen.na_shifting_bottom_nav_tab_min_width_inactive));
            inactiveWidth = Math.min(inactiveWidth, mView.getResources().getDimensionPixelSize(R.dimen.na_shifting_bottom_nav_tab_max_width_inactive));
            return inactiveWidth;
        }

        private void beginDelayedTransition() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(mView);
            }
        }

        private void animateTextSize(boolean selected, boolean fixed) {
            if (fixed) {
                float endingTextSize = mView.getResources().getDimension(selected ? R.dimen.na_fixed_bottom_nav_text_size_active
                        : R.dimen.na_fixed_bottom_nav_text_size_inactive);
                ValueAnimator textSizeAnim = ValueAnimator.ofFloat(((TextView) mView.getChildAt(1)).getTextSize(),
                        endingTextSize);
                textSizeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ((TextView) mView.getChildAt(1)).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                (float) animation.getAnimatedValue());
                    }
                });
                setupAnimator(textSizeAnim).start();
            } else {
                ((TextView) mView.getChildAt(1)).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        mView.getResources().getDimension(R.dimen.na_shifting_bottom_nav_text_size));
            }
        }

        private void animateImageAlpha(boolean selected) {
            ValueAnimator imageAlphaAnim = ValueAnimator.ofFloat(mView.getChildAt(0).getAlpha(),
                    selected ? 1f : Utils.getFloatResource(mView.getContext(),
                            mUsesDarkTheme ? R.dimen.na_inactive_opacity_dark : R.dimen.na_inactive_opacity));
            imageAlphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mView.getChildAt(0).setAlpha((float) animation.getAnimatedValue());
                }
            });
            setupAnimator(imageAlphaAnim).start();
        }

        private void animateTextColor(boolean selected, boolean fixed) {
            // If fixed, we'll actually animate the text color
            // but if shifting, we'll just have the text be VISIBLE/GONE
            int endingColor;
            if (selected) {
                endingColor = mAccentColor;
            } else {
                endingColor = ContextCompat.getColor(mView.getContext(),
                        mUsesDarkTheme ? R.color.bottom_bar_inactive_tint_dark
                                : R.color.bottom_bar_inactive_tint);
            }
            if (fixed) {
                mView.getChildAt(1).setVisibility(VISIBLE);
                ValueAnimator textColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(),
                        ((TextView) mView.getChildAt(1)).getCurrentTextColor(), endingColor);
                textColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ((TextView) mView.getChildAt(1)).setTextColor((int) animation.getAnimatedValue());
                    }
                });
                setupAnimator(textColorAnim).start();
            } else {
                ((TextView) mView.getChildAt(1)).setTextColor(endingColor);
                mView.getChildAt(1).setVisibility(selected ? VISIBLE : GONE);
            }
        }

        private void animateImageTint(boolean selected) {
            int originalColor = mUsesDarkTheme ? Color.WHITE : Color.BLACK;
            ValueAnimator imageTintAnim = ValueAnimator.ofObject(new ArgbEvaluator(),
                    selected ? originalColor : mAccentColor,
                    selected ? mAccentColor : originalColor);
            imageTintAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Drawable wrappedDrawable = null;
                    if (mOriginalDrawable != null) {
                        wrappedDrawable = DrawableCompat.wrap(mOriginalDrawable);
                    } else if (mOriginalDrawableResource != 0) {
                        wrappedDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(mView.getContext(),
                                mOriginalDrawableResource));
                    }

                    if (wrappedDrawable != null) {
                        wrappedDrawable = DrawableCompat.wrap(wrappedDrawable);
                        DrawableCompat.setTint(wrappedDrawable, (int) animation.getAnimatedValue());
                        ((ImageView) mView.getChildAt(0)).setImageDrawable(wrappedDrawable);
                    }
                }
            });
            setupAnimator(imageTintAnim).start();
        }

        @Nullable
        public Drawable getIcon() {
            return ((ImageView) mView.getChildAt(0)).getDrawable();
        }

        public Tab setIcon(Drawable d) {
            mOriginalDrawable = d;
            ((ImageView) mView.getChildAt(0)).setImageDrawable(tintDrawableForTheme(mOriginalDrawable));
            return this;
        }

        public Tab setIcon(@DrawableRes int res) {
            mOriginalDrawableResource = res;
            mOriginalDrawable = ContextCompat.getDrawable(mView.getContext(), res);
            ((ImageView) mView.getChildAt(0))
                    .setImageDrawable(tintDrawableForTheme(mOriginalDrawable));
            return this;
        }

        private Drawable tintDrawableForTheme(Drawable d) {
            Drawable wrappedDrawable = DrawableCompat.wrap(d);
            DrawableCompat.setTint(wrappedDrawable, mUsesDarkTheme ? Color.WHITE : Color.BLACK);
            return wrappedDrawable;
        }

        public CharSequence getText() {
            return ((TextView) mView.getChildAt(1)).getText();
        }

        public Tab setText(CharSequence s) {
            ((TextView) mView.getChildAt(1)).setText(s);
            return this;
        }

        public Tab setText(@StringRes int res) {
            ((TextView) mView.getChildAt(1)).setText(res);
            return this;
        }

        public Object getTag() {
            return mView.getTag();
        }

        public Tab setTag(Object tag) {
            mView.setTag(tag);
            return this;
        }

        public View getView() {
            return mView;
        }
    }
}
