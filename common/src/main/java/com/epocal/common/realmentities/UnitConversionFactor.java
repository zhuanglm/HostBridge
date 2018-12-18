package com.epocal.common.realmentities;

import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.Units;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 6/8/2017.
 */

public class UnitConversionFactor extends RealmObject{
    private double mConversionValue;

    public double getConversionValue() {
        return mConversionValue;
    }

    public void setConversionValue(double conversionValue) {
        mConversionValue = conversionValue;
    }

    @Ignore
    private AnalyteName analyteName;
    private Integer analyte;

    public AnalyteName getAnalyteName() {
        return AnalyteName.fromInt(getAnalyte());
    }
    public void setAnalyteName(AnalyteName analyteName) {
        setAnalyte(analyteName.value);
    }
    private Integer getAnalyte() {
        return analyte;
    }
    private void setAnalyte(Integer analyte) {
        this.analyte = analyte;
    }

    @Ignore
    private Units fromunitType;
    private Integer fromunit;

    public Units getfromUnitType() {
        return Units.fromInt(getfromUnit());
    }
    public void setfromUnitType(Units unitType) {
        setfromUnit(unitType.value);
    }
    private Integer getfromUnit() {
        return fromunit;
    }
    private void setfromUnit(Integer fromunit) {
        this.fromunit = fromunit;
    }

    @Ignore
    private Units tounitType;
    private Integer tounit;

    public Units gettoUnitType() {
        return Units.fromInt(gettoUnit());
    }
    public void settoUnitType(Units unitType) {
        settoUnit(unitType.value);
    }
    private Integer gettoUnit() {
        return tounit;
    }
    private void settoUnit(Integer tounit) {
        this.tounit = tounit;
    }

}
