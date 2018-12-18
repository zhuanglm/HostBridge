package com.epocal.testhistoryfeature.data.patienttestdata;

import com.epocal.common_ui.list.ISelectableListItem;
import com.epocal.common_ui.list.ListItemSectionHeader;

import static com.epocal.testhistoryfeature.data.patienttestdata.TestRecordListItemHeader.ChildSelectionState.ALL;

/**
 * This class reprensents the Model object for the header row in THGroupedDataAdapter.
 */
public class TestRecordListItemHeader extends ListItemSectionHeader implements ISelectableListItem {
    public enum ChildSelectionState { ALL, NONE, PARTIAL }
    private boolean isSelected = false;
    private int mNumberOfChildren = 0;
    private ChildSelectionState mChildSelectionState = ChildSelectionState.NONE;
    private int mDateRangeValue; // a value representing DateRange type.

    TestRecordListItemHeader(String title, int dateRangeValue) {
        super(title);
        this.mDateRangeValue = dateRangeValue;
    }

    TestRecordListItemHeader(String title, boolean selected, int noOfChildren, int dateRangeValue) {
        this(title, dateRangeValue);
        this.isSelected = selected;
        this.mNumberOfChildren = noOfChildren;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    int getNumberOfChildren() {
        return mNumberOfChildren;
    }

//    public void setNumberOfChildren(int noOfChildren) {
//        mNumberOfChildren = noOfChildren;
//    }

    ChildSelectionState getChildSelectionState() {
        return mChildSelectionState;
    }

    void setChildSelectionState(ChildSelectionState childSelectionState) {
        this.mChildSelectionState = childSelectionState;
        // Child's selection state changes header's selection state as follows:
        isSelected = (childSelectionState == ALL);
    }

    int getDateRangeValue() {
        return mDateRangeValue;
    }
}
