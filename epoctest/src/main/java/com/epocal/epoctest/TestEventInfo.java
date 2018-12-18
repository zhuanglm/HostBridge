package com.epocal.epoctest;

import com.epocal.common.types.TestErrorCode;

/**
 * Created by bmate on 7/18/2017.
 * This is the relay object between testcontroller and uitestcontroller
 */

public class TestEventInfo {

    private TestEventInfoType mTestEventInfoType;
    private Object mTestEventData;
    private TestStatusErrorType mTestStatusErrorType;
    private TestStatusType mTestStatusType;

    public TestEventInfoType getTestEventInfoType() {
        return mTestEventInfoType;
    }

    public void setTestEventInfoType(TestEventInfoType testEventInfoType) {
        mTestEventInfoType = testEventInfoType;
    }

    public Object getTestEventData() {
        return mTestEventData;
    }

    public void setTestEventData(Object testEventData) {
        mTestEventData = testEventData;
    }

    public TestStatusErrorType getTestStatusErrorType() {
        return mTestStatusErrorType;
    }

    public void setTestStatusErrorType(TestStatusErrorType testStatusErrorType) {
        mTestStatusErrorType = testStatusErrorType;
    }

    public TestStatusType getTestStatusType() {
        return mTestStatusType;
    }

    public void setTestStatusType(TestStatusType testStatusType) {
        mTestStatusType = testStatusType;
    }
}
