package com.epocal.testhistoryfeature.ui.fragment;

import com.epocal.common.realmentities.TestRecord;
import java.util.List;

import io.realm.Realm.Transaction.OnSuccess;
import io.realm.Realm.Transaction.OnError;
import io.realm.RealmResults;
import com.epocal.testhistoryfeature.data.type.ActionType;

/**
 * This interface is used to communicate between THMainMultiSelectList Fragment to
 * TestHistoryMain Activity.
 *
 * @since 2018-10-06
 *
 * <h4>Interface Usage</h4>
 *
 * <p>
 * <b>getTestRecords():</b><br />
 * The TestRecord List is held by the Activity and the fragment calls this method to obtain
 * the parent's Activity's TestRecord data when it is ready to draw UI.
 * </p>
 *
 * <p>
 * <b>onFragmentClose():</b><br />
 * Called when THMainMultiSelectListFragment is dismissed.
 * </p>
 *
 * <p>
 * <b>onActionSelect():</b><br />
 * Called when THMainMultiSelectListFragment the option menu (Mark as Unsent, Mark as Sent,
 * or Delete) is selected.
 * </p>
 *
 * @see com.epocal.testhistoryfeature.ui.TestHistoryMainActivity
 * @see THMainMultiSelectListFragment
 *
 */
public interface ITHMainFragmentListener {
    RealmResults<TestRecord> getTestRecords();
    void onFragmentClose();
    void onActionSelect(ActionType actionType, List<Long> idList, OnSuccess onSuccess, OnError onError);
}
