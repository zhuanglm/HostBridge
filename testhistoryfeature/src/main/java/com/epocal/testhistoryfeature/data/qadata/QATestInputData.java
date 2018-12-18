package com.epocal.testhistoryfeature.data.qadata;

import android.content.Context;
import android.util.SparseIntArray;

import com.epocal.common.realmentities.TestDetail;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.types.QAFluidType;
import com.epocal.common_ui.util.StringResourceValues;

public class QATestInputData {
    private String NULL_VALUE = "-";
    public String fluidLot;
    public String fluidType;
    public String testType;
    public String referenceId;
    public String expiryDate;
    public String comments;

    public QATestInputData(Context context, TestRecord testRecord) {
        fluidLot = (testRecord.getSubjectId() == null) ? NULL_VALUE : testRecord.getSubjectId();

        if (testRecord.getTestDetail() != null) {
            TestDetail testDetail = testRecord.getTestDetail();
            if (testDetail.getQaSampleInfo() != null) {
                // TODO: String value should match IStepperItemBehavior fluidTypeBehavior()?
                // TODO: For now, just print enum value as String.
                QAFluidType fluidTypeEnumVal = testDetail.getQaSampleInfo().getQaFluidType();
                fluidType = fluidTypeEnumVal.toString();
            } else {
                fluidType = NULL_VALUE;
            }

            comments = (testDetail.getComment() == null) ? NULL_VALUE : testDetail.getComment();
        }

        SparseIntArray testTypeIntToStringResourceInt = StringResourceValues.setTestType();
        int resourceKey = testTypeIntToStringResourceInt.get(testRecord.getType().value);
        testType = context.getResources().getString(resourceKey);

        // TODO: referenceId comes from EVAD which is not implemented yet.
        referenceId = "Not Implemented";
        // TODO: expiryDate comes from EVAD which is not implemented yet.
        expiryDate = "Not Implemented";
    }
}
