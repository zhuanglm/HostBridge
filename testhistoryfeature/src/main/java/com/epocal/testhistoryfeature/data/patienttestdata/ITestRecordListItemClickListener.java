package com.epocal.testhistoryfeature.data.patienttestdata;

import com.epocal.common.realmentities.TestRecord;

/**
 * Callback interface of the row tap (single tap and long hold) of the list of Test Records
 * in Recycler View
 */
public interface ITestRecordListItemClickListener {
    void onClickListItem(TestRecord record);
    void onLongClickListItem(TestRecord record);
}
