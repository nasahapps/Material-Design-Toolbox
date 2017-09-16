package com.nasahapps.mdt

import android.annotation.TargetApi
import android.os.Build
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup

/**
 * Created by Hakeem on 10/17/16.
 */

object MaterialAnimationUtils {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @JvmStatic
    fun createChangeBoundsAnimation(disappearingView: View, appearingView: View) {
        TransitionManager.beginDelayedTransition(disappearingView.parent as ViewGroup, ChangeBounds())
        disappearingView.left = appearingView.left
    }

}
