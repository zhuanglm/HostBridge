package com.epocal.common.realmentities;

import com.epocal.common.epocobjects.ITestField;
import com.epocal.common.types.EpocTestFieldGroupType;
import com.epocal.common.types.EpocTestFieldType;
import com.epocal.common.types.EpocTestFieldOptionType;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bmate on 6/2/2017.
 * Custom epocTest variable input object.
 * The TestRecord -> TestDetail may have a list of these
 */

public class CustomTestVariable extends RealmObject implements ITestField

{
    @PrimaryKey
    private long id =-1;
    private long testRecordId = -1;
    private String name;
    private String value;

    private int mDisplayOrder;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTestRecordId() {
        return testRecordId;
    }

    public void setTestRecordId(long testRecordID) {
        this.testRecordId = testRecordID;
    }

    public int getDisplayOrder() {
        return mDisplayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        mDisplayOrder = displayOrder;
    }

    @Ignore
    private EpocTestFieldOptionType mEpocTestFieldOptionType;
    private int mFieldOptionTypeValue;
    @Ignore
    private EpocTestFieldType mFieldType = EpocTestFieldType.CUSTOM;
    private int mFieldTypeValue = 999;
    @Ignore
    private EpocTestFieldGroupType mFieldGroupType;
    private int mFieldGroupTypeValue;

    public EpocTestFieldOptionType getEpocTestFieldOptionType() {
        return EpocTestFieldOptionType.fromInt(getFieldOptionTypeValue());
    }

    public void setEpocTestFieldOptionType(EpocTestFieldOptionType epocTestFieldOptionType) {
        setFieldOptionTypeValue(epocTestFieldOptionType.value);
    }

    private int getFieldOptionTypeValue() {
        return mFieldOptionTypeValue;
    }

    private void setFieldOptionTypeValue(int fieldOptionTypeValue) {
        mFieldOptionTypeValue = fieldOptionTypeValue;
    }

    public EpocTestFieldType getFieldType() {
        return EpocTestFieldType.fromInt(getFieldTypeValue());
    }
    private int getFieldTypeValue() {
        return mFieldTypeValue;
    }


    public EpocTestFieldGroupType getFieldGroupType() {
        return EpocTestFieldGroupType.fromInt(getFieldGroupTypeValue());
    }

    public void setFieldGroupType(EpocTestFieldGroupType fieldGroupType) {
        setFieldGroupTypeValue(fieldGroupType.value);
    }

    private int getFieldGroupTypeValue() {
        return mFieldGroupTypeValue;
    }

    private void setFieldGroupTypeValue(int fieldGroupTypeValue) {
        mFieldGroupTypeValue = fieldGroupTypeValue;
    }

    @Override
    public boolean isCustom() {
        return true;
    }

}
