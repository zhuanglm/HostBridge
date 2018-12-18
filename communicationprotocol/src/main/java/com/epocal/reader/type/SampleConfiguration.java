package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.FixedArray;
import com.epocal.util.ByteUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dning on 6/27/2017.
 */

public class SampleConfiguration extends DataFragment
{
    private byte mNumSequenceBlocks = 0;
    private float mSequenceFrequency;
    private byte mAnalogClock;
    private byte mNumAcceptedSamples;
    private FixedArray<Byte> mBlockLengths; //NSB*1
    private FixedArray<Byte> mSampleSequence;   //SUM(Block Lengths)*14

    public byte getNumSequenceBlocks() {
        return mNumSequenceBlocks;
    }

    public void setNumSequenceBlocks(byte mNumSequenceBlocks) {
        this.mNumSequenceBlocks = mNumSequenceBlocks;
        init();
    }

    public float getSequenceFrequency() {
        return mSequenceFrequency;
    }

    public void setSequenceFrequency(float mSequenceFrequency) {
        this.mSequenceFrequency = mSequenceFrequency;
    }

    public byte getAnalogClock() {
        return mAnalogClock;
    }

    public void setAnalogClock(byte mAnalogClock) {
        this.mAnalogClock = mAnalogClock;
    }

    public byte getNumAcceptedSamples() {
        return mNumAcceptedSamples;
    }

    public void setNumAcceptedSamples(byte mNumAcceptedSamples) {
        this.mNumAcceptedSamples = mNumAcceptedSamples;
    }

    public FixedArray<Byte> getBlockLengths() {
        return mBlockLengths;
    }

    public void setBlockLengths(FixedArray<Byte> mBlockLengths) {
        this.mBlockLengths = mBlockLengths;
    }

    public FixedArray<Byte> getSampleSequence() {
        return mSampleSequence;
    }

    public void setSampleSequence(FixedArray<Byte> mSampleSequence) {
        this.mSampleSequence = mSampleSequence;
    }

    private void init()
    {
        if (mNumSequenceBlocks > 0)
        {
            mBlockLengths = new FixedArray<Byte>(Byte.class, mNumSequenceBlocks * 1);
            int checkSum = 0;
            for(byte b : mBlockLengths.getData()){
                checkSum += ByteUtil.toUnsignedInt(b);
            }
            mSampleSequence = new FixedArray<Byte>(Byte.class, checkSum);
        }
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(BigEndianBitConverter.getBytes(mSequenceFrequency));
        output.write(mAnalogClock);
        output.write(mNumAcceptedSamples);
        output.write(mNumSequenceBlocks);
        if (mNumSequenceBlocks > 0)
        {
            output.write(FixedArray.toPrimitive(mBlockLengths.getData()));
            output.write(FixedArray.toPrimitive(mSampleSequence.getData()));
        }
        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mSequenceFrequency = BigEndianBitConverter.toSingle(dsr, 4, 0);
        mAnalogClock = dsr.readByte();
        mNumAcceptedSamples = dsr.readByte();
        mNumSequenceBlocks = dsr.readByte();

        if (mNumSequenceBlocks > 0)
        {
            init();
            mBlockLengths.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mBlockLengths.FixedLen)));
            mSampleSequence.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mSampleSequence.FixedLen)));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SequenceFrequency: " + mSequenceFrequency);
        sb.append("AnalogClock: " + mAnalogClock);
        sb.append("NumAcceptedSamples: " + mNumAcceptedSamples);
        sb.append("NumSequenceBlocks: " + mNumSequenceBlocks);
        return sb.toString();
    }
}
