package com.epocal.reader.type.Legacy;

/**
 * Created by rzhuang on July 31 2018.
 */

public class DAM_ADC_Configuration {
    public byte getFilterOrder() {
        return mFilterOrder;
    }

    public void setFilterOrder(byte filterOrder) {
        mFilterOrder = filterOrder;
    }

    public byte getInputBuffer() {
        return mInputBuffer;
    }

    public void setInputBuffer(byte inputBuffer) {
        mInputBuffer = inputBuffer;
    }

    public byte getPolarityMode() {
        return mPolarityMode;
    }

    public void setPolarityMode(byte polarityMode) {
        mPolarityMode = polarityMode;
    }

    public int getVDACOffset() {
        return mVDACOffset;
    }

    public void setVDACOffset(int VDACOffset) {
        this.mVDACOffset = VDACOffset;
    }

    public byte getPGA() {
        return mPGA;
    }

    public void setPGA(byte PGA) {
        this.mPGA = PGA;
    }

    private byte mFilterOrder;
    private byte mInputBuffer;
    private byte mPolarityMode;
    private int mVDACOffset;
    private byte mPGA;
}
