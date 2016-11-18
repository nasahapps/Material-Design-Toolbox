package com.nasahapps.mdt.chips;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.nasahapps.mdt.Utils;

/**
 * Created by hhasan on 11/18/16.
 */

public class Chip extends LinearLayout {

    private AppCompatTextView mText;
    private AppCompatImageView mAvatarImage, mCancelIcon;

    public Chip(Context context) {
        this(context, null);
    }

    public Chip(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.mdt_chip_rounded_background));

        mAvatarImage = new AppCompatImageView(getContext());
        LayoutParams avatarLp = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.mdt_chip_avatar_image_size),
                getResources().getDimensionPixelSize(R.dimen.mdt_chip_avatar_image_size));
        addView(mAvatarImage, avatarLp);

        mText = new AppCompatTextView(getContext());
        mText.setTextColor(ContextCompat.getColor(getContext(), R.color.mdt_black_87));
        mText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.mdt_chip_text_size));
        mText.setPadding(getResources().getDimensionPixelSize(R.dimen.mdt_chip_padding_left_right),
                getResources().getDimensionPixelSize(R.dimen.mdt_chip_padding_top_bottom),
                getResources().getDimensionPixelSize(R.dimen.mdt_chip_padding_left_right),
                getResources().getDimensionPixelSize(R.dimen.mdt_chip_padding_top_bottom));
        mText.setMaxLines(1);
        mText.setEllipsize(TextUtils.TruncateAt.END);
        addView(mText);

        mCancelIcon = new AppCompatImageView(getContext());
        mCancelIcon.setImageResource(R.drawable.ic_mdt_chip_cancel);
        mCancelIcon.setImageAlpha(138);
        LayoutParams cancelLp = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.mdt_chip_cancel_icon_size),
                getResources().getDimensionPixelSize(R.dimen.mdt_chip_cancel_icon_size));
        cancelLp.setMargins(getResources().getDimensionPixelSize(R.dimen.mdt_chip_cancel_margin_left_right),
                0, getResources().getDimensionPixelSize(R.dimen.mdt_chip_cancel_margin_left_right), 0);
        addView(mCancelIcon, cancelLp);

        mText.setText("Example Chip");
        Drawable avatarImage = ContextCompat.getDrawable(getContext(), R.drawable.mdt_test_avatar);
        Bitmap avatarBitmap = Utils.convertDrawableToBitmap(avatarImage);
        RoundedBitmapDrawable avatar = RoundedBitmapDrawableFactory.create(getResources(), avatarBitmap);
        avatar.setCircular(true);
        mAvatarImage.setImageDrawable(avatar);
    }
}
