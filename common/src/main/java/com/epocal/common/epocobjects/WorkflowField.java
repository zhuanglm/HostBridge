package com.epocal.common.epocobjects;

import com.epocal.common.types.EpocTestFieldGroupType;
import com.epocal.common.types.EpocTestFieldType;

/**
 * Created by bmate on 29/11/2017.
 */

public class WorkflowField {
    private EpocTestFieldType mEpocTestFieldType;
    private EpocTestFieldGroupType mEpocTestFieldGroupType;
    private boolean mEditable = true;

    public EpocTestFieldType getEpocTestFieldType() {
        return mEpocTestFieldType;
    }

    public void setEpocTestFieldType(EpocTestFieldType epocTestFieldType) {
        mEpocTestFieldType = epocTestFieldType;
    }

    public EpocTestFieldGroupType getEpocTestFieldGroupType() {
        return mEpocTestFieldGroupType;
    }

    public void setEpocTestFieldGroupType(EpocTestFieldGroupType epocTestFieldGroupType) {
        mEpocTestFieldGroupType = epocTestFieldGroupType;
    }

    public boolean isEditable() {
        return mEditable;
    }

    public void setEditable(boolean editable) {
        mEditable = editable;
    }
}
