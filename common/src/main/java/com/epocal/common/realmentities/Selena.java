package com.epocal.common.realmentities;

import com.epocal.common.types.EnabledSelectedOptionType;
import com.epocal.common.types.SelenaFamilyType;
import com.epocal.common.types.SelenaOperationTypes;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 8/8/2017.
 */

public class Selena extends RealmObject {
    private int mOrder = -1;
    private boolean mIsCustom = false;
    private String mName;
    private int mMapId;

    public int getMapId() {
        return mMapId;
    }

    public void setMapId(int mMapId) {
        this.mMapId = mMapId;
    }

    @Ignore
    private SelenaFamilyType mSelenaFamilyType;
    private Integer mSelenaFamilyTypeValue;
    @Ignore
    private EnabledSelectedOptionType mEnabledSelectedOptionType;
    private Integer mEnabledSelectedOptionTypeValue;

    public int getOrder() {
        return mOrder;
    }

    public void setOrder(int order) {
        mOrder = order;
    }

    public boolean isCustom() {
        return mIsCustom;
    }

    public void setCustom(boolean custom) {
        mIsCustom = custom;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public SelenaFamilyType getSelenaFamilyType() {
        return SelenaFamilyType.fromInt(getSelenaFamilyTypeValue());
    }

    public void setSelenaFamilyType(SelenaFamilyType selenaFamilyType) {
        setSelenaFamilyTypeValue(selenaFamilyType.value);
    }

    private Integer getSelenaFamilyTypeValue() {
        return mSelenaFamilyTypeValue;
    }

    private void setSelenaFamilyTypeValue(Integer selenaFamilyTypeValue) {
        mSelenaFamilyTypeValue = selenaFamilyTypeValue;
    }

    public EnabledSelectedOptionType getEnabledSelectedOptionType() {
        return EnabledSelectedOptionType.fromInt(getEnabledSelectedOptionTypeValue());
    }

    public void setEnabledSelectedOptionType(EnabledSelectedOptionType enabledSelectedOptionType) {
        setEnabledSelectedOptionTypeValue(enabledSelectedOptionType.value);
    }

    public void setEnabledSelectedOptionType(SelenaOperationTypes selenaOperationTypes) {
        setEnabledSelectedOptionTypeValue(selenaOperationTypes.value);
    }

    private Integer getEnabledSelectedOptionTypeValue() {
        return mEnabledSelectedOptionTypeValue;
    }

    private void setEnabledSelectedOptionTypeValue(Integer enabledSelectedOptionTypeValue) {
        mEnabledSelectedOptionTypeValue = enabledSelectedOptionTypeValue;
    }
}
