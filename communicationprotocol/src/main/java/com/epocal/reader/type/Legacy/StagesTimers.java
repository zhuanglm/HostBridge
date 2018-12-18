package com.epocal.reader.type.Legacy;

/**
 * Created by rzhuang on Aug 23 2018.
 */

public class StagesTimers {
    public short getCalibrationExpiryTimer() {
        return mCalibrationExpiryTimer;
    }

    public void setCalibrationExpiryTimer(short calibrationExpiryTimer) {
        this.mCalibrationExpiryTimer = calibrationExpiryTimer;
    }

    public short getSampleIntroductionTimer() {
        return mSampleIntroductionTimer;
    }

    public void setSampleIntroductionTimer(short sampleIntroductionTimer) {
        this.mSampleIntroductionTimer = sampleIntroductionTimer;
    }

    public short getSampleCollectionTimer() {
        return mSampleCollectionTimer;
    }

    public void setSampleCollectionTimer(short sampleCollectionTimer) {
        this.mSampleCollectionTimer = sampleCollectionTimer;
    }

    // Stage timers
    private short mCalibrationExpiryTimer;
    private short mSampleIntroductionTimer;
    private short mSampleCollectionTimer;
}
