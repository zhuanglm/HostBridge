package com.epocal.common.eventmessages;

/**
 * Created by bmate on 19/01/2018.
 */

public class TestDataProcessorCallback {
    private TestDataProcessorEventType mEventType;
    public TestDataProcessorCallback(TestDataProcessorEventType eventType){
        this.mEventType = eventType;
    }
    public TestDataProcessorEventType getEventType() {
        return mEventType;
    }

    public void setEventType(TestDataProcessorEventType eventType) {
        mEventType = eventType;
    }
}
