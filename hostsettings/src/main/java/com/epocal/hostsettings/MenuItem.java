package com.epocal.hostsettings;

import com.epocal.common_ui.types.UIEditFieldType;

/**
 * MenuItem is used to hold the child row data for ExpandedListView.
 * Each item represent a Menu and the user binds click handler for the view holding menu data.
 *
 * mValue -- is optional and can be null.
 * Created on 08/06/2017.
 */

public class MenuItem {
    private int mItemId;
    private String mTitle;
    private String mValue;
    private boolean isEnabled;
    private boolean isChecked;
    private UIEditFieldType mUIEditFieldType = UIEditFieldType.Unknown;
    private boolean mEditable = false;

    public MenuItem(int id, String title, UIEditFieldType uiEditFieldType) {
        mItemId = id;
        mTitle = title;
        mValue = null;
        isEnabled = true;
        mUIEditFieldType = uiEditFieldType;
    }

    public MenuItem(int id, String title, String value, UIEditFieldType uiEditFieldType) {
        this(id, title, uiEditFieldType);
        mValue = value;
    }

    public long getItemId() {
        return mItemId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
    }

    public boolean hasValue() {
        return (mValue != null);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public UIEditFieldType getUIEditFieldType() {
        return mUIEditFieldType;
    }

    public boolean getEditable() {
        return mEditable;
    }

    public void setmEditable(boolean value) {
        this.mEditable = value;
    }

    public String toString() {
        return String.format("Menu: title=%s, value=%s  isEnabled=%b", mTitle, mValue, isEnabled);
    }
}
