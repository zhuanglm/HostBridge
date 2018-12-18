package com.epocal.testhistoryfeature.data.statusdata;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.SparseIntArray;

import com.epocal.common.qaverfication.QATestStatus;
import com.epocal.common.realmentities.EqcInfo;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.TestResult;
import com.epocal.common_ui.list.ListItem;
import com.epocal.common_ui.list.StyledTextListItem;
import com.epocal.common_ui.qaresults.SimpleTest;
import com.epocal.common_ui.testresults.ResultModel;
import com.epocal.testhistoryfeature.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.RealmList;

/**
 * This class parse the Test Record object to group the data into related QA category.
 *
 * @see TestStatusDataAdapter
 */
class RelatedTestStatusCategoryDataUtil {
    final protected Context mContext;
    final TestRecord        mTestRecord;

    private final List<SimpleTest> mAnalyteQCTestList;
    private final List<SimpleTest> mAnalyteCVTestList;
    private List<StyledTextListItem> mCriticalRangeList;
    private List<StyledTextListItem> mReportableRangeList;
    List<StyledTextListItem> mReferenceRangeList;
    private final SparseIntArray mCategoryToTitleResIdMap;
    List<RelatedTestStatusCategoryData> mCategoryList;
    final List<ListItem> mDeviceInfoList;

    RelatedTestStatusCategoryDataUtil(Context context, TestRecord testRecord, EqcInfo eqcInfo) {
        mContext = context;
        mTestRecord = testRecord;
        mCategoryToTitleResIdMap = buildRelatedTestStatusCategoryEnumToResIdMap();
        mAnalyteQCTestList = buildQCAnalyteTestList(testRecord);
        mAnalyteCVTestList = buildCVAnalyteTestList(testRecord);
        mDeviceInfoList = buildDeviceInfoList(eqcInfo);
        buildReferenceRanges(testRecord);
    }

    List<RelatedTestStatusCategoryData> getCategoryList() {
        if (mCategoryList == null) {
            mCategoryList = createQATestCategoryDataList(mContext, mTestRecord);
        }
        return mCategoryList;
    }

    List<SimpleTest> getAnalyteQCTestList() {
        return mAnalyteQCTestList;
    }

    List<SimpleTest> getAnalyteCVTestList() {
        return mAnalyteCVTestList;
    }

    List<ListItem> getDeviceInfoList() {
        return mDeviceInfoList;
    }

    List<StyledTextListItem> getReferenceRangeList() {
        return mReferenceRangeList;
    }

    List<StyledTextListItem> getCriticalRangeList() {
        return mCriticalRangeList;
    }

    List<StyledTextListItem> getReportableRangeList() {
        return mReportableRangeList;
    }

    int getChildrenCount(RelatedTestStatusCategory groupCategory) {
        int childrenCount = 0;
        switch (groupCategory) {
            case QC:
                childrenCount = (mAnalyteQCTestList == null) ? 0 : 1;
                break;

            case CV:
                childrenCount = (mAnalyteCVTestList == null) ? 0 : 1;
                break;

            case DEVICE_INFO:
                childrenCount = (mDeviceInfoList == null) ? 0 : mDeviceInfoList.size();
                break;

            case REFERENCE_RANGE:
                childrenCount = (mReferenceRangeList == null) ? 0 : mReferenceRangeList.size();
                break;

            case CRITICAL_RANGE:
                childrenCount = (mCriticalRangeList == null) ? 0 : mCriticalRangeList.size();
                break;

            case REPORTABLE_RANGE:
                childrenCount = (mReportableRangeList == null) ? 0 : mReportableRangeList.size();
                break;

            case EQC:
            case TQC:
                //childrenCount = 0;
                break;
        }
        return childrenCount;
    }

    // hasMoreInfo --  true - indicate that the group has a child object.
    //                 false - no child object and the group becomes non-expandable.
    protected List<RelatedTestStatusCategoryData> createQATestCategoryDataList(Context context, TestRecord testRecord) {
        List<RelatedTestStatusCategoryData> testResultCategories = new ArrayList<>();

        //(Context context, String testName, SpannableStringBuilder subTitle, Boolean hasPassedTest, Boolean hasMoreInfo)

        // 0 - QC Test --
        // TODO: _BEGIN Currently QC data retrieval from Reader is not implemented.
        // TODO: for testing purpose, use commented out subtitle and hasMoreInfo (true).
        //SpannableStringBuilder subtitle = buildQAStatusString(context, false, testRecord.getLastEqcDateTime());
        //boolean hasMoreInfo = true;
        // TODO: _END
        SpannableStringBuilder qcInfoStatus = new SpannableStringBuilder(context.getResources().getString(R.string.unknown_test_status));
        testResultCategories.add(
                new RelatedTestStatusCategoryData(RelatedTestStatusCategory.QC,
                        getCategoryTitle(RelatedTestStatusCategory.QC),
                        qcInfoStatus,
                        null,
                        false));

        // 1 - CV Test --
        // TODO: _BEGIN Currently CV data retrieval from Reader is not implemented.
        // TODO: for testing purpose, use commented out subtitle and hasMoreInfo (true).
        SpannableStringBuilder cvInfoStatus = new SpannableStringBuilder(context.getResources().getString(R.string.unknown_test_status));
        testResultCategories.add(
                new RelatedTestStatusCategoryData(RelatedTestStatusCategory.CV,
                        getCategoryTitle(RelatedTestStatusCategory.CV),
                        cvInfoStatus,
                        null,
                        false));

        // 2 - EQC - Reader runs EQC test right after the card insert and before each Patient Blood Test
        //     and must passed in order to run Blood Test.
        //     Thus, the status must be always success.
        QATestStatus eqcStatus = (testRecord.getLastEqcDateTime() == null) ? QATestStatus.Unknown : QATestStatus.Passed;
        Date eqcDatetime =  testRecord.getLastEqcDateTime();
        SpannableStringBuilder subtitleEqc = buildQAStatusString(context, QATestStatusToBoolean(eqcStatus), eqcDatetime);
        testResultCategories.add(
                new RelatedTestStatusCategoryData(RelatedTestStatusCategory.EQC,
                        getCategoryTitle(RelatedTestStatusCategory.EQC),
                        subtitleEqc,
                        QATestStatusToBoolean(eqcStatus),
                        false));

        // 3 - TQC (Thermal QC) -- TQC is optional test for Patient Blood Test and is populated only when the user selects
        // to run the TQC test during Patient Blood Test.
        QATestStatus tqaStatus =  (testRecord.getReader() == null) ? null : testRecord.getReader().getLastTQAPassFail();
        Date tqaDatetime =  (testRecord.getReader() == null) ? null : testRecord.getReader().getLastTQADateTime();
        SpannableStringBuilder subtitleTqc = buildQAStatusString(context, QATestStatusToBoolean(tqaStatus), tqaDatetime);
        testResultCategories.add(
                new RelatedTestStatusCategoryData(RelatedTestStatusCategory.TQC,
                        getCategoryTitle(RelatedTestStatusCategory.TQC),
                        subtitleTqc,
                        QATestStatusToBoolean(tqaStatus),
                        false));

        // 4 - Device Info
        // TODO: _BEGIN Currently Device Info retrieval from Reader is not supported.
        // TODO: Need to change below two lines when the info becomes available.
        SpannableStringBuilder deviceInfoStatus = new SpannableStringBuilder(context.getResources().getString(R.string.unknown_test_status));
        boolean deviceHasMoreInfo = listHasMoreInfo(mDeviceInfoList);
        // TODO: _END
        testResultCategories.add(
                new RelatedTestStatusCategoryData(RelatedTestStatusCategory.DEVICE_INFO,
                        getCategoryTitle(RelatedTestStatusCategory.DEVICE_INFO),
                        deviceInfoStatus,
                        null,
                        deviceHasMoreInfo));

        // TODO: Question -- what to display for if the test status is not applicable like ranges? Show no status string (subtitle).
        // 5 - Reference Range
        SpannableStringBuilder rangeNotFoundSubTitle = new SpannableStringBuilder(context.getResources().getString(R.string.range_list_not_found));
        SpannableStringBuilder rangeSubTitle = listHasMoreInfo(mReferenceRangeList) ? null : rangeNotFoundSubTitle;
        boolean rangeHasMoreInfo = listHasMoreInfo(mReferenceRangeList);
        testResultCategories.add(
                new RelatedTestStatusCategoryData(RelatedTestStatusCategory.REFERENCE_RANGE,
                        getCategoryTitle(RelatedTestStatusCategory.REFERENCE_RANGE),
                        rangeSubTitle,
                        null,
                        rangeHasMoreInfo));
        // 6 - Critical Range
        rangeSubTitle = listHasMoreInfo(mCriticalRangeList) ? null : rangeNotFoundSubTitle;
        rangeHasMoreInfo = listHasMoreInfo(mCriticalRangeList);
        testResultCategories.add(
                new RelatedTestStatusCategoryData(RelatedTestStatusCategory.CRITICAL_RANGE,
                        getCategoryTitle(RelatedTestStatusCategory.CRITICAL_RANGE),
                        rangeSubTitle,
                        null,
                        rangeHasMoreInfo));
        // 7 - Reportable Range
        rangeSubTitle = listHasMoreInfo(mReportableRangeList) ? null : rangeNotFoundSubTitle;
        rangeHasMoreInfo = listHasMoreInfo(mReportableRangeList);
        testResultCategories.add(
                new RelatedTestStatusCategoryData(RelatedTestStatusCategory.REPORTABLE_RANGE,
                        getCategoryTitle(RelatedTestStatusCategory.REPORTABLE_RANGE),
                        rangeSubTitle,
                        null,
                        rangeHasMoreInfo));

        return testResultCategories;
    }

    private List<SimpleTest> buildQCAnalyteTestList(TestRecord testRecord) {
        // TODO: Parse reader's QC data from testRecord.
        // TODO: Currently data does not exist and just returns null.
        // TODO: This code is a placeholder and need to implement it when
        // TODO: QC data becomes available.
        return null;

//        // NOTE: Use below is fake data for testing.
//        List<SimpleTest> list = new ArrayList<>();
//        list.add(new SimpleTest("pH", true));
//        list.add(new SimpleTest("pCO2", false));
//        list.add(new SimpleTest("Lac", true));
//        list.add(new SimpleTest("Na+", true));
//        list.add(new SimpleTest("K+", true));
//        list.add(new SimpleTest("Ca++", true));
//        list.add(new SimpleTest("Glu", true));
//        list.add(new SimpleTest("pO2", false));
//        list.add(new SimpleTest("AlveolarO2", true));
//        list.add(new SimpleTest("eGFRj", true));
//        return list;
    }

    private List<SimpleTest> buildCVAnalyteTestList(TestRecord testRecord) {
        // TODO: This code is a placeholder and need to implement it when
        // TODO: CV data becomes available.
        return buildQCAnalyteTestList(testRecord);
    }

    private List<ListItem> buildDeviceInfoList(EqcInfo eqcInfo) {
        List<ListItem> list = new ArrayList<>();
        if (eqcInfo != null) {
            list.add(new ListItem("Ambient Temperature", String.valueOf(eqcInfo.getAmbientTemperature())));
            list.add(new ListItem("Ambient Pressure", String.valueOf(eqcInfo.getAmbientPressure())));
            list.add(new ListItem("Battery Level", String.valueOf(eqcInfo.getBatteryLevel() * 100)));
        }
        return list;
    }

    private void buildReferenceRanges(TestRecord testRecord) {
        // Result contains 3 ranges
        // - Reference Range
        // - Critical Range
        // - Reportable Range

        RealmList<TestResult> results = testRecord.getTestResults();
        if (results == null) {
            return;
        }

        List<StyledTextListItem> referenceRangeList = new ArrayList<>();
        List<StyledTextListItem> reportableRangeList = new ArrayList<>();
        List<StyledTextListItem> criticalRangeList = new ArrayList<>();

        for (TestResult testResult : results) {
            ResultModel resultModel = new ResultModel(testResult);
            SpannableString title = resultModel.getAnalyteAbbr();
            // Reference Range
            String value = formatRange(resultModel.getReferenceLow(), resultModel.getReferenceHigh(), resultModel.getUnit());
            referenceRangeList.add(new StyledTextListItem(title, new SpannableString(value)));
            // Reportable Range
            value = formatRange(resultModel.getReportableLow(), resultModel.getReportableHigh(), resultModel.getUnit());
            reportableRangeList.add(new StyledTextListItem(title, new SpannableString(value)));
            // Critical Range
            value = formatRange(resultModel.getCriticalLow(), resultModel.getCriticalHigh(), resultModel.getUnit());
            criticalRangeList.add(new StyledTextListItem(title, new SpannableString(value)));
        }

        mReferenceRangeList = referenceRangeList;
        mCriticalRangeList = criticalRangeList;
        mReportableRangeList = reportableRangeList;
    }

    private static String formatRange(String low, String high, String unit) {
        return low + " - " + high + " " + unit;
    }

    static Boolean QATestStatusToBoolean(QATestStatus qaTestStatus) {
        Boolean status;
        if (qaTestStatus == QATestStatus.Passed) {
            status = true;
        } else if (qaTestStatus == QATestStatus.Failed) {
            status = false;
        } else {
            status = null;
        }
        return status;
    }

    /**
     * QAStatusCategory shows the status string in the following format:
     *
     * "-Test Pass or Fail- <b>-Date-</b> at <b>-Time-</b>"
     *
     * (e.g)
     * "Passed <b>Mar 13, 2019</b> at <b>12:30pm</b>"
     *
     * Note: Date and Time strings are in the bold letters.
     *
     * @param context    -- context to build String resources.
     * @param testPassed -- testStatu. Possible values are: null, true, false.
     * @param datetime   -- date and time when the test has run.
     * @return styled Status String which includes the test status, test date and test time.
     */
    static SpannableStringBuilder buildQAStatusString(Context context, Boolean testPassed, Date datetime) {
        String testStatusUnknown = context.getResources().getString(R.string.unknown_test_status);

        if ((testPassed == null) || (datetime == null)) {
            // If test status (pass or fail) is missing, or its datetime is missing,
            // consider it unknown case.
            return new SpannableStringBuilder(testStatusUnknown);
        }

        // Get localized string from resource
        String testStatusString = (testPassed) ? context.getResources().getString(R.string.passed) : context.getResources().getString(R.string.failed);

        // Parse date and time strings
        String[] dateNTime = parseDateAndTime(datetime);
        if (dateNTime.length < 2) {
            return new SpannableStringBuilder(testStatusUnknown);
        }
        String dateString = dateNTime[0];
        String timeString = dateNTime[1];

        return styleQAStatusString(testStatusString, dateString, timeString);
    }

    private static String[] parseDateAndTime(Date datetime) {
        Locale locale =  Locale.getDefault();
        String datetimePattern = "dd-MMM-yy hh:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(datetimePattern, locale);
        String datetimeString = format.format(datetime);
        return datetimeString.split(" ");
    }

    private static SpannableStringBuilder styleQAStatusString(String testStatusString, String dateString, String timeString) {

        SpannableString boldDateString = new SpannableString(dateString);
        boldDateString.setSpan(new StyleSpan(Typeface.BOLD), 0, dateString.length(), 0);

        SpannableString boldTimeString = new SpannableString(timeString);
        boldTimeString.setSpan(new StyleSpan(Typeface.BOLD), 0, timeString.length(), 0);

        return new SpannableStringBuilder()
                .append(testStatusString)
                .append(" ")
                .append(boldDateString)
                .append(" at ")
                .append(boldTimeString);
    }

    /**
     * Checks if the given list contains any items.
     *
     * @param list -- a list to check
     * @return -- true -- list contains items.
     *         -- false - list is null or its size is 0.
     */
    static boolean listHasMoreInfo(List list) {
        return ((list!=null) && !list.isEmpty());
    }


    String getCategoryTitle(RelatedTestStatusCategory category) {
        int resId = mCategoryToTitleResIdMap.get(category.value);
        // If no matching resId is found, it  resId is 0. Return an empty string in this case.
        return (resId == 0) ? "" : mContext.getString(resId);
    }

    /**
     * Create Mapping of RelatedTestStatusCategory enum int value to corresponding string Resource Id.
     * @return Map of int (RelatedTestStatusCategory enum value) to Resource Id int (string representation
     * of enum value.)
     */
    private static SparseIntArray buildRelatedTestStatusCategoryEnumToResIdMap() {
        SparseIntArray keyValues = new SparseIntArray();

        keyValues.put(RelatedTestStatusCategory.QC.value, R.string.related_test_status_category_qc);
        keyValues.put(RelatedTestStatusCategory.CV.value, R.string.related_test_status_category_cv);
        keyValues.put(RelatedTestStatusCategory.EQC.value, R.string.related_test_status_category_eqc);
        keyValues.put(RelatedTestStatusCategory.TQC.value, R.string.related_test_status_category_tqc);
        keyValues.put(RelatedTestStatusCategory.DEVICE_INFO.value, R.string.related_test_status_category_device_info);
        keyValues.put(RelatedTestStatusCategory.REFERENCE_RANGE.value, R.string.related_test_status_category_reference_range);
        keyValues.put(RelatedTestStatusCategory.CRITICAL_RANGE.value, R.string.related_test_status_category_critical_range);
        keyValues.put(RelatedTestStatusCategory.REPORTABLE_RANGE.value, R.string.related_test_status_category_reportable_range);

        return keyValues;
    }
}