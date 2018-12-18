package com.epocal.common.realmentities;

import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.BloodSampleType;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 6/6/2017.
 */

public class Range extends RealmObject {
    private Boolean isCustom;
    private String name;
    private String cardTypes;
    private RealmList<RangeValue> rangeValues;

    public Boolean getCustom() {
        return isCustom;
    }

    public void setCustom(Boolean custom) {
        isCustom = custom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(String cardTypes) {
        this.cardTypes = cardTypes;
    }

    public RealmList<RangeValue> getRangeValues() {
        return rangeValues;
    }

    public void setRangeValues(RealmList<RangeValue> rangeValues) {
        this.rangeValues = rangeValues;
    }

    @Ignore
    private BloodSampleType sampleType;
    private Integer bloodSampleType;

    public BloodSampleType getSampleType() {
        return BloodSampleType.fromInt(getBloodSampleType());
    }
    public void setSampleType(BloodSampleType sampleType) {
        setBloodSampleType(sampleType.value);
    }
    private Integer getBloodSampleType() {
        return bloodSampleType;
    }
    private void setBloodSampleType(Integer bloodSampleType) {
        this.bloodSampleType = bloodSampleType;
    }

    public RangeValue getRangevalue(AnalyteName analyteName) {
        RangeValue retval = null;
        if (rangeValues!=null && rangeValues.size()>0){
            for (RangeValue rv: rangeValues){
                if (rv.getAnalyteName().equals(analyteName)){
                   retval = rv;
                    break;
                }
            }
        }
        return retval;
    }
}
