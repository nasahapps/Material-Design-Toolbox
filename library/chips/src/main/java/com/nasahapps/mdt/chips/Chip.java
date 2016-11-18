package com.nasahapps.mdt.chips;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by hhasan on 11/18/16.
 */

public class Chip extends LinearLayout {

    private AppCompatTextView mText;

    public Chip(Context context) {
        this(context, null);
    }

    public Chip(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setGravity(HORIZONTAL);

        mText = new AppCompatTextView(getContext(), null, R.style.TextAppearance_AppCompat);

        addView(mText);

        if (isInEditMode()) {
            mText.setText("Example Chip");
        }
    }
}
