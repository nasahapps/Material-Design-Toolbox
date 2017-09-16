package com.nasahapps.mdt.components

import android.content.Context
import android.graphics.Color
import android.support.annotation.IntDef
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v4.view.MarginLayoutParamsCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.ViewUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout

import com.nasahapps.mdt.R
import com.nasahapps.mdt.Utils

/**
 * Created by Hakeem on 4/12/16.
 *
 *
 * A [android.widget.TextView] that may appear on hover and focus when the user hovers over an
 * element with a cursor, focuses on an element using a keyboard (usually through the TAB key), or
 * upon touch (without releasing) in a touch UI. It may contain textual indentification for the
 * element in question. It may also contain brief helper text regarding the function of the element.
 * The label itself cannot receive input focus.
 *
 *
 * [http://www.google.com/design/spec/components/tooltips.html](http://www.google.com/design/spec/components/tooltips.html)
 */
class Tooltip @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                        defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {
    /**
     * Return the duration.
     *
     * @see .setDuration
     */
    /**
     * Set how long to show the view for.
     *
     * @see .LENGTH_SHORT
     *
     * @see .LENGTH_LONG
     */
    var duration = LENGTH_SHORT
        @Duration get
        set(@Duration value) {
            field = value
        }
    internal var mAnchor: View? = null

    init {
        val dp16 = Utils.dpToPixel(context, 16)
        val dp6 = Utils.dpToPixel(context, 6)

        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        setPadding(dp16, dp6, dp16, dp6)
        setSingleLine()
        setBackgroundColor(ContextCompat.getColor(context, R.color.mdt_grey_700))
        setTextColor(Color.WHITE)
        gravity = Gravity.CENTER

        if (isInEditMode) {
            text = "Tooltip text"
        }
    }

    fun show() {
        if (parent != null) {
            (parent as ViewGroup).removeView(this)
        }

        val rootView = mAnchor?.rootView as? ViewGroup
        rootView?.addView(this, FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))

        alpha = 0f

        post {
            val anchorCoords = IntArray(2)
            mAnchor?.getLocationOnScreen(anchorCoords)

            val lp = layoutParams as FrameLayout.LayoutParams

            var y = anchorCoords[1] + Utils.dpToPixel(context, 24) + Utils.dpToPixel(context, 32)
            // To prevent being cut off on the bottom of the screen
            if (y + measuredHeight >= getRootView().height) {
                y = anchorCoords[1] - Utils.dpToPixel(context, 32)
            }
            lp.setMargins(0, y, 0, 0)

            var anchorMidX = anchorCoords[0] + (mAnchor?.width ?: 0) / 2
            if (ViewUtils.isLayoutRtl(mAnchor)) {
                anchorMidX = Utils.getRtlX(context, anchorMidX)
            }
            var x = anchorMidX - measuredWidth / 2
            // To prevent being cut off on the left side of the screen
            if (x < 0) {
                x = Utils.dpToPixel(context, 8)
            }
            // To prevent being cut off on the right side of the screen
            val rootViewWidth = getRootView().width
            if (x + measuredWidth >= rootViewWidth) {
                x = rootViewWidth - measuredWidth - Utils.dpToPixel(context, 8)
            }
            MarginLayoutParamsCompat.setMarginStart(lp, x)
            layoutParams = lp

            // Animate Tooltip in
            alpha = 0.9f
            val fadeIn = AlphaAnimation(0f, 0.9f)
            fadeIn.duration = 250
            fadeIn.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {

                }

                override fun onAnimationEnd(animation: Animation) {
                    // Then animate it back out
                    val duration = (if (duration == LENGTH_LONG) 3500 else 2000).toLong()
                    val fadeOut = AlphaAnimation(0.9f, 0f)
                    fadeOut.startOffset = duration
                    fadeOut.duration = 250
                    fadeOut.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {

                        }

                        override fun onAnimationEnd(animation: Animation) {
                            if (parent != null) {
                                (parent as ViewGroup).removeView(this@Tooltip)
                            }
                        }

                        override fun onAnimationRepeat(animation: Animation) {

                        }
                    })
                    startAnimation(fadeOut)
                }

                override fun onAnimationRepeat(animation: Animation) {

                }
            })
            startAnimation(fadeIn)
        }
    }

    @IntDef(LENGTH_SHORT.toLong(), LENGTH_LONG.toLong())
    @Retention(AnnotationRetention.SOURCE)
    annotation class Duration

    companion object {
        /**
         * Show the view or text notification for a short period of time (2 seconds). This time could be
         * user-definable. This is the default.
         *
         * @see .setDuration
         */
        const val LENGTH_SHORT = 0
        /**
         * Show the view or text notification for a long period of time (3.5 seconds). This time could be
         * user-definable.
         *
         * @see .setDuration
         */
        const val LENGTH_LONG = 1

        @JvmStatic
        fun makeTooltip(c: Context, text: CharSequence, @Duration length: Int,
                        anchor: View): Tooltip {
            val tooltip = Tooltip(c)
            tooltip.text = text

            if (length == LENGTH_LONG) {
                tooltip.duration = length
            }
            tooltip.mAnchor = anchor

            return tooltip
        }

        @JvmStatic
        fun makeTooltip(c: Context, @StringRes stringRes: Int, @Duration length: Int,
                        anchor: View): Tooltip {
            val tooltip = Tooltip(c)
            tooltip.setText(stringRes)

            if (length == LENGTH_LONG) {
                tooltip.duration = length
            }
            tooltip.mAnchor = anchor

            return tooltip
        }
    }
}
