package com.epocal.common.realmentities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rzhuang on Aug 7 2018.
 */

public class EqcValue extends RealmObject {

    @PrimaryKey
    private long id =-1;
    private long mEqcInfoId = -1;

    private int mPacket;
    private String mStatus;
    private String mReadings;

    public long getId() { return id; }
    public void setId(long id) {
        this.id = id;
    }

    public long getEqcInfoId() {
        return mEqcInfoId;
    }
    public void setEqcInfoId(long eqcInfoId) {
        this.mEqcInfoId = eqcInfoId;
    }

    public int getPacket() { return mPacket; }
    public void setPacket(int packet) {
        this.mPacket = packet;
    }

    public String getStatus() { return mStatus; }
    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getReadings() { return mReadings; }
    public void setReadings(String readings) {
        this.mReadings = readings;
    }

    public EqcValue() {
    }

    public EqcValue(int packet, String readings) {
        mPacket = packet;
        mReadings = readings;
    }

    public void updateFrom(EqcValue input){
        this.setPacket(input.getPacket());
        this.setStatus(input.getStatus());
        this.setReadings(input.getReadings());
    }
}