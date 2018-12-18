package com.epocal.common.realmentities;

import com.epocal.common.types.AgeUnitType;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.Gender;
import com.epocal.common.types.RangeIgnoreInfo;
import com.epocal.common.types.RangeValueMetaInfo;
import com.epocal.util.EnumSetUtil;

import java.util.EnumSet;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 6/6/2017.
 */

public class RangeValue extends RealmObject {
    private Double referenceLow;
    private Double referenceHigh;
    private Double criticalLow;
    private Double criticalHigh;
    private Range mRange;
    private Boolean fasting; // may be true, false or null
    private Integer ageLowLimit;
    private Integer ageHighLimit;
    private boolean isDefaultRangeValue;

    public boolean isDefaultRangeValue() {
        return isDefaultRangeValue;
    }

    public void setDefaultRangeValue(boolean defaultRangeValue) {
        isDefaultRangeValue = defaultRangeValue;
    }

    public Double getReferenceLow() {
        return referenceLow;
    }

    public void setReferenceLow(Double referenceLow) {
        this.referenceLow = referenceLow;
    }

    public Double getReferenceHigh() {
        return referenceHigh;
    }

    public void setReferenceHigh(Double referenceHigh) {
        this.referenceHigh = referenceHigh;
    }

    public Double getCriticalLow() {
        return criticalLow;
    }

    public void setCriticalLow(Double criticalLow) {
        this.criticalLow = criticalLow;
    }

    public Double getCriticalHigh() {
        return criticalHigh;
    }

    public void setCriticalHigh(Double criticalHigh) {
        this.criticalHigh = criticalHigh;
    }

    public Range getRange() {
        return mRange;
    }

    public void setRange(Range range) {
        mRange = range;
    }

    public Integer getAgeLowLimit() {
        return ageLowLimit;
    }

    public void setAgeLowLimit(Integer ageLowLimit) {
        this.ageLowLimit = ageLowLimit;
        adjustAgeDependancy();
    }

    public Integer getAgeHighLimit() {
        return ageHighLimit;
    }

    public void setAgeHighLimit(Integer ageHighLimit) {
        this.ageHighLimit = ageHighLimit;
        adjustAgeDependancy();
    }

    public Boolean getFasting() {
        return fasting;

    }

    public void setFasting(Boolean fasting) {
        this.fasting = fasting;
        adjustFastingDependancy();

    }

    @Ignore
    private AnalyteName analyteName;
    private Integer analyte;

    public AnalyteName getAnalyteName() {
        return AnalyteName.fromInt(getAnalyte());
    }

    public void setAnalyteName(AnalyteName analyteName) {
        setAnalyte(analyteName.value);
    }

    private Integer getAnalyte() {
        return analyte;
    }

    private void setAnalyte(Integer analyte) {
        this.analyte = analyte;
    }

    @Ignore
    private AgeUnitType mAgeUnitType = AgeUnitType.UNKNOWN;
    private Integer ageUnitValue;

    public AgeUnitType getAgeUnitType() {
        return AgeUnitType.fromInt(getAgeUnitValue());
    }

    public void setAgeUnitType(AgeUnitType ageUnitType) {
        setAgeUnitValue(ageUnitType.value);
    }

    private Integer getAgeUnitValue() {
        return ageUnitValue;
    }

    private  void setAgeUnitValue(Integer ageUnitValue) {
        this.ageUnitValue = ageUnitValue;
    }

    @Ignore
    private EnumSet<RangeIgnoreInfo> ignoreInfo = EnumSet.of(RangeIgnoreInfo.IgnoreNone);
    private long ignoreInfoValue;

    public EnumSet<RangeIgnoreInfo> getIgnoreInfo() {
        return EnumSetUtil.toSet(getIgnoreInfoValue(),RangeIgnoreInfo.class);
    }

    public void setIgnoreInfo(EnumSet<RangeIgnoreInfo> ignoreInfo) {
        setIgnoreInfoValue(EnumSetUtil.encode(ignoreInfo));
    }

    private long getIgnoreInfoValue() {
        return ignoreInfoValue;
    }

    private void setIgnoreInfoValue(long ignoreInfoValue) {
        this.ignoreInfoValue = ignoreInfoValue;
    }

    @Ignore
    // not to be synced with EDM
    private EnumSet<RangeValueMetaInfo> dependencyInfo = EnumSet.of(RangeValueMetaInfo.NO_DEPENDENCY);
    private long dependencyInfoValue;

    public EnumSet<RangeValueMetaInfo> getDependencyInfo() {
        return EnumSetUtil.toSet(getDependencyInfoValue(),RangeValueMetaInfo.class);
    }
    // this is internally set when gender or agelimits  or fasting are set
    private void setDependencyInfo(EnumSet<RangeValueMetaInfo> dependencyInfo) {
        setDependencyInfoValue(EnumSetUtil.encode(dependencyInfo));
    }

    private long getDependencyInfoValue() {
        return dependencyInfoValue;
    }

    private void setDependencyInfoValue(long dependencyInfoValue) {
        this.dependencyInfoValue = dependencyInfoValue;
    }
    @Ignore
    private Gender mGender = Gender.Unknown;
    private Integer genderValue;

    public Gender getGender() {
        return Gender.fromInt(getGenderValue());
    }

    public void setGender(Gender gender) {
        setGenderValue(gender.value);
        adjustGenderDependancy();
    }

    private Integer getGenderValue() {
        return genderValue;

    }

    private void setGenderValue(Integer genderValue) {

        this.genderValue = genderValue;
    }
    private void adjustAgeDependancy(){

        dependencyInfo.remove(RangeValueMetaInfo.DEPENDS_ON_AGE);
           if (this.ageLowLimit!=null && this.ageHighLimit!=null && this.ageHighLimit>this.ageLowLimit){
               dependencyInfo.add(RangeValueMetaInfo.DEPENDS_ON_AGE);
           }
    }
    private void adjustFastingDependancy(){
        dependencyInfo.remove(RangeValueMetaInfo.DEPENDS_ON_FASTING);
        if (this.fasting){
            dependencyInfo.add(RangeValueMetaInfo.DEPENDS_ON_FASTING);
        }
    }
    private void adjustGenderDependancy(){
        dependencyInfo.remove(RangeValueMetaInfo.DEPENDS_ON_GENDER);
        if (this.mGender.compareTo(Gender.Unknown)!=0){
            dependencyInfo.add(RangeValueMetaInfo.DEPENDS_ON_FASTING);
        }
    }
}
