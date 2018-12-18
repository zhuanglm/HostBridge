package com.epocal.common.realmentities;

import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.EnabledSelectedOptionType;
import com.epocal.common.types.am.Units;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 6/8/2017.
 */

public class Analyte extends RealmObject {

    private double reportableHigh;
    private double defaultUnitReportableHigh;
    private double reportableLow;
    private double defaultUnitReportableLow;
    private int displayorder;
    private RealmList<AnalyteUnitInfo> mMetaInfo;
    private RealmList<UnitConversionFactor> mUnitConversionFactors;

    public double getReportableHigh() {
        return reportableHigh;
    }

    public void setReportableHigh(double reportableHigh) {
        this.reportableHigh = reportableHigh;
    }

    public double getDefaultUnitReportableHigh() {
        return defaultUnitReportableHigh;
    }

    public void setDefaultUnitReportableHigh(double defaultUnitReportableHigh) {
        this.defaultUnitReportableHigh = defaultUnitReportableHigh;
    }

    public double getReportableLow() {
        return reportableLow;
    }

    public void setReportableLow(double reportableLow) {
        this.reportableLow = reportableLow;
    }

    public double getDefaultUnitReportableLow() {
        return defaultUnitReportableLow;
    }

    public void setDefaultUnitReportableLow(double defaultUnitReportableLow) {
        this.defaultUnitReportableLow = defaultUnitReportableLow;
    }

    public int getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(int displayorder) {
        this.displayorder = displayorder;
    }

    public RealmList<AnalyteUnitInfo> getMetaInfo() {
        return mMetaInfo;
    }

    public void setMetaInfo(RealmList<AnalyteUnitInfo> metaInfo) {
        mMetaInfo = metaInfo;
    }

    public RealmList<UnitConversionFactor> getUnitConversionFactors() {
        return mUnitConversionFactors;
    }

    public void setUnitConversionFactors(RealmList<UnitConversionFactor> unitConversionFactors) {
        mUnitConversionFactors = unitConversionFactors;
    }

    @Ignore
    private EnabledSelectedOptionType mOptionType;
    private Integer enablement;

    public EnabledSelectedOptionType getOptionType() {
        return EnabledSelectedOptionType.fromInt(getEnablement());
    }

    public void setOptionType(EnabledSelectedOptionType optionType) {
        setEnablement(optionType.value);
    }

    private Integer getEnablement() {
        return enablement;
    }

    private void setEnablement(Integer enablement) {
        this.enablement = enablement;
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
}
