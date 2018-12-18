package com.epocal.reader.type.Legacy;

import com.epocal.common.types.am.BubbleDetectMode;

/**
 * Created by rzhuang on Aug 23 2018.
 */

public class BDConfig {
    public BDConfig() {
        BDMode = BubbleDetectMode.AllAlwaysOn;
    }

    // bubble detect
    public BubbleDetectMode BDMode;

    public int getBDFrequency() {
        return mBDFrequency;
    }

    public void setBDFrequency(int BDFrequency) {
        this.mBDFrequency = BDFrequency;
    }

    public void setCalInitTime(byte calInitTime) {
        mCalInitTime = calInitTime;
    }

    public float getAirInitThreshold() {
        return mAirInitThreshold;
    }

    public void setAirInitThreshold(float airInitThreshold) {
        mAirInitThreshold = airInitThreshold;
    }

    public float getFluidInitThreshold() {
        return mFluidInitThreshold;
    }

    public void setFluidInitThreshold(float fluidInitThreshold) {
        mFluidInitThreshold = fluidInitThreshold;
    }

    public float getAirAfterFluidThreshold() {
        return mAirAfterFluidThreshold;
    }

    public void setAirAfterFluidThreshold(float airAfterFluidThreshold) {
        mAirAfterFluidThreshold = airAfterFluidThreshold;
    }

    public float getFluidAfterFluidThreshold() {
        return mFluidAfterFluidThreshold;
    }

    public void setFluidAfterFluidThreshold(float fluidAfterFluidThreshold) {
        mFluidAfterFluidThreshold = fluidAfterFluidThreshold;
    }

    private int mBDFrequency;
    private byte mCalInitTime;
    private float mAirInitThreshold;
    private float mFluidInitThreshold;
    private float mAirAfterFluidThreshold;
    private float mFluidAfterFluidThreshold;
    //private short mCalibrationExpiry;
}