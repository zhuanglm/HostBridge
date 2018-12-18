package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.testselection;

/**
 * TestSelectionChildItem is used to hold the child row data for ExpandedListView.
 * Each item represent a Menu and the user binds click handler for the view holding menu data.
 *
 * mValue -- is optional and can be null.
 * Created on 08/06/2017.
 */

class TestSelectionChildItem {
    private int mItemId;
    private int mAnalyteId;
    private String mTitle;
    private String mValue;
    private boolean mIsEnabled;
    private Integer mEnablement;

    TestSelectionChildItem(int id, String title, int enablement, int analyteId) {
        mAnalyteId = analyteId;
        mItemId = id;
        mTitle = title;
        mEnablement = enablement;
        mValue = null;
        mIsEnabled = true;
    }

    Integer getEnablement() {
        return mEnablement;
    }

    int geAnalyteId() {
        return mAnalyteId;
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
        return mIsEnabled;
    }

    public void setEnabled(boolean enabled) {
        mIsEnabled = enabled;
    }

    public String toString() {
        return String.format("Menu: title=%s, value=%s  mIsEnabled=%b", mTitle, mValue, mIsEnabled);
    }
}