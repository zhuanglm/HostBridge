package com.epocal.common_ui.testresults;

/**
 * Created by ybrunet on 2017-12-05.
 */

public class GroupedTestResultDataItem implements IGroupedTestResultDataItem {
    private int mResultIndex = -1;

    public GroupedTestResultDataItem(int resultIndex) {
        this.mResultIndex = resultIndex;
    }

    @Override
    public boolean isListHeader() {
        return false;
    }

    @Override
    public boolean isSectionHeader() {
        return false;
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public int getTestResultIndex() {
        return mResultIndex;
    }
}
