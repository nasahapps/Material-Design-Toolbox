package com.nasahapps.materialdesigntoolbox.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nasahapps.materialdesigntoolbox.R;
import com.nasahapps.materialdesigntoolbox.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by hhasan on 4/15/16.
 */
public class StepperLayout extends HorizontalScrollView {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    View mConnectorLine;
    LinearLayout mStepperLayout;
    int mOrientation, mStepperMargin;

    public StepperLayout(Context context) {
        super(context);
        init(null);
    }

    public StepperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StepperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setBackgroundColor(Color.WHITE);
        ViewCompat.setElevation(this, Utils.dpToPixel(getContext(), 4));
        setPadding(Utils.dpToPixel(getContext(), 16), 0, Utils.dpToPixel(getContext(), 16), 0);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setClipToPadding(false);

        RelativeLayout rl = new RelativeLayout(getContext());
        addView(rl, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mConnectorLine = new View(getContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.dpToPixel(getContext(), 1));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            lp.addRule(RelativeLayout.ALIGN_END, R.id.nh_stepper_layout);
            lp.addRule(RelativeLayout.ALIGN_START, R.id.nh_stepper_layout);
        } else {
            lp.addRule(RelativeLayout.ALIGN_RIGHT, R.id.nh_stepper_layout);
            lp.addRule(RelativeLayout.ALIGN_LEFT, R.id.nh_stepper_layout);
        }
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        mConnectorLine.setLayoutParams(lp);
        mConnectorLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.nh_grey_400));
        rl.addView(mConnectorLine, 0);

        mStepperLayout = new LinearLayout(getContext());
        mStepperLayout.setId(R.id.nh_stepper_layout);
        mStepperLayout.setGravity(Gravity.CENTER);
        mStepperLayout.setOrientation(LinearLayout.HORIZONTAL);
        rl.addView(mStepperLayout, 1,
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.StepperLayout, 0, 0);
            try {
                mOrientation = a.getInt(R.styleable.StepperLayout_stepperLayoutOrientation, HORIZONTAL);
                mStepperMargin = a.getDimensionPixelSize(R.styleable.StepperLayout_stepperLayoutMargin,
                        Utils.dpToPixel(getContext(), 56));
            } finally {
                a.recycle();
            }
        }
    }

    public Stepper getStepper(int index) {
        return (Stepper) mStepperLayout.getChildAt(index);
    }

    public int getStepperCount() {
        return mStepperLayout.getChildCount();
    }

    @Override
    public void addView(View child) {
        if (child instanceof Stepper) {
            mStepperLayout.addView(child);
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof Stepper) {
            mStepperLayout.addView(child, index, params);

            // Doing this AFTER calling addView() works. Calling it before addView() did not work.
            if (params instanceof MarginLayoutParams && mStepperLayout.getChildCount() > 1) {
                // Add start margin
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                MarginLayoutParamsCompat.setMarginStart(lp, mStepperMargin);
                child.setLayoutParams(lp);
            }

        } else {
            super.addView(child, index, params);
        }
    }

    @Override
    public void addView(View child, int width, int height) {
        if (child instanceof Stepper) {
            mStepperLayout.addView(child, width, height);
        } else {
            super.addView(child, width, height);
        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (child instanceof Stepper) {
            mStepperLayout.addView(child, params);

            // Doing this AFTER calling addView() works. Calling it before addView() did not work.
            if (params instanceof MarginLayoutParams && mStepperLayout.getChildCount() > 1) {
                // Add start margin
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                MarginLayoutParamsCompat.setMarginStart(lp, mStepperMargin);
                child.setLayoutParams(lp);
            }
        } else {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(View child, int index) {
        if (child instanceof Stepper) {
            mStepperLayout.addView(child, index);
        } else {
            super.addView(child, index);
        }
    }

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }
}
