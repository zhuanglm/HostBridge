package com.epocal.common_ui.testresults;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * The custom list to display Patient and QA Test Results.
 * To use this class, place the list in the layout file as follows:
 *
 *     <com.epocal.common_ui.testresults.TestResultsListView
 *          android:id="@+id/list_view_test_results"
 *          android:layout_width="match_parent"
 *          android:layout_height="wrap_content" />
 *
 * This list takes the custom adapter which transforms the TestRecord
 * data into list data.
 *
 * There are two variations of adapter provided:
 * -- TestResultsAdapterHistory -- the list has no callback buttons. used by
 *    test history feature module.
 * -- TestResultsAdapterPatientTest -- the list has several buttons for
 *    callbacks. used by epoc test procedure module.
 *
 */
public class TestResultsListView extends ListView {
    public TestResultsListView(Context context) {
        super(context);
    }

    public TestResultsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestResultsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestResultsListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setAdapter(BaseTestResultsAdapter adapter) {
        super.setAdapter(adapter);
    }
}
