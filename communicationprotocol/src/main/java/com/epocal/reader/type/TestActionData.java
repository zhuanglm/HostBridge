package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.EpocTime;
import com.epocal.reader.common.FixedArray;
import com.epocal.reader.enumtype.DAMStageType;
import com.epocal.reader.enumtype.ReaderStateType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * Created by dning on 7/25/2017.
 */

public class TestActionData extends DataFragment {
    private int mTimeStamp;
    private ReaderStateType mReaderState;
    private DAMStageType mDAMStage;
    private byte mBlockNumber;
    private byte mBlockLength;
    private byte mExtraSignalsLength;
    private FixedArray<Byte> mQCResults;
    private byte mBDFlags;
    private ChannelDataPairCollection mSampledSignalsValues;
    private ChannelDataPairCollection mExtraSignalsValues;

    public int getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        mTimeStamp = timeStamp;
    }

    public ReaderStateType getReaderState() {
        return mReaderState;
    }

    public void setReaderState(ReaderStateType readerState) {
        mReaderState = readerState;
    }

    public DAMStageType getDAMStage() {
        return mDAMStage;
    }

    public void setDAMStage(DAMStageType mDAMStage) {
        this.mDAMStage = mDAMStage;
    }

    public byte getBlockNumber() {
        return mBlockNumber;
    }

    public void setBlockNumber(byte blockNumber) {
        mBlockNumber = blockNumber;
    }

    public byte getBlockLength() {
        return mBlockLength;
    }

    public void setBlockLength(byte blockLength) {
        mBlockLength = blockLength;
    }

    public byte getExtraSignalsLength() {
        return mExtraSignalsLength;
    }

    public void setExtraSignalsLength(byte extraSignalsLength) {
        mExtraSignalsLength = extraSignalsLength;
    }

    public FixedArray<Byte> getQCResults() {
        return mQCResults;
    }

    public void setQCResults(FixedArray<Byte> mQCResults) {
        this.mQCResults = mQCResults;
    }

    public byte getBDFlags() {
        return mBDFlags;
    }

    public void setBDFlags(byte BDFlags) {
        this.mBDFlags = mBDFlags;
    }

    public ChannelDataPairCollection getSampledSignalsValues() {
        return mSampledSignalsValues;
    }

    public void setSampledSignalsValues(ChannelDataPairCollection sampledSignalsValues) {
        mSampledSignalsValues = sampledSignalsValues;
    }

    public ChannelDataPairCollection getExtraSignalsValues() {
        return mExtraSignalsValues;
    }

    public void setExtraSignalsValues(ChannelDataPairCollection extraSignalsValues) {
        mExtraSignalsValues = extraSignalsValues;
    }

    public TestActionData()
    {
        mTimeStamp = (int) EpocTime.timePOSIXMilliseconds();
        mQCResults = new FixedArray<Byte>(Byte.class, 3);
        mSampledSignalsValues = new ChannelDataPairCollection();
        mExtraSignalsValues = new ChannelDataPairCollection();
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(BigEndianBitConverter.getBytes((int)mTimeStamp));
        output.write(mReaderState.value);
        output.write(mDAMStage.value);
        output.write(mBlockNumber);
        output.write(mBlockLength);
        output.write(mExtraSignalsLength);
        output.write(FixedArray.toPrimitive(mQCResults.getData()));
        output.write(mBDFlags);
        output.write(mSampledSignalsValues.toBytes());
        output.write(mExtraSignalsValues.toBytes());

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mTimeStamp = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mReaderState = ReaderStateType.convert(dsr.readByte());
        mDAMStage = DAMStageType.convert(dsr.readByte());
        mBlockNumber = dsr.readByte();
        mBlockLength = dsr.readByte();
        mExtraSignalsLength = dsr.readByte();
        mQCResults.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mQCResults.FixedLen)));
        mBDFlags = dsr.readByte();
        mSampledSignalsValues.readBytes(dsr, mBlockLength);
        mExtraSignalsValues.readBytes(dsr, mExtraSignalsLength);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TimeStamp: " + mTimeStamp);
        sb.append("ReaderStateC: " + mReaderState.toString());
        sb.append("DAMStage: " + mDAMStage.toString());
        sb.append("BlockNumber: " + mBlockNumber);
        sb.append("BlockLength: " + mBlockLength);
        sb.append("ExtraSignalsLength: " + mExtraSignalsLength);
        sb.append("QCResults: " + mQCResults.toString());
        sb.append("BDFlags: " + mBDFlags);
        sb.append("SampledSignalsValues: " + mSampledSignalsValues.toString());
        sb.append("ExtraSignalsValues: " + mExtraSignalsValues.toString());
        return sb.toString();
    }

    public String getSampleString()
    {
        StringBuilder tempString = new StringBuilder(mSampledSignalsValues.getCount() *10 + 200);

        NumberFormat numFormat = NumberFormat.getNumberInstance();

        if (mSampledSignalsValues.getPairList() != null)
        {
            for (int i = 0; i < mSampledSignalsValues.getCount(); i++)
            {
                tempString.append((mSampledSignalsValues.getPairList().get(i)).getChannelType() + "|" + numFormat.format(mSampledSignalsValues.getPairList().get(i).getValue()) + "\t");
            }
        }

        if (mExtraSignalsValues.getPairList() != null)
        {
            for (int i = 0; i < mExtraSignalsValues.getCount(); i++)
            {
                tempString.append((mExtraSignalsValues.getPairList().get(i)).getChannelType() + "|" + numFormat.format(mExtraSignalsValues.getPairList().get(i).getValue()) + "\t");
            }
        }
        return tempString.toString();
    }
}
