package com.epocal.reader.type.Legacy;

import java.util.Calendar;

/**
 * Created by rzhuang on Nov 21 2018.
 */

@SuppressWarnings("unused")
public class ReaderTQAInfo
{
    public String getReaderSerialNumber() {
        return mReaderSerialNumber;
    }

    public void setReaderSerialNumber(String mReaderSerialNumber) {
        this.mReaderSerialNumber = mReaderSerialNumber;
    }

    public String getOperatorId() {
        return mOperatorId;
    }

    public void setOperatorId(String mOperatorId) {
        this.mOperatorId = mOperatorId;
    }

    public String getBatteryLevel() {
        return mBatteryLevel;
    }

    public void setBatteryLevel(String mBatteryLevel) {
        this.mBatteryLevel = mBatteryLevel;
    }

    public String getAmbientTemperature() {
        return mAmbientTemperature;
    }

    public void setAmbientTemperature(String mAmbientTemperature) {
        this.mAmbientTemperature = mAmbientTemperature;
    }

    public String getAmbientPressure() {
        return mAmbientPressure;
    }

    public void setAmbientPressure(String mAmbientPressure) {
        this.mAmbientPressure = mAmbientPressure;
    }

    public String getHostSerialNumber() {
        return mHostSerialNumber;
    }

    public void setHostSerialNumber(String mHostSerialNumber) {
        this.mHostSerialNumber = mHostSerialNumber;
    }

    public String getHostVersion() {
        return mHostVersion;
    }

    public void setHostVersion(String mHostVersion) {
        this.mHostVersion = mHostVersion;
    }

    public String getReaderHardware() {
        return mReaderHardware;
    }

    public void setReaderHardware(String mReaderHardware) {
        this.mReaderHardware = mReaderHardware;
    }

    public String getReaderMechanical() {
        return mReaderMechanical;
    }

    public void setReaderMechanical(String mReaderMechanical) {
        this.mReaderMechanical = mReaderMechanical;
    }

    public String getReaderSWVersion() {
        return mReaderSWVersion;
    }

    public void setReaderSWVersion(String mReaderSWVersion) {
        this.mReaderSWVersion = mReaderSWVersion;
    }

    public String getPassFail() {
        return mPassFail;
    }

    public void setPassFail(String mPassFail) {
        this.mPassFail = mPassFail;
    }

    public String getTopHeater() {
        return mTopHeater;
    }

    public void setTopHeater(String mTopHeater) {
        this.mTopHeater = mTopHeater;
    }

    public String getBottomHeater() {
        return mBottomHeater;
    }

    public void setBottomHeater(String mBottomHeater) {
        this.mBottomHeater = mBottomHeater;
    }

    public String getSIBVersion() {
        return SIBVersion;
    }

    public void setSIBVersion(String SIBVersion) {
        this.SIBVersion = SIBVersion;
    }

    public Calendar getTQADateTime() {
        return mTQADateTime;
    }

    public void setTQADateTime(Calendar mTQADateTime) {
        this.mTQADateTime = mTQADateTime;
    }

    public double getBottomHeaterFloat() {
        return mBottomHeaterFloat;
    }

    public void setBottomHeaterFloat(double mBottomHeaterFloat) {
        this.mBottomHeaterFloat = mBottomHeaterFloat;
    }

    public double getTopHeaterFloat() {
        return mTopHeaterFloat;
    }

    public void setTopHeaterFloat(double mTopHeaterFloat) {
        this.mTopHeaterFloat = mTopHeaterFloat;
    }

    public double getAmbientFloat() {
        return mAmbientFloat;
    }

    public void setAmbientFloat(double mAmbientFloat) {
        this.mAmbientFloat = mAmbientFloat;
    }

    public boolean isPassed() {
        return mPassed;
    }

    public void setPassed(boolean mPassed) {
        this.mPassed = mPassed;
    }

    private String mReaderSerialNumber;
    private String mOperatorId;
    private String mBatteryLevel;
    private String mAmbientTemperature;
    private String mAmbientPressure;
    //private String mSensorConfig;
    private String mHostSerialNumber;
    private String mHostVersion;
    private String mReaderHardware;
    private String mReaderMechanical;
    private String mReaderSWVersion;
    private String mPassFail;
    private String mTopHeater;
    private String mBottomHeater;
    private String SIBVersion;

    private Calendar mTQADateTime;
    private double mBottomHeaterFloat;
    private double mTopHeaterFloat;
    private double mAmbientFloat;
    private boolean mPassed;

    public ReaderTQAInfo()
    {

    }
}