package com.epocal.reader.type;

/**
 * Created by dning on 6/30/2017.
 */

public class ChannelDataPair {
    private byte mChannelType;
    private float mValue;

    public byte getChannelType() {
        return mChannelType;
    }

    public void setChannelType(byte mChannelType) {
        this.mChannelType = mChannelType;
    }

    public float getValue() {
        return mValue;
    }

    public void setValue(float mValue) {
        this.mValue = mValue;
    }

    public ChannelDataPair()
    {
    }

    public ChannelDataPair(byte mChannelType, float mValue)
    {
        this.mChannelType = mChannelType;
        this.mValue = mValue;
    }
}
