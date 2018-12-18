package com.epocal.common_ui.testresults;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.TestResult;
import com.epocal.common.types.ResultStatus;
import com.epocal.common_ui.R;

import static com.epocal.common.types.ResultStatus.AboveReportableRange;
import static com.epocal.common.types.ResultStatus.BelowReportableRange;
import static com.epocal.common.types.ResultStatus.CriticalAboveReportableRange;
import static com.epocal.common.types.ResultStatus.CriticalBelowReportableRange;
import static com.epocal.common.types.ResultStatus.CriticalHigh;
import static com.epocal.common.types.ResultStatus.CriticalLow;
import static com.epocal.common.types.ResultStatus.ReferenceAboveReportableRange;
import static com.epocal.common.types.ResultStatus.ReferenceBelowReportableRange;
import static com.epocal.common.types.ResultStatus.ReferenceHigh;
import static com.epocal.common.types.ResultStatus.ReferenceLow;

public class TestResultsAdapterQATest extends BaseTestResultsAdapter {
    private LinearLayout mFailedResultsLayout;
    private AppCompatButton mButtonCloseAndTransmit;
    private ITestResultPanelListener mCallback;

    public TestResultsAdapterQATest(Context context, TestRecord testRecord, ITestResultPanelListener callback) {
        super(context, testRecord);
        mCallback = callback;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        // Item can be Section Header or TestResult item.
        IGroupedTestResultDataItem item = (IGroupedTestResultDataItem) this.getItem(position);

        // Inflate the correct XMLs
        if (position == 0) {
            // Inflate the header XML at position 0
            view = layoutInflater.inflate(R.layout.test_results_header_qa_test, parent, false);
            mFailedResultsLayout = view.findViewById(R.id.qc_failed_layout);
            TextView tvFailedResults = (TextView) view.findViewById(R.id.text_view_failed_results);
            if(tvFailedResults != null) {
                tvFailedResults.setText(mContext.getString(R.string.analytes_out_of_range, getCriticalResultCount()));
            }
            mButtonCloseAndTransmit = (AppCompatButton) view.findViewById(R.id.button_close_and_transmit);
            mButtonCloseAndTransmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onCloseTransmitButtonClick();
                }
            });
            showCriticalResultsMessage(hasCriticalResults());
        } else if (position > 0) { // inflate the test result XML for positions greater than 0
            if (item.isSectionHeader()) {
                view = layoutInflater.inflate(R.layout.test_result_section_header, parent, false);
            } else {
                view = layoutInflater.inflate(R.layout.test_result_item, parent, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onListRowClick();
                    }
                });
            }
        }

        // Fill in the list view items
        if (view != null) {
            if (position > 0) {
                if (item.isSectionHeader()) {
                    TextView tvSectionHeader = (TextView) view.findViewById(R.id.tv_test_result_section_header);
                    tvSectionHeader.setText(item.getTitle());
                } else {
                    TextView tvAnalyteName = (TextView) view.findViewById(R.id.text_view_analyte_name);
                    TextView tvAnalyteValue = (TextView) view.findViewById(R.id.text_view_analyte_value);
                    TextView tvAnalyteUnit = (TextView) view.findViewById(R.id.text_view_analyte_unit);
                    TextView tvReferenceLow = (TextView) view.findViewById(R.id.text_view_reference_low);
                    TextView tvReferenceHigh = (TextView) view.findViewById(R.id.text_view_reference_high);
                    ImageView imageViewBlueBar = (ImageView) view.findViewById(R.id.image_view_blue_bar);
                    LinearLayout llBlueBar = (LinearLayout) view.findViewById(R.id.ll_bar_graph);
                    LinearLayout imageViewBgLayout = (LinearLayout) view.findViewById(R.id.image_view_background);

                    //mPresenter.addResultsItems(position - 1, imageViewBlueBar, tvAnalyteName, tvAnalyteValue, tvAnalyteUnit, tvReferenceLow, tvReferenceHigh, llBlueBar); // Because the ListView has one extra header element, so the correct id match for the TestResult items is the ListView position minus one.

                    int resultIndex = item.getTestResultIndex();
                    //ResultModel resultModel = new ResultModel(mPresenter.getTestResult(resultIndex));
                    mPresenter.addResultsItems(resultIndex, imageViewBlueBar, tvAnalyteName, tvAnalyteValue, tvAnalyteUnit, tvReferenceLow, tvReferenceHigh, llBlueBar, imageViewBgLayout); // Because the ListView has one extra header element, so the correct id match for the TestResult items is the ListView position minus one.
                }
            }
        }

        return view;
    }

    @Override
    public boolean hasCriticalResults() {
        return getCriticalResultCount() > 0;
    }

    protected int getCriticalResultCount() {
        int count = 0;
        for (TestResult testResult : mTestRecord.getTestResults()) {
            if (isCriticalResult(testResult.getResultStatus())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Show and hide the message and also change the color of the button to red or blue respectively
     *
     * @param show the boolean flag to show or hide the message
     */
    public void showCriticalResultsMessage(boolean show) {
        if (mFailedResultsLayout != null) {
            if (show) {
                mFailedResultsLayout.setVisibility(View.VISIBLE);
            } else {
                mFailedResultsLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void drawAnalyteBar(ImageView imageView, LinearLayout.LayoutParams layoutParams, LinearLayout imageViewBgLayout, ResultStatus resultStatus) {
        super.drawAnalyteBar(imageView, layoutParams, imageViewBgLayout, resultStatus);

        drawQATestAnalyteBar(imageView, imageViewBgLayout, isCriticalResult(resultStatus));
    }
}
