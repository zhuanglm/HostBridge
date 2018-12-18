package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.FixedArray;
import com.epocal.util.UnsignedType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 8/15/2017.
 */

public class CalDeliverySlowDownCounts extends DataFragment
{
    private byte mCalDeliveryIntervalsCount;
    private byte mCalFinalIntervalsCount;

    private FixedArray<Byte> mDeliverySlowDownData;
    private FixedArray<Byte> mFinalSlowDownData;

    public byte getCalDeliveryIntervalsCount() {
        return mCalDeliveryIntervalsCount;
    }

    public void setCalDeliveryIntervalsCount(byte mCalDeliveryIntervalsCount) {
        this.mCalDeliveryIntervalsCount = mCalDeliveryIntervalsCount;
    }

    public byte getCalFinalIntervalsCount() {
        return mCalFinalIntervalsCount;
    }

    public void setCalFinalIntervalsCount(byte mCalFinalIntervalsCount) {
        this.mCalFinalIntervalsCount = mCalFinalIntervalsCount;
    }

    public FixedArray<Byte> getDeliverySlowDownData() {
        return mDeliverySlowDownData;
    }

    public void setDeliverySlowDownData(FixedArray<Byte> mDeliverySlowDownData) {
        this.mDeliverySlowDownData = mDeliverySlowDownData;
    }

    public FixedArray<Byte> getFinalSlowDownData() {
        return mFinalSlowDownData;
    }

    public void setFinalSlowDownData(FixedArray<Byte> mFinalSlowDownData) {
        this.mFinalSlowDownData = mFinalSlowDownData;
    }

    public CalDeliverySlowDownCounts()
    {
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(mCalDeliveryIntervalsCount);
        output.write(mCalFinalIntervalsCount);

        output.write(FixedArray.toPrimitive(mDeliverySlowDownData.getData()));
        output.write(FixedArray.toPrimitive(mFinalSlowDownData.getData()));

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mCalDeliveryIntervalsCount = dsr.readByte();
        mCalFinalIntervalsCount = dsr.readByte();

        mDeliverySlowDownData = new FixedArray<Byte>(Byte.class, mCalDeliveryIntervalsCount);
        mFinalSlowDownData = new FixedArray<Byte>(Byte.class, mCalFinalIntervalsCount);
        mDeliverySlowDownData.setData(Byte.class, FixedArray.toClass(dsr.readFixedLength(dsr, mDeliverySlowDownData.FixedLen)));
        mFinalSlowDownData.setData(Byte.class, FixedArray.toClass(dsr.readFixedLength(dsr, mFinalSlowDownData.FixedLen)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CalDeliveryIntervalsCount: " + mCalDeliveryIntervalsCount);
        sb.append("CalFinalIntervalsCount: " + mCalFinalIntervalsCount);

        sb.append("DeliverySlowDownData: " + mDeliverySlowDownData.toString());
        sb.append("FinalSlowDownData: " + mFinalSlowDownData.toString());
        return sb.toString();
    }
}

