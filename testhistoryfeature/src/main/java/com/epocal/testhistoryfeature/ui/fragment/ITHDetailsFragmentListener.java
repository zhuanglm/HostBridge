package com.epocal.testhistoryfeature.ui.fragment;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.testhistoryfeature.THDetailsContract;

/**
 * This interface is to communicate between TestHistoryDetails Tab Fragments to TestHistoryDetails Activity.
 * The TestRecord data is held by the Activity and all tab fragments calls this method to obtain the parent's
 * Activity's TestRecord data when it is ready to draw UI.
 *
 * @since 2018-10-06
 *
 * <h4>Interface Usage</h4>
 *
 * <p>
 *     <b>getTestRecords():</b><br />
 *     The TestRecord List is held by the Activity and the fragment calls this method to obtain
 *     the parent's Activity's TestRecord data when it is ready to draw UI.
 * </p>
 *
 * <p>
 *     <b>getDetailsModel():</b><br />
 *     Model object is held by the Activity and the fragment calls this method to obtain
 *     Model object to retrieve TestRecord's related data (e.g) Workflow.
 * </p>
 *
 * @see com.epocal.testhistoryfeature.ui.TestHistoryDetailsActivity
 */
public interface ITHDetailsFragmentListener {
    TestRecord getTestRecord();
    THDetailsContract.Model getDetailsModel();
}
