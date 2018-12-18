package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.EpocVersion;
import com.epocal.reader.common.FixedArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/8/2017.
 */

public class AdditionalIdInfo extends DataFragment {

    public EpocVersion getCpFirmwareVer() {
        return mCpFirmwareVer;
    }

    public void setCpFirmwareVer(EpocVersion mCpFirmwareVer) {
        this.mCpFirmwareVer = mCpFirmwareVer;
    }

    public EpocVersion getVpFirmwareVer() {
        return mVpFirmwareVer;
    }

    public void setVpFirmwareVer(EpocVersion mVpFirmwareVer) {
        this.mVpFirmwareVer = mVpFirmwareVer;
    }

    public EpocVersion getBarcodeFirmwareVer() {
        return mBarcodeFirmwareVer;
    }

    public void setBarcodeFirmwareVer(EpocVersion mBarcodeFirmwareVer) {
        this.mBarcodeFirmwareVer = mBarcodeFirmwareVer;
    }

    public EpocVersion getCooxFirmwareVer() {
        return mCooxFirmwareVer;
    }

    public void setCooxFirmwareVer(EpocVersion mCooxFirmwareVer) {
        this.mCooxFirmwareVer = mCooxFirmwareVer;
    }

    public EpocVersion getCompTableVer() {
        return mCompTableVer;
    }

    public void setCompTableVer(EpocVersion mCompTableVer) {
        this.mCompTableVer = mCompTableVer;
    }

    public short getCompTableType() {
        return mCompTableType;
    }

    public void setCompTableType(short mCompTableType) {
        this.mCompTableType = mCompTableType;
    }

    public byte getMainboardCid() {
        return mMainboardCid;
    }

    public void setMainboardCid(byte mMainboardCid) {
        this.mMainboardCid = mMainboardCid;
    }

    public FixedArray<Byte> getMbSerialNum() {
        return mMbSerialNum;
    }

    public void setMbSerialNum(FixedArray<Byte> mMbSerialNum) {
        this.mMbSerialNum = mMbSerialNum;
    }

    public byte getSibCid() {
        return mSibCid;
    }

    public void setSibCid(byte mSibCid) {
        this.mSibCid = mSibCid;
    }

    public FixedArray<Byte> getSIBSerialNum() {
        return mSIBSerialNum;
    }

    public void setSIBSerialNum(FixedArray<Byte> mSIBSerialNum) {
        this.mSIBSerialNum = mSIBSerialNum;
    }

    public int getSpectrometerSerialNum() {
        return mSpectrometerSerialNum;
    }

    public void setSpectrometerSerialNum(int mSpectrometerSerialNum) {
        this.mSpectrometerSerialNum = mSpectrometerSerialNum;
    }

    public short getMechanicId() {
        return mMechanicId;
    }

    public void setMechanicId(short mMechanicId) {
        this.mMechanicId = mMechanicId;
    }

    public EpocVersion getBluetoothVer() {
        return mBluetoothVer;
    }

    public void setBluetoothVer(EpocVersion mBluetoothVer) {
        this.mBluetoothVer = mBluetoothVer;
    }

    public FixedArray<Byte> getBtProductVariant() {
        return mBtProductVariant;
    }

    private void setBtProductVariant(FixedArray<Byte> mBtProductVariant) {
        this.mBtProductVariant = mBtProductVariant;
    }

    public FixedArray<Byte> getBtCsrBuildNum() {
        return mBtCsrBuildNum;
    }

    private void setBtCsrBuildNum(FixedArray<Byte> mBtCsrBuildNum) {
        this.mBtCsrBuildNum = mBtCsrBuildNum;
    }

    public byte getBtTdkBuildNum() {
        return mBtTdkBuildNum;
    }

    public void setBtTdkBuildNum(byte mBtTdkBuildNum) {
        this.mBtTdkBuildNum = mBtTdkBuildNum;
    }

    public FixedArray<Byte> getBtTdkVer() {
        return mBtTdkVer;
    }

    private void setBtTdkVer(FixedArray<Byte> mBtTdkVer) {
        this.mBtTdkVer = mBtTdkVer;
    }

    public FixedArray<Byte> getBtAddress() {
        return mBtAddress;
    }

    private void setBtAddress(FixedArray<Byte> mBtAddress) {
        this.mBtAddress = mBtAddress;
    }

    public FixedArray<Byte> getBtDeviceManufacturer() {
        return mBtDeviceManufacturer;
    }

    private void setBtDeviceManufacturer(FixedArray<Byte> mBtDeviceManufacturer) {
        this.mBtDeviceManufacturer = mBtDeviceManufacturer;
    }

    public FixedArray<Byte> getBtChipsetManufacturer() {
        return mBtChipsetManufacturer;
    }

    private void setBtChipsetManufacturer(FixedArray<Byte> mBtChipsetManufacturer) {
        this.mBtChipsetManufacturer = mBtChipsetManufacturer;
    }

    public byte getBtLinkSupervisionTimeout() {
        return mBtLinkSupervisionTimeout;
    }

    public void setBtLinkSupervisionTimeout(byte mBtLinkSupervisionTimeout) {
        this.mBtLinkSupervisionTimeout = mBtLinkSupervisionTimeout;
    }

    public FixedArray<Byte> getCpCpuInfo() {
        return mCpCpuInfo;
    }

    public void setCpCpuInfo(FixedArray<Byte> mCpCpuInfo) {
        this.mCpCpuInfo = mCpCpuInfo;
    }

    public FixedArray<Byte> getVpCpuInfo() {
        return mVpCpuInfo;
    }

    public void setVpCpuInfo(FixedArray<Byte> mVpCpuInfo) {
        this.mVpCpuInfo = mVpCpuInfo;
    }

    public FixedArray<Byte> getBarcodeCamera() {
        return mBarcodeCamera;
    }

    public void setBarcodeCamera(FixedArray<Byte> mBarcodeCamera) {
        this.mBarcodeCamera = mBarcodeCamera;
    }

    private EpocVersion mCpFirmwareVer;
    private EpocVersion mVpFirmwareVer;
    private EpocVersion mBarcodeFirmwareVer;
    private EpocVersion mCooxFirmwareVer;
    private EpocVersion mCompTableVer;
    private short mCompTableType;
    private byte mMainboardCid;
    private FixedArray<Byte> mMbSerialNum;
    private byte mSibCid;
    private FixedArray<Byte> mSIBSerialNum;
    private int mSpectrometerSerialNum;
    private short mMechanicId;
    private EpocVersion mBluetoothVer;
    private FixedArray<Byte> mBtProductVariant;
    private FixedArray<Byte> mBtCsrBuildNum;
    private byte mBtTdkBuildNum;
    private FixedArray<Byte> mBtTdkVer;
    private FixedArray<Byte> mBtAddress;
    private FixedArray<Byte> mBtDeviceManufacturer;
    private FixedArray<Byte> mBtChipsetManufacturer;
    private byte mBtLinkSupervisionTimeout;
    private FixedArray<Byte> mCpCpuInfo;// { get; private set; }  // Currently unused
    private FixedArray<Byte> mVpCpuInfo;// { get; private set; }  // Currently unused
    private FixedArray<Byte> mBarcodeCamera;// { get; private set; }  // Currently unused

    public AdditionalIdInfo() {
        mCpFirmwareVer = new EpocVersion(1, 0, 0, 0);
        mVpFirmwareVer = new EpocVersion(1, 0, 0, 0);
        mBarcodeFirmwareVer = new EpocVersion(1, 0, 0, 0);
        mCompTableVer = new EpocVersion(1, 0, 0, 0);
        mCpFirmwareVer = new EpocVersion(1, 0, 0, 0);
        mMbSerialNum = new FixedArray<Byte>(Byte.class, 10);
        mSIBSerialNum = new FixedArray<Byte>(Byte.class, 10);
        mBluetoothVer = new EpocVersion(1, 0, 0, 0);
        mBtProductVariant = new FixedArray<Byte>(Byte.class, 25);
        mBtCsrBuildNum = new FixedArray<Byte>(Byte.class, 4);
        mBtTdkVer = new FixedArray<Byte>(Byte.class, 6);
        mBtAddress = new FixedArray<Byte>(Byte.class, 12);
        mBtDeviceManufacturer = new FixedArray<Byte>(Byte.class, 23);
        mBtChipsetManufacturer = new FixedArray<Byte>(Byte.class, 3);
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(mCpFirmwareVer.toBytes(3));
        output.write(mVpFirmwareVer.toBytes(3));
        output.write(mBarcodeFirmwareVer.toBytes(3));
        output.write(mCooxFirmwareVer.toBytes(3));
        output.write(mCompTableVer.toBytes(2));
        output.write(mCompTableType);
        output.write(mMainboardCid);
        output.write(FixedArray.toPrimitive(mMbSerialNum.getData()));
        output.write(mSibCid);
        output.write(FixedArray.toPrimitive(mSIBSerialNum.getData()));
        output.write(mSpectrometerSerialNum);
        output.write(mMechanicId);
        output.write(mBluetoothVer.toBytes(1));
        output.write(FixedArray.toPrimitive(mBtProductVariant.getData()));
        output.write(FixedArray.toPrimitive(mBtCsrBuildNum.getData()));
        output.write(mBtTdkBuildNum);
        output.write(FixedArray.toPrimitive(mBtTdkVer.getData()));
        output.write(FixedArray.toPrimitive(mBtAddress.getData()));
        output.write(FixedArray.toPrimitive(mBtDeviceManufacturer.getData()));
        output.write(FixedArray.toPrimitive(mBtChipsetManufacturer.getData()));
        output.write(mBtLinkSupervisionTimeout);
        //NOT DEFINED (0 bytes in spec)
        //output.write(CpCpuInfo);
        //output.write(VpCpuInfo);
        //output.write(BarcodeCamera);
        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {

        mCpFirmwareVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 3));
        mVpFirmwareVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 3));
        mBarcodeFirmwareVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 3));
        mCooxFirmwareVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 3));
        mCompTableVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 2));
        mCompTableType = BigEndianBitConverter.toInt16(dsr, 2, 0);
        mMainboardCid = dsr.readByte();
        mMbSerialNum.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mMbSerialNum.FixedLen)));
        mSibCid = dsr.readByte();
        mSIBSerialNum.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mSIBSerialNum.FixedLen)));
        mSpectrometerSerialNum = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mMechanicId = BigEndianBitConverter.toInt16(dsr, 2, 0);
        mBluetoothVer = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 1));
        mBtProductVariant.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mBtProductVariant.FixedLen)));
        mBtCsrBuildNum.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mBtCsrBuildNum.FixedLen)));
        mBtTdkBuildNum = dsr.readByte();
        mBtTdkVer.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mBtTdkVer.FixedLen)));
        mBtAddress.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mBtAddress.FixedLen)));
        mBtDeviceManufacturer.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mBtDeviceManufacturer.FixedLen)));
        mBtChipsetManufacturer.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mBtChipsetManufacturer.FixedLen)));
        mBtLinkSupervisionTimeout = dsr.readByte();
        //NOT DEFINED (0 bytes in spec)
        //CpCpuInfo;
        //VpCpuInfo;
        //BarcodeCamera;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CpFirmwareVer: " + mCpFirmwareVer.toString());
        sb.append("VpFirmwareVer: " + mVpFirmwareVer.toString());
        sb.append("BarcodeFirmwareVer: " + mBarcodeFirmwareVer.toString());
        sb.append("CooxFirmwareVer: " + mCooxFirmwareVer.toString());
        sb.append("CompTableVer: " + mCompTableVer.toString());
        sb.append("CompTableType: " + mCompTableType);
        sb.append("MainboardCid: " + mMainboardCid);
        sb.append("MbSerialNum: " + mMbSerialNum.toString());
        sb.append("SpectrometerSerialNum: " + mSpectrometerSerialNum);
        sb.append("MechanicId: " + mMechanicId);
        sb.append("BluetoothVer: " + mBluetoothVer.toString());
        sb.append("BtProductVariant: " + mBtProductVariant.toString());
        sb.append("BtCsrBuildNum: " + mBtCsrBuildNum.toString());
        sb.append("BtTdkBuildNum: " + mBtTdkBuildNum);
        sb.append("BtTdkVer: " + mBtTdkVer.toString());
        sb.append("BtAddress: " + mBtAddress.toString());
        sb.append("BtDeviceManufacturer: " + mBtDeviceManufacturer.toString());
        sb.append("BtChipsetManufacturer: " + mBtChipsetManufacturer.toString());
        sb.append("BtLinkSupervisionTimeout: " + mBtLinkSupervisionTimeout);
        return sb.toString();
    }
}
