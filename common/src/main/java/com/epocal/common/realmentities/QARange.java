package com.epocal.common.realmentities;

import com.epocal.common.epocobjects.ICloneable;
import com.epocal.util.StringUtil;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by bmate on 6/5/2017.
 */

public class QARange extends RealmObject implements ICloneable{

    private Boolean isCustom;
    private String name;
    private String cardTypes;
    private RealmList<QARangeValue> rangeValues;
    @Ignore
    private ArrayList<String > supportedCardList = new ArrayList<String>();

    public ArrayList<String> getSupportedCardList() {
                return supportedCardList;
    }

//    public void setSupportedCardList(ArrayList<String> supportedCardList) {
//        this.supportedCardList = supportedCardList;
//    }

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
        if (!StringUtil.empty(cardTypes)){
            String[] splitValue = cardTypes.split("|");
            supportedCardList.clear();

            for (String cardType:splitValue) {
                supportedCardList.add(cardType);
            }
        }
    }

    public RealmList<QARangeValue> getRangeValues() {
        return rangeValues;
    }

    public void setRangeValues(RealmList<QARangeValue> rangeValues) {
        this.rangeValues = rangeValues;
    }
    // creates an unmanaged clone
    private QARange cloneThis(){
        QARange retval = new QARange();
        retval.setName(this.getName());
        retval.setCardTypes(this.getCardTypes());
        retval.setCustom(this.getCustom());
        if (retval.getRangeValues()==null){
            retval.setRangeValues(new RealmList<QARangeValue>());
        }

        for (QARangeValue val: getRangeValues()) {
            retval.getRangeValues().add(val);
        }
        for (String cardType : getSupportedCardList()){
            retval.getSupportedCardList().add(cardType);
        }
        return retval;
    }
    @Override
    public Object Clone() {
        return cloneThis();
    }
}
