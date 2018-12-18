package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings;

import java.util.List;

/**
 * FieldSettingsMenuGroup is used to hold the header row data for ExpandedListView.
 *
 * Created on 08/06/2017.
 */

public class FieldSettingsMenuGroup {

    private int mGroupId;
    private String mTitle;
    private boolean isEnabled;
    private List<FieldSettingsMenuItem> mChildMenuList;

    public FieldSettingsMenuGroup(String title, List<FieldSettingsMenuItem> childMenuList) {
        mTitle = title;
        mChildMenuList = childMenuList;
        isEnabled = true;
    }

    public FieldSettingsMenuGroup(String title, List<FieldSettingsMenuItem> childMenuList, int groupId) {
        mGroupId = groupId;
        mTitle = title;
        mChildMenuList = childMenuList;
        isEnabled = true;
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

    public List<FieldSettingsMenuItem> getChildMenuList() {
        return mChildMenuList;
    }

    public void setChildMenuList(List<FieldSettingsMenuItem> childMenuList) {
        this.mChildMenuList = childMenuList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("FieldSettingsMenuGroup: title=%s numOfChildren=%d isEnabled=%b\n", mTitle, mChildMenuList.size(), isEnabled));
        for (int i = 0; i < getChildMenuList().size(); i++) {
            sb.append("  --"+getChildMenuList().get(i).toString());
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}
