package com.nasahapps.mdt.components

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.MarginLayoutParamsCompat
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.nasahapps.mdt.R
import com.nasahapps.mdt.Utils

/**
 * Created by hhasan on 4/15/16.
 *
 *
 * A [RelativeLayout] that conveys progress through numbered steps. It can also be used for
 * navigation.
 *
 *
 * [http://www.google.com/design/spec/components/steppers.html](http://www.google.com/design/spec/components/steppers.html)
 */
class HorizontalStepperLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                        defStyleAttr: Int = 0) : HorizontalScrollView(context, attrs, defStyleAttr) {

    protected val mConnectorLine: View
    protected val mStepperLayout: LinearLayout
    protected var mStepperMargin: Int = 0

    init {
        setBackgroundColor(Color.WHITE)
        ViewCompat.setElevation(this, Utils.dpToPixel(context, 4).toFloat())
        setPadding(Utils.dpToPixel(context, 16), 0, Utils.dpToPixel(context, 16), 0)
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        clipToPadding = false

        val rl = RelativeLayout(context)
        addView(rl, FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        mConnectorLine = View(context)
        val lp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                Utils.dpToPixel(context, 1))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            lp.addRule(RelativeLayout.ALIGN_END, R.id.mdt_stepper_layout)
            lp.addRule(RelativeLayout.ALIGN_START, R.id.mdt_stepper_layout)
        } else {
            lp.addRule(RelativeLayout.ALIGN_RIGHT, R.id.mdt_stepper_layout)
            lp.addRule(RelativeLayout.ALIGN_LEFT, R.id.mdt_stepper_layout)
        }
        lp.addRule(RelativeLayout.CENTER_VERTICAL)
        mConnectorLine.layoutParams = lp
        mConnectorLine.setBackgroundColor(ContextCompat.getColor(context, R.color.mdt_grey_400))
        rl.addView(mConnectorLine, 0)

        mStepperLayout = LinearLayout(context)
        mStepperLayout.id = R.id.mdt_stepper_layout
        mStepperLayout.orientation = LinearLayout.HORIZONTAL
        rl.addView(mStepperLayout, 1,
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.StepperLayout, 0, 0)
            try {
                mStepperMargin = a.getDimensionPixelSize(R.styleable.StepperLayout_stepperLayoutMargin,
                        Utils.dpToPixel(context, 56))
            } finally {
                a.recycle()
            }
        }
    }

    fun getStepper(index: Int): Stepper {
        return mStepperLayout.getChildAt(index) as Stepper
    }

    val stepperCount: Int
        get() {
            var count = 0
            for (i in 0 until mStepperLayout.childCount) {
                if (mStepperLayout.getChildAt(i) is Stepper) {
                    count++
                }
            }

            return count
        }

    fun setStepperState(index: Int, @Stepper.State state: Int) {
        val stepper = getStepper(index)
        stepper.state = state
        if (state == Stepper.ACTIVE) {
            // Also scroll to that stepper if just made active
            smoothScrollTo(stepper.x.toInt(), stepper.y.toInt())
        }
    }

    override fun addView(child: View) {
        if (child is Stepper) {
            mStepperLayout.addView(child)
        } else {
            super.addView(child)
        }
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child is Stepper) {
            mStepperLayout.addView(child, index, params)

            // Doing this AFTER calling addView() works. Calling it before addView() did not work.
            val lp = child.getLayoutParams() as LinearLayout.LayoutParams
            if (child.orientation == LinearLayout.VERTICAL) {
                lp.gravity = Gravity.TOP
            } else {
                lp.gravity = Gravity.CENTER_VERTICAL
            }
            if (mStepperLayout.childCount > 1) {
                // Add start margin
                MarginLayoutParamsCompat.setMarginStart(lp, mStepperMargin)
            }
            child.setLayoutParams(lp)
        } else {
            super.addView(child, index, params)
        }
    }

    override fun addView(child: View, width: Int, height: Int) {
        if (child is Stepper) {
            mStepperLayout.addView(child, width, height)
        } else {
            super.addView(child, width, height)
        }
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        if (child is Stepper) {
            mStepperLayout.addView(child, params)

            // Doing this AFTER calling addView() works. Calling it before addView() did not work.
            val lp = child.getLayoutParams() as LinearLayout.LayoutParams
            if (child.orientation == LinearLayout.VERTICAL) {
                lp.gravity = Gravity.TOP
            } else {
                lp.gravity = Gravity.CENTER_VERTICAL
            }
            if (mStepperLayout.childCount > 1) {
                // Add start margin
                MarginLayoutParamsCompat.setMarginStart(lp, mStepperMargin)
            }
            child.setLayoutParams(lp)
        } else {
            super.addView(child, params)
        }
    }

    override fun addView(child: View, index: Int) {
        if (child is Stepper) {
            mStepperLayout.addView(child, index)
        } else {
            super.addView(child, index)
        }
    }
}
