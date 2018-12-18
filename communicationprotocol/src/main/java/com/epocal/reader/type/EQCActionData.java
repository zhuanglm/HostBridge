package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.EpocTime;
import com.epocal.reader.common.FixedArray;
import com.epocal.reader.enumtype.DAMStageType;
import com.epocal.reader.enumtype.ReaderStateType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/30/2017.
 */

public class EQCActionData extends DataFragment
{
    private int mTimeStamp;
    private ReaderStateType mReaderState;
    private DAMStageType mDAMStage;
    private byte mBlockNumber;
    private byte mBlockLength;
    private FixedArray<Byte> mQCResults;
    private ChannelDataPairCollection mSampledSignalsValues;

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

    public void setDAMStage(DAMStageType DAMStage) {
        this.mDAMStage = DAMStage;
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

    public FixedArray<Byte> getQCResults() {
        return mQCResults;
    }

    public void setQCResults(FixedArray<Byte> QCResults) {
        this.mQCResults = QCResults;
    }

    public ChannelDataPairCollection getSampledSignalsValues() {
        return mSampledSignalsValues;
    }

    public void setSampledSignalsValues(ChannelDataPairCollection sampledSignalsValues) {
        mSampledSignalsValues = sampledSignalsValues;
    }

    public EQCActionData()
    {
        mTimeStamp = (int) EpocTime.timePOSIXMilliseconds();
        mQCResults = new FixedArray<Byte>(Byte.class, 3);
        mSampledSignalsValues = new ChannelDataPairCollection();
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(BigEndianBitConverter.getBytes((int)mTimeStamp));
        output.write(mReaderState.value);
        output.write(mDAMStage.value);
        output.write(mBlockNumber);
        output.write(mBlockLength);
        output.write(FixedArray.toPrimitive(mQCResults.getData()));
        output.write(mSampledSignalsValues.toBytes());

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
        mQCResults.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mQCResults.FixedLen)));
        mSampledSignalsValues.readBytes(dsr, mBlockLength);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TimeStamp: " + mTimeStamp);
        sb.append("ReaderStateC: " + mReaderState.toString());
        sb.append("DAMStage: " + mDAMStage.toString());
        sb.append("BlockNumber: " + mBlockNumber);
        sb.append("BlockLength: " + mBlockLength);
        sb.append("QCResults: " + mQCResults.toString());
        sb.append("SampledSignalsValues: " + mSampledSignalsValues.toString());
        return sb.toString();
    }
}
