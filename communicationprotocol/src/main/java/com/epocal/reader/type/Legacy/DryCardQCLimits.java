package com.epocal.reader.type.Legacy;

/**
 * Created by rzhuang on Aug 9 2018.
 */

public class DryCardQCLimits {

    public float getAmperometricLow() {
        return mAmperometricLow;
    }

    public void setAmperometricLow(float mAmperometricLow) {
        this.mAmperometricLow = mAmperometricLow;
    }

    public float getThirtyKLow() {
        return mThirtyKLow;
    }

    public void setThirtyKLow(float mThirtyKLow) {
        this.mThirtyKLow = mThirtyKLow;
    }

    public float getAmperometricHigh() {
        return mAmperometricHigh;
    }

    public void setAmperometricHigh(float mAmperometricHigh) {
        this.mAmperometricHigh = mAmperometricHigh;
    }

    public float getThirtyKHigh() {
        return mThirtyKHigh;
    }

    public void setThirtyKHigh(float mThirtyKHigh) {
        this.mThirtyKHigh = mThirtyKHigh;
    }

    private float mAmperometricLow;
    private float mThirtyKLow;
    private float mAmperometricHigh;
    private float mThirtyKHigh;
}