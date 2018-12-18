package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings;

/**
 * FieldSettingsMenuItem is used to hold the child row data for ExpandedListView.
 * Each item represent a Menu and the user binds click handler for the view holding menu data.
 *
 * mValue -- is optional and can be null.
 * Created on 08/06/2017.
 */

class FieldSettingsMenuItem {
    private int mItemId;
    private String mTitle;
    private String mValue;
    private boolean isEnabled;

    FieldSettingsMenuItem(int id, String title) {
        mItemId = id;
        mTitle = title;
        mValue = null;
        isEnabled = true;
    }

    long getItemId() {
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

    public String toString() {
        return String.format("Menu: title=%s, value=%s  isEnabled=%b", mTitle, mValue, isEnabled);
    }
}
