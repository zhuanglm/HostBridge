package com.epocal.common.realmentities;

import com.epocal.common.epocobjects.ICloneable;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.ResultStatus;
import com.epocal.common.types.am.Units;


import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bmate on 4/3/2017.
 */

public class TestResult extends RealmObject {
    @PrimaryKey
    private long id =-1;
    private long testRecordId =-1;
    private String barcode = "";
    private Double value;
    private Double reportableLow;
    private Double reportableHigh;
    private Double referenceLow;
    private Double referenceHigh;
    private Double criticalLow;
    private Double criticalHigh;
    private int returnCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTestRecordId() {
        return testRecordId;
    }

    public void setTestRecordId(long testRecordId) {
        this.testRecordId = testRecordId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getReportableLow() {
        return reportableLow;
    }

    public void setReportableLow(Double reportableLow) {
        this.reportableLow = reportableLow;
    }

    public Double getReportableHigh() {
        return reportableHigh;
    }

    public void setReportableHigh(Double reporableHigh) {
        this.reportableHigh = reporableHigh;
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

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Ignore
    private ResultStatus resultStatus;
    private int resultStatusType;

    public ResultStatus getResultStatus() {
        return ResultStatus.fromInt(getResultStatusType());
    }
    public void setResultStatus(ResultStatus resultStatus) {
        setResultStatusType(resultStatus.value);
    }
    private int getResultStatusType() {
        return resultStatusType;
    }
    private void setResultStatusType(int resultStatusType) {
        this.resultStatusType = resultStatusType;
    }

    @Ignore
    private AnalyteName analyteName;
    private int analyte;

    public AnalyteName getAnalyteName() {
        return AnalyteName.fromInt(getAnalyte());
    }

    public void setAnalyteName(AnalyteName analyteName) {
        setAnalyte(analyteName.value);
    }

    private int getAnalyte() {
        return analyte;
    }

    private void setAnalyte(int analyte) {
        this.analyte = analyte;
    }

    @Ignore
    private Units unitType;
    private int unit;

    public Units getUnitType() {
        return Units.fromInt(getUnit());
    }

    public void setUnitType(Units unitType) {
        setUnit(unitType.value);
    }

    private int getUnit() {
        return unit;
    }

    private void setUnit(int unit) {
        this.unit = unit;
    }

    public void updateFrom(TestResult input){
        this.setAnalyteName(input.getAnalyteName());
        this.setBarcode(input.getBarcode());
        this.setCriticalHigh(input.getCriticalHigh());
        this.setCriticalLow(input.getCriticalLow());
        this.setReferenceHigh(input.getReferenceHigh());
        this.setReferenceLow(input.getReferenceLow());
        this.setReportableHigh(input.getReportableHigh());
        this.setReportableLow(input.getReportableLow());
        this.setResultStatus(input.getResultStatus());
        this.setReturnCode(input.getReturnCode());
        this.setUnitType(input.getUnitType());
        this.setValue(input.getValue());
    }

}
