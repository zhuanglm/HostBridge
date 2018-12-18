package com.epocal.testhistoryfeature.ui.fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.testhistoryfeature.data.testresults.TestResultsAdapterHistory;
import com.epocal.common_ui.testresults.TestResultsListView;

/**
 * This class represents Details Screen Tab 1 to display the Test Result.
 *
 * @since 2018-10-06
 *
 * @see com.epocal.testhistoryfeature.ui.TestHistoryDetailsActivity
 */
public class THDetailsTestResultsFragment extends Fragment {
    private static final String TAG = THDetailsTestResultsFragment.class.getSimpleName();

    Context mContext;
    TestRecord mTestRecord;
    TestResultsListView mListView;
    TestResultsAdapterHistory mTestResultsAdapter;

    ITHDetailsFragmentListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        // This view will inflate the common_ui layout file.
        View view = inflater.inflate(com.epocal.common_ui.R.layout.listview_test_results, container, false);
        mListView = view.findViewById(com.epocal.common_ui.R.id.list_view_test_results);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup data adapter to display the content
        mTestRecord = mCallback.getTestRecord();
        updateTestResults(mTestRecord);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        try {
            mCallback = (ITHDetailsFragmentListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(TAG + " Parent Activity must implement THDetailsFragmentLilstener interface.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
        mCallback = null;
        mTestRecord = null;
    }

    public void updateTestResults(TestRecord testRecord) {
        Log.v(TAG, "->->-> Updating the results");

        if (testRecord != null) {
            if (testRecord.getTestResults() != null) {
                // Each time the testResult is sent, it is treated as a new testResult (not an update to the existing one)
                // New data adapter is created to reset the data to redraw the list
                mTestResultsAdapter = new TestResultsAdapterHistory(mContext, testRecord);
                mListView.setAdapter(mTestResultsAdapter);
            }
        }
    }
}
