package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.EpocTime;
import com.epocal.reader.common.FixedArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 8/16/2017.
 */

public class DryCardCheckData extends DataFragment
{
    private int mTimeStamp;
    private byte mBlockNumber;
    private byte mBlockLength;
    private FixedArray<Byte> mQCResults;
    private ChannelDataPairCollection mSampledSignalsValues;

    public DryCardCheckData()
    {
        mTimeStamp = (int) EpocTime.timePOSIXMilliseconds();
        mQCResults = new FixedArray<Byte>(Byte.class, 3);
        mSampledSignalsValues = new ChannelDataPairCollection();
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(BigEndianBitConverter.getBytes((int)mTimeStamp));
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
        mBlockNumber = dsr.readByte();
        mBlockLength = dsr.readByte();
        mQCResults.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mQCResults.FixedLen)));
        mSampledSignalsValues.readBytes(dsr, mBlockLength);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TimeStamp: " + mTimeStamp);
        sb.append("BlockNumber: " + mBlockNumber);
        sb.append("BlockLength: " + mBlockLength);
        sb.append("QCResults: " + mQCResults.toString());
        sb.append("SampledSignalsValues: " + mSampledSignalsValues.toString());
        return sb.toString();
    }
}
