package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.EpocTime;
import com.epocal.reader.common.FixedArray;
import com.epocal.reader.enumtype.ReaderStateType;
import com.epocal.util.UnsignedType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * Created by dning on 7/18/2017.
 */

public class DPMActionData extends DataFragment
{
    private UnsignedType.UInt32 mTimeStamp;
    private ReaderStateType mReaderStateType;
    private byte mSequenceLength;
    private ChannelDataPairCollection mIndicatorValues;

    public UnsignedType.UInt32 getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(UnsignedType.UInt32 mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public ReaderStateType getReaderStateType() {
        return mReaderStateType;
    }

    public void setReaderStateType(ReaderStateType mReaderStateType) {
        this.mReaderStateType = mReaderStateType;
    }

    public byte getSequenceLength() {
        return mSequenceLength;
    }

    public void setSequenceLength(byte mSequenceLength) {
        this.mSequenceLength = mSequenceLength;
    }

    public ChannelDataPairCollection getIndicatorValues() {
        return mIndicatorValues;
    }

    public void setIndicatorValues(ChannelDataPairCollection mIndicatorValues) {
        this.mIndicatorValues = mIndicatorValues;
    }

    public DPMActionData()
    {
        mTimeStamp = new UnsignedType.UInt32((int) EpocTime.timePOSIXMilliseconds());
        mIndicatorValues = new ChannelDataPairCollection();
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(BigEndianBitConverter.getBytes(mTimeStamp.getValue()));
        output.write(mReaderStateType.value);
        output.write(mSequenceLength);
        output.write(mIndicatorValues.toBytes());
        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mTimeStamp.setValue(BigEndianBitConverter.toInt32(dsr, 4, 0));
        mReaderStateType = ReaderStateType.convert(dsr.readByte());
        mSequenceLength = dsr.readByte();
        mIndicatorValues.readBytes(dsr, mSequenceLength);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TimeStamp: " + mTimeStamp);
        sb.append("ReaderStateType: " + mReaderStateType.toString());
        sb.append("SequenceLength: " + mSequenceLength);
        sb.append("IndicatorValues: " + mIndicatorValues.toString());
        return sb.toString();
    }

    public String getSampleString()
    {
        StringBuilder tempString = new StringBuilder();
        NumberFormat numFormat = NumberFormat.getNumberInstance();

        if (mIndicatorValues.getPairList() != null)
        {
            for (int i = 0; i < mIndicatorValues.getCount(); i++)
            {
                tempString.append((mIndicatorValues.getPairList().get(i)).getChannelType() + "|" + numFormat.format(mIndicatorValues.getPairList().get(i).getValue()) + "\t");
            }
        }
        return tempString.toString();
    }
}
