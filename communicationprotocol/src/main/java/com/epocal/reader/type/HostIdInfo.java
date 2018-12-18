package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.EpocTime;
import com.epocal.reader.common.EpocVersion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/13/2017.
 */

public class HostIdInfo extends DataFragment
{
    public byte getType() {
        return mType;
    }

    public void setType(byte mType) {
        this.mType = mType;
    }

    public byte getCompileType() {
        return mCompileType;
    }

    public void setCompileType(byte mCompileType) {
        this.mCompileType = mCompileType;
    }

    public EpocVersion getSoftwareVersion() {
        return mSoftwareVersion;
    }

    public void setSoftwareVersion(EpocVersion mSoftwareVersion) {
        this.mSoftwareVersion = mSoftwareVersion;
    }

    public EpocVersion getHostId() {
        return mHostId;
    }

    public void setHostId(EpocVersion mHostId) {
        this.mHostId = mHostId;
    }

    public long getHostTime() {
        return mHostTime;
    }

    public void setHostTime(long mHostTime) {
        this.mHostTime = mHostTime;
    }

    public int getSupportedInterfaces() {
        return mSupportedInterfaces;
    }

    public void setSupportedInterfaces(int mSupportedInterfaces) {
        this.mSupportedInterfaces = mSupportedInterfaces;
    }

    public int getSupportedSessions() {
        return mSupportedSessions;
    }

    public void setSupportedSessions(int mSupportedSessions) {
        this.mSupportedSessions = mSupportedSessions;
    }

    private byte mType;
    private byte mCompileType;
    private EpocVersion mSoftwareVersion;
    private EpocVersion mHostId;
    private long mHostTime;
    private int mSupportedInterfaces;
    private int mSupportedSessions;

    public HostIdInfo()
    {
        mSoftwareVersion = new EpocVersion(1, 0, 0);
        mHostId = new EpocVersion(1, 0, 0);
        mHostTime = (long) EpocTime.timePOSIXMilliseconds();
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(mType);
        output.write(mCompileType);
        output.write(mSoftwareVersion.toBytes(3));
        output.write(mHostId.toBytes(4));
        output.write(BigEndianBitConverter.getBytes(mHostTime));
        output.write(BigEndianBitConverter.getBytes(mSupportedInterfaces));
        output.write(BigEndianBitConverter.getBytes(mSupportedSessions));
        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mType = dsr.readByte();
        mCompileType = dsr.readByte();
        mSoftwareVersion = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 3));
        mHostId = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 4));
        mHostTime = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mSupportedInterfaces = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mSupportedSessions = BigEndianBitConverter.toInt32(dsr, 4, 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Type: " + Byte.toString(mType));
        sb.append("Compile Type: " + Byte.toString(mCompileType));
        sb.append("Software Version: " + mSoftwareVersion.toString());
        sb.append("Host Id: " + mHostId.toString());
        sb.append("Supported Interfaces: " + mSupportedInterfaces);
        sb.append("Supported Session: " + mSupportedSessions);
        return sb.toString();
    }
}
