package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.FixedArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/26/2017.
 */

public class EQCConfiguration extends DataFragment
{
    private ConfigurationHeader mHeader;
    private FixedArray<Float> mQCLimits;
    private FixedArray<Byte> mChannelQCKeys;
    private BDConfiguration mBDConfiguration;
    private DAMADC mADC;
    private byte mDuration;
    private SampleConfiguration mSampleConfiguration;
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

    public FixedArray<Byte> getChannelQCKeys() {
        return mChannelQCKeys;
    }

    public void setChannelQCKeys(FixedArray<Byte> mChannelQCKeys) {
        this.mChannelQCKeys = mChannelQCKeys;
    }

    public BDConfiguration getBDConfiguration() {
        return mBDConfiguration;
    }

    public void setBDConfiguration(BDConfiguration mBDConfiguration) {
        this.mBDConfiguration = mBDConfiguration;
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
        mDuration = mDuration;
    }

    public SampleConfiguration getmSampleConfiguration() {
        return mSampleConfiguration;
    }

    public void setmSampleConfiguration(SampleConfiguration mSampleConfiguration) {
        this.mSampleConfiguration = mSampleConfiguration;
    }

    public FixedArray<Byte> getmExtraBytes() {
        return mExtraBytes;
    }

    public void setmExtraBytes(FixedArray<Byte> mExtraBytes) {
        this.mExtraBytes = mExtraBytes;
    }

    public EQCConfiguration()
    {
        mHeader = new ConfigurationHeader();
        mQCLimits = new FixedArray<Float>(Float.class, 80);
        mChannelQCKeys = new FixedArray<Byte>(Byte.class, 100);
        mBDConfiguration = new BDConfiguration();
        mADC = new DAMADC();
        mSampleConfiguration = new SampleConfiguration();
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        for (int i = 0; i < mQCLimits.getData().length; i++)
            output.write(BigEndianBitConverter.getBytes(mQCLimits.getData()[i]));
        output.write(FixedArray.toPrimitive(mChannelQCKeys.getData()));
        output.write(mBDConfiguration.toBytes());
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

        mChannelQCKeys.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mChannelQCKeys.FixedLen)));
        mBDConfiguration.readBytes(dsr);
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
        sb.append("BDConfiguration: " + mBDConfiguration.toString());
        sb.append("DAMADC: " + mADC.toString());
        sb.append("Duration: " + mDuration);
        sb.append("SampleConfiguration: " + mSampleConfiguration.toString());
        return sb.toString();
    }
}
