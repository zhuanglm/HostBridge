package com.epocal.common_ui.testresults;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.TestResult;
import com.epocal.common.types.ResultStatus;

import static android.view.View.GONE;
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

/**
 * The adapter which takes in TestRecord object and displays the test results
 */

public abstract class BaseTestResultsAdapter extends BaseAdapter implements ITestResultsAdapterView {
    static private final String TAG = BaseTestResultsAdapter.class.getSimpleName();

    protected Context mContext;
    protected TestResultsAdapterPresenter mPresenter;
    protected TextView mTvCriticalResults;
    protected TestRecord mTestRecord;

    public BaseTestResultsAdapter(Context context, TestRecord testRecord) {
        mContext = context;
        mTestRecord = testRecord;
        mPresenter = new TestResultsAdapterPresenter(mTestRecord.getTestResults(), this, context);
    }

    @Override
    public int getCount() {
        return mPresenter.getGroupedTestResultsListSize();
    }

    @Override
    public Object getItem(int position) {
        return mPresenter.getGroupedTestResultDataItem(position);
    }

    @Override
    public long getItemId(int position) {
        if (position == 0) {
            return 0;
        } else {
            return mPresenter.getGroupedTestResultId(position);
        }
    }

    @Override
    abstract public View getView(int position, View view, ViewGroup parent);

    /**
     * This method populates the text view with the correct value of analyte or an error message.
     * This method uses SpannableString for proper formatting of the text.
     *
     * @param tv    the TextView
     * @param value the String value to populate the TextView with
     */
    @Override
    public void setTextViewText(TextView tv, SpannableString value) {
        tv.setText(value);
    }

    /**
     * This method populates the text view with the correct value of analyte or an error message.
     * This method uses simple String where no formatting is required.
     *
     * @param tv    the TextView
     * @param value the String value to populate the TextView with
     */
    @Override
    public void setTextViewText(TextView tv, String value) {
        tv.setText(value);
    }

    /**
     * This method draws the blue bar graph
     *
     * @param imageView    the image view which is used as the blue bar graph
     * @param layoutParams the parameters which decide how long the bar graph be
     */
    @Override
    public void drawAnalyteBar(ImageView imageView, LinearLayout.LayoutParams layoutParams, LinearLayout imageViewBgLayout, ResultStatus resultStatus) {
        imageView.setLayoutParams(layoutParams);
    }

    /**
     * Hide the blue bar altogether. This is needed when there is an error in the calculation.
     *
     * @param llBlueBar the LinearLayout which has the bar in it
     */
    @Override
    public void hideAnalyteBar(LinearLayout llBlueBar) {
        llBlueBar.setVisibility(GONE);
    }

    @Override
    public boolean hasCriticalResults() {
        boolean hasCriticalResults = false;
        for (TestResult testResult : mTestRecord.getTestResults()) {
            ResultStatus resultStatus = testResult.getResultStatus();
            if (resultStatus == ResultStatus.CriticalHigh || resultStatus == ResultStatus.CriticalLow) {
                hasCriticalResults = true;
                break;
            }
        }
        return hasCriticalResults;
    }

    public void drawPatientTestAnalyteBar(ImageView imageView, LinearLayout imageViewBgLayout, ResultStatus resultStatus) {
        if ( resultStatus == CriticalLow || resultStatus == CriticalHigh || resultStatus == CriticalBelowReportableRange || resultStatus == CriticalAboveReportableRange) {
            imageView.setBackgroundColor(Color.parseColor("#de1f00"));
            imageViewBgLayout.setBackgroundColor(Color.parseColor("#f3d7d2"));
        }
    }

    public void drawQATestAnalyteBar(ImageView imageView, LinearLayout imageViewBgLayout, boolean criticalResult) {
        if (criticalResult) {
            imageView.setBackgroundColor(Color.parseColor("#de1f00"));
            imageViewBgLayout.setBackgroundColor(Color.parseColor("#f3d7d2"));
        }
    }

    public boolean isCriticalResult(ResultStatus resultStatus) {
        return false;
    }
}