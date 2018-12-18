package com.epocal.testhistoryfeature.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.WorkFlow;
import com.epocal.common.types.am.TestMode;
import com.epocal.testhistoryfeature.R;
import com.epocal.testhistoryfeature.THDetailsContract;
import com.epocal.testhistoryfeature.data.patientdata.PatientTestInputDataAdapter;
import com.epocal.testhistoryfeature.data.qadata.QATestInputDataAdapter;

/**
 * This class represents Details Screen Tab 2 to display the user input data entered
 * at the time of running the test.
 *
 * @since 2018-10-06
 *
 * @see com.epocal.testhistoryfeature.ui.TestHistoryDetailsActivity
 */
public class THDetailsTestInputDataFragment extends Fragment {
    private static final String TAG = THDetailsTestInputDataFragment.class.getSimpleName();
    private Context mContext;
    private TestRecord mTestRecord;
    private RecyclerView mListView;
    private THDetailsContract.Model mDataModel;
    private ITHDetailsFragmentListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_input_data, container, false);

        mListView = view.findViewById(R.id.list_view_input_data);
        mListView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(layoutManager);
//        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        mListView.addItemDecoration(decoration);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup data adapter to display the content
        mTestRecord = mCallback.getTestRecord();
        mDataModel = mCallback.getDetailsModel();
        WorkFlow workFlow = mDataModel.fetchWorkFlow(mTestRecord.getWorkflowItems());

        if (mTestRecord.getTestMode() == TestMode.BloodTest) {
            // Patient Test
            PatientTestInputDataAdapter dataAdapter = new PatientTestInputDataAdapter(mContext, mTestRecord, workFlow);
            mListView.setAdapter(dataAdapter);
        } else {
            // QA Test
            QATestInputDataAdapter dataAdapter = new QATestInputDataAdapter(mContext, mTestRecord);
            mListView.setAdapter(dataAdapter);
        }
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
        mDataModel = null;
        mTestRecord = null;
    }

}
