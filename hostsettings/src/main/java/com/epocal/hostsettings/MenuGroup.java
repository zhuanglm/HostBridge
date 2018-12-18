package com.epocal.hostsettings;

import java.util.List;

/**
 * MenuGroup is used to hold the header row data for ExpandedListView.
 *
 * Created on 08/06/2017.
 */

public class MenuGroup {

    private String mTitle;
    private boolean isEnabled;
    private List<MenuItem> mChildMenuList;

    public MenuGroup(String title, List<MenuItem> childMenuList) {
        mTitle = title;
        mChildMenuList = childMenuList;
        isEnabled = true;
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

    public List<MenuItem> getChildMenuList() {
        return mChildMenuList;
    }

    public void setChildMenuList(List<MenuItem> childMenuList) {
        this.mChildMenuList = childMenuList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("MenuGroup: title=%s numOfChildren=%d isEnabled=%b\n", mTitle, mChildMenuList.size(), isEnabled));
        for (int i = 0; i < getChildMenuList().size(); i++) {
            sb.append("  --"+getChildMenuList().get(i).toString());
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}
