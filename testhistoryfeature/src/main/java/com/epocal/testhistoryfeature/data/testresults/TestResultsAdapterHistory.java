package com.epocal.testhistoryfeature.data.testresults;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.types.ResultStatus;
import com.epocal.common.types.TestErrorCode;
import com.epocal.common.types.am.TestMode;
import com.epocal.common_ui.testresults.BaseTestResultsAdapter;
import com.epocal.common_ui.testresults.IGroupedTestResultDataItem;
import com.epocal.testhistoryfeature.R;
import com.epocal.testhistoryfeature.data.patienttestdata.THDataHelper;


/**
 * The adapter which displays the test results in Details Screen Tab 1 - Test Results.
 *
 * This adapter extends from BaseTestResultsAdapter in common_ui module and
 * share the same screen layout with epoctestprocedure module.
 *
 * @see com.epocal.testhistoryfeature.ui.TestHistoryDetailsActivity
 */

public class TestResultsAdapterHistory extends BaseTestResultsAdapter {

    public TestResultsAdapterHistory(Context context, TestRecord testRecord) {
        super(context, testRecord);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        // Item can be Section Header or TestResult item.
        IGroupedTestResultDataItem item = (IGroupedTestResultDataItem) this.getItem(position);

        // Inflate the correct XMLs
        if (position == 0) {
            // Inflate the header XML at position 0
            view = layoutInflater.inflate(R.layout.test_results_header_test_history, parent, false);
            bindHeaderRow(view, mTestRecord);
        } else if (position > 0) { // inflate the test result XML for positions greater than 0
            if (item.isSectionHeader()) {
                view = layoutInflater.inflate(R.layout.test_result_section_header, parent, false);
            } else {
                view = layoutInflater.inflate(R.layout.test_result_item, parent, false);
            }
        }

        // Fill in the list view items
        if (view != null) {
            if (position > 0) {
                if (item.isSectionHeader()) {
                    TextView tvSectionHeader = view.findViewById(R.id.tv_test_result_section_header);
                    tvSectionHeader.setText(item.getTitle());
                } else {
                    TextView tvAnalyteName = view.findViewById(R.id.text_view_analyte_name);
                    TextView tvAnalyteValue = view.findViewById(R.id.text_view_analyte_value);
                    TextView tvAnalyteUnit = view.findViewById(R.id.text_view_analyte_unit);
                    TextView tvReferenceLow = view.findViewById(R.id.text_view_reference_low);
                    TextView tvReferenceHigh = view.findViewById(R.id.text_view_reference_high);
                    ImageView imageViewBlueBar = view.findViewById(R.id.image_view_blue_bar);
                    LinearLayout llBlueBar = view.findViewById(R.id.ll_bar_graph);
                    LinearLayout imageViewBgLayout = view.findViewById(com.epocal.common_ui.R.id.image_view_background);

                    int resultIndex = item.getTestResultIndex();
                    mPresenter.addResultsItems(resultIndex, imageViewBlueBar, tvAnalyteName, tvAnalyteValue, tvAnalyteUnit, tvReferenceLow, tvReferenceHigh, llBlueBar, imageViewBgLayout); // Because the ListView has one extra header element, so the correct id match for the TestResult items is the ListView position minus one.
                }
            }
        }

        return view;
    }

    private void bindHeaderRow(View rootView, TestRecord record) {
        // 1. Get view references
        TextView datetimeTextView = rootView.findViewById(R.id.tv_datetime);
        TextView operatorTextView = rootView.findViewById(R.id.tv_op_name);
        TextView syncStateTextView = rootView.findViewById(R.id.tv_sync_state);
        TextView testStatusTextView = rootView.findViewById(R.id.tv_status);

        // 2. Set text values
        datetimeTextView.setText(THDataHelper.formatDateTime(record.getTestDateTime()));
        String operator = String.valueOf(mContext.getResources().getText(R.string.operator)) +
                ' ' + THDataHelper.getDisplayUserId(mContext, record);
        operatorTextView.setText(operator);

        if (THDataHelper.isTestRecordSent(record)) {
            syncStateTextView.setText(R.string.sent);
        } else {
            syncStateTextView.setText(R.string.unsent);
        }

        if ((record.getTestErrorCode() != null) &&
            (record.getTestErrorCode() != TestErrorCode.NoError))
        {
            // TODO: Need to display localized error code string.
            testStatusTextView.setText(mContext.getString(R.string.test_error_code, record.getTestErrorCode().value));
            testStatusTextView.setVisibility(View.VISIBLE);
        } else if (record.getRejected()) {
            testStatusTextView.setText(R.string.test_rejected);
            testStatusTextView.setVisibility(View.VISIBLE);
        } else {
            testStatusTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void drawAnalyteBar(ImageView imageView, LinearLayout.LayoutParams layoutParams, LinearLayout imageViewBgLayout, ResultStatus resultStatus) {
        super.drawAnalyteBar(imageView, layoutParams, imageViewBgLayout, resultStatus);
        if (mTestRecord.getTestMode() == TestMode.QA) {
            super.drawQATestAnalyteBar(imageView, imageViewBgLayout, isCriticalResult(resultStatus));
        } else {
            super.drawPatientTestAnalyteBar(imageView, imageViewBgLayout, resultStatus);
        }
    }
}