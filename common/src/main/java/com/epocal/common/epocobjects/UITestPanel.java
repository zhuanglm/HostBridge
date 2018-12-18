package com.epocal.common.epocobjects;

import com.epocal.common.types.EpocTestPanelType;
import com.epocal.common.types.am.AnalyteName;

import java.util.ArrayList;

/**
 * Created by bmate on 26/09/2017.
 */

public class UITestPanel {
    private String mName;
    private boolean isSelected;
    private EpocTestPanelType mTestPanelType;
    private int mDisplayOrder;

    public EpocTestPanelType getTestPanelType() {
        return mTestPanelType;
    }

    public void setTestPanelType(EpocTestPanelType testPanelType) {
        mTestPanelType = testPanelType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }


    public int getDisplayOrder() {
        return mDisplayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        mDisplayOrder = displayOrder;
    }

}
