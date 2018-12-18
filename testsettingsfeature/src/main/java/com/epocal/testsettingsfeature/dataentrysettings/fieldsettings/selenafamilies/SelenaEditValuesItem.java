package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.selenafamilies;

import com.epocal.common.types.SelenaFamilyType;

/**
 * TestSelectionChildItem is used to hold the child row data for ExpandedListView.
 * Each item represent a Menu and the user binds click handler for the view holding menu data.
 *
 * mValue -- is optional and can be null.
 * Created by Zeeshan A Zakaria on 09/08/2017.
 */

class SelenaEditValuesItem {


    private SelenaFamilyType mSelenafamilyType;
    private int mAnalyteId;
    private String mTitle;
    private String mValue;
    private boolean mIsEnabled;
    private Integer mEnablement;

    SelenaEditValuesItem(String title, int enablement, SelenaFamilyType selenafamilyType) {
        mAnalyteId = 0;
        mSelenafamilyType = selenafamilyType;
        mTitle = title;
        mEnablement = enablement;
        mValue = null;
        mIsEnabled = true;
    }

    public SelenaFamilyType getSelenafamilyType() {
        return mSelenafamilyType;
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