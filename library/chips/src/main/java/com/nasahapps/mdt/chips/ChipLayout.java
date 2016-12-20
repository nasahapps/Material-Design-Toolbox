package com.nasahapps.mdt.chips;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.nasahapps.mdt.Utils;

import org.apmem.tools.layouts.FlowLayout;

/**
 * Created by hhasan on 12/20/16.
 * <br>
 * Subclasses <a href="https://github.com/ApmeM/android-flowlayout">FlowLayout</a> for overflowing
 * chips from one line to the next.
 * <br>
 * Will auto-search relevant contacts by name, phone number, or email if the READ_CONTACTS permission
 * has been granted. No suggestions will be given if the permission is denied.
 */

public class ChipLayout extends FlowLayout implements TextWatcher,
        ChipContactLoader.ContactAdapter.OnContactSelectedListener {

    @Nullable
    private AppCompatEditText mEditText;
    private ContactType mContactType = ContactType.PHONE_NUMBER;
    private boolean mEditTextEnabled = true;
    private ChipContactLoader mContactLoader;

    public ChipLayout(Context context) {
        this(context, null);
    }

    public ChipLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ChipLayout, 0, 0);
            try {
                int contactType = ta.getInt(R.styleable.ChipLayout_chipContactType, 0);
                if (contactType < ContactType.values().length) {
                    mContactType = ContactType.values()[contactType];
                }
                mEditTextEnabled = ta.getBoolean(R.styleable.ChipLayout_chipEditTextEnabled, true);
            } finally {
                ta.recycle();
            }
        }

        if (mEditTextEnabled) {
            mEditText = new AppCompatEditText(getContext());
            mEditText.addTextChangedListener(this);
            mEditText.setMaxLines(1);
            mEditText.setMinimumWidth(Utils.dpToPixel(getContext(), 80));
            mEditText.setGravity(Gravity.CENTER_VERTICAL);
            mEditText.setBackgroundDrawable(null);
            mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            addView(mEditText);

            Contacts.initialize(getContext());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(getClass().getSimpleName(), "User is searching for contact " + s);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.e(getClass().getSimpleName(), "READ_CONTACTS permission denied, unable to get contact suggestions");
        }

        if (mContactLoader != null) {
            mContactLoader.setQuery(s);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onContactSelected(Contact contact) {
        if (mEditText != null) {
            mEditText.getText().clear();
        }

        Chip chip = new Chip(getContext(), contact);
        int dp8 = Utils.dpToPixel(getContext(), 8);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(dp8, dp8, dp8, dp8);
        addView(chip, getChildCount() - 1, lp);
    }

    public void setChipContactLoader(ChipContactLoader loader) {
        mContactLoader = loader;
        if (mContactLoader != null) {
            mContactLoader.setPopupAnchorView(mEditText);
            mContactLoader.setOnContactSelectedListener(this);
        }
    }

    public enum ContactType {
        PHONE_NUMBER,
        EMAIL
    }
}
