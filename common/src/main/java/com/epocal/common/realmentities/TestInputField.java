package com.epocal.common.realmentities;

import com.epocal.common.epocobjects.ITestField;
import com.epocal.common.types.EpocTestFieldGroupType;
import com.epocal.common.types.EpocTestFieldType;
import com.epocal.common.types.EpocTestFieldOptionType;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 7/21/2017.
 * Factory epocTest variable input object, UX.
 * The workflowitem is built-up of these.
 */

public class TestInputField extends RealmObject implements ITestField{
    private int mDisplayOrder;

    public int getDisplayOrder() {
        return mDisplayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        mDisplayOrder = displayOrder;
    }

    @Ignore
    private EpocTestFieldType mFieldType;
    private int mFieldTypeValue;

    public EpocTestFieldType getFieldType() {
        return EpocTestFieldType.fromInt(getFieldTypeValue());
    }

    public void setFieldType(EpocTestFieldType fieldType) {
       setFieldTypeValue(fieldType.value);
    }

    private int getFieldTypeValue() {
        return mFieldTypeValue;
    }

    private void setFieldTypeValue(int fieldTypeValue) {
        mFieldTypeValue = fieldTypeValue;
    }



    public boolean isCustom() {
        return false;
    }
}
