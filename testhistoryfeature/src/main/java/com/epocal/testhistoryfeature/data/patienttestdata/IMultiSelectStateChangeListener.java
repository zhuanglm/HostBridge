package com.epocal.testhistoryfeature.data.patienttestdata;

import java.util.List;

/**
 * Callback interface to communicate between the Recycler View (in Test Selection Screen)
 * to Test Select Screen (THMainMultiSelectListFragment).
 *
 * <p>
 * On Test Select Screen, when the user taps a row of Recycler view to change the row selection
 * state, the parent (Test selection) Screen needs to be informed of newly selected id(s) as
 * it needs to update the selection status in the ActionBar and is able to apply the bulk actions
 * to selection list.
 * </p>
 */
public interface IMultiSelectStateChangeListener {
    void onSelectonStateChanged(List<Long> selectedItemIdList);
}
