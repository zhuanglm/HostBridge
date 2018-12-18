package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.testselection;

import java.util.ArrayList;
import java.util.List;

/**
 * TestSelectionGroupItem is used to hold the header row data for ExpandedListView.
 *
 * Created on 08/06/2017.
 */

public class TestSelectionGroupItem {

    private int mGroupId;
    private String mTitle;
    private boolean isEnabled;
    private int mSelected;
    private int mEnablement;

    private ArrayList<TestSelectionChildItem> mChildMenuList;
    TestSelectionGroupItem(String title, ArrayList<TestSelectionChildItem> childMenuList, int enablement) {
        mTitle = title;
        mChildMenuList = childMenuList;
        isEnabled = true;
        mEnablement = enablement;
    }

    public int getEnablement() {
        return mEnablement;
    }

    int getGroupId() {
        return mGroupId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public List<TestSelectionChildItem> getChildMenuList() {
        return mChildMenuList;
    }

    public void setChildMenuList(ArrayList<TestSelectionChildItem> childMenuList) {
        this.mChildMenuList = childMenuList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("TestSelectionGroupItem: title=%s numOfChildren=%d isEnabled=%b\n", mTitle, mChildMenuList.size(), isEnabled));
        for (int i = 0; i < getChildMenuList().size(); i++) {
            sb.append("  --"+getChildMenuList().get(i).toString());
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}
