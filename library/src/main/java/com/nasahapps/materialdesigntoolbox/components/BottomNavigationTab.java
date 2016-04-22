package com.nasahapps.materialdesigntoolbox.components;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nasahapps.materialdesigntoolbox.R;
import com.nasahapps.materialdesigntoolbox.Utils;

/**
 * Created by Hakeem on 4/20/16.
 */
public class BottomNavigationTab extends LinearLayout implements View.OnClickListener {

    private ImageView mIcon;
    private TextView mText;

    private boolean mIsSelected, mIsFixed, mUsesDarkTheme;
    private Drawable mOriginalDrawable;
    private int mOriginalDrawableResource, mAccentColor;

    public BottomNavigationTab(Context context) {
        super(context);
        init(null);
    }

    public BottomNavigationTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        setBackgroundResource(typedValue.resourceId);
        setOnClickListener(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            // Pre-Kitkat, we'll just use the standard LayoutTransition APIs
            // but post-Kitkat, we'll use the new Transition APIs for layout adjustments
            setLayoutTransition(new LayoutTransition());
        }
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        adjustMinimumWidth(mIsFixed);

        int iconSize = getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_icon_size);
        mIcon = new AppCompatImageView(getContext());
        mIcon.setLayoutParams(new LayoutParams(iconSize, iconSize));
        mIcon.setAlpha(Utils.getFloatResource(getContext(),
                mUsesDarkTheme ? R.dimen.na_inactive_opacity_dark : R.dimen.na_inactive_opacity));
        addView(mIcon);

        mText = new AppCompatTextView(getContext());
        mText.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mText.setSingleLine();
        mText.setGravity(Gravity.CENTER);
        float textSize;
        if (mIsFixed) {
            textSize = getResources().getDimension(mIsSelected ? R.dimen.na_fixed_bottom_nav_text_size_active
                    : R.dimen.na_fixed_bottom_nav_text_size_inactive);
        } else {
            textSize = getResources().getDimension(R.dimen.na_shifting_bottom_nav_text_size);
        }
        mText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mText.setTextColor(ContextCompat.getColor(getContext(), mUsesDarkTheme ? R.color.bottom_bar_inactive_tint_dark
                : R.color.bottom_bar_inactive_tint));
        addView(mText);
        if (!mIsFixed) {
            mText.setVisibility(mIsSelected ? View.VISIBLE : View.GONE);
        }

        if (attrs != null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.BottomNavigationTab, 0, 0);
            try {
                String text = ta.getString(R.styleable.BottomNavigationTab_bottomNavTabText);
                if (text != null) {
                    mText.setText(text);
                }

                Drawable icon = ta.getDrawable(R.styleable.BottomNavigationTab_bottomNavTabIcon);
                if (icon != null) {
                    mIcon.setImageDrawable(icon);
                }
            } finally {
                ta.recycle();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int position = (((ViewGroup) v.getParent())).indexOfChild(v);
        if (getParent() instanceof OnTabSelectedListener) {
            if (mIsSelected) {
                // This tab has been reselected
                ((OnTabSelectedListener) getParent()).onTabReselected(this, position);
            } else {
                // New tab selected
                ((OnTabSelectedListener) getParent()).onTabSelected(this, position);
            }
        }
    }

    private void adjustMinimumWidth(boolean fixed) {
        if (fixed) {
            setMinimumWidth(getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_tab_min_width));
        } else {
            setMinimumWidth(getResources().getDimensionPixelSize(mIsSelected ? R.dimen.na_shifting_bottom_nav_tab_min_width_active
                    : R.dimen.na_shifting_bottom_nav_tab_min_width_inactive));
        }
    }

    public boolean isFixed() {
        return mIsFixed;
    }

    public BottomNavigationTab setFixed(boolean fixed) {
        mIsFixed = fixed;
        return this;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
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
    }

    private Animator setupAnimator(Animator anim) {
        anim.setDuration(BottomNavigationBar.ANIM_DURATION);
        anim.setInterpolator(BottomNavigationBar.ANIM_INTERPOLATOR);
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
            alpha = Utils.getFloatResource(getContext(),
                    mUsesDarkTheme ? R.dimen.na_inactive_opacity_dark : R.dimen.na_inactive_opacity);
        }
        getChildAt(0).setAlpha(alpha);

        int textColor;
        if (mUsesDarkTheme) {
            textColor = mIsSelected ? Color.WHITE : ContextCompat.getColor(getContext(),
                    R.color.bottom_bar_inactive_tint_dark);
        } else {
            textColor = mIsSelected ? mAccentColor : ContextCompat.getColor(getContext(),
                    R.color.bottom_bar_inactive_tint);
        }
        // noinspection ResourceType
        ((TextView) getChildAt(1)).setTextColor(textColor);
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

        int fullWidth = ((View) getParent()).getMeasuredWidth();
        int tabWidth;
        // For these tabs to have the "suggested" width (particularly for shifting tabs),
        // the absolute minimum width of the entire bottom nav bar must be at least 352dp for 5 tabs
        // (288 for 4 tabs)
        // Any smaller and we can't apply the suggested tab width
        if ((getTabCount() == 4 && Utils.pixelToDp(getContext(), fullWidth) <= 288)
                || (getTabCount() == 5 && Utils.pixelToDp(getContext(), fullWidth) <= 352)) {
            tabWidth = fullWidth / getTabCount();
        } else {
            if (fixed) {
                tabWidth = Math.max(fullWidth / getTabCount(), getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_tab_min_width));
                tabWidth = Math.min(tabWidth, getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_tab_max_width));
            } else {
                tabWidth = mIsSelected ? calculateActiveTabWidth() : calculateInactiveTabWidth();
            }
        }

        LayoutParams lp = (LayoutParams) getLayoutParams();
        lp.width = tabWidth;
        setLayoutParams(lp);
    }

    private int calculateActiveTabWidth() {
        int fullWidth = ((View) getParent()).getMeasuredWidth();
        int inactiveMinWidth = getResources().getDimensionPixelSize(R.dimen.na_shifting_bottom_nav_tab_min_width_inactive);
        int activeWidth = fullWidth - ((getTabCount() - 1) * inactiveMinWidth);
        activeWidth = Math.max(activeWidth, getResources().getDimensionPixelSize(R.dimen.na_shifting_bottom_nav_tab_min_width_active));
        activeWidth = Math.min(activeWidth, getResources().getDimensionPixelSize(R.dimen.na_shifting_bottom_nav_tab_max_width_active));
        return activeWidth;
    }

    private int calculateInactiveTabWidth() {
        int activeWidth = calculateActiveTabWidth();
        int fullWidth = ((View) getParent()).getMeasuredWidth();
        int inactiveWidth = ((fullWidth - activeWidth) / (getTabCount() - 1));
        inactiveWidth = Math.max(inactiveWidth, getResources().getDimensionPixelSize(R.dimen.na_shifting_bottom_nav_tab_min_width_inactive));
        inactiveWidth = Math.min(inactiveWidth, getResources().getDimensionPixelSize(R.dimen.na_shifting_bottom_nav_tab_max_width_inactive));
        return inactiveWidth;
    }

    private void beginDelayedTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(this);
        }
    }

    private void animateTextSize(boolean selected, boolean fixed) {
        if (fixed) {
            float endingTextSize = getResources().getDimension(selected ? R.dimen.na_fixed_bottom_nav_text_size_active
                    : R.dimen.na_fixed_bottom_nav_text_size_inactive);
            ValueAnimator textSizeAnim = ValueAnimator.ofFloat(((TextView) getChildAt(1)).getTextSize(),
                    endingTextSize);
            textSizeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ((TextView) getChildAt(1)).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            (float) animation.getAnimatedValue());
                }
            });
            setupAnimator(textSizeAnim).start();
        } else {
            ((TextView) getChildAt(1)).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.na_shifting_bottom_nav_text_size));
        }
    }

    private void animateImageAlpha(boolean selected) {
        ValueAnimator imageAlphaAnim = ValueAnimator.ofFloat(getChildAt(0).getAlpha(),
                selected ? 1f : Utils.getFloatResource(getContext(),
                        mUsesDarkTheme ? R.dimen.na_inactive_opacity_dark : R.dimen.na_inactive_opacity));
        imageAlphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getChildAt(0).setAlpha((float) animation.getAnimatedValue());
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
            endingColor = ContextCompat.getColor(getContext(),
                    mUsesDarkTheme ? R.color.bottom_bar_inactive_tint_dark
                            : R.color.bottom_bar_inactive_tint);
        }
        if (fixed) {
            getChildAt(1).setVisibility(View.VISIBLE);
            ValueAnimator textColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(),
                    ((TextView) getChildAt(1)).getCurrentTextColor(), endingColor);
            textColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ((TextView) getChildAt(1)).setTextColor((int) animation.getAnimatedValue());
                }
            });
            setupAnimator(textColorAnim).start();
        } else {
            ((TextView) getChildAt(1)).setTextColor(endingColor);
            getChildAt(1).setVisibility(selected ? View.VISIBLE : View.GONE);
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
                    wrappedDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(),
                            mOriginalDrawableResource));
                }

                if (wrappedDrawable != null) {
                    wrappedDrawable = DrawableCompat.wrap(wrappedDrawable);
                    DrawableCompat.setTint(wrappedDrawable, (int) animation.getAnimatedValue());
                    ((ImageView) getChildAt(0)).setImageDrawable(wrappedDrawable);
                }
            }
        });
        setupAnimator(imageTintAnim).start();
    }

    private int getTabCount() {
        int count = 0;
        ViewGroup parent = (ViewGroup) getParent();
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i) instanceof BottomNavigationTab) {
                count++;
            }
        }

        return count;
    }

    @Nullable
    public Drawable getIcon() {
        return ((ImageView) getChildAt(0)).getDrawable();
    }

    public BottomNavigationTab setIcon(Drawable d) {
        mOriginalDrawable = d;
        ((ImageView) getChildAt(0)).setImageDrawable(tintDrawableForTheme(mOriginalDrawable));
        return this;
    }

    public BottomNavigationTab setIcon(@DrawableRes int res) {
        mOriginalDrawableResource = res;
        mOriginalDrawable = ContextCompat.getDrawable(getContext(), res);
        ((ImageView) getChildAt(0))
                .setImageDrawable(tintDrawableForTheme(mOriginalDrawable));
        return this;
    }

    private Drawable tintDrawableForTheme(Drawable d) {
        Drawable wrappedDrawable = DrawableCompat.wrap(d);
        DrawableCompat.setTint(wrappedDrawable, mUsesDarkTheme ? Color.WHITE : Color.BLACK);
        return wrappedDrawable;
    }

    public CharSequence getText() {
        return ((TextView) getChildAt(1)).getText();
    }

    public BottomNavigationTab setText(CharSequence s) {
        ((TextView) getChildAt(1)).setText(s);
        return this;
    }

    public BottomNavigationTab setText(@StringRes int res) {
        ((TextView) getChildAt(1)).setText(res);
        return this;
    }

    public interface OnTabSelectedListener {
        void onTabSelected(BottomNavigationTab tab, int position);

        void onTabReselected(BottomNavigationTab tab, int position);
    }
}
