package com.nasahapps.mdt.fabutils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.transition.ArcMotion;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.nasahapps.mdt.Utils;

import java.lang.ref.WeakReference;

/**
 * Created by Hakeem on 10/17/16.
 */

public class FabAnimationHelper {

    private static final TimeInterpolator DEFAULT_INTERPOLATOR = new FastOutSlowInInterpolator();

    private WeakReference<FloatingActionButton> mFab;
    private int[] mOriginalCoordinates;
    private float mOriginalElevation;
    private long mDuration = 750;
    private TimeInterpolator mInterpolator = DEFAULT_INTERPOLATOR;
    private ValueAnimator mTranslationAnimator, mBackgroundTintAnimator, mImageAlphaAnimator;
    private Animator mCircularRevealAnimator;
    private int mOriginalColor, mOriginalImageAlpha;
    private long mStartDelay;

    public FabAnimationHelper(FloatingActionButton fab) {
        mFab = new WeakReference<>(fab);
        setInitialValues();
    }

    private static void calculateTranslationForCenterRepositioning(View originalView, View newView, float[] array) {
        if (array.length < 2) {
            throw new RuntimeException("Array must be of size 2!");
        }

        int[] originalCoords = new int[2];
        int[] newCoords = new int[2];
        Utils.getAbsoluteCoordinates(originalView, originalCoords);
        Utils.getAbsoluteCoordinates(newView, newCoords);

        int centerX = newCoords[0] + (newView.getWidth() / 2) - (originalView.getWidth() / 2);
        int centerY = newCoords[1] + (newView.getHeight() / 2) - (originalView.getHeight() / 2);

        array[0] = centerX - originalCoords[0];
        array[1] = centerY - originalCoords[1];
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Path createArcMotionPath(float startX, float startY, float endX, float endY) {
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMinimumHorizontalAngle(15f);
        arcMotion.setMinimumVerticalAngle(0f);
        arcMotion.setMaximumAngle(90f);
        return arcMotion.getPath(startX, startY, endX, endY);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void animateFromView(View fromView) {
        animate(fromView, true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void animateToView(View toView) {
        animate(toView, false);
    }

    private void animate(final View view, final boolean reversed) {
        if (mFab.get() != null && view != null) {
            mFab.get().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mTranslationAnimator != null && mTranslationAnimator.isStarted()) {
                        mTranslationAnimator.end();
                    }
                    if (mCircularRevealAnimator != null && mCircularRevealAnimator.isStarted()) {
                        mCircularRevealAnimator.end();
                    }
                    if (mBackgroundTintAnimator != null && mBackgroundTintAnimator.isStarted()) {
                        mBackgroundTintAnimator.end();
                    }
                    if (mImageAlphaAnimator != null && mImageAlphaAnimator.isStarted()) {
                        mImageAlphaAnimator.end();
                    }

                    final WeakReference<View> viewWeakReference = new WeakReference<>(view);
                    final FloatingActionButton fab = mFab.get();
                    fab.animate().setListener(null);

                    if (reversed) {
                        animateCircularReveal(true, viewWeakReference);
                    } else {
                        animateFabElevation(ViewCompat.getElevation(view));
                        animateFabTranslation(false, viewWeakReference, 0);
                        animateFabBackgroundTint(false, viewWeakReference, 0);
                        animateFabImageAlpha(false, 0);
                    }
                }
            }, mStartDelay);
        }
    }

    private void setInitialValues() {
        if (mFab.get() != null) {
            mFab.get().post(new Runnable() {
                @Override
                public void run() {
                    if (mFab.get() != null) {
                        FloatingActionButton fab = mFab.get();
                        mOriginalCoordinates = new int[2];
                        Utils.getAbsoluteCoordinates(fab, mOriginalCoordinates);

                        mOriginalElevation = fab.getCompatElevation();

                        ColorStateList backgroundTintList = fab.getBackgroundTintList();
                        if (backgroundTintList != null) {
                            mOriginalColor = backgroundTintList.getDefaultColor();
                        }

                        mOriginalImageAlpha = fab.getImageAlpha();
                    }
                }
            });
        }
    }

    private void animateFabElevation(float elevation) {
        if (mFab.get() != null) {
            mFab.get().setCompatElevation(elevation);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateFabTranslation(boolean reversed, final WeakReference<View> viewRef, long delay) {
        if (mFab.get() != null && viewRef.get() != null) {
            if (!reversed) {
                final float[] transArray = new float[2];
                calculateTranslationForCenterRepositioning(mFab.get(), viewRef.get(), transArray);

                mTranslationAnimator = ObjectAnimator.ofFloat(mFab.get(), "translationX", "translationY",
                        createArcMotionPath(0, 0, transArray[0], transArray[1]));
                mTranslationAnimator.setInterpolator(mInterpolator);
                mTranslationAnimator.addUpdateListener(new DelayedAnimatorUpdateListener() {
                    @Override
                    public void doDelayedAnimation() {
                        animateCircularReveal(false, viewRef);
                        mTranslationAnimator.removeAllUpdateListeners();
                    }
                });
                if (mDuration != 0) {
                    mTranslationAnimator.setDuration(mDuration);
                }
                mTranslationAnimator.setStartDelay(delay);
                mTranslationAnimator.start();
            } else {
                if (mFab.get() != null) {
                    FloatingActionButton fab = mFab.get();
                    mTranslationAnimator = ObjectAnimator.ofFloat(fab, "translationX", "translationY",
                            createArcMotionPath(fab.getTranslationX(), fab.getTranslationY(), 0, 0));
                    mTranslationAnimator.setInterpolator(mInterpolator);
                    mTranslationAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animateFabElevation(mOriginalElevation);
                            mTranslationAnimator.removeAllListeners();
                        }
                    });
                    if (mDuration != 0) {
                        mTranslationAnimator.setDuration(mDuration);
                    }
                    mTranslationAnimator.setStartDelay(delay);
                    mTranslationAnimator.start();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateCircularReveal(boolean reversed, final WeakReference<View> viewRef) {
        if (mFab.get() != null && viewRef.get() != null) {
            if (!reversed) {
                if (viewRef.get() != null && mFab.get() != null) {
                    viewRef.get().setVisibility(View.VISIBLE);

                    // Have the "center" be the center of the revealing view
                    int centerX = viewRef.get().getWidth() / 2;
                    int centerY = viewRef.get().getHeight() / 2;
                    mCircularRevealAnimator = ViewAnimationUtils.createCircularReveal(viewRef.get(),
                            centerX, centerY, 0f, viewRef.get().getWidth() * 1.5f);
                    mCircularRevealAnimator.setInterpolator(mInterpolator);
                    if (mDuration != 0) {
                        mCircularRevealAnimator.setDuration(mDuration);
                    }
                    mCircularRevealAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (mFab.get() != null) {
                                mFab.get().setVisibility(View.INVISIBLE);
                            }

                            mCircularRevealAnimator.removeAllListeners();
                        }
                    });
                    mCircularRevealAnimator.start();
                }
            } else {
                mFab.get().setVisibility(View.VISIBLE);
                int centerX = viewRef.get().getWidth() / 2;
                int centerY = viewRef.get().getHeight() / 2;
                mCircularRevealAnimator = ViewAnimationUtils.createCircularReveal(viewRef.get(),
                        centerX, centerY, viewRef.get().getWidth() * 1.3f, 0f);
                mCircularRevealAnimator.setInterpolator(mInterpolator);
                if (mDuration != 0) {
                    mCircularRevealAnimator.setDuration(mDuration);
                }
                mCircularRevealAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (viewRef.get() != null) {
                            viewRef.get().setVisibility(View.INVISIBLE);
                        }

                        mCircularRevealAnimator.removeAllListeners();
                    }
                });
                mCircularRevealAnimator.start();

                long delay = (long) (mCircularRevealAnimator.getDuration() * 0.75f);
                animateFabTranslation(true, viewRef, delay);
                animateFabBackgroundTint(true, viewRef, delay);
                animateFabImageAlpha(true, delay);
            }
        }
    }

    private void animateFabBackgroundTint(boolean reversed, WeakReference<View> viewRef, long delay) {
        if (mFab.get() != null && viewRef.get() != null) {
            Drawable backgroundDrawable = viewRef.get().getBackground();
            if (backgroundDrawable instanceof ColorDrawable) {
                int startColor = reversed ? ((ColorDrawable) backgroundDrawable).getColor() : mOriginalColor;
                final int endColor = reversed ? mOriginalColor : ((ColorDrawable) backgroundDrawable).getColor();

                mBackgroundTintAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
                if (mDuration != 0) {
                    mBackgroundTintAnimator.setDuration(mDuration);
                }
                mBackgroundTintAnimator.setInterpolator(mInterpolator);
                mBackgroundTintAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (mFab.get() != null) {
                            mFab.get().setBackgroundTintList(ColorStateList.valueOf((int) animation.getAnimatedValue()));
                        }
                    }
                });
                mBackgroundTintAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (mFab.get() != null) {
                            // Force the end color in case this animation was ended early
                            mFab.get().setBackgroundTintList(ColorStateList.valueOf(endColor));
                        }

                        mBackgroundTintAnimator.removeAllUpdateListeners();
                        mBackgroundTintAnimator.removeAllListeners();
                    }
                });
                mBackgroundTintAnimator.setStartDelay(delay);
                mBackgroundTintAnimator.start();
            }
        }
    }

    private void animateFabImageAlpha(boolean reversed, long delay) {
        if (mFab.get() != null) {
            int startValue = reversed ? 0 : mOriginalImageAlpha;
            final int endValue = reversed ? mOriginalImageAlpha : 0;
            mImageAlphaAnimator = ValueAnimator.ofInt(startValue, endValue);
            mImageAlphaAnimator.setInterpolator(mInterpolator);
            if (mDuration != 0) {
                mImageAlphaAnimator.setDuration(mDuration);
            }
            mImageAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (mFab.get() != null) {
                        mFab.get().setImageAlpha((int) animation.getAnimatedValue());
                    }
                }
            });
            mImageAlphaAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mFab.get() != null) {
                        // Force the end image alpha in case this animation was ended early
                        mFab.get().setImageAlpha(endValue);
                    }

                    mBackgroundTintAnimator.removeAllUpdateListeners();
                    mBackgroundTintAnimator.removeAllListeners();
                }
            });
            mImageAlphaAnimator.setStartDelay(delay);
            mImageAlphaAnimator.start();
        }
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public TimeInterpolator getInterpolator() {
        return mInterpolator;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        mInterpolator = interpolator;
    }

    public long getStartDelay() {
        return mStartDelay;
    }

    public void setStartDelay(long startDelay) {
        mStartDelay = startDelay;
    }

    private static abstract class DelayedAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private boolean mAnimated;

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float value = animation.getAnimatedFraction();
            if (value > 0.9f && !mAnimated) {
                mAnimated = true;
                doDelayedAnimation();
            }
        }

        public abstract void doDelayedAnimation();

    }

}
