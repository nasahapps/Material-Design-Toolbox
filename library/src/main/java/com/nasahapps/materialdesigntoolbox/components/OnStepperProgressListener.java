package com.nasahapps.materialdesigntoolbox.components;

/**
 * Created by Hakeem on 4/17/16.
 */
public interface OnStepperProgressListener {

    void onStepSelected(int position);

    void onStepDeselected(int position);

    void onStepCompleted(int position);

    void onStepsFinished();
}
