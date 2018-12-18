package com.epocal.common.realmentities;

import com.epocal.common.types.EpocTestFieldGroupType;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 7/21/2017.
 */

public class WorkflowItem extends RealmObject {
    @Ignore
    private EpocTestFieldGroupType mFieldGroupType;
    private int mFieldGroupTypeValue;

    private RealmList<CustomTestInputField> mCustomFieldList;
    private RealmList<TestInputField> mFieldList;
    private int mDisplayOrder;
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

    public RealmList<CustomTestInputField> getCustomFieldList() {
        return mCustomFieldList;
    }

    public void setCustomFieldList(RealmList<CustomTestInputField> customFieldList) {
        mCustomFieldList = customFieldList;
    }

    public RealmList<TestInputField> getFieldList() {
        return mFieldList;
    }

    public void setFieldList(RealmList<TestInputField> fieldList) {
        mFieldList = fieldList;
    }

    public int getDisplayOrder() {
        return mDisplayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        mDisplayOrder = displayOrder;
    }
}
