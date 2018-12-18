package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/26/2017.
 */

public class GeneralConfiguration extends DataFragment
{
    private ConfigurationHeader mHeader;
    private short mConnWaitExpiry;
    private byte mPowerSaveModeTimer;
    private byte mDeviceOnBatteryPowerOffTimer;
    private short mDeviceOnExtPowerOffTimer;
    private short mKeepAliveFreq;

    public ConfigurationHeader getHeader() {
        return mHeader;
    }

    public void setHeader(ConfigurationHeader mHeader) {
        this.mHeader = mHeader;
    }

    public short getConnWaitExpiry() {
        return mConnWaitExpiry;
    }

    public void setConnWaitExpiry(short mConnWaitExpiry) {
        this.mConnWaitExpiry = mConnWaitExpiry;
    }

    public byte getPowerSaveModeTimer() {
        return mPowerSaveModeTimer;
    }

    public void setPowerSaveModeTimer(byte mPowerSaveModeTimer) {
        this.mPowerSaveModeTimer = mPowerSaveModeTimer;
    }

    public byte getDeviceOnBatteryPowerOffTimer() {
        return mDeviceOnBatteryPowerOffTimer;
    }

    public void setDeviceOnBatteryPowerOffTimer(byte mDeviceOnBatteryPowerOffTimer) {
        this.mDeviceOnBatteryPowerOffTimer = mDeviceOnBatteryPowerOffTimer;
    }

    public short getDeviceOnExtPowerOffTimer() {
        return mDeviceOnExtPowerOffTimer;
    }

    public void setDeviceOnExtPowerOffTimer(short mDeviceOnExtPowerOffTimer) {
        this.mDeviceOnExtPowerOffTimer = mDeviceOnExtPowerOffTimer;
    }

    public short getKeepAliveFreq() {
        return mKeepAliveFreq;
    }

    public void setKeepAliveFreq(short mKeepAliveFreq) {
        this.mKeepAliveFreq = mKeepAliveFreq;
    }

    public GeneralConfiguration()
    {
        mHeader = new ConfigurationHeader();
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(BigEndianBitConverter.getBytes(mConnWaitExpiry));
        output.write(mPowerSaveModeTimer);
        output.write(mDeviceOnBatteryPowerOffTimer);
        output.write(BigEndianBitConverter.getBytes(mDeviceOnExtPowerOffTimer));
        output.write(BigEndianBitConverter.getBytes(mKeepAliveFreq));
        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();

        mHeader.setData(bytes);
        return mHeader.toBytes();
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mHeader.readBytes(dsr);
        mConnWaitExpiry = BigEndianBitConverter.toInt16(dsr, 2, 0);
        mPowerSaveModeTimer = dsr.readByte();
        mDeviceOnBatteryPowerOffTimer = dsr.readByte();
        mDeviceOnExtPowerOffTimer = BigEndianBitConverter.toInt16(dsr, 2, 0);
        mKeepAliveFreq = BigEndianBitConverter.toInt16(dsr, 2, 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Header: " + mHeader.toString());
        sb.append("ConnWait Expiry: " + mConnWaitExpiry);
        sb.append("PowerSaveModeTimer: " + mPowerSaveModeTimer);
        sb.append("DeviceOnBatteryPowerOffTimer: " + mDeviceOnBatteryPowerOffTimer);
        sb.append("KeepAliveFreq: " + mKeepAliveFreq);
        return sb.toString();
    }
}
