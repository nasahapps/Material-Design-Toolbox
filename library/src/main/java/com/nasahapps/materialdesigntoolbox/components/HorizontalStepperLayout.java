package com.nasahapps.materialdesigntoolbox.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
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

/**
 * Created by hhasan on 4/15/16.
 * <p/>
 * A {@link RelativeLayout} that conveys progress through numbered steps. It can also be used for
 * navigation.
 * <p/>
 * http://www.google.com/design/spec/components/steppers.html
 */
public class HorizontalStepperLayout extends HorizontalScrollView {

    View mConnectorLine;
    LinearLayout mStepperLayout;
    int mStepperMargin;

    public HorizontalStepperLayout(Context context) {
        super(context);
        init(null);
    }

    public HorizontalStepperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HorizontalStepperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                Utils.dpToPixel(getContext(), 1));
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
        mStepperLayout.setOrientation(LinearLayout.HORIZONTAL);
        rl.addView(mStepperLayout, 1,
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.StepperLayout, 0, 0);
            try {
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
        int count = 0;
        for (int i = 0; i < mStepperLayout.getChildCount(); i++) {
            if (mStepperLayout.getChildAt(i) instanceof Stepper) {
                count++;
            }
        }

        return count;
    }

    public void setStepperActive(int index, boolean active) {
        Stepper stepper = getStepper(index);
        stepper.setActive(active);
        if (active) {
            // Also scroll to that stepper
            smoothScrollTo((int) stepper.getX(), (int) stepper.getY());
        }
    }

    public void setStepperCompleted(int index, boolean completed) {
        getStepper(index).setCompleted(completed);
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
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
            if (((Stepper) child).getOrientation() == LinearLayout.VERTICAL) {
                lp.gravity = Gravity.TOP;
            } else {
                lp.gravity = Gravity.CENTER_VERTICAL;
            }
            if (mStepperLayout.getChildCount() > 1) {
                // Add start margin
                MarginLayoutParamsCompat.setMarginStart(lp, mStepperMargin);
            }
            child.setLayoutParams(lp);
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
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
            if (((Stepper) child).getOrientation() == LinearLayout.VERTICAL) {
                lp.gravity = Gravity.TOP;
            } else {
                lp.gravity = Gravity.CENTER_VERTICAL;
            }
            if (mStepperLayout.getChildCount() > 1) {
                // Add start margin
                MarginLayoutParamsCompat.setMarginStart(lp, mStepperMargin);
            }
            child.setLayoutParams(lp);
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
}
