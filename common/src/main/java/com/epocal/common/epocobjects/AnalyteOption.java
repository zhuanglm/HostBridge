package com.epocal.common.epocobjects;

import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.EnabledSelectedOptionType;

/**
 * Created by bmate on 6/8/2017.
 */

public class AnalyteOption {
    private AnalyteName mAnalyteName;
    private int mDisplayOrder;
    private EnabledSelectedOptionType mOptionType;

    public AnalyteName getAnalyteName() {
        return mAnalyteName;
    }

    public void setAnalyteName(AnalyteName analyteName) {
        mAnalyteName = analyteName;
    }

    public int getDisplayOrder() {
        return mDisplayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        mDisplayOrder = displayOrder;
    }

    public EnabledSelectedOptionType getOptionType() {
        return mOptionType;
    }

    public void setOptionType(EnabledSelectedOptionType optionType) {
        mOptionType = optionType;
    }
}
