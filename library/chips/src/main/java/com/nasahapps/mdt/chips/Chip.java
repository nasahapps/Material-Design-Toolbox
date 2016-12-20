package com.nasahapps.mdt.chips;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.github.tamir7.contacts.Contact;
import com.nasahapps.mdt.Utils;

/**
 * Created by hhasan on 11/18/16.
 */

public class Chip extends LinearLayout implements View.OnClickListener {

    private AppCompatTextView mText, mInitialIcon;
    private AppCompatImageView mAvatarImage, mCancelIcon;

    public Chip(Context context) {
        super(context);
        init(null);
    }

    public Chip(Context context, Contact contact) {
        this(context);
        mText.setText(contact.getDisplayName());
        if (!TextUtils.isEmpty(contact.getPhotoUri())) {
            mInitialIcon.setVisibility(GONE);
            mAvatarImage.setVisibility(VISIBLE);
            mAvatarImage.setImageURI(Uri.parse(contact.getPhotoUri()));
            mAvatarImage.setImageDrawable(Utils.getRoundedDrawable(context, mAvatarImage.getDrawable()));
        } else {
            mAvatarImage.setVisibility(GONE);
            mInitialIcon.setVisibility(VISIBLE);
            mInitialIcon.setText(contact.getDisplayName().substring(0, 1));
        }
        mCancelIcon.setVisibility(VISIBLE);
    }

    public Chip(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.mdt_chip_rounded_background));

        FrameLayout iconLayout = new FrameLayout(getContext());
        addView(iconLayout, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mAvatarImage = new AppCompatImageView(getContext());
        FrameLayout.LayoutParams avatarLp = new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.mdt_chip_avatar_image_size),
                getResources().getDimensionPixelSize(R.dimen.mdt_chip_avatar_image_size));
        mAvatarImage.setVisibility(GONE);
        iconLayout.addView(mAvatarImage, avatarLp);

        mInitialIcon = new AppCompatTextView(getContext());
        mInitialIcon.setVisibility(GONE);
        mInitialIcon.setTextColor(Color.WHITE);
        mInitialIcon.setGravity(Gravity.CENTER);
        mInitialIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        mInitialIcon.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.mdt_chip_initial_background));
        mInitialIcon.setSupportBackgroundTintList(ColorStateList.valueOf(Utils.getColorFromAttribute(getContext(), R.attr.colorAccent)));
        iconLayout.addView(mInitialIcon, new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.mdt_chip_avatar_image_size),
                getResources().getDimensionPixelSize(R.dimen.mdt_chip_avatar_image_size)));

        mText = new AppCompatTextView(getContext());
        mText.setTextColor(ContextCompat.getColor(getContext(), R.color.mdt_black_87));
        mText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.mdt_chip_text_size));
        mText.setPadding(getResources().getDimensionPixelSize(R.dimen.mdt_chip_padding_left_right),
                getResources().getDimensionPixelSize(R.dimen.mdt_chip_padding_top_bottom),
                getResources().getDimensionPixelSize(R.dimen.mdt_chip_padding_left_right),
                getResources().getDimensionPixelSize(R.dimen.mdt_chip_padding_top_bottom));
        mText.setMaxLines(1);
        mText.setEllipsize(TextUtils.TruncateAt.END);
        mText.setFreezesText(true);
        addView(mText);

        mCancelIcon = new AppCompatImageView(getContext());
        mCancelIcon.setImageResource(R.drawable.ic_mdt_chip_cancel);
        mCancelIcon.setImageAlpha(138); // 54% alpha
        LayoutParams cancelLp = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.mdt_chip_cancel_icon_size),
                getResources().getDimensionPixelSize(R.dimen.mdt_chip_cancel_icon_size));
        cancelLp.setMargins(getResources().getDimensionPixelSize(R.dimen.mdt_chip_cancel_margin_left_right),
                0, getResources().getDimensionPixelSize(R.dimen.mdt_chip_cancel_margin_left_right), 0);
        mCancelIcon.setVisibility(GONE);
        addView(mCancelIcon, cancelLp);

        if (attrs != null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Chip, 0, 0);
            try {
                mText.setText(ta.getString(R.styleable.Chip_chipText));
                mCancelIcon.setVisibility(ta.getBoolean(R.styleable.Chip_chipShowClose, false) ? VISIBLE : GONE);
                boolean showContactImage = ta.getBoolean(R.styleable.Chip_chipShowContactImage, false);
                boolean showInitial = ta.getBoolean(R.styleable.Chip_chipShowInitial, false);
                mAvatarImage.setVisibility(showContactImage ? VISIBLE : GONE);
                if (showContactImage) {
                    // If there is an image to show, show it. Else show just the first letter of the contact name
                    int imageResource = ta.getResourceId(R.styleable.Chip_chipContactImage, 0);
                    if (imageResource != 0) {
                        setRoundedImage(imageResource);
                        mInitialIcon.setVisibility(GONE);
                    }
                } else if (showInitial && !TextUtils.isEmpty(mText.getText())) {
                    // Use the first letter of the text as an initial, use that as the left icon
                    CharSequence initial = mText.getText().toString();
                    mInitialIcon.setText(initial);
                    int initialBackgroundColor = ta.getColor(R.styleable.Chip_chipInitialBackgroundColor, 0);
                    if (initialBackgroundColor != 0) {
                        mInitialIcon.setSupportBackgroundTintList(ColorStateList.valueOf(initialBackgroundColor));
                    }
                    int initialTextColor = ta.getColor(R.styleable.Chip_chipInitialTextColor, 0);
                    if (initialTextColor != 0) {
                        mInitialIcon.setTextColor(initialTextColor);
                    }
                    mInitialIcon.setVisibility(VISIBLE);
                    mAvatarImage.setVisibility(GONE);
                }
            } finally {
                ta.recycle();
            }
        }

        setOnClickListener(this);
        mCancelIcon.setOnClickListener(this);

        adjustTextPadding();
    }

    private void adjustTextPadding() {
        int textStartPadding, textEndPadding;
        if (mAvatarImage.getVisibility() == VISIBLE) {
            // 8dp start padding
            textStartPadding = getResources().getDimensionPixelSize(R.dimen.mdt_chip_padding_left_right_smaller);
        } else {
            textStartPadding = getResources().getDimensionPixelSize(R.dimen.mdt_chip_padding_left_right);
        }

        if (mCancelIcon.getVisibility() == VISIBLE) {
            textEndPadding = 0;
        } else {
            textEndPadding = getResources().getDimensionPixelSize(R.dimen.mdt_chip_padding_left_right);
        }

        mText.setPaddingRelative(textStartPadding, mText.getPaddingTop(), textEndPadding, mText.getPaddingBottom());
    }

    @Override
    public void onClick(View view) {
        if (view == mCancelIcon) {
            // Remove this Chip from its parent layout
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    private void setRoundedImage(@DrawableRes int res) {
        mAvatarImage.setImageDrawable(Utils.getRoundedDrawable(getContext(), res));
    }

    private void setRoundedImage(@Nullable Drawable drawable) {
        mAvatarImage.setImageDrawable(Utils.getRoundedDrawable(getContext(), drawable));
    }

    public void setText(CharSequence text) {
        mText.setText(text);
    }

    public CharSequence getText() {
        return mText.getText();
    }

    public void setText(@StringRes int res) {
        mText.setText(res);
    }

    public void setCancelIconVisible(boolean visible) {
        mCancelIcon.setVisibility(visible ? VISIBLE : GONE);
        adjustTextPadding();
    }

    public void setAvatarImageVisible(boolean visible) {
        mAvatarImage.setVisibility(visible ? VISIBLE : GONE);
        adjustTextPadding();
    }

    public void setAvatarImageResource(@DrawableRes int res) {
        setAvatarImageVisible(res != 0);
        setRoundedImage(res);
    }

    public Drawable getAvatarImage() {
        return mAvatarImage.getDrawable();
    }

    public void setAvatarImage(Drawable drawable) {
        setAvatarImageVisible(drawable != null);
        setRoundedImage(drawable);
    }

    public CharSequence getInitialText() {
        return mInitialIcon.getText();
    }

    public void setInitialText(String text) {
        if (TextUtils.isEmpty(text) || text.length() > 1) {
            throw new IllegalArgumentException("Text must only be one character long!");
        }

        mInitialIcon.setText(text);
    }

    @ColorInt
    public int getInitialTextColor() {
        return mInitialIcon.getCurrentTextColor();
    }

    public void setInitialTextColor(@ColorInt int color) {
        mInitialIcon.setTextColor(color);
    }

    public void setInitialTextVisible(boolean visible) {
        mInitialIcon.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setInitialTextBackgroundTint(@ColorInt int color) {
        mInitialIcon.setSupportBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void setOnCancelClickListener(OnClickListener listener) {
        mCancelIcon.setOnClickListener(listener);
    }

    public void setOnAvatarImageClickListener(OnClickListener listener) {
        mAvatarImage.setOnClickListener(listener);
    }
}
