package com.nasahapps.mdt;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by Hakeem on 10/17/16.
 */

public class MaterialAnimationUtils {

    private static final int DURATION = 1000;
    private static final TimeInterpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void createChangeBoundsAnimation(final View originalView, final View newView) {
//        Scene scene;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            scene = new Scene((ViewGroup) appearingView.getParent(), appearingView);
//        } else {
//            scene = new Scene((ViewGroup) appearingView.getParent(), (ViewGroup) appearingView.getParent());
//        }
//        TransitionManager.go(scene, new ChangeBounds().setDuration(2000));

//        appearingView.setVisibility(View.VISIBLE);
//        disappearingView.setVisibility(View.INVISIBLE);
        final float[] transArray = new float[2];
        final int[] originalCoords = new int[2];
        Utils.getAbsoluteCoordinates(originalView, originalCoords);
        calculateTranslationForCenterRepositioning(originalView, newView, transArray);
        originalView.animate().z(0f).setDuration(DURATION).setInterpolator(INTERPOLATOR);
        originalView.animate().translationX(transArray[0]).setDuration(DURATION).setInterpolator(INTERPOLATOR);
        originalView.animate().translationY(transArray[1]).setDuration(DURATION).setInterpolator(INTERPOLATOR)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        newView.setVisibility(View.VISIBLE);
                        // Have the "center" be where the old view currently is
//                            int centerX = (int)(originalView.getTranslationX() + (transArray[0] * value));
//                            int centerY = (int)(originalView.getTranslationY() + (transArray[1] * value));
                        int centerX = newView.getWidth() / 2;
                        int centerY = newView.getHeight() / 2;
                        Animator animator = ViewAnimationUtils.createCircularReveal(newView,
                                centerX, centerY, originalView.getWidth(), newView.getWidth() * 1.3f);
                        animator.setInterpolator(INTERPOLATOR);
                        animator.setDuration(DURATION);
                        animator.start();
                    }
                });
//                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//                    private boolean mCircularRevealShown;
//
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        float value = (float) animation.getAnimatedValue();
//                        if (value > 0.6f && !mCircularRevealShown) {
//                            mCircularRevealShown = true;
//                            newView.setVisibility(View.VISIBLE);
//                            // Have the "center" be where the old view currently is
////                            int centerX = (int)(originalView.getTranslationX() + (transArray[0] * value));
////                            int centerY = (int)(originalView.getTranslationY() + (transArray[1] * value));
//                            int centerX = newView.getWidth() / 2;
//                            int centerY = newView.getHeight() / 2;
//                            Animator animator = ViewAnimationUtils.createCircularReveal(newView,
//                                    centerX, centerY, originalView.getWidth(), newView.getWidth() * 1.3f);
//                            animator.setInterpolator(INTERPOLATOR);
//                            animator.setDuration(DURATION);
//                            animator.start();
//                        }
//                    }
//                });
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

}
