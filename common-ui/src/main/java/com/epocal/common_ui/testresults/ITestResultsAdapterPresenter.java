package com.epocal.common_ui.testresults;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epocal.common.realmentities.TestResult;

/**
 * The Interface class
 *
 * Created by Zeeshan A Zakaria on 10/6/2017.
 */

interface ITestResultsAdapterPresenter {
    int getTestResultsSize();
    TestResult getTestResult(int index);
    long getTestResultId(int index);
    void addResultsItems(int position, ImageView imageViewBlueBar, TextView tvAnalyteName, TextView tvAnalyteValue, TextView tvAnalyteUnit,
                         TextView tvReferenceLow, TextView tvReferenceHigh, LinearLayout llBlueBar, LinearLayout imageViewBgLayout);
}
