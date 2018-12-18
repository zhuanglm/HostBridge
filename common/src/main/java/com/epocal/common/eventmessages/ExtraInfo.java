package com.epocal.common.eventmessages;

/**
 * Created by bmate on 7/25/2017.
 */

public class ExtraInfo {
    private Object mValue;
    private String mName;
    public ExtraInfo (String name, Object value){
        this.mName = name;
        this.mValue = value;
    }
    public Object getValue() {
        return mValue;
    }

    public void setValue(Object value) {
        mValue = value;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
