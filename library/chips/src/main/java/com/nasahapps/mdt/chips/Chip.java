package com.nasahapps.mdt.chips;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nasahapps.mdt.Utils;

/**
 * Created by hhasan on 11/18/16.
 */

public class Chip extends LinearLayout implements View.OnClickListener {

    private AppCompatTextView mText;
    private AppCompatImageView mAvatarImage, mCancelIcon;

    public Chip(Context context) {
        this(context, null);
    }

    public Chip(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.mdt_chip_rounded_background));

        mAvatarImage = new AppCompatImageView(getContext());
        LayoutParams avatarLp = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.mdt_chip_avatar_image_size),
                getResources().getDimensionPixelSize(R.dimen.mdt_chip_avatar_image_size));
        mAvatarImage.setVisibility(GONE);
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
                mAvatarImage.setVisibility(showContactImage ? VISIBLE : GONE);
                if (showContactImage) {
                    // If there is an image to show, show it. Else show just the first letter of the contact name
                    int imageResource = ta.getResourceId(R.styleable.Chip_chipContactImage, 0);
                    if (imageResource != 0) {
                        setRoundedImage(imageResource);
                    }
                }
            } finally {
                ta.recycle();
            }
        }

        setOnClickListener(this);

        if (isInEditMode()) {
            mText.setText("Example Chip");
            setRoundedImage(R.drawable.mdt_test_avatar);
        }

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

    @TargetApi(Build.VERSION_CODES.M)
    private void showContactInfoPopup() {
        LinearLayout openParentLayout = new LinearLayout(getContext());
        openParentLayout.setOrientation(VERTICAL);
        openParentLayout.addView(createUnfocusedContactChip(true));
        openParentLayout.addView(createUnfocusedContactChip(false));
        openParentLayout.addView(createUnfocusedContactChip(false));

        PopupWindow popupWindow = new PopupWindow(openParentLayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(getResources().getDimensionPixelSize(R.dimen.mdt_chip_open_layout_elevation));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            popupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOverlapAnchor(true);
        popupWindow.showAsDropDown(this);
    }

    private LinearLayout createUnfocusedContactChip(boolean isTopMost) {
        int dp16 = Utils.dpToPixel(getContext(), 16);
        int dp40 = Utils.dpToPixel(getContext(), 40);

        int colorControlHighlight = Utils.getColorFromAttribute(getContext(), R.attr.colorControlHighlight);
        int primaryTextColor, secondaryTextColor;
        if (Utils.shouldUseWhiteText(colorControlHighlight) && isTopMost) {
            primaryTextColor = Color.WHITE;
            secondaryTextColor = ContextCompat.getColor(getContext(), R.color.mdt_white_70);
        } else {
            primaryTextColor = Color.BLACK;
            secondaryTextColor = ContextCompat.getColor(getContext(), R.color.mdt_black_87);
        }

        LinearLayout layout = new LinearLayout(getContext());
        // Create the top-most item
        layout.setOrientation(HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        int layoutTopBottomPadding = isTopMost ? dp16 : (dp16 / 2);
        layout.setPadding(dp16, layoutTopBottomPadding, dp16, layoutTopBottomPadding);
        if (isTopMost) {
            layout.setBackgroundColor(colorControlHighlight);
        }

        // Adding the avatar
        ImageView avatar = new AppCompatImageView(getContext());
        LayoutParams avatarLp = new LayoutParams(dp40, dp40);
        avatar.setLayoutParams(avatarLp);
        avatar.setImageDrawable(Utils.getRoundedDrawable(getContext(), R.drawable.mdt_test_avatar));
        layout.addView(avatar);

        // Adding the name/email (or email if not top-most)
        if (isTopMost) {
            LinearLayout textLayout = new LinearLayout(getContext());
            textLayout.setOrientation(VERTICAL);
            LayoutParams textLayoutLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textLayoutLp.setMargins(dp16, 0, dp16, 0);
            textLayout.setLayoutParams(textLayoutLp);
            layout.addView(textLayout);

            TextView name = new AppCompatTextView(getContext());
            name.setTextColor(primaryTextColor);
            name.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dpToPixel(getContext(), 16));
            name.setText("Contact Name");
            textLayout.addView(name);
            TextView email = new AppCompatTextView(getContext());
            email.setTextColor(secondaryTextColor);
            email.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dpToPixel(getContext(), 14));
            email.setText("primaryemail@email.com");
            textLayout.addView(email);

            // Adding the close icon to the top-most item
            ImageView closeIcon = new AppCompatImageView(getContext());
            closeIcon.setImageDrawable(Utils.getTintedDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mdt_chip_cancel),
                    primaryTextColor));
            layout.addView(closeIcon);
        } else {
            TextView email = new AppCompatTextView(getContext());
            email.setTextColor(secondaryTextColor);
            email.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dpToPixel(getContext(), 14));
            email.setText("primaryemail@email.com");
            LayoutParams emailLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            emailLp.setMargins(dp16, 0, dp16, 0);
            email.setLayoutParams(emailLp);
            layout.addView(email);
        }

        return layout;
    }

    @Override
    public void onClick(View view) {
        showContactInfoPopup();
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

    public void setAvatarImage(Drawable drawable) {
        setAvatarImageVisible(drawable != null);
        setRoundedImage(drawable);
    }

    public void setOnCancelClickListener(OnClickListener listener) {
        mCancelIcon.setOnClickListener(listener);
    }

    public void setOnAvatarImageClickListener(OnClickListener listener) {
        mAvatarImage.setOnClickListener(listener);
    }
}
