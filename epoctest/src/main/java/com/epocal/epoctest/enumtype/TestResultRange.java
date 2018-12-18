package com.epocal.epoctest.enumtype;

import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.Gender;

/**
 * Created by dning on 11/1/2017.
 */

public class TestResultRange {
    private double mReferenceLow;
    private double mReferenceHigh;
    private double mCriticalLow;
    private double mCriticalHigh;
    private Boolean mFasting; // may be true, false or null
    private int mAgeLowLimit;
    private int mAgeHighLimit;
    private boolean mIsDefaultRangeValue;

    private AnalyteName mAnalyteName;
    private int mAnalyte;

    private int mAgeUnitValue;
    private long mIgnoreInfoValue;
    private long mDependencyInfoValue;

    private Gender mGender = Gender.Unknown;
    private int mGenderValue;


    public double getReferenceLow() {
        return mReferenceLow;
    }

    public void setReferenceLow(double referenceLow) {
        mReferenceLow = referenceLow;
    }

    public double getReferenceHigh() {
        return mReferenceHigh;
    }

    public void setReferenceHigh(double referenceHigh) {
        mReferenceHigh = referenceHigh;
    }

    public double getCriticalLow() {
        return mCriticalLow;
    }

    public void setCriticalLow(double criticalLow) {
        mCriticalLow = criticalLow;
    }

    public double getCriticalHigh() {
        return mCriticalHigh;
    }

    public void setCriticalHigh(double criticalHigh) {
        mCriticalHigh = criticalHigh;
    }

    public Boolean getFasting() {
        return mFasting;
    }

    public void setFasting(Boolean fasting) {
        mFasting = fasting;
    }

    public int getAgeLowLimit() {
        return mAgeLowLimit;
    }

    public void setAgeLowLimit(int ageLowLimit) {
        mAgeLowLimit = ageLowLimit;
    }

    public int getAgeHighLimit() {
        return mAgeHighLimit;
    }

    public void setAgeHighLimit(int ageHighLimit) {
        mAgeHighLimit = ageHighLimit;
    }

    public boolean isDefaultRangeValue() {
        return mIsDefaultRangeValue;
    }

    public void setDefaultRangeValue(boolean defaultRangeValue) {
        mIsDefaultRangeValue = defaultRangeValue;
    }

    public com.epocal.common.types.am.AnalyteName getAnalyteName() {
        return mAnalyteName;
    }

    public void setAnalyteName(com.epocal.common.types.am.AnalyteName analyteName) {
        mAnalyteName = analyteName;
    }

    public int getAnalyte() {
        return mAnalyte;
    }

    public void setAnalyte(int analyte) {
        mAnalyte = analyte;
    }

    public int getAgeUnitValue() {
        return mAgeUnitValue;
    }

    public void setAgeUnitValue(int ageUnitValue) {
        mAgeUnitValue = ageUnitValue;
    }

    public long getIgnoreInfoValue() {
        return mIgnoreInfoValue;
    }

    public void setIgnoreInfoValue(long ignoreInfoValue) {
        mIgnoreInfoValue = ignoreInfoValue;
    }

    public long getDependencyInfoValue() {
        return mDependencyInfoValue;
    }

    public void setDependencyInfoValue(long dependencyInfoValue) {
        mDependencyInfoValue = dependencyInfoValue;
    }

    public com.epocal.common.types.am.Gender getGender() {
        return mGender;
    }

    public void setGender(com.epocal.common.types.am.Gender gender) {
        mGender = gender;
    }

    public int getGenderValue() {
        return mGenderValue;
    }

    public void setGenderValue(int genderValue) {
        mGenderValue = genderValue;
    }

    public TestResultRange()
    {
        mFasting = true;
    }
}
