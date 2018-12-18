package com.epocal.readersettings;

/**
 * MenuItem is used to hold the child row data for ExpandedListView.
 * Each item represent a Menu and the user binds click handler for the view holding menu data.
 *
 * mValue -- is optional and can be null.
 * <p>
 * Created on 01/15/2017.
 * <p>
 * Based on class in hostsettings
 */

public class MenuItem {
    private int mItemId;
    private String mTitle;
    private String mValue;
    private boolean isEnabled;

    public MenuItem(int id, String title) {
        mItemId = id;
        mTitle = title;
        mValue = null;
        isEnabled = true;
    }

    public MenuItem(int id, String title, String value) {
        this(id, title);
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

    public String toString() {
        return String.format("Menu: title=%s, value=%s  isEnabled=%b", mTitle, mValue, isEnabled);
    }
}
