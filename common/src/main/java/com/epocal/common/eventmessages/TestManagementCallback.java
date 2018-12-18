package com.epocal.common.eventmessages;

/**
 * Created by bmate on 7/27/2017.
 */

public class TestManagementCallback {
    private TestManagementEventType  mTestManagementEventType;
    String mReaderBTA;
    boolean mFromInactivity;

    public TestManagementCallback(TestManagementEventType eventType, String readerBTA){
        this.mTestManagementEventType = eventType;
        this.mReaderBTA  = readerBTA;
    }
    public TestManagementCallback(TestManagementEventType eventType, boolean handleInactivity){
        this.mTestManagementEventType = eventType;
        this.mFromInactivity  = handleInactivity;
    }

    public TestManagementEventType getTestManagementEventType() {
        return mTestManagementEventType;
    }

    public void setTestManagementEventType(TestManagementEventType testManagementEventType) {
        mTestManagementEventType = testManagementEventType;
    }

    public String getReaderBTA() {
        return mReaderBTA;
    }
    public void setReaderBTA(String readerBTA) {
        mReaderBTA = readerBTA;
    }

    public boolean getFromInactivity() {
        return mFromInactivity;
    }

    public void setFromInactivity(boolean fromInactivity) {
        mFromInactivity = fromInactivity;
    }
}

