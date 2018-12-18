package com.epocal.reader.type;

import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.FixedArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/26/2017.
 */

public class BTDescriptor extends DataFragment
{
    private ConfigurationHeader mHeader;
    private FixedArray<Byte> mBTPIN;
    private FixedArray<Byte> mReserved;
    private BTConfiguration mBTConfiguration;
    private FixedArray<Byte> mDeviceBTName;

    public ConfigurationHeader getHeader() {
        return mHeader;
    }

    public void setHeader(ConfigurationHeader mHeader) {
        this.mHeader = mHeader;
    }

    public FixedArray<Byte> getBTPIN() {
        return mBTPIN;
    }

    public void setBTPIN(FixedArray<Byte> mBTPIN) {
        this.mBTPIN = mBTPIN;
    }

    public FixedArray<Byte> getReserved() {
        return mReserved;
    }

    public void setReserved(FixedArray<Byte> mReserved) {
        this.mReserved = mReserved;
    }

    public BTConfiguration getBTConfiguration() {
        return mBTConfiguration;
    }

    public void setBTConfiguration(BTConfiguration mBTConfiguration) {
        this.mBTConfiguration = mBTConfiguration;
    }

    public FixedArray<Byte> getDeviceBTName() {
        return mDeviceBTName;
    }

    public void setDeviceBTName(FixedArray<Byte> mDeviceBTName) {
        this.mDeviceBTName = mDeviceBTName;
    }

    public BTDescriptor()
    {
        mHeader = new ConfigurationHeader();
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(FixedArray.toPrimitive(mBTPIN.getData()));
        output.write(FixedArray.toPrimitive(mReserved.getData()));
        output.write(mBTConfiguration.toBytes());
        output.write(FixedArray.toPrimitive(mDeviceBTName.getData()));
        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();

        mHeader.setData(bytes);
        return mHeader.toBytes();
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mHeader.readBytes(dsr);
        mBTPIN.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mBTPIN.FixedLen)));
        mReserved.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mReserved.FixedLen)));
        mBTConfiguration.readBytes(dsr);
        mDeviceBTName.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mDeviceBTName.FixedLen)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Header: " + mHeader.toString());
        sb.append("BTPIN: " + mBTPIN.toString());
        sb.append("Reserved: " + mReserved.toString());
        sb.append("BTConfiguration: " + mBTConfiguration.toString());
        sb.append("DeviceBTName: " + mDeviceBTName.toString());
        return sb.toString();
    }
}
