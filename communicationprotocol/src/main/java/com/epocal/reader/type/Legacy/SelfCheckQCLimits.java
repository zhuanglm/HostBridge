package com.epocal.reader.type.Legacy;

/**
 * Created by rzhuang on Aug 15 2018.
 */

@SuppressWarnings({"unused","FieldCanBeLocal"})
public class SelfCheckQCLimits {

    public void setPotentiometricLow(float potentiometricLow) {
        mPotentiometricLow = potentiometricLow;
    }

    public void setPotentiometricHigh(float potentiometricHigh) {
        mPotentiometricHigh = potentiometricHigh;
    }

    public void setAmperometricLow(float amperometricLow) {
        mAmperometricLow = amperometricLow;
    }

    public void setAmperometricHigh(float amperometricHigh) {
        mAmperometricHigh = amperometricHigh;
    }

    public void setConductivityLow(float conductivityLow) {
        mConductivityLow = conductivityLow;
    }

    public void setConductivityHigh(float conductivityHigh) {
        mConductivityHigh = conductivityHigh;
    }

    public void setGroundLow(float groundLow) {
        mGroundLow = groundLow;
    }

    public void setGroundHigh(float groundHigh) {
        mGroundHigh = groundHigh;
    }

    private float mPotentiometricLow;
    private float mPotentiometricHigh;
    private float mAmperometricLow;
    private float mAmperometricHigh;
    private float mConductivityLow;
    private float mConductivityHigh;
    private float mGroundLow;
    private float mGroundHigh;
}
