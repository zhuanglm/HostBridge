package com.epocal.testhistoryfeature.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.epocal.common.realmentities.EqcInfo;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.testhistoryfeature.R;
import com.epocal.testhistoryfeature.THDetailsContract;
import com.epocal.testhistoryfeature.data.statusdata.TestStatusDataAdapter;

/**
 * This class represents Details Screen Tab 3 to display the Status
 *
 * @since 2018-10-06
 *
 * @see com.epocal.testhistoryfeature.ui.TestHistoryDetailsActivity
 */
public class THDetailsTestQAStatusFragment extends Fragment {
    private static final String TAG = THDetailsTestQAStatusFragment.class.getSimpleName();
    private Context mContext;
    private TestRecord mTestRecord;
    private THDetailsContract.Model mDataModel;
    private ITHDetailsFragmentListener mCallback;
    private ExpandableListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_status, container, false);

        mListView = view.findViewById(R.id.exp_list_view);
        mListView.setDividerHeight(0);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup data adapter to display the content
        mTestRecord = mCallback.getTestRecord();
        mDataModel = mCallback.getDetailsModel();
        EqcInfo eqcInfo = mDataModel.fetchEqc();

        TestStatusDataAdapter adapter = new TestStatusDataAdapter(mContext, mTestRecord, eqcInfo);
        mListView.setAdapter(adapter);
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
