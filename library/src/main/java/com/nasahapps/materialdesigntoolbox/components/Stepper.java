package com.nasahapps.materialdesigntoolbox.components;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nasahapps.materialdesigntoolbox.R;
import com.nasahapps.materialdesigntoolbox.Utils;

/**
 * Created by Hakeem on 4/14/16.
 */
public class Stepper extends LinearLayout {

    TextView mStepperCircleText;
    ImageView mStepperCircleCheck;
    TextView mStepperTitle;
    TextView mStepperSubtitle;

    boolean mActive, mCompleted, mError;
    int mAccent;

    public Stepper(Context context) {
        super(context);
        init(null);
    }

    public Stepper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Stepper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setBackgroundColor(Color.WHITE);
        setGravity(Gravity.CENTER);

        FrameLayout fl = new FrameLayout(getContext());
        addView(fl, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        // Create mStepperCircleText and mStepperCircleCheck and add it to this FrameLayout
        mStepperCircleText = new AppCompatTextView(getContext());
        mStepperCircleText.setBackgroundResource(R.drawable.stepper_circle_background);
        mStepperCircleText.setGravity(Gravity.CENTER);
        mStepperCircleText.setTextColor(Color.WHITE);
        mStepperCircleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
        fl.addView(mStepperCircleText, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        mStepperCircleCheck = new AppCompatImageView(getContext());
        fl.addView(mStepperCircleCheck, new FrameLayout.LayoutParams(Utils.dpToPixel(getContext(), 24),
                Utils.dpToPixel(getContext(), 24)));

        LinearLayout ll = new LinearLayout(getContext());
        ll.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.setGravity(Gravity.CENTER_VERTICAL);
        ll.setOrientation(VERTICAL);
        addView(ll);
        // Create mStepperTitle and mStepperSubtitle and add it to this LinearLayout
        mStepperTitle = new AppCompatTextView(getContext());
        mStepperTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        ll.addView(mStepperTitle, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        mStepperSubtitle = new AppCompatTextView(getContext());
        mStepperSubtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
        mStepperSubtitle.setTextColor(ContextCompat.getColor(getContext(), R.color.nh_black_54));
        ll.addView(mStepperSubtitle, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Stepper, 0, 0);
            try {
                String title = a.getString(R.styleable.Stepper_stepperTitle);
                if (title != null) {
                    mStepperTitle.setText(title);
                }

                String subtitle = a.getString(R.styleable.Stepper_stepperSubtitle);
                if (subtitle != null) {
                    mStepperSubtitle.setText(subtitle);
                }

                String stepText = a.getString(R.styleable.Stepper_stepperStepText);
                if (stepText != null) {
                    mStepperCircleText.setText(stepText);
                }

                mActive = a.getBoolean(R.styleable.Stepper_stepperActive, false);
                mCompleted = a.getBoolean(R.styleable.Stepper_stepperCompleted, false);
                mError = a.getBoolean(R.styleable.Stepper_stepperError, false);
                mAccent = a.getColor(R.styleable.Stepper_stepperAccent,
                        Utils.getColorFromAttribute(getContext(), R.attr.colorPrimary));
            } finally {
                a.recycle();
            }
        }

        setupView();
    }

    private void setupView() {
        if (getChildCount() != 0) {
            if (getOrientation() == HORIZONTAL) {
                setPadding(Utils.dpToPixel(getContext(), 8), Utils.dpToPixel(getContext(), 24),
                        Utils.dpToPixel(getContext(), 8), Utils.dpToPixel(getContext(), 24));

                LinearLayout ll = (LinearLayout) getChildAt(1);
                ll.setGravity(Gravity.CENTER_VERTICAL);
                LayoutParams lp = (LayoutParams) ll.getLayoutParams();
                MarginLayoutParamsCompat.setMarginStart(lp, Utils.dpToPixel(getContext(), 8));
                ll.setLayoutParams(lp);
            } else {
                int dp24 = Utils.dpToPixel(getContext(), 24);
                setPadding(dp24, dp24, dp24, dp24);

                LinearLayout ll = (LinearLayout) getChildAt(1);
                ll.setGravity(Gravity.CENTER_HORIZONTAL);
                LayoutParams lp = (LayoutParams) ll.getLayoutParams();
                lp.setMargins(lp.leftMargin, Utils.dpToPixel(getContext(), 16), lp.rightMargin, lp.bottomMargin);
                ll.setLayoutParams(lp);
            }

            if (mActive) {
                ViewCompat.setBackgroundTintList(mStepperCircleText, ColorStateList.valueOf(mAccent));
                mStepperTitle.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                mStepperTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.nh_black_87));
            } else {
                ViewCompat.setBackgroundTintList(mStepperCircleText, ColorStateList.valueOf(ContextCompat.getColor(getContext(),
                        R.color.nh_black_38)));
                mStepperTitle.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
                mStepperTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.nh_black_38));
            }
            mStepperSubtitle.setTextColor(ContextCompat.getColor(getContext(), R.color.nh_black_54));

            if (mCompleted || mError) {
                mStepperCircleText.setVisibility(GONE);
                mStepperCircleCheck.setVisibility(VISIBLE);
                if (mError) {
                    mStepperCircleCheck.setImageDrawable(Utils.getTintedDrawable(ContextCompat.getDrawable(getContext(),
                            R.drawable.ic_nh_report_problem), ContextCompat.getColor(getContext(), R.color.nh_red_500)));
                    mStepperTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.nh_red_500));
                    mStepperSubtitle.setTextColor(ContextCompat.getColor(getContext(), R.color.nh_red_500));
                } else {
                    mStepperCircleCheck.setImageDrawable(Utils.getTintedDrawable(ContextCompat.getDrawable(getContext(),
                            R.drawable.ic_nh_check_circle), mAccent));
                    mStepperTitle.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                    mStepperTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.nh_black_87));
                }
            } else {
                mStepperCircleCheck.setVisibility(GONE);
                mStepperCircleText.setVisibility(VISIBLE);
            }

            if (TextUtils.isEmpty(mStepperSubtitle.getText())) {
                mStepperSubtitle.setVisibility(GONE);
            } else {
                mStepperSubtitle.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        setupView();
    }

    public CharSequence getStepperCircleText() {
        return mStepperCircleText.getText();
    }

    public void setStepperCircleText(CharSequence text) {
        mStepperCircleText.setText(text);
    }

    public CharSequence getStepperTitle() {
        return mStepperTitle.getText();
    }

    public void setStepperTitle(CharSequence text) {
        mStepperTitle.setText(text);
    }

    public CharSequence getStepperSubtitle() {
        return mStepperSubtitle.getText();
    }

    public void setStepperSubtitle(CharSequence text) {
        mStepperSubtitle.setText(text);
    }

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean active) {
        mActive = active;
        setupView();
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
        setupView();
    }

    public boolean isError() {
        return mError;
    }

    public void setError(boolean error) {
        mError = error;
        setupView();
    }
}
