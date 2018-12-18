package com.epocal.common.realmentities;

import com.epocal.common.types.InputFieldType;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 8/3/2017.
 */

public class InputFieldConfig extends RealmObject{
    @Ignore
    private InputFieldType mInputFieldType;
    private Integer InputFieldTypeValue;
    private Integer mTrimBeginning;
    private Integer mTrimEnding;
    private Integer mTrim2DBeginning;
    private Integer mTrim2DEnding;
    private Integer mMaximumLength;
    private Integer mMinimumLength;
    private Integer mBarcodes;

    public InputFieldType getInputFieldType() {
        return InputFieldType.fromInt(getInputFieldTypeValue());
    }

    public void setInputFieldType(InputFieldType inputFieldType) {
        setInputFieldTypeValue(inputFieldType.value);
    }

    private Integer getInputFieldTypeValue() {
        return InputFieldTypeValue;
    }

    private void setInputFieldTypeValue(Integer inputFieldTypeValue) {
        InputFieldTypeValue = inputFieldTypeValue;
    }

    public Integer getTrimBeginning() {
        return mTrimBeginning;
    }

    public void setTrimBeginning(Integer trimBeginning) {
        mTrimBeginning = trimBeginning;
    }

    public Integer getTrimEnding() {
        return mTrimEnding;
    }

    public void setTrimEnding(Integer trimEnding) {
        mTrimEnding = trimEnding;
    }

    public Integer getTrim2DBeginning() {
        return mTrim2DBeginning;
    }

    public void setTrim2DBeginning(Integer trim2DBeginning) {
        mTrim2DBeginning = trim2DBeginning;
    }

    public Integer getTrim2DEnding() {
        return mTrim2DEnding;
    }

    public void setTrim2DEnding(Integer trim2DEnding) {
        mTrim2DEnding = trim2DEnding;
    }

    public Integer getMaximumLength() {
        return mMaximumLength;
    }

    public void setMaximumLength(Integer maximumLength) {
        mMaximumLength = maximumLength;
    }

    public Integer getMinimumLength() {
        return mMinimumLength;
    }

    public void setMinimumLength(Integer minimumLength) {
        mMinimumLength = minimumLength;
    }

    public Integer getBarcodes() {
        return mBarcodes;
    }

    public void setBarcodes(Integer barcodes) {
        mBarcodes = barcodes;
    }
}
