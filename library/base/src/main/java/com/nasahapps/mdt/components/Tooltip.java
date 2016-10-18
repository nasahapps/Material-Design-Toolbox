package com.nasahapps.mdt.components;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.nasahapps.mdt.R;
import com.nasahapps.mdt.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Hakeem on 4/12/16.
 * <p/>
 * A {@link android.widget.TextView} that may appear on hover and focus when the user hovers over an
 * element with a cursor, focuses on an element using a keyboard (usually through the TAB key), or
 * upon touch (without releasing) in a touch UI. It may contain textual indentification for the
 * element in question. It may also contain brief helper text regarding the function of the element.
 * The label itself cannot receive input focus.
 * <p/>
 * <a href=http://www.google.com/design/spec/components/tooltips.html>http://www.google.com/design/spec/components/tooltips.html</a>
 */
public class Tooltip extends AppCompatTextView {

    /**
     * Show the view or text notification for a short period of time (2 seconds). This time could be
     * user-definable. This is the default.
     *
     * @see #setDuration
     */
    public static final int LENGTH_SHORT = 0;
    /**
     * Show the view or text notification for a long period of time (3.5 seconds). This time could be
     * user-definable.
     *
     * @see #setDuration
     */
    public static final int LENGTH_LONG = 1;
    int mDuration = LENGTH_SHORT;
    View mAnchor;

    public Tooltip(Context context) {
        super(context);
        init();
    }

    public Tooltip(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Tooltip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public static Tooltip makeTooltip(Context c, CharSequence text, @Duration int length,
                                      @NonNull View anchor) {
        Tooltip tooltip = new Tooltip(c);
        tooltip.setText(text);

        if (length == LENGTH_LONG) {
            tooltip.mDuration = length;
        }
        tooltip.mAnchor = anchor;

        return tooltip;
    }

    public static Tooltip makeTooltip(Context c, @StringRes int stringRes, @Duration int length,
                                      @NonNull View anchor) {
        Tooltip tooltip = new Tooltip(c);
        tooltip.setText(stringRes);

        if (length == LENGTH_LONG) {
            tooltip.mDuration = length;
        }
        tooltip.mAnchor = anchor;

        return tooltip;
    }

    private void init() {
        int dp16 = Utils.dpToPixel(getContext(), 16);
        int dp6 = Utils.dpToPixel(getContext(), 6);

        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        setPadding(dp16, dp6, dp16, dp6);
        setSingleLine();
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.mdt_grey_700));
        setTextColor(Color.WHITE);
        setGravity(Gravity.CENTER);

        if (isInEditMode()) {
            setText("Tooltip text");
        }
    }

    /**
     * Return the duration.
     *
     * @see #setDuration
     */
    @Duration
    public int getDuration() {
        return mDuration;
    }

    /**
     * Set how long to show the view for.
     *
     * @see #LENGTH_SHORT
     * @see #LENGTH_LONG
     */
    public void setDuration(@Duration int duration) {
        mDuration = duration;
    }

    public void show() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }

        ViewGroup rootView = (ViewGroup) mAnchor.getRootView();
        rootView.addView(this, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        setAlpha(0f);

        post(new Runnable() {
            @Override
            public void run() {
                int[] anchorCoords = new int[2];
                mAnchor.getLocationOnScreen(anchorCoords);

                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();

                int y = anchorCoords[1] + Utils.dpToPixel(getContext(), 24) + Utils.dpToPixel(getContext(), 32);
                // To prevent being cut off on the bottom of the screen
                if (y + getMeasuredHeight() >= getRootView().getHeight()) {
                    y = anchorCoords[1] - Utils.dpToPixel(getContext(), 32);
                }
                lp.setMargins(0, y, 0, 0);

                int anchorMidX = anchorCoords[0] + mAnchor.getWidth() / 2;
                if (ViewUtils.isLayoutRtl(mAnchor)) {
                    anchorMidX = Utils.getRtlX(getContext(), anchorMidX);
                }
                int x = anchorMidX - getMeasuredWidth() / 2;
                // To prevent being cut off on the left side of the screen
                if (x < 0) {
                    x = Utils.dpToPixel(getContext(), 8);
                }
                // To prevent being cut off on the right side of the screen
                int rootViewWidth = getRootView().getWidth();
                if (x + getMeasuredWidth() >= rootViewWidth) {
                    x = rootViewWidth - getMeasuredWidth() - Utils.dpToPixel(getContext(), 8);
                }
                MarginLayoutParamsCompat.setMarginStart(lp, x);
                setLayoutParams(lp);

                // Animate Tooltip in
                setAlpha(0.9f);
                AlphaAnimation fadeIn = new AlphaAnimation(0f, 0.9f);
                fadeIn.setDuration(250);
                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Then animateToView it back out
                        long duration = mDuration == LENGTH_LONG ? 3500 : 2000;
                        AlphaAnimation fadeOut = new AlphaAnimation(0.9f, 0f);
                        fadeOut.setStartOffset(duration);
                        fadeOut.setDuration(250);
                        fadeOut.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                if (getParent() != null) {
                                    ((ViewGroup) getParent()).removeView(Tooltip.this);
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        startAnimation(fadeOut);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                startAnimation(fadeIn);
            }
        });
    }

    @IntDef({LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }
}
