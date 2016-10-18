package com.nasahapps.mdt;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;

import java.lang.ref.WeakReference;

/**
 * Created by Hakeem on 10/17/16.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class FabAnimationHelper {

    private static final int DURATION = 650;
    private static final TimeInterpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    private WeakReference<FloatingActionButton> mFab;
    private int[] mOriginalCoordinates;
    private float mOriginalElevation;
    private boolean mAnimated;

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

        int centerX = (newView.getWidth() / 2) - (originalView.getWidth() / 2);
        int centerY = (newCoords[1] + newView.getHeight() / 2) - (originalView.getHeight() / 2);

        array[0] = centerX - originalCoords[0];
        array[1] = centerY - originalCoords[1];
    }

    public void animate(View newView) {
        if (mFab.get() != null && newView != null) {
            final WeakReference<View> viewWeakReference = new WeakReference<>(newView);
            final FloatingActionButton fab = mFab.get();
            fab.animate().setListener(null);

            if (mAnimated) {
                animateCircularReveal(true, viewWeakReference);
            } else {
                animateFabElevation(false);
                animateFabTranslation(false, viewWeakReference);
            }

            mAnimated = !mAnimated;
        }
    }

    private void setInitialValues() {
        if (mFab.get() != null) {
            mFab.get().post(new Runnable() {
                @Override
                public void run() {
                    if (mFab.get() != null) {
                        mOriginalCoordinates = new int[2];
                        Utils.getAbsoluteCoordinates(mFab.get(), mOriginalCoordinates);
                        mOriginalElevation = mFab.get().getCompatElevation();
                    }
                }
            });
        }
    }

    private void animateFabElevation(boolean reversed) {
        if (mFab.get() != null) {
            mFab.get().setCompatElevation(reversed ? mOriginalElevation : 0f);
        }
    }

    private void animateFabTranslation(boolean reverse, WeakReference<View> viewRef) {
        animateFabTranslation(reverse, viewRef, 0);
    }

    private void animateFabTranslation(boolean reversed, final WeakReference<View> viewRef, long delay) {
        if (mFab.get() != null && viewRef.get() != null) {
            if (!reversed) {
                final float[] transArray = new float[2];
                calculateTranslationForCenterRepositioning(mFab.get(), viewRef.get(), transArray);

                mFab.get().animate().translationX(transArray[0]).setInterpolator(INTERPOLATOR).setDuration(DURATION);
                mFab.get().animate().translationY(transArray[1]).setInterpolator(INTERPOLATOR).setDuration(DURATION)
                        .setUpdateListener(new DelayedAnimatorUpdateListener() {
                            @Override
                            public void doDelayedAnimation(float value) {
                                animateCircularReveal(false, viewRef);
                            }
                        });
            } else {
                if (mFab.get() != null) {
                    FloatingActionButton fab = mFab.get();
                    fab.animate().translationX(0f).setInterpolator(INTERPOLATOR).setStartDelay(delay).setDuration(DURATION);
                    fab.animate().translationY(0f).setInterpolator(INTERPOLATOR).setStartDelay(delay).setDuration(DURATION)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    animateFabElevation(true);
                                }
                            });
                }
            }
        }
    }

    private void animateCircularReveal(boolean reversed, final WeakReference<View> viewRef) {
        if (mFab.get() != null && viewRef.get() != null) {
            if (!reversed) {
                if (viewRef.get() != null && mFab.get() != null) {
                    viewRef.get().setVisibility(View.VISIBLE);
                    int[] fabCoords = new int[2];
                    int[] viewCoords = new int[2];
                    Utils.getAbsoluteCoordinates(mFab.get(), fabCoords);
                    Utils.getAbsoluteCoordinates(viewRef.get(), viewCoords);

                    // Have the "center" be where the fab currently is
                    int centerX = fabCoords[0] - viewCoords[0];
                    int centerY = fabCoords[1] - viewCoords[1];
                    Animator animator = ViewAnimationUtils.createCircularReveal(viewRef.get(),
                            centerX, centerY, 0f, viewRef.get().getWidth() * 1.5f);
                    animator.setInterpolator(INTERPOLATOR);
                    animator.setDuration(DURATION);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (mFab.get() != null) {
                                mFab.get().setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                    animator.start();
                }
            } else {
                mFab.get().setVisibility(View.VISIBLE);
                int centerX = viewRef.get().getWidth() / 2;
                int centerY = viewRef.get().getHeight() / 2;
                Animator animator = ViewAnimationUtils.createCircularReveal(viewRef.get(),
                        centerX, centerY, viewRef.get().getWidth() * 1.3f, 0f);
                animator.setInterpolator(INTERPOLATOR);
                animator.setDuration(DURATION);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (viewRef.get() != null) {
                            viewRef.get().setVisibility(View.INVISIBLE);
                        }
                    }
                });
                animator.start();
                animateFabTranslation(true, viewRef, (long) (animator.getDuration() * 0.6f));
            }
        }
    }

    private static abstract class DelayedAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private boolean mAnimated;

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float value = (float) animation.getAnimatedValue();
            if (value > 0.5f && !mAnimated) {
                mAnimated = true;
                doDelayedAnimation(value);
            }
        }

        public abstract void doDelayedAnimation(float value);

    }

}
