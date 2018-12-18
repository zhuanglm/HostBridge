package com.epocal.common.realmentities;

import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.AnalyteType;
import com.epocal.common.types.am.Units;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 6/8/2017.
 */

public class AnalyteUnitInfo extends RealmObject {


    private String mPrecision;
    private Double mFactoryReportableHigh;
    private Double mFactoryReportableLow;
    private Boolean isFactoryReference;

    public String getPrecision() {
        return mPrecision;
    }

    public void setPrecision(String precision) {
        mPrecision = precision;
    }

    public Double getFactoryReportableHigh() {
        return mFactoryReportableHigh;
    }

    public void setFactoryReportableHigh(Double factoryReportableHigh) {
        mFactoryReportableHigh = factoryReportableHigh;
    }

    public Double getFactoryReportableLow() {
        return mFactoryReportableLow;
    }

    public void setFactoryReportableLow(Double factoryReportableLow) {
        mFactoryReportableLow = factoryReportableLow;
    }

    public Boolean getFactoryReference() {
        return isFactoryReference;
    }

    public void setFactoryReference(Boolean factoryReference) {
        isFactoryReference = factoryReference;
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
    private Units unitType;
    private Integer unit;

    public Units getUnitType() {
        return Units.fromInt(getUnit());
    }

    public void setUnitType(Units unitType) {
        setUnit(unitType.value);
    }

    private Integer getUnit() {
        return unit;
    }

    private void setUnit(Integer unit) {
        this.unit = unit;
    }

    @Ignore
    private AnalyteType mAnalyteType;
    private Integer typeOfAnalyte;

    public AnalyteType getAnalyteType() {
        return AnalyteType.fromInt(getTypeOfAnalyte());
    }

    public void setAnalyteType(AnalyteType analyteType) {
        setTypeOfAnalyte(analyteType.value);
    }

    private Integer getTypeOfAnalyte() {
        return typeOfAnalyte;
    }

    private void setTypeOfAnalyte(Integer typeOfAnalyte) {
        this.typeOfAnalyte = typeOfAnalyte;
    }


}
