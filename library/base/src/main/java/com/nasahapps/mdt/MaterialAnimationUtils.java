package com.nasahapps.mdt;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Hakeem on 10/17/16.
 */

public class MaterialAnimationUtils {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void createChangeBoundsAnimation(View disappearingView, View appearingView) {
        Scene scene;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scene = new Scene((ViewGroup) disappearingView.getParent(), appearingView);
        } else {
            scene = new Scene((ViewGroup) disappearingView.getParent(), (ViewGroup) appearingView.getParent());
        }

        TransitionManager.beginDelayedTransition((ViewGroup) disappearingView.getParent(), new ChangeBounds());
        disappearingView.setLeft(appearingView.getLeft());
    }

}
