package com.epocal.common.realmentities;

import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.Units;
import com.epocal.util.StringUtil;


import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 6/5/2017.
 */

public class QARangeValue extends RealmObject {

    private Double referenceLow;
    private Double referenceHigh;
    private String cardLotType; //dd
    private String cardLotRange; // ddddd[-ddddd]
    private Integer fluidRowId; // for matching range with fluid data
    private QARange range;
    private Boolean isCardDependant;

    public Double getReferenceLow() {
        return referenceLow;
    }

    public void setReferenceLow(Double referenceLow) {
        this.referenceLow = referenceLow;
    }

    public Double getReferenceHigh() {
        return referenceHigh;
    }

    public void setReferenceHigh(Double referenceHigh) {
        this.referenceHigh = referenceHigh;
    }

    public String getCardLotType() {
        return cardLotType;
    }

    public void setCardLotType(String cardLotType) {
        this.cardLotType = cardLotType;
    }

    public String getCardLotRange() {
        return cardLotRange;
    }

    public void setCardLotRange(String cardLotRange) {
        this.cardLotRange = cardLotRange;
    }

    public Integer getFluidRowId() {
        return fluidRowId;
    }

    public void setFluidRowId(Integer fluidRowId) {
        this.fluidRowId = fluidRowId;
    }

    public QARange getRange() {
        return range;
    }

    public void setRange(QARange range) {
        this.range = range;
    }

    public Boolean getCardDependant() {
        return (!StringUtil.empty(getCardLotType())&& !StringUtil.empty(getCardLotRange()));
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
    private Units unitType;
    private Integer unit;

    public Units getUnitType() {
        return Units.fromInt(getUnit());
    }
    public void setUnitType(Units unitType) {
        setUnit(unitType.value);
    }
    private Integer getUnit() {
        return unit;
    }
    private void setUnit(Integer unit) {
        this.unit = unit;
    }


}
