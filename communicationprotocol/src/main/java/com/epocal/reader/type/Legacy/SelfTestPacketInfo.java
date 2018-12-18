package com.epocal.reader.type.Legacy;

import java.util.ArrayList;

/**
 * Created by rzhuang on Aug 2 2018.
 */

public class SelfTestPacketInfo {
    public String getPacketNumber() {
        return mPacketNumber;
    }

    public void setPacketNumber(String mPacketNumber) {
        this.mPacketNumber = mPacketNumber;
    }

    private String mPacketNumber;
    public ArrayList values;

    public SelfTestPacketInfo() {
        values = new ArrayList();
    }
}