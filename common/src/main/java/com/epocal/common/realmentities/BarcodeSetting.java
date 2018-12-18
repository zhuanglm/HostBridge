package com.epocal.common.realmentities;

import com.epocal.common.types.BarcodeSymbologiesType;
import com.epocal.common.types.InputFieldType;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BarcodeSetting  extends RealmObject {

    @PrimaryKey
    private long id;
    private int InputField;
    private int CropBegin;
    private int CropEnd;
    private long Symbologies;

    public BarcodeSetting() {}

    public BarcodeSetting(BarcodeSetting src) {
        if (src != null) {
            id = src.getId();
            InputField = src.getInputField();
            CropBegin = src.getCropBegin();
            CropEnd = src.getCropEnd();
            Symbologies = src.getSymbologies();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getInputField()
    {
        return InputField;
    }

    public void setInputField(int mInputField)
    {
        InputField = mInputField;
    }

    public InputFieldType getInputFieldType()
    {
        return InputFieldType.fromInt(InputField);
    }

    public void setInputFieldType(InputFieldType mInputFieldType)
    {
        InputField = mInputFieldType.value;
    }

    public int getCropBegin()
    {
        return CropBegin;
    }

    public void setCropBegin(int mCropBegin)
    {
        CropBegin = mCropBegin;
    }

    public int getCropEnd()
    {
        return CropEnd;
    }

    public void setCropEnd(int mCropEnd)
    {
        CropEnd = mCropEnd;
    }

    public long getSymbologies()
    {
        return Symbologies;
    }

    public void setSymbologies(long mSymbologies)
    {
        Symbologies = mSymbologies;
    }

    public void resetFactoryDefault() {
        CropBegin = 0;
        CropEnd = 0;
        Symbologies = BarcodeSymbologiesType.noBarcode.value;
    }

}
