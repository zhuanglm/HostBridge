package com.epocal.common_ui.testresults;

/**
 * Created by ybrunet on 2017-12-05.
 */

public class GroupedTestResultListHeaderDataItem implements IGroupedTestResultDataItem {
    @Override
    public boolean isListHeader() {
        return true;
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
        return 0;
    }
}
