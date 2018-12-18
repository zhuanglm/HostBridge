package com.epocal.common_ui.testresults;

import android.text.SpannableString;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epocal.common.types.ResultStatus;

/**
 * The View interface
 *
 * Created by Zeeshan A Zakaria on 10/6/2017.
 */

interface ITestResultsAdapterView {
    void setTextViewText(TextView textView, SpannableString s);
    void setTextViewText(TextView textView, String s);
    void drawAnalyteBar(ImageView imageView, LinearLayout.LayoutParams layoutParams, LinearLayout imageViewBgLayout, ResultStatus resultStatus);
    void hideAnalyteBar(LinearLayout llBarGraph);
    boolean hasCriticalResults();
}
