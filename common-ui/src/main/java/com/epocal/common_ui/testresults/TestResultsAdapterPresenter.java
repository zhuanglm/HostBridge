package com.epocal.common_ui.testresults;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epocal.common.epocobjects.AnalyteGroups;
import com.epocal.common.realmentities.TestResult;
import com.epocal.common.types.ResultStatus;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common_ui.R;

import java.util.ArrayList;

import io.realm.RealmList;


import static com.epocal.common.types.ResultStatus.CNC;
import static com.epocal.common.types.ResultStatus.CriticalHigh;
import static com.epocal.common.types.ResultStatus.CriticalLow;
import static com.epocal.common.types.ResultStatus.FailediQC;
import static com.epocal.common.types.ResultStatus.ReferenceHigh;
import static com.epocal.common.types.ResultStatus.ReferenceLow;

/**
 * The Presenter class for the TestResultsAdapter to make the adapter class simpler
 * <p>
 * Created by Zeeshan A Zakaria on 10/6/2017.
 */

public class TestResultsAdapterPresenter implements ITestResultsAdapterPresenter {

    private Context mContext;
    private BaseTestResultsAdapter mView;
    private RealmList<TestResult> mTestResults;
    private ArrayList<IGroupedTestResultDataItem> mSortedTestResultsWithSectionHeader;

    TestResultsAdapterPresenter(RealmList<TestResult> testResults, BaseTestResultsAdapter view, Context context) {
        mTestResults = testResults;
        mView = view;
        mContext = context;
        mSortedTestResultsWithSectionHeader = sortTestResultsToAnalyteGroup(mTestResults);
    }

    /**
     * @return the number of elements in the test results List
     */
    @Override
    public int getTestResultsSize() {
        return mTestResults.size();
    }

    /**
     * @return the number of elements in the test results List including the section header
     */
    int getGroupedTestResultsListSize() {
        return mSortedTestResultsWithSectionHeader.size();
    }

    /**
     * @param index the position of the TestResult object in the ListView
     * @return the TestResult object
     */
    @Override
    public TestResult getTestResult(int index) {
        return mTestResults.get(index);
    }

    IGroupedTestResultDataItem getGroupedTestResultDataItem(int index) {
        return mSortedTestResultsWithSectionHeader.get(index);
    }

    public TestResult getTestResultFromGroupedTestResultDataItem(IGroupedTestResultDataItem item) {
        if (item.isSectionHeader()) {
            return null;
        } else {
            return mTestResults.get(item.getTestResultIndex());
        }
    }

    public TestResult getGroupedTestResult(int index) {
        IGroupedTestResultDataItem item = mSortedTestResultsWithSectionHeader.get(index);
        if (item.isSectionHeader()) {
            return null;
        } else {
            return mTestResults.get(item.getTestResultIndex());
        }
    }

    /**
     * @param index the position of the test result in the ListView
     * @return the id of the corresponding TestResult object
     */
    @Override
    public long getTestResultId(int index) {
        return mTestResults.get(index).getId();
    }

    long getGroupedTestResultId(int index) {
        IGroupedTestResultDataItem item = mSortedTestResultsWithSectionHeader.get(index);
        if (item.isListHeader() || item.isSectionHeader()) {
            // Result Id is not applicable for Header item
            return 0;
        } else {
            long id = 0;
            int resultIndex = item.getTestResultIndex();
            if ((resultIndex >= 0) && (resultIndex < mTestResults.size())) {
                id = mTestResults.get(item.getTestResultIndex()).getId();
            }
            return id;
        }
    }

    /**
     * This method decides what text would go in the Analyte value place. It is either the analyte's
     * calculated value, or it is one of the error messages.
     *
     * @param resultModel the result model object
     * @param textView    the TextView which is populated with the analyte value or an error message
     */
    private void setAnalyteValueText(ResultModel resultModel, TextView textView) {
        String value = resultModel.getAnalyteValue();
        ResultStatus resultStatus = resultModel.getResultStatus();

        if (resultStatus == CNC || resultStatus == FailediQC) {
            value = getStatusMessage(resultStatus);
        } else if (resultModel.getAnalyteValue() != null) {
            if (resultModel.getAnalyteVal() < resultModel.getReportableLowV()) {
                value = "<" + resultModel.getReportableLowV();

            } else if (resultModel.getAnalyteVal() > resultModel.getReportableHighV()) {
                value = ">" + resultModel.getReportableHighV();
            }
        }

        mView.setTextViewText(textView, value);
    }

    /**
     * @param resultModel the result model object
     * @param imageView   the image view which is used for the blue graph bar
     */
    private void drawTheBlueBar(ResultModel resultModel, ImageView imageView, LinearLayout llBlueBar, LinearLayout imageViewBgLayout) {
        ResultStatus resultStatus = resultModel.getResultStatus();
        if (resultStatus == CNC || resultStatus == FailediQC || resultModel.valuesOutOfBoundary()) {
            mView.hideAnalyteBar(llBlueBar);
        } else {
            Float pixelDensity = mContext.getResources().getDisplayMetrics().density;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resultModel.getBarWidthSAM(pixelDensity), resultModel.getBarHeight(pixelDensity));
            mView.drawAnalyteBar(imageView, layoutParams, imageViewBgLayout, resultStatus);
        }
    }

    /**
     * This method gets the message to be displayed based on the failed result ENUM flag. This
     * method is not coded in the model object in order to keep the context within the adapter
     * class.
     *
     * @param resultStatus the result status
     * @return the returned localized string value
     */
    private String getStatusMessage(ResultStatus resultStatus) {
        switch (ResultStatus.fromInt(resultStatus.value)) {
            case CNC:
                return mContext.getString(R.string.text_failed_iqc);
            case FailediQC:
                return mContext.getString(R.string.text_failed_iqc);
            default:
                return "";
        }
    }

    /**
     * This method creates the ResultModel objects one by one and populates the corresponding view items
     *
     * @param index            the position of the item in the list view
     * @param imageViewBlueBar the image view which is used for the blue graph bar
     * @param tvAnalyteValue   the TextView which is populated with the analyte value or an error message
     */
    @Override
    public void addResultsItems(int index, ImageView imageViewBlueBar, TextView tvAnalyteName, TextView tvAnalyteValue, TextView tvAnalyteUnit,
                                TextView tvReferenceLow, TextView tvReferenceHigh, LinearLayout llBlueBar, LinearLayout imageViewBgLayout) {
        ResultModel resultModel = new ResultModel(mTestResults.get(index));
        drawTheBlueBar(resultModel, imageViewBlueBar, llBlueBar, imageViewBgLayout);
        setAnalyteValueText(resultModel, tvAnalyteValue);

        mView.setTextViewText(tvAnalyteName, resultModel.getAnalyteAbbr());
        mView.setTextViewText(tvAnalyteUnit, resultModel.getUnit());
        mView.setTextViewText(tvReferenceLow, resultModel.getReferenceLow());
        mView.setTextViewText(tvReferenceHigh, resultModel.getReferenceHigh());
    }

    private ArrayList<IGroupedTestResultDataItem> sortTestResultsToAnalyteGroup(RealmList<TestResult> testResults) {
        String[] sectionHeaderTitles = new String[3];
        sectionHeaderTitles[0] = mContext.getString(R.string.analyte_group_gases);
        sectionHeaderTitles[1] = mContext.getString(R.string.analyte_group_chem);
        sectionHeaderTitles[2] = mContext.getString(R.string.analyte_group_meta);

        ArrayList<Integer> gasesPlus = new ArrayList<>();
        ArrayList<Integer> chemicalsPlus = new ArrayList<>();
        ArrayList<Integer> metabolitesPlus = new ArrayList<>();

        // Sort testResults in AnalyteGroup
        for (int resultIndex = 0; resultIndex < testResults.size(); resultIndex++) {
            TestResult result = testResults.get(resultIndex);
            AnalyteName analyteName = result.getAnalyteName();

            if (AnalyteGroups.GASES.contains(analyteName)) {
                gasesPlus.add(resultIndex);
            } else if (AnalyteGroups.CHEMICALS.contains(analyteName)) {
                chemicalsPlus.add(resultIndex);
            } else if (AnalyteGroups.METABOLITES.contains(analyteName)) {
                metabolitesPlus.add(resultIndex);
            } else {
                Log.v("TAG", "Unknown AnalyteGroup=" + analyteName);
            }
        }

        // Make a List of sorted testResults with Section header
        ArrayList<IGroupedTestResultDataItem> sortedDataArray = new ArrayList<>();

        // Index 0 is the List Header (with Document button)
        sortedDataArray.add(new GroupedTestResultListHeaderDataItem());

        if (gasesPlus.size() > 0) {
            // Gases - section header
            sortedDataArray.add(new GroupedTestResultSectionHeaderDataItem(sectionHeaderTitles[0]));
            // Gases - test result list
            for (int j = 0; j < gasesPlus.size(); j++) {
                sortedDataArray.add(new GroupedTestResultDataItem(gasesPlus.get(j)));
            }
        }

        if (chemicalsPlus.size() > 0) {
            // Chemicals - section header
            sortedDataArray.add(new GroupedTestResultSectionHeaderDataItem(sectionHeaderTitles[1]));
            // Chemicals - test result list
            for (int j = 0; j < chemicalsPlus.size(); j++) {
                sortedDataArray.add(new GroupedTestResultDataItem(chemicalsPlus.get(j)));
            }
        }

        if (metabolitesPlus.size() > 0) {
            // Metabolites - section header
            sortedDataArray.add(new GroupedTestResultSectionHeaderDataItem(sectionHeaderTitles[2]));
            // Metabolites - test result list
            for (int j = 0; j < metabolitesPlus.size(); j++) {
                sortedDataArray.add(new GroupedTestResultDataItem(metabolitesPlus.get(j)));
            }
        }

        return sortedDataArray;
    }
}