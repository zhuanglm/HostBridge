package com.epocal.epoctest.uimessage;

/**
 * The UI Message class
 *
 * Created by bmate on 7/17/2017.
 */

public class EpocTestUIMessage {
    private int mStringResourceID;
    private ForeColorType mForeColorType;
    private Object[] mOptionalStringParameters;

    EpocTestUIMessage(int stringResourceID, ForeColorType foreColorType) {
        mStringResourceID = stringResourceID;
        mForeColorType = foreColorType;
    }

    public int getStringResourceID() {
        return mStringResourceID;
    }

    public void setStringResourceID(int stringResourceID) {
        mStringResourceID = stringResourceID;
    }

    public ForeColorType getForeColorType() {
        return mForeColorType;
    }

    public void setForeColorType(ForeColorType foreColorType) {
        mForeColorType = foreColorType;
    }

    public Object[] getOptionalStringParameters() {
        return mOptionalStringParameters;
    }
    public void setOptionalStringParameters(Object[] optionalStringParameters) {
        this.mOptionalStringParameters = optionalStringParameters;
    }
}
