package com.epocal.common_ui.testresults;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.types.ResultStatus;
import com.epocal.common_ui.R;

/**
 * The adapter which displays the test results
 */

public class TestResultsAdapterPatientTest extends BaseTestResultsAdapter {

    private LinearLayout mLayoutCriticalResults;
    private AppCompatButton mButtonDocumentResults, mButtonCloseAndTransmit;
    private ITestResultPanelListener mCallback;
    private boolean mDocumentResultClicked = false;

    public TestResultsAdapterPatientTest(Context context, TestRecord testRecord, ITestResultPanelListener callback) {
        super(context, testRecord);
        mCallback = callback;
    }

    private void updateTitleHeader(TextView message0) {
        if (mDocumentResultClicked && message0 != null) {
            if (message0 != null)
                message0.setText(mContext.getString(R.string.document_critical_results));
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        // Item can be Section Header or TestResult item.
        IGroupedTestResultDataItem item = (IGroupedTestResultDataItem) this.getItem(position);

        // Inflate the correct XMLs
        if (position == 0) {
            // Inflate the header XML at position 0
            view = layoutInflater.inflate(R.layout.test_results_header_patient_test, parent, false);
            mLayoutCriticalResults = view.findViewById(R.id.critical_results_layout);
            mButtonDocumentResults = (AppCompatButton) view.findViewById(R.id.button_document_results);
            mButtonCloseAndTransmit = (AppCompatButton) view.findViewById(R.id.button_close_and_transmit);
            final TextView message0 = view.findViewById((R.id.message_0));
            mButtonDocumentResults.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hasCriticalResults()) {
                        mDocumentResultClicked = true;
                        updateTitleHeader(message0);
                    }
                    mCallback.onDocumentButtonClick();
                }
            });
            mButtonCloseAndTransmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onCloseTransmitButtonClick();
                }
            });
            updateTitleHeader(message0);
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

    /**
     * Show and hide the message and also change the color of the button to red or blue respectively
     *
     * @param show the boolean flag to show or hide the message
     */
    public void showCriticalResultsMessage(boolean show) {
        if (mLayoutCriticalResults != null && mButtonDocumentResults != null) {
            if (show) {
                mLayoutCriticalResults.setVisibility(View.VISIBLE);
                mButtonDocumentResults.setBackgroundColor(mContext.getResources().getColor(R.color.colorRed));
            } else {
                mLayoutCriticalResults.setVisibility(View.GONE);
                mButtonDocumentResults.setBackgroundColor(mContext.getResources().getColor(R.color.primaryBlueNew));
            }
        }
    }

    @Override
    public void drawAnalyteBar(ImageView imageView, LinearLayout.LayoutParams layoutParams, LinearLayout imageViewBgLayout, ResultStatus resultStatus) {
        super.drawAnalyteBar(imageView, layoutParams, imageViewBgLayout, resultStatus);
        super.drawPatientTestAnalyteBar(imageView, imageViewBgLayout, resultStatus);
    }
}