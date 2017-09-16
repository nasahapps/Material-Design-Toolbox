package com.nasahapps.mdt.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.support.annotation.IntDef
import android.support.v4.content.ContextCompat
import android.support.v4.view.MarginLayoutParamsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.nasahapps.mdt.R
import com.nasahapps.mdt.Utils

/**
 * Created by Hakeem on 4/14/16.
 */
class Stepper @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                        defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    protected val mStepperCircleText: TextView
    protected val mStepperCircleCheck: ImageView
    protected val mStepperTitle: TextView
    protected val mStepperSubtitle: TextView

    protected var mAccent: Int = 0
    protected var mState: Int = 0

    init {
        setBackgroundColor(Color.WHITE)
        gravity = Gravity.CENTER

        val fl = FrameLayout(context)
        addView(fl, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        // Create mStepperCircleText and mStepperCircleCheck and add it to this FrameLayout
        mStepperCircleText = AppCompatTextView(context)
        mStepperCircleText.setBackgroundResource(R.drawable.mdt_stepper_circle_background)
        mStepperCircleText.gravity = Gravity.CENTER
        mStepperCircleText.setTextColor(Color.WHITE)
        mStepperCircleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        fl.addView(mStepperCircleText, FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))

        mStepperCircleCheck = AppCompatImageView(context)
        fl.addView(mStepperCircleCheck, FrameLayout.LayoutParams(Utils.dpToPixel(context, 24),
                Utils.dpToPixel(context, 24)))

        val ll = LinearLayout(context)
        ll.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        ll.gravity = Gravity.CENTER_VERTICAL
        ll.orientation = LinearLayout.VERTICAL
        addView(ll)
        // Create mStepperTitle and mStepperSubtitle and add it to this LinearLayout
        mStepperTitle = AppCompatTextView(context)
        mStepperTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        ll.addView(mStepperTitle, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))

        mStepperSubtitle = AppCompatTextView(context)
        mStepperSubtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        mStepperSubtitle.setTextColor(ContextCompat.getColor(context, R.color.mdt_black_54))
        ll.addView(mStepperSubtitle, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))

        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.Stepper, 0, 0)
            try {
                val title = a.getString(R.styleable.Stepper_stepperTitle)
                if (title != null) {
                    mStepperTitle.text = title
                }

                val subtitle = a.getString(R.styleable.Stepper_stepperSubtitle)
                if (subtitle != null) {
                    mStepperSubtitle.text = subtitle
                }

                mState = a.getInt(R.styleable.Stepper_stepperState, INACTIVE)
                mAccent = a.getColor(R.styleable.Stepper_stepperAccent,
                        Utils.getColorFromAttribute(context, R.attr.colorPrimary))
            } finally {
                a.recycle()
            }
        }

        setupView()
    }

    private fun setupView() {
        if (childCount != 0) {
            if (orientation == LinearLayout.HORIZONTAL) {
                setPadding(Utils.dpToPixel(context, 8), Utils.dpToPixel(context, 24),
                        Utils.dpToPixel(context, 8), Utils.dpToPixel(context, 24))

                val ll = getChildAt(1) as LinearLayout
                ll.gravity = Gravity.CENTER_VERTICAL
                val lp = ll.layoutParams as LinearLayout.LayoutParams
                MarginLayoutParamsCompat.setMarginStart(lp, Utils.dpToPixel(context, 8))
                ll.layoutParams = lp
            } else {
                val dp24 = Utils.dpToPixel(context, 24)
                setPadding(dp24, dp24, dp24, dp24)

                val ll = getChildAt(1) as LinearLayout
                ll.gravity = Gravity.CENTER_HORIZONTAL
                val lp = ll.layoutParams as LinearLayout.LayoutParams
                lp.setMargins(lp.leftMargin, Utils.dpToPixel(context, 16), lp.rightMargin, lp.bottomMargin)
                ll.layoutParams = lp
            }

            if (mState == ACTIVE) {
                if (mStepperCircleText.background is GradientDrawable) {
                    (mStepperCircleText.background as GradientDrawable).setColor(mAccent)
                } else {
                    ViewCompat.setBackgroundTintList(mStepperCircleText, ColorStateList.valueOf(mAccent))
                }
                mStepperTitle.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                mStepperTitle.setTextColor(ContextCompat.getColor(context, R.color.mdt_black_87))
            } else {
                if (mStepperCircleText.background is GradientDrawable) {
                    (mStepperCircleText.background as GradientDrawable).setColor(ContextCompat.getColor(context,
                            R.color.mdt_black_38))
                } else {
                    ViewCompat.setBackgroundTintList(mStepperCircleText, ColorStateList.valueOf(ContextCompat.getColor(context,
                            R.color.mdt_black_38)))
                }
                mStepperTitle.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                mStepperTitle.setTextColor(ContextCompat.getColor(context, R.color.mdt_black_38))
            }
            mStepperSubtitle.setTextColor(ContextCompat.getColor(context, R.color.mdt_black_54))

            if (mState == COMPLETED || mState == ERROR) {
                mStepperCircleText.visibility = View.GONE
                mStepperCircleCheck.visibility = View.VISIBLE
                if (mState == ERROR) {
                    mStepperCircleCheck.setImageDrawable(Utils.getTintedDrawable(ContextCompat.getDrawable(context,
                            R.drawable.ic_mdt_report_problem), ContextCompat.getColor(context, R.color.mdt_red_500)))
                    mStepperTitle.setTextColor(ContextCompat.getColor(context, R.color.mdt_red_500))
                    mStepperSubtitle.setTextColor(ContextCompat.getColor(context, R.color.mdt_red_500))
                } else {
                    mStepperCircleCheck.setImageDrawable(Utils.getTintedDrawable(ContextCompat.getDrawable(context,
                            R.drawable.ic_mdt_check_circle), mAccent))
                    mStepperTitle.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                    mStepperTitle.setTextColor(ContextCompat.getColor(context, R.color.mdt_black_87))
                }
            } else {
                mStepperCircleCheck.visibility = View.GONE
                mStepperCircleText.visibility = View.VISIBLE
            }

            if (TextUtils.isEmpty(mStepperSubtitle.text)) {
                mStepperSubtitle.visibility = View.GONE
            } else {
                mStepperSubtitle.visibility = View.VISIBLE
            }
        }
    }

    private // We have a separate index variable to count the number of Stepper siblings instead of
            // overall siblings
    val stepperPositionInLayout: Int
        get() {
            val parent = parent as? ViewGroup
            var index = 0
            if (parent != null) {
                for (i in 0 until parent.childCount) {
                    if (parent.getChildAt(i) is Stepper) {
                        if (parent.getChildAt(i) === this) {
                            return index
                        } else {
                            index++
                        }
                    }
                }
            }

            return -1
        }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mStepperCircleText.text = (stepperPositionInLayout + 1).toString()
    }

    override fun setOrientation(orientation: Int) {
        super.setOrientation(orientation)
        setupView()
    }

    var stepperTitle: CharSequence
        get() = mStepperTitle.text
        set(text) {
            mStepperTitle.text = text
        }

    var stepperSubtitle: CharSequence
        get() = mStepperSubtitle.text
        set(text) {
            mStepperSubtitle.text = text
        }

    var state: Int
        @State
        get() = mState
        set(@State state) {
            mState = state
            setupView()
        }

    @IntDef(ACTIVE.toLong(), INACTIVE.toLong(), COMPLETED.toLong(), ERROR.toLong())
    @Retention(AnnotationRetention.SOURCE)
    annotation class State

    companion object {
        const val ACTIVE = 1
        const val INACTIVE = 2
        const val COMPLETED = 3
        const val ERROR = 4
    }
}
