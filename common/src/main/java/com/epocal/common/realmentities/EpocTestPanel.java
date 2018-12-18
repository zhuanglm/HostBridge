package com.epocal.common.realmentities;

import com.epocal.common.types.EpocTestPanelType;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.util.EnumSetUtil;

import java.util.EnumSet;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 8/15/2017.
 */

public class EpocTestPanel extends RealmObject{
    private String mName;
    private int mDisplayOrder;
    private boolean mIsCustom;


    public boolean isCustom() {
        return mIsCustom;
    }

    public void setCustom(boolean custom) {
        mIsCustom = custom;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getDisplayOrder() {
        return mDisplayOrder;
    }

    public void setDisplayOrder(int mDisplayOrder) {
        this.mDisplayOrder = mDisplayOrder;
    }

    @Ignore
    private EpocTestPanelType mTestPanelType;
    private int testPanelTypeValue;

    public EpocTestPanelType getTestPanelType() {
        return EpocTestPanelType.fromInt(getTestPanelTypeValue());
    }

    public void setTestPanelType(EpocTestPanelType testPanelType) {
        setTestPanelTypeValue(testPanelType.value);
    }

    private int getTestPanelTypeValue() {
        return testPanelTypeValue;
    }

    private void setTestPanelTypeValue(int testPanelTypeValue) {
        this.testPanelTypeValue = testPanelTypeValue;
    }

    // not to be synced with EDM
    @Ignore
    private EnumSet<AnalyteName> analyteNamesEnumSet ;
    private long analyteNamesValue = AnalyteName.ENUM_UNINITIALIZED.value;

    public EnumSet<AnalyteName> getAnalyteNamesEnumSet() {
        return EnumSetUtil.toSet(getAnalyteNamesValue(),AnalyteName.class);
    }

    public void setAnalyteNamesEnumSet(EnumSet<AnalyteName> analyteNameEnumSet) {
        setAnalyteNamesValue(EnumSetUtil.encode(analyteNameEnumSet));
    }

    private long getAnalyteNamesValue() {
        return analyteNamesValue;
    }

    private void setAnalyteNamesValue(long enumSetValue) {

        this.analyteNamesValue = enumSetValue;
    }

}
