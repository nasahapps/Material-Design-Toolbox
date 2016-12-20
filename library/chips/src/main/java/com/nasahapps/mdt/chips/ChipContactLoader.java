package com.nasahapps.mdt.chips;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.Query;
import com.nasahapps.mdt.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhasan on 12/20/16.
 */

public class ChipContactLoader extends Fragment {

    private static final String EXTRA_TYPE = "type";
    private RecyclerView mRecyclerView;
    private View mPopupAnchorView;
    private PopupWindow mPopupWindow;
    private List<Contact> mContacts = new ArrayList<>();
    private ChipLayout.ContactType mContactType;
    private ContactAdapter.OnContactSelectedListener mOnContactSelectedListener;

    /**
     * Returns a new ChipContactLoader, or if one already exists within the given FragmentManger, returns
     * that instance
     */
    public static ChipContactLoader getInstance(FragmentManager fm, ChipLayout.ContactType type) {
        ChipContactLoader loader = (ChipContactLoader) fm.findFragmentByTag(ChipContactLoader.class.getSimpleName());
        if (loader != null) {
            return loader;
        } else {
            Bundle args = new Bundle();
            args.putSerializable(EXTRA_TYPE, type);
            loader = new ChipContactLoader();
            loader.setArguments(args);
            fm.beginTransaction()
                    .add(0, loader, ChipContactLoader.class.getSimpleName())
                    .commit();
            return loader;
        }
    }

    // Since this is a headless fragment, we'll just return a null view. We're creating a ListView
    // that will be in a PopupWindow anchored to whatever called upon this Fragment.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContactType = (ChipLayout.ContactType) getArguments().getSerializable(EXTRA_TYPE);

        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new ContactAdapter(mContacts, mOnContactSelectedListener));

        mPopupWindow = new PopupWindow(mRecyclerView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPopupWindow.setElevation(getResources().getDimensionPixelSize(R.dimen.mdt_chip_open_layout_elevation));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            mPopupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Utils.isInNightMode(getActivity()) ? Color.BLACK : Color.WHITE));
        mPopupWindow.setOutsideTouchable(true);

        return null;
    }

    public void showPopup() {
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAsDropDown(mPopupAnchorView);
        }
    }

    public void dismissPopup() {
        mPopupWindow.dismiss();
    }

    public void setPopupAnchorView(View popupAnchorView) {
        mPopupAnchorView = popupAnchorView;
    }

    public void setQuery(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            dismissPopup();
        } else {
            Query query;
            if (mContactType == ChipLayout.ContactType.PHONE_NUMBER) {
                query = Contacts.getQuery().hasPhoneNumber().whereContains(Contact.Field.PhoneNumber, text);
            } else {
                query = Contacts.getQuery().whereContains(Contact.Field.Email, text);
            }

            mContacts.clear();
            mContacts.addAll(query.find());
            mRecyclerView.getAdapter().notifyDataSetChanged();

            if (mRecyclerView.getAdapter().getItemCount() == 0) {
                dismissPopup();
            } else {
                showPopup();
            }
        }
    }

    public void setOnContactSelectedListener(ContactAdapter.OnContactSelectedListener onContactSelectedListener) {
        mOnContactSelectedListener = onContactSelectedListener;
        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            ((ContactAdapter) mRecyclerView.getAdapter()).setOnContactSelectedListener(onContactSelectedListener);
        }
    }

    static class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

        private List<Contact> mContacts;
        private OnContactSelectedListener mListener;

        ContactAdapter(List<Contact> contacts) {
            this(contacts, null);
        }

        ContactAdapter(List<Contact> contacts, OnContactSelectedListener listener) {
            mContacts = contacts;
            mListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mdt_list_chip_contact, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Contact contact = mContacts.get(position);

            if (!TextUtils.isEmpty(contact.getPhotoUri())) {
                holder.pictureIcon.setVisibility(View.VISIBLE);
                // First set the image URI to get the drawable
                holder.pictureIcon.setImageURI(Uri.parse(contact.getPhotoUri()));
                // Then use that same drawable and round it
                holder.pictureIcon.setImageDrawable(Utils.getRoundedDrawable(holder.pictureIcon.getContext(),
                        holder.pictureIcon.getDrawable()));
            } else {
                holder.pictureIcon.setVisibility(View.GONE);
            }

            holder.firstLineText.setText(contact.getDisplayName());
            if (!contact.getEmails().isEmpty()) {
                holder.secondLineText.setText(contact.getEmails().get(0).getAddress());
            }
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }

        public void setOnContactSelectedListener(OnContactSelectedListener listener) {
            mListener = listener;
        }

        public interface OnContactSelectedListener {
            void onContactSelected(Contact contact);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView pictureIcon;
            TextView firstLineText, secondLineText;

            ViewHolder(View v) {
                super(v);
                pictureIcon = (ImageView) v.findViewById(R.id.pictureIcon);
                firstLineText = (TextView) v.findViewById(R.id.firstLine);
                secondLineText = (TextView) v.findViewById(R.id.secondLine);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onContactSelected(mContacts.get(getAdapterPosition()));
                        }
                    }
                });
            }
        }
    }
}
