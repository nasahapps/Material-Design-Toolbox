package com.nasahapps.materialdesigntoolbox.components;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nasahapps.materialdesigntoolbox.R;
import com.nasahapps.materialdesigntoolbox.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Hakeem on 4/16/16.
 */
public class StepperProgressLayout extends RelativeLayout {

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_DOTS = 1;
    public static final int TYPE_BAR = 2;

    TextView mStepTextView;
    FrameLayout mContainerView;
    LinearLayout mBottomBar, mStepDotsLayout;
    Button mBackButton, mNextButton;
    ProgressBar mStepProgressBar;

    int mMaxProgress, mCurrentProgress = 1, mProgressType, mAccent;
    String mFinishText = "Finish", mNextText = "Next";

    public StepperProgressLayout(Context context) {
        super(context);
        init(null);
    }

    public StepperProgressLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StepperProgressLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int dp16 = Utils.dpToPixel(getContext(), 16);

        mStepTextView = new AppCompatTextView(getContext());
        ViewCompat.setElevation(mStepTextView, Utils.dpToPixel(getContext(), 4));
        mStepTextView.setPadding(dp16, dp16, dp16, dp16);
        TextViewCompat.setTextAppearance(mStepTextView, android.support.v7.appcompat.R.style.TextAppearance_AppCompat);
        addView(mStepTextView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mContainerView = new FrameLayout(getContext());
        mContainerView.setId(R.id.nh_stepper_progress_container);
        LayoutParams containerLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        containerLp.addRule(ABOVE, R.id.nh_stepper_progress_bottom_bar);
        containerLp.addRule(BELOW, R.id.nh_stepper_progress_text);
        addView(mContainerView, containerLp);

        mBottomBar = new LinearLayout(getContext());
        ViewCompat.setElevation(mBottomBar, Utils.dpToPixel(getContext(), 4));
        mBottomBar.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams bottomBarLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomBarLp.addRule(ALIGN_PARENT_BOTTOM);
        addView(mBottomBar, bottomBarLp);

        mBackButton = new AppCompatButton(getContext());
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mBackButton, R.drawable.ic_arrow_left, 0, 0, 0);
        mBackButton.setText("Back");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBackButton.setBackground(Utils.getDrawableFromAttribute(getContext(),
                    R.attr.selectableItemBackgroundBorderless));
        } else {
            mBackButton.setBackgroundDrawable(Utils.getDrawableFromAttribute(getContext(),
                    R.attr.selectableItemBackgroundBorderless));
        }
        LinearLayout.LayoutParams backLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        MarginLayoutParamsCompat.setMarginStart(backLp, dp16 / 2);
        mBottomBar.addView(mBackButton, backLp);
        mBackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgress(mCurrentProgress - 1);
            }
        });

        FrameLayout progressFl = new FrameLayout(getContext());
        LinearLayout.LayoutParams progressLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        progressLp.setMargins(dp16 * 2, 0, dp16 * 2, 0);
        progressLp.gravity = Gravity.CENTER;
        mBottomBar.addView(progressFl, progressLp);

        mStepDotsLayout = new LinearLayout(getContext());
        mStepDotsLayout.setGravity(Gravity.CENTER);
        mStepDotsLayout.setOrientation(LinearLayout.HORIZONTAL);
        mStepDotsLayout.setVisibility(GONE);
        progressFl.addView(mStepDotsLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        mStepProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        mStepProgressBar.setIndeterminate(false);
        mStepProgressBar.setVisibility(GONE);
        progressFl.addView(mStepProgressBar, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        mNextButton = new AppCompatButton(getContext());
        mNextButton.setText("Next");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNextButton.setBackground(Utils.getDrawableFromAttribute(getContext(),
                    R.attr.selectableItemBackground));
        } else {
            mNextButton.setBackgroundDrawable(Utils.getDrawableFromAttribute(getContext(),
                    R.attr.selectableItemBackground));
        }
        LinearLayout.LayoutParams nextLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        MarginLayoutParamsCompat.setMarginEnd(nextLp, dp16 / 2);
        mBottomBar.addView(mNextButton, nextLp);
        mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentProgress < mMaxProgress) {
                    setProgress(mCurrentProgress + 1);
                }
            }
        });

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.StepperProgressLayout, 0, 0);
            try {
                mMaxProgress = a.getInt(R.styleable.StepperProgressLayout_stepperMaxProgress, 1);
                mCurrentProgress = a.getInt(R.styleable.StepperProgressLayout_stepperProgress, 1);

                String backText = a.getString(R.styleable.StepperProgressLayout_stepperBackButtonText);
                if (backText != null) {
                    mBackButton.setText(backText);
                }

                String nextText = a.getString(R.styleable.StepperProgressLayout_stepperNextButtonText);
                if (nextText != null) {
                    mNextText = nextText;
                }

                String finishText = a.getString(R.styleable.StepperProgressLayout_steeperFinishButtonText);
                if (finishText != null) {
                    mFinishText = finishText;
                }

                mProgressType = a.getInt(R.styleable.StepperProgressLayout_stepperProgressType, TYPE_TEXT);
                mAccent = a.getColor(R.styleable.StepperProgressLayout_stepperProgressAccent,
                        Utils.getColorFromAttribute(getContext(), R.attr.colorPrimary));
            } finally {
                a.recycle();
            }
        }

        setupView();
    }

    private void setupView() {
        switch (mProgressType) {
            case TYPE_TEXT:
                mStepTextView.setVisibility(VISIBLE);
                mStepDotsLayout.setVisibility(GONE);
                mStepProgressBar.setVisibility(GONE);
                break;
            case TYPE_DOTS:
                mStepTextView.setVisibility(GONE);
                mStepDotsLayout.setVisibility(VISIBLE);
                mStepProgressBar.setVisibility(GONE);

                mStepDotsLayout.removeAllViews();
                for (int i = 0; i < mMaxProgress; i++) {
                    View dot = createDotView();
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Utils.dpToPixel(getContext(), 8),
                            Utils.dpToPixel(getContext(), 8));
                    lp.setMargins(Utils.dpToPixel(getContext(), 4), 0, Utils.dpToPixel(getContext(), 4), 0);
                    mStepDotsLayout.addView(dot, lp);
                }
                break;
            case TYPE_BAR:
                mStepTextView.setVisibility(GONE);
                mStepDotsLayout.setVisibility(GONE);
                mStepProgressBar.setVisibility(VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mStepProgressBar.setProgressTintList(ColorStateList.valueOf(mAccent));
                } else {
                    mStepProgressBar.getProgressDrawable().setColorFilter(mAccent, PorterDuff.Mode.SRC_IN);
                }
                break;
        }

        updateView();
    }

    private View createDotView() {
        View v = new View(getContext());
        v.setBackgroundResource(R.drawable.stepper_circle_background);
        ViewCompat.setBackgroundTintList(v,
                ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.nh_black_38)));
        return v;
    }

    private void updateView() {
        if (mProgressType == TYPE_TEXT) {
            mStepTextView.setText(String.format("Step %d of %d", mCurrentProgress, mMaxProgress));
        } else if (mProgressType == TYPE_DOTS) {
            for (int i = 0; i < mStepDotsLayout.getChildCount(); i++) {
                View dot = mStepDotsLayout.getChildAt(i);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) dot.getLayoutParams();
                int dp;
                Drawable background = ContextCompat.getDrawable(getContext(), R.drawable.stepper_circle_background);
                if (i == mCurrentProgress - 1) {
                    // Dot enlarged at 12dp
                    dp = Utils.dpToPixel(getContext(), 12);
                    // And tinted background
                    background = Utils.getTintedDrawable(background, mAccent);
                } else {
                    // Regular size of 8dp
                    dp = Utils.dpToPixel(getContext(), 8);
                    // Remove tinted background
                    background = Utils.getTintedDrawable(background, ContextCompat.getColor(getContext(),
                            R.color.nh_black_38));
                }
                lp.width = dp;
                lp.height = dp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    dot.setBackground(background);
                } else {
                    dot.setBackgroundDrawable(background);
                }
                dot.setLayoutParams(lp);
            }
        } else if (mProgressType == TYPE_BAR) {
            mStepProgressBar.setMax(mMaxProgress);
            mStepProgressBar.setProgress(mCurrentProgress);
        }

        if (mCurrentProgress <= 1) {
            // Hide the back button when on step 1
            mBackButton.setVisibility(INVISIBLE);
        } else {
            mBackButton.setVisibility(VISIBLE);
        }

        if (mCurrentProgress == mMaxProgress) {
            // Change the next button text to the finish text
            mNextButton.setText(mFinishText);
            mNextButton.setCompoundDrawables(null, null, null, null);
        } else {
            mNextButton.setText(mNextText);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mNextButton, 0, 0,
                    R.drawable.ic_arrow_right, 0);
        }
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
        setupView();
    }

    public int getProgress() {
        return mCurrentProgress;
    }

    public void setProgress(int currentProgress) {
        mCurrentProgress = currentProgress;
        updateView();
    }

    public CharSequence getBackButtonText() {
        return mBackButton.getText();
    }

    public void setBackButtonText(CharSequence text) {
        mBackButton.setText(text);
    }

    public CharSequence getNextButtonText() {
        return mNextButton.getText();
    }

    public void setNextButtonText(CharSequence text) {
        mNextButton.setText(text);
    }

    public CharSequence getFinishButtonText() {
        return mFinishText;
    }

    public void setFinishButtonText(String text) {
        mFinishText = text;
    }

    @IntDef({TYPE_TEXT, TYPE_DOTS, TYPE_BAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ProgressType {
    }
}
