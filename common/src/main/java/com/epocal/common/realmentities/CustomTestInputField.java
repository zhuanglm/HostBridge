package com.epocal.common.realmentities;

import com.epocal.common.epocobjects.ITestField;
import com.epocal.common.types.EpocTestFieldGroupType;
import com.epocal.common.types.EpocTestFieldOptionType;
import com.epocal.common.types.EpocTestFieldType;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 8/24/2017.
 * used in a WorkflowItem object for UX display in stepper
 */

public class CustomTestInputField extends RealmObject implements ITestField {
    private String name;
    private boolean mEditable;
    private int mDisplayOrder;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEditable() {
        return mEditable;
    }

    public void setEditable(boolean editable) {
        mEditable = editable;
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

