package com.epocal.common.realmentities;

import com.epocal.common.types.QAFluidType;
import com.epocal.common.types.RangeIgnoreInfo;
import com.epocal.common.types.TestType;
import com.epocal.util.EnumSetUtil;

import java.util.EnumSet;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bmate on 6/1/2017.
 */

public class QASampleInfo extends RealmObject{
    @PrimaryKey
    private long id =-1;
    private String name;
    private String description;
    private Boolean isCustom;
    private QARange qaRange;
    private EVADCardLotDescription cardLotDescriptor;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCustom() {
        return isCustom;
    }

    public void setCustom(Boolean custom) {
        isCustom = custom;
    }

    public QARange getQaRange() {
        return qaRange;
    }

    public void setQaRange(QARange qaRange) {
        this.qaRange = qaRange;
    }

    public EVADCardLotDescription getCardLotDescriptor() {
        return cardLotDescriptor;
    }

    public void setCardLotDescriptor(EVADCardLotDescription cardLotDescriptor) {
        this.cardLotDescriptor = cardLotDescriptor;
    }

    @Ignore
    private EnumSet<TestType> type = EnumSet.of(TestType.Unknown, TestType.Blood, TestType.QualityControl, TestType.CalVer, TestType.Proficiency, TestType.ThermalCheck, TestType.EQC, TestType.Other);
    private long testTypeValue;

    public EnumSet<TestType> getTestType() {
        return EnumSetUtil.toSet(getTestTypeValue(),TestType.class);
    }

    public void setTestType(EnumSet<TestType> type) {
        setTestTypeValue(EnumSetUtil.encode(type));
    }

    private long getTestTypeValue() {
        return testTypeValue;
    }

    private void setTestTypeValue(long testTypeValue) {
        this.testTypeValue = testTypeValue;
    }

    @Ignore
    private QAFluidType qaFluidType;
    private Integer qaFluid;

    public QAFluidType getQaFluidType() {
        return QAFluidType.fromInt(getQaFluid());
    }
    public void setQaFluidType(QAFluidType qaFluidType) {
        setQaFluid(qaFluidType.value);
    }
    private Integer getQaFluid() {
        return qaFluid;
    }
    private void setQaFluid(Integer qaFluid) {
        this.qaFluid = qaFluid;
    }
}
