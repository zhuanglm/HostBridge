package com.epocal.epoctestprocedure.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.types.am.TestMode;
import com.epocal.common_ui.testresults.ITestResultPanelListener;
import com.epocal.common_ui.testresults.TestResultsAdapterPatientTest;
import com.epocal.common_ui.testresults.TestResultsAdapterQATest;
import com.epocal.common_ui.testresults.TestResultsListView;
import com.epocal.epoctestprocedure.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestResultsFragment extends Fragment implements ITestResultPanelListener {
    private static final String TAG = TestResultsFragment.class.getSimpleName();

    TestRecord mTestRecord;
    Context mContext;
    TestResultsListView mListView;
    private TestResultsFragment.OnTestResultSelectedListener mCallback;
    private TestMessageAndInstructionFragment.OnMessagePanelSelectedListener mCallback2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        // This view will inflate the common_ui layout file.
        View view = inflater.inflate(R.layout.fragment_test_results, container, false);
        mListView = (TestResultsListView) view.findViewById(com.epocal.common_ui.R.id.list_view_test_results);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup data adapter to display the content
        updateTestResults();
    }

    public void updateTestResults() {
        Log.v("Host4", "->->-> Updating the results");

        mTestRecord = mCallback.getTestRecord();
        if ((mTestRecord != null) && (mTestRecord.getTestResults() != null)) {
            // Each time the testResult is sent, it is treated as a new testResult (not an update to the existing one)
            // New data adapter is created to reset the data to redraw the list
            if (mTestRecord.getTestMode() == TestMode.QA) {
                mListView.setAdapter(new TestResultsAdapterQATest(getActivity(), mTestRecord, this));
            } else {
                mListView.setAdapter(new TestResultsAdapterPatientTest(getActivity(), mTestRecord, this));
            }
        }
    }

    private void verifyCallbackSupport(Context context) {
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        if ( (context instanceof TestMessageAndInstructionFragment.OnMessagePanelSelectedListener) &&
                (context instanceof  OnTestResultSelectedListener) )
        {
            mCallback2 = (TestMessageAndInstructionFragment.OnMessagePanelSelectedListener) context;
            mCallback = (OnTestResultSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnTestResultSelectedListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach()");
        super.onAttach(context);
        verifyCallbackSupport(context);
    }

    //Android 23 and below uses this onAttach signature
    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach()");
        super.onAttach(activity);
        verifyCallbackSupport(activity);
    }
    //////////////////////////////
    // ITestResultPanelListener
    /////////////////////////////
    @Override
    public void onDocumentButtonClick() {
        mCallback2.openDocumentResultsStep();
    }

    @Override
    public void onCloseTransmitButtonClick() {
        mCallback2.closeActivity();
    }

    @Override
    public void onListRowClick() {
        mCallback.onTestResultSelected();
    }

    // Container Activity must implement this interface
    public interface OnTestResultSelectedListener {
        void onTestResultSelected();
        TestRecord getTestRecord();
    }
}

