package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.EpocVersion;
import com.epocal.reader.enumtype.ReaderDistribution;
import com.epocal.reader.enumtype.ReaderType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/7/2017.
 */

public class MainIdInfo extends DataFragment
{
    public ReaderType getReaderType() {
        return mReaderType;
    }

    public void setReaderType(ReaderType mReaderType) {
        this.mReaderType = mReaderType;
    }

    public ReaderDistribution getReaderDistribution() {
        return mReaderDistribution;
    }

    public void setReaderDistribution(ReaderDistribution mReaderDistribution) {
        this.mReaderDistribution = mReaderDistribution;
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

    public int getReaderTime() {
        return mReaderTime;
    }

    public void setReaderTime(int mReaderTime) {
        this.mReaderTime = mReaderTime;
    }

    public int getDeviceSerialNum() {
        return mDeviceSerialNum;
    }

    public void setDeviceSerialNum(int mDeviceSerialNum) {
        this.mDeviceSerialNum = mDeviceSerialNum;
    }

    public EpocVersion getReaderFirmwareVer() {
        return mReaderFirmwareVer;
    }

    public void setReaderFirmwareVer(EpocVersion mReaderFirmwareVer) {
        this.mReaderFirmwareVer = mReaderFirmwareVer;
    }

    public EpocVersion getMbHardwareVer() {
        return mMbHardwareVer;
    }

    public void setMbHardwareVer(EpocVersion mMbHardwareVer) {
        this.mMbHardwareVer = mMbHardwareVer;
    }

    public EpocVersion getSibHardwareVer() {
        return mSibHardwareVer;
    }

    public void setSibHardwareVer(EpocVersion mSibHardwareVer) {
        this.mSibHardwareVer = mSibHardwareVer;
    }

    public EpocVersion getGeneralConfigVer() {
        return mGeneralConfigVer;
    }

    public void setGeneralConfigVer(EpocVersion mGeneralConfigVer) {
        this.mGeneralConfigVer = mGeneralConfigVer;
    }

    public EpocVersion getBtDescriptorVer() {
        return mBtDescriptorVer;
    }

    public void setBtDescriptorVer(EpocVersion mBtDescriptorVer) {
        this.mBtDescriptorVer = mBtDescriptorVer;
    }

    public EpocVersion getMaintTestRecordNum() {
        return mMaintTestRecordNum;
    }

    public void setMaintTestRecordNum(EpocVersion mMaintTestRecordNum) {
        this.mMaintTestRecordNum = mMaintTestRecordNum;
    }

    public EpocVersion getEqcConfigVer() {
        return mEqcConfigVer;
    }

    public void setEqcConfigVer(EpocVersion mEqcConfigVer) {
        this.mEqcConfigVer = mEqcConfigVer;
    }

    public EpocVersion getDryCardCheckConfigVer() {
        return mDryCardCheckConfigVer;
    }

    public void setDryCardCheckConfigVer(EpocVersion mDryCardCheckConfigVer) {
        this.mDryCardCheckConfigVer = mDryCardCheckConfigVer;
    }

    public EpocVersion getBgeTestConfigVer() {
        return mBgeTestConfigVer;
    }

    public void setBgeTestConfigVer(EpocVersion mBgeTestConfigVer) {
        this.mBgeTestConfigVer = mBgeTestConfigVer;
    }

    public byte getBgeCardType() {
        return mBgeCardType;
    }

    public void setBgeCardType(byte mBgeCardType) {
        this.mBgeCardType = mBgeCardType;
    }

    public EpocVersion getDevicePciMonitorVer() {
        return mDevicePciMonitorVer;
    }

    public void setDevicePciMonitorVer(EpocVersion mDevicePciMonitorVer) {
        this.mDevicePciMonitorVer = mDevicePciMonitorVer;
    }

    private ReaderType mReaderType;
    private ReaderDistribution mReaderDistribution;
    private int mSupportedInterfaces;
    private int mSupportedSessions;
    private int mReaderTime;
    private int mDeviceSerialNum;
    private EpocVersion mReaderFirmwareVer;                     //4 byte
    private EpocVersion mMbHardwareVer;                         //2 byte
    private EpocVersion mSibHardwareVer;                        //1 byte
    private EpocVersion mGeneralConfigVer;                      //3 byte
    private EpocVersion mBtDescriptorVer;                       //3 byte
    private EpocVersion mMaintTestRecordNum;                    //3 byte
    private EpocVersion mEqcConfigVer;                          //3 byte
    private EpocVersion mDryCardCheckConfigVer;                 //3 byte
    private EpocVersion mBgeTestConfigVer;                      //3 byte
    private byte mBgeCardType;
    private EpocVersion mDevicePciMonitorVer;                   //3 byte

    public MainIdInfo() {
        mReaderTime = 0;
        mReaderFirmwareVer = new EpocVersion(1, 0, 0, 0);
        mMbHardwareVer = new EpocVersion(1, 0, 0, 0);
        mSibHardwareVer = new EpocVersion(1, 0, 0, 0);
        mGeneralConfigVer = new EpocVersion(1, 0, 0, 0);
        mBtDescriptorVer = new EpocVersion(1, 0, 0, 0);
        mMaintTestRecordNum = new EpocVersion(1, 0, 0, 0);
        mEqcConfigVer = new EpocVersion(1, 0, 0, 0);
        mDryCardCheckConfigVer = new EpocVersion(1, 0, 0, 0);
        mBgeTestConfigVer = new EpocVersion(1, 0, 0, 0);
        mDevicePciMonitorVer = new EpocVersion(1, 0, 0, 0);
    }

    @Override
    public byte[] toBytes() throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write((byte)mReaderType.value);
        output.write((byte)mReaderDistribution.value);
        output.write(BigEndianBitConverter.getBytes(mSupportedInterfaces));
        output.write(BigEndianBitConverter.getBytes(mSupportedSessions));
        output.write(BigEndianBitConverter.getBytes(mReaderTime));
        output.write(BigEndianBitConverter.getBytes(mDeviceSerialNum));
        output.write(mReaderFirmwareVer.toBytes(4));
        output.write(mMbHardwareVer.toBytes(2));
        output.write(mSibHardwareVer.toBytes(1));
        output.write(mGeneralConfigVer.toBytes(3));
        output.write(mBtDescriptorVer.toBytes(3));
        output.write(mMaintTestRecordNum.toBytes(3));
        output.write(mEqcConfigVer.toBytes(3));
        output.write(mDryCardCheckConfigVer.toBytes(3));
        output.write(mBgeTestConfigVer.toBytes(3));
        output.write((byte)mBgeCardType);
        output.write(mDevicePciMonitorVer.toBytes(3));

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException
    {
        mReaderType = ReaderType.convert(dsr.readByte());
        mReaderDistribution = ReaderDistribution.convert(dsr.readByte());
        mSupportedInterfaces = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mSupportedSessions = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mReaderTime = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mDeviceSerialNum = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mReaderFirmwareVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 4));
        mMbHardwareVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 2));
        mSibHardwareVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 1));
        mGeneralConfigVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 3));
        mBtDescriptorVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 3));
        mMaintTestRecordNum = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 3));
        mEqcConfigVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 3));
        mDryCardCheckConfigVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 3));
        mBgeTestConfigVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 3));
        mBgeCardType = dsr.readByte();
        mDevicePciMonitorVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 3));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reader Type: " + mReaderType.toString());
        sb.append("Reader Distribution: " + mReaderDistribution.toString());
        sb.append("Supported Interfaces: " + mSupportedInterfaces);
        sb.append("Supported Sessions: " + mSupportedSessions);
        sb.append("DeviceSerialNum: " + mDeviceSerialNum);
        sb.append("ReaderFirmwareVer: " + mReaderFirmwareVer.toString());
        sb.append("MbHardwareVer: " + mMbHardwareVer.toString());
        sb.append("SibHardwareVer: " + mSibHardwareVer.toString());
        sb.append("GeneralConfigVer: " + mGeneralConfigVer.toString());
        sb.append("BtDescriptorVer: " + mBtDescriptorVer.toString());
        sb.append("MaintTestRecordNum: " + mMaintTestRecordNum);
        sb.append("EqcConfigVer: " + mEqcConfigVer.toString());
        sb.append("DryCardCheckConfigVer: " + mDryCardCheckConfigVer.toString());
        sb.append("BgeTestConfigVer: " + mBgeTestConfigVer.toString());
        sb.append("BgeCardType: " + mBgeCardType);
        sb.append("DevicePciMonitorVer: " + mDevicePciMonitorVer.toString());
        return sb.toString();
    }
}
