package com.nasahapps.mdt.components

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.IntDef
import android.support.v4.content.ContextCompat
import android.support.v4.view.MarginLayoutParamsCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.nasahapps.mdt.R
import com.nasahapps.mdt.Utils
import java.util.ArrayList

/**
 * Created by Hakeem on 4/16/16.
 *
 *
 * A [RelativeLayout] that conveys progress through numbered steps. It can also be used for
 * navigation.
 *
 *
 * [http://www.google.com/design/spec/components/steppers.html](http://www.google.com/design/spec/components/steppers.html)
 */
class StepperProgressLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                      defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    protected val mStepTextView: TextView
    protected val mContainerView: FrameLayout
    protected val mBottomBar: LinearLayout
    protected val mStepDotsLayout: LinearLayout
    protected val mBackButton: Button
    protected val mNextButton: Button
    protected val mStepProgressBar: ProgressBar

    protected var mMaxProgress: Int = 0
    protected var mCurrentProgress = 1
    protected var mProgressType: Int = 0
    protected var mAccent: Int = 0
    protected var mFinishText = "Finish"
    protected var mNextText = "Next"

    protected val mOnStepperProgressListeners: MutableList<OnStepperProgressListener> = ArrayList()

    init {
        val dp16 = Utils.dpToPixel(context, 16)

        mStepTextView = AppCompatTextView(context)
        ViewCompat.setElevation(mStepTextView, Utils.dpToPixel(context, 4).toFloat())
        mStepTextView.setPadding(dp16, dp16, dp16, dp16)
        mStepTextView.id = R.id.mdt_stepper_progress_text
        mStepTextView.setBackgroundColor(Color.WHITE)
        TextViewCompat.setTextAppearance(mStepTextView, android.support.v7.appcompat.R.style.TextAppearance_AppCompat)
        addView(mStepTextView, RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        mContainerView = FrameLayout(context)
        mContainerView.id = R.id.mdt_stepper_progress_container
        val containerLp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        containerLp.addRule(RelativeLayout.ABOVE, R.id.mdt_stepper_progress_bottom_bar)
        containerLp.addRule(RelativeLayout.BELOW, R.id.mdt_stepper_progress_text)
        addView(mContainerView, containerLp)

        mBottomBar = LinearLayout(context)
        ViewCompat.setElevation(mBottomBar, Utils.dpToPixel(context, 4).toFloat())
        mBottomBar.orientation = LinearLayout.HORIZONTAL
        mBottomBar.id = R.id.mdt_stepper_progress_bottom_bar
        mBottomBar.setBackgroundColor(Color.WHITE)
        val bottomBarLp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        bottomBarLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        addView(mBottomBar, bottomBarLp)

        mBackButton = AppCompatButton(context)
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mBackButton, R.drawable.ic_mdt_arrow_left, 0, 0, 0)
        mBackButton.text = "Back"
        mBackButton.background = Utils.getDrawableFromAttribute(context,
                R.attr.selectableItemBackground)
        val backLp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        MarginLayoutParamsCompat.setMarginStart(backLp, dp16 / 2)
        mBottomBar.addView(mBackButton, backLp)
        mBackButton.setOnClickListener { progress = mCurrentProgress - 1 }

        val progressFl = FrameLayout(context)
        val progressLp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        progressLp.setMargins(dp16 * 2, 0, dp16 * 2, 0)
        progressLp.gravity = Gravity.CENTER
        mBottomBar.addView(progressFl, progressLp)

        mStepDotsLayout = LinearLayout(context)
        mStepDotsLayout.gravity = Gravity.CENTER
        mStepDotsLayout.orientation = LinearLayout.HORIZONTAL
        mStepDotsLayout.visibility = View.GONE
        progressFl.addView(mStepDotsLayout, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))

        mStepProgressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        mStepProgressBar.isIndeterminate = false
        mStepProgressBar.visibility = View.GONE
        progressFl.addView(mStepProgressBar, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))

        mNextButton = AppCompatButton(context)
        mNextButton.text = "Next"
        mNextButton.background = Utils.getDrawableFromAttribute(context,
                R.attr.selectableItemBackground)
        val nextLp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        MarginLayoutParamsCompat.setMarginEnd(nextLp, dp16 / 2)
        mBottomBar.addView(mNextButton, nextLp)
        mNextButton.setOnClickListener {
            if (mCurrentProgress < mMaxProgress) {
                progress = mCurrentProgress + 1
            } else {
                for (listener in mOnStepperProgressListeners) {
                    listener.onStepsFinished()
                }
            }
        }

        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.StepperProgressLayout, 0, 0)
            try {
                mMaxProgress = a.getInt(R.styleable.StepperProgressLayout_stepperMaxProgress, 1)
                mCurrentProgress = a.getInt(R.styleable.StepperProgressLayout_stepperProgress, 1)

                val backText = a.getString(R.styleable.StepperProgressLayout_stepperBackButtonText)
                if (backText != null) {
                    mBackButton.text = backText
                }

                val nextText = a.getString(R.styleable.StepperProgressLayout_stepperNextButtonText)
                if (nextText != null) {
                    mNextText = nextText
                }

                val finishText = a.getString(R.styleable.StepperProgressLayout_steeperFinishButtonText)
                if (finishText != null) {
                    mFinishText = finishText
                }

                mProgressType = a.getInt(R.styleable.StepperProgressLayout_stepperProgressType, TYPE_TEXT)
                mAccent = a.getColor(R.styleable.StepperProgressLayout_stepperProgressAccent,
                        Utils.getColorFromAttribute(context, R.attr.colorPrimary))
            } finally {
                a.recycle()
            }
        }

        setupView()
    }

    private fun setupView() {
        when (mProgressType) {
            TYPE_TEXT -> {
                mStepTextView.visibility = View.VISIBLE
                mStepDotsLayout.visibility = View.GONE
                mStepProgressBar.visibility = View.GONE
            }
            TYPE_DOTS -> {
                mStepTextView.visibility = View.GONE
                mStepDotsLayout.visibility = View.VISIBLE
                mStepProgressBar.visibility = View.GONE

                mStepDotsLayout.removeAllViews()
                for (i in 0 until mMaxProgress) {
                    val dot = createDotView()
                    val lp = LinearLayout.LayoutParams(Utils.dpToPixel(context, 8),
                            Utils.dpToPixel(context, 8))
                    lp.setMargins(Utils.dpToPixel(context, 4), 0, Utils.dpToPixel(context, 4), 0)
                    mStepDotsLayout.addView(dot, lp)
                }
            }
            TYPE_BAR -> {
                mStepTextView.visibility = View.GONE
                mStepDotsLayout.visibility = View.GONE
                mStepProgressBar.visibility = View.VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mStepProgressBar.progressTintList = ColorStateList.valueOf(mAccent)
                } else {
                    mStepProgressBar.progressDrawable.setColorFilter(mAccent, PorterDuff.Mode.SRC_IN)
                }
            }
        }

        updateView()
    }

    private fun createDotView(): View {
        val v = View(context)
        v.setBackgroundResource(R.drawable.mdt_stepper_circle_background)
        ViewCompat.setBackgroundTintList(v,
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.mdt_black_38)))
        return v
    }

    private fun updateView() {
        if (mProgressType == TYPE_TEXT) {
            mStepTextView.text = String.format("Step %d of %d", mCurrentProgress, mMaxProgress)
        } else if (mProgressType == TYPE_DOTS) {
            val dp8 = Utils.dpToPixel(context, 8)
            val dp12 = Utils.dpToPixel(context, 12)
            for (i in 0 until mStepDotsLayout.childCount) {
                val dot = mStepDotsLayout.getChildAt(i)
                // flag for seeing which dots are actually changing and animate if needed
                var selectionChanged = false
                val finalSize: Int
                var background = ContextCompat.getDrawable(context, R.drawable.mdt_stepper_circle_background)
                if (i == mCurrentProgress - 1) {
                    // Dot enlarged at 12dp
                    finalSize = dp12
                    // And tinted background
                    if (background is GradientDrawable) {
                        background.setColor(mAccent)
                    } else {
                        background = Utils.getTintedDrawable(background, mAccent)
                    }
                    // Set a tag saying that this dot is the currently selected one
                    if (!(dot.tag as Boolean)) {
                        selectionChanged = true
                    }
                    dot.tag = true
                } else {
                    // Regular size of 8dp
                    finalSize = dp8
                    // Remove tinted background
                    if (background is GradientDrawable) {
                        background.setColor(ContextCompat.getColor(context,
                                R.color.mdt_black_38))
                    } else {
                        background = Utils.getTintedDrawable(background, ContextCompat.getColor(context,
                                R.color.mdt_black_38))
                    }
                    // Unset the dot's tag for it's no longer selected
                    if (dot.tag as Boolean) {
                        selectionChanged = true
                    }
                    dot.tag = false
                }

                if (selectionChanged) {
                    // Animate the dot size, if needed
                    val originalSize = if (dot.tag as Boolean) dp8 else dp12
                    val sizeAnim = ValueAnimator.ofInt(originalSize, finalSize)
                    sizeAnim.addUpdateListener { animation ->
                        val lp = dot.layoutParams as LinearLayout.LayoutParams
                        lp.width = animation.animatedValue as Int
                        lp.height = animation.animatedValue as Int
                        dot.layoutParams = lp
                    }
                    sizeAnim.start()
                }

                dot.background = background
            }
        } else if (mProgressType == TYPE_BAR) {
            mStepProgressBar.max = mMaxProgress * 100
            // Animate the progress
            val oldProgress = mStepProgressBar.progress
            val progressAnim = ValueAnimator.ofInt(oldProgress, mCurrentProgress * 100)
            progressAnim.addUpdateListener { animation -> mStepProgressBar.progress = animation.animatedValue as Int }
            progressAnim.start()
        }

        if (mCurrentProgress <= 1) {
            // Hide the back button when on step 1
            mBackButton.visibility = View.INVISIBLE
        } else {
            mBackButton.visibility = View.VISIBLE
        }

        if (mCurrentProgress == mMaxProgress) {
            // Change the next button text to the finish text
            mNextButton.text = mFinishText
            mNextButton.setCompoundDrawables(null, null, null, null)
        } else {
            mNextButton.text = mNextText
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mNextButton, 0, 0,
                    R.drawable.ic_mdt_arrow_right, 0)
        }
    }

    var maxProgress: Int
        get() = mMaxProgress
        set(maxProgress) {
            mMaxProgress = maxProgress
            setupView()
        }

    var progress: Int
        get() = mCurrentProgress
        set(currentProgress) {
            for (listener in mOnStepperProgressListeners) {
                listener.onStepSelected(currentProgress - 1)
                listener.onStepDeselected(mCurrentProgress - 1)
            }

            mCurrentProgress = currentProgress
            updateView()
        }

    var progressType: Int
        @ProgressType
        get() = mProgressType
        set(@ProgressType progressType) {
            mProgressType = progressType
            setupView()
        }

    var backButtonText: CharSequence
        get() = mBackButton.text
        set(text) {
            mBackButton.text = text
        }

    var nextButtonText: CharSequence
        get() = mNextButton.text
        set(text) {
            mNextButton.text = text
        }

    val finishButtonText: CharSequence
        get() = mFinishText

    fun setFinishButtonText(text: String) {
        mFinishText = text
    }

    fun addOnStepperProgressListener(listener: OnStepperProgressListener) {
        if (!mOnStepperProgressListeners.contains(listener)) {
            mOnStepperProgressListeners.add(listener)
        }
    }

    fun removeOnStepperProgressListener(listener: OnStepperProgressListener) {
        mOnStepperProgressListeners.remove(listener)
    }

    override fun addView(child: View) {
        if (child !== mStepTextView && child !== mContainerView
                && child !== mBottomBar) {
            // Add it to our container view
            mContainerView.addView(child)
        } else {
            super.addView(child)
        }
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child !== mStepTextView && child !== mContainerView
                && child !== mBottomBar) {
            // Add it to our container view
            mContainerView.addView(child, index, params)
        } else {
            super.addView(child, index, params)
        }
    }

    override fun addView(child: View, width: Int, height: Int) {
        if (child !== mStepTextView && child !== mContainerView
                && child !== mBottomBar) {
            // Add it to our container view
            mContainerView.addView(child, width, height)
        } else {
            super.addView(child, width, height)
        }
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        if (child !== mStepTextView && child !== mContainerView
                && child !== mBottomBar) {
            // Add it to our container view
            mContainerView.addView(child, params)
        } else {
            super.addView(child, params)
        }
    }

    override fun addView(child: View, index: Int) {
        if (child !== mStepTextView && child !== mContainerView
                && child !== mBottomBar) {
            // Add it to our container view
            mContainerView.addView(child, index)
        } else {
            super.addView(child, index)
        }
    }

    fun setContainerView(containerView: FrameLayout) {
        mContainerView.removeAllViews()
        mContainerView.addView(containerView)
    }

    fun removeContainerView() {
        mContainerView.removeAllViews()
    }

    @IntDef(TYPE_TEXT.toLong(), TYPE_DOTS.toLong(), TYPE_BAR.toLong())
    @Retention(AnnotationRetention.SOURCE)
    annotation class ProgressType

    interface OnStepperProgressListener {

        fun onStepSelected(position: Int)

        fun onStepDeselected(position: Int)

        fun onStepsFinished()
    }

    companion object {
        const val TYPE_TEXT = 0
        const val TYPE_DOTS = 1
        const val TYPE_BAR = 2
    }
}
