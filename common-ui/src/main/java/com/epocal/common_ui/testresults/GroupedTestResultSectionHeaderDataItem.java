package com.epocal.common_ui.testresults;

/**
 * Created by ybrunet on 2017-12-05.
 */

public class GroupedTestResultSectionHeaderDataItem implements IGroupedTestResultDataItem {
    private String mTitle;

    public GroupedTestResultSectionHeaderDataItem(String sectionTitle) {
        this.mTitle = sectionTitle;
    }

    @Override
    public boolean isListHeader() {
        return false;
    }

    @Override
    public boolean isSectionHeader() {
        return true;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public int getTestResultIndex() {
        return -1;
    }
}
