package com.epocal.testhistoryfeature.data.statusdata;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.epocal.common.qaverfication.QATestStatus;
import com.epocal.common.realmentities.EqcInfo;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.testhistoryfeature.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QATestRelatedTestStatusCategoryDataUtil extends RelatedTestStatusCategoryDataUtil {

    QATestRelatedTestStatusCategoryDataUtil(Context context, TestRecord testRecord, EqcInfo eqcInfo) {
        super(context, testRecord, eqcInfo);
    }

    @Override
    List<RelatedTestStatusCategoryData> getCategoryList() {
        if (mCategoryList == null) {
            mCategoryList = createQATestCategoryDataList(mContext, mTestRecord);
        }
        return mCategoryList;
    }

    // hasMoreInfo --  true - indicate that the group has a child object.
    //                 false - no child object and the group becomes non-expandable.
    @Override
    protected List<RelatedTestStatusCategoryData> createQATestCategoryDataList(Context context, TestRecord testRecord) {

        List<RelatedTestStatusCategoryData> testResultCategories = new ArrayList<>();

        //(Context context, String testName, SpannableStringBuilder subTitle, Boolean hasPassedTest, Boolean hasMoreInfo)

        // 1 - EQC - Reader runs EQC test right after the card insert and before each Patient Blood Test
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

        // 2 - Device Info
        // TODO: _BEGIN Currently Device Info retrieval from Reader is not supported.
        // TODO: Need to change below two lines when the info becomes available.
        SpannableStringBuilder deviceInfoStatus = new SpannableStringBuilder(context.getResources().getString(R.string.unknown_test_status));
        boolean deviceHasMoreInfo = listHasMoreInfo(mDeviceInfoList);
        // TODO: _END
        testResultCategories.add(
                new RelatedTestStatusCategoryData(RelatedTestStatusCategory.DEVICE_INFO,
                        getCategoryTitle(RelatedTestStatusCategory.DEVICE_INFO),
                        deviceInfoStatus, null,
                        deviceHasMoreInfo));

        // TODO: Question -- what to display for if the test status is not applicable like ranges? Show no status string (subtitle).
        // 3 - Reference Range
        SpannableStringBuilder rangeNotFoundSubTitle = new SpannableStringBuilder(context.getResources().getString(R.string.range_list_not_found));
        SpannableStringBuilder rangeSubTitle = listHasMoreInfo(mReferenceRangeList) ? null : rangeNotFoundSubTitle;
        boolean rangeHasMoreInfo = listHasMoreInfo(mReferenceRangeList);
        testResultCategories.add(
                new RelatedTestStatusCategoryData(RelatedTestStatusCategory.REFERENCE_RANGE,
                        getCategoryTitle(RelatedTestStatusCategory.REFERENCE_RANGE),
                        rangeSubTitle,
                        null,
                        rangeHasMoreInfo));

        return testResultCategories;
    }
}
