package com.epocal.testhistoryfeature.data.patienttestdata;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common_ui.list.ISelectableListItem;

/**
 * This class represents the Model object for the child row in THGroupedDataAdapter.
 */
public class TestRecordListItem implements ISelectableListItem {
    private TestRecord mTestRecord;
    private boolean isSelected;

    private TestRecordListItem(TestRecord testRecord) {
        this.mTestRecord = testRecord;
        this.isSelected = false;
    }

    TestRecordListItem(TestRecord testRecord, boolean selected) {
        this(testRecord);
        this.isSelected = selected;
    }

    @Override
    public boolean isSectionHeader() {
        return false;
    }

    @Override
    public String getTitle() {
        return mTestRecord.getSubjectId();
    }

    @Override
    public String getValue() {
        return "";
    }

    public long getId() {
        return mTestRecord.getId();
    }

    public TestRecord getTestRecord() {
        return mTestRecord;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
