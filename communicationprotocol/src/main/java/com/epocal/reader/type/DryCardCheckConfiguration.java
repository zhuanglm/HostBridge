package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.FixedArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/27/2017.
 */

public class DryCardCheckConfiguration extends DataFragment
{
    private ConfigurationHeader mHeader;
    private FixedArray<Float> mQCLimits;
    private BDConfigurationDryCardCheck mBDConfiguration;
    private float mBDAirThreshold;
    private float mBDFluidThreshold;
    private DAMADC mADC; //documentation ERROR. Says 8 bytes, but reference to selfcheck is 16bytes ????
    private byte mDuration;
    private SampleConfiguration mSampleConfiguration; //wrong doc again
    private FixedArray<Byte> mExtraBytes; //remaining bytes

    public ConfigurationHeader getHeader() {
        return mHeader;
    }

    public void setHeader(ConfigurationHeader mHeader) {
        this.mHeader = mHeader;
    }

    public FixedArray<Float> getQCLimits() {
        return mQCLimits;
    }

    public void setQCLimits(FixedArray<Float> mQCLimits) {
        this.mQCLimits = mQCLimits;
    }

    public BDConfigurationDryCardCheck getBDConfiguration() {
        return mBDConfiguration;
    }

    public void setBDConfiguration(BDConfigurationDryCardCheck mBDConfiguration) {
        this.mBDConfiguration = mBDConfiguration;
    }

    public float getBDAirThreshold() {
        return mBDAirThreshold;
    }

    public void setBDAirThreshold(float mBDAirThreshold) {
        this.mBDAirThreshold = mBDAirThreshold;
    }

    public float getBDFluidThreshold() {
        return mBDFluidThreshold;
    }

    public void setBDFluidThreshold(float mBDFluidThreshold) {
        this.mBDFluidThreshold = mBDFluidThreshold;
    }

    public DAMADC getADC() {
        return mADC;
    }

    public void setADC(DAMADC mADC) {
        this.mADC = mADC;
    }

    public byte getDuration() {
        return mDuration;
    }

    public void setDuration(byte mDuration) {
        this.mDuration = mDuration;
    }

    public SampleConfiguration getSampleConfiguration() {
        return mSampleConfiguration;
    }

    public void setSampleConfiguration(SampleConfiguration mSampleConfiguration) {
        this.mSampleConfiguration = mSampleConfiguration;
    }

    public FixedArray<Byte> getExtraBytes() {
        return mExtraBytes;
    }

    public void setExtraBytes(FixedArray<Byte> mExtraBytes) {
        this.mExtraBytes = mExtraBytes;
    }

    public DryCardCheckConfiguration()
    {
        mHeader = new ConfigurationHeader();
        mQCLimits = new FixedArray<Float>(Float.class, 4);
        mBDConfiguration = new BDConfigurationDryCardCheck();
        mADC = new DAMADC();
        mSampleConfiguration = new SampleConfiguration();
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        for (int i = 0; i < mQCLimits.getData().length; i++)
            output.write(BigEndianBitConverter.getBytes(mQCLimits.getData()[i]));
        output.write(mBDConfiguration.toBytes());
        output.write(BigEndianBitConverter.getBytes(mBDAirThreshold));
        output.write(BigEndianBitConverter.getBytes(mBDFluidThreshold));
        output.write(mADC.toBytes());
        output.write(mDuration);
        output.write(mSampleConfiguration.toBytes());
        if (mExtraBytes != null && mExtraBytes.getData().length > 0)
            output.write(FixedArray.toPrimitive(mExtraBytes.getData()));

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        mHeader.setData(bytes);
        return mHeader.toBytes();
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mHeader.readBytes(dsr);
        for (int i = 0; i < mQCLimits.FixedLen; i++)
            mQCLimits.getData()[i] = BigEndianBitConverter.toSingle(dsr, 4, 0);

        mBDConfiguration.readBytes(dsr);
        mBDAirThreshold = BigEndianBitConverter.toSingle(dsr, 4, 0);
        mBDFluidThreshold = BigEndianBitConverter.toSingle(dsr, 4, 0);
        mADC.readBytes(dsr);
        mDuration = dsr.readByte();
        mSampleConfiguration.readBytes(dsr);
        byte[] remainingData = DataStreamReader.readRemainingData(dsr);
        if (remainingData.length > 0)
        {
            mExtraBytes = new FixedArray<Byte>(Byte.class, remainingData.length);
            mExtraBytes.setData(Byte.class, FixedArray.toClass(remainingData));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Header: " + mHeader.toString());
        sb.append("BDConfigurationDryCardCheck: " + mBDConfiguration.toString());
        sb.append("BDAirThreshold: " + mBDAirThreshold);
        sb.append("BDFluidThreshold: " + mBDFluidThreshold);
        sb.append("DAMADC: " + mADC.toString());
        sb.append("Duration: " + mDuration);
        sb.append("SampleConfiguration: " + mSampleConfiguration.toString());
        return sb.toString();
    }
}
