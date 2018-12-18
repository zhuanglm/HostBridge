package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.CardStatus;
import com.epocal.reader.enumtype.SelfCheckResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/22/2017.
 */

public class HardwareStatus extends DataFragment
{
    //***added by rzhuang at Aug 1st for Legacy
    private float mDamCPUTemperature;
    private byte[] mSelfCheckResults;
    private float mBarometricSensor;

    public void setDamCPUTemperature(float damCPUTemperature) {
        this.mDamCPUTemperature = damCPUTemperature;
    }

    public void setSelfCheckResults(byte[] selfCheckResults) {
        this.mSelfCheckResults = selfCheckResults;
    }

    public void setBarometricSensor() {
        mBarometricSensor = (mBarometricPressureSensor1 + mBarometricPressureSensor2) / 2;
    }

    public float getBarometricPressureSensor() {
        return mBarometricSensor;
    }

    public byte[] getSelfCheckResults() {
        return mSelfCheckResults;
    }

    public float getDamCPUTemperature() {
        return mDamCPUTemperature;
    }

    //*****************************************

    private float mBatteryLevel;
    private float mAmbientTemp1;
    private float mAmbientTemp2;
    private float mBarometricPressureSensor1;
    private float mBarometricPressureSensor2;
    private float mBGETopHeaterTemp;
    private float mBGEBottomHeaterTemp;
    private float mBGECooxHeater;
    private CardStatus mCardStatus;

    private SelfCheckResult mBgeBootupEqcResult;
    private SelfCheckResult mBgeL1EqcResult;
    private SelfCheckResult mBgeL2EqcResult;
    private SelfCheckResult mCooxEqcResult;

    private SelfCheckResult mSampleDeliveryEqcResult;
    private SelfCheckResult mMotorsEqcResult;
    private SelfCheckResult mPressureSensorsEqcResult;
    private SelfCheckResult mBGEBottomHeaterEqcResult;
    private SelfCheckResult mBGETopHeaterEqcResult;
    private SelfCheckResult mCooxHeaterEqcResult;
    private SelfCheckResult mAmbientTemperatureEqcResult;
    private SelfCheckResult mOpticalSpeedEqcResult;
    private SelfCheckResult mOpticalSampleDetectionEqcResult;
    private SelfCheckResult mBGEOpticalOverflowEqcResult;
    private SelfCheckResult mCooxOpticalOverflowEqcResult;
    private SelfCheckResult mAccelerometerEqcResult;
    private SelfCheckResult mBarcodeEqcResult;

    private byte mInterfaces;
    private byte mSpare;

    public float getBatteryLevel() {
        return mBatteryLevel;
    }

    public void setBatteryLevel(float batteryLevel) {
        mBatteryLevel = batteryLevel;
    }

    public float getAmbientTemp1() {
        return mAmbientTemp1;
    }

    public void setAmbientTemp1(float ambientTemp1) {
        mAmbientTemp1 = ambientTemp1;
    }

    public float getAmbientTemp2() {
        return mAmbientTemp2;
    }

    public void setAmbientTemp2(float ambientTemp2) {
        mAmbientTemp2 = ambientTemp2;
    }

    public float getBarometricPressureSensor1() {
        return mBarometricPressureSensor1;
    }

    public void setBarometricPressureSensor1(float barometricPressureSensor1) {
        mBarometricPressureSensor1 = barometricPressureSensor1;
    }

    public float getBarometricPressureSensor2() {
        return mBarometricPressureSensor2;
    }

    public void setBarometricPressureSensor2(float barometricPressureSensor2) {
        mBarometricPressureSensor2 = barometricPressureSensor2;
    }

    public float getBGETopHeaterTemp() {
        return mBGETopHeaterTemp;
    }

    public void setBGETopHeaterTemp(float BGETopHeaterTemp) {
        this.mBGETopHeaterTemp = BGETopHeaterTemp;
    }

    public float getBGEBottomHeaterTemp() {
        return mBGEBottomHeaterTemp;
    }

    public void setBGEBottomHeaterTemp(float BGEBottomHeaterTemp) {
        this.mBGEBottomHeaterTemp = BGEBottomHeaterTemp;
    }

    public float getBGECooxHeater() {
        return mBGECooxHeater;
    }

    public void setBGECooxHeater(float BGECooxHeater) {
        this.mBGECooxHeater = BGECooxHeater;
    }

    public com.epocal.reader.enumtype.CardStatus getCardStatus() {
        return mCardStatus;
    }

    public void setCardStatus(com.epocal.reader.enumtype.CardStatus cardStatus) {
        mCardStatus = cardStatus;
    }

    public SelfCheckResult getBgeBootupEqcResult() {
        return mBgeBootupEqcResult;
    }

    public void setBgeBootupEqcResult(SelfCheckResult bgeBootupEqcResult) {
        mBgeBootupEqcResult = bgeBootupEqcResult;
    }

    public SelfCheckResult getBgeL1EqcResult() {
        return mBgeL1EqcResult;
    }

    public void setBgeL1EqcResult(SelfCheckResult bgeL1EqcResult) {
        mBgeL1EqcResult = bgeL1EqcResult;
    }

    public SelfCheckResult getBgeL2EqcResult() {
        return mBgeL2EqcResult;
    }

    public void setBgeL2EqcResult(SelfCheckResult bgeL2EqcResult) {
        mBgeL2EqcResult = bgeL2EqcResult;
    }

    public SelfCheckResult getCooxEqcResult() {
        return mCooxEqcResult;
    }

    public void setCooxEqcResult(SelfCheckResult cooxEqcResult) {
        mCooxEqcResult = cooxEqcResult;
    }

    public SelfCheckResult getSampleDeliveryEqcResult() {
        return mSampleDeliveryEqcResult;
    }

    public void setSampleDeliveryEqcResult(SelfCheckResult sampleDeliveryEqcResult) {
        mSampleDeliveryEqcResult = sampleDeliveryEqcResult;
    }

    public SelfCheckResult getMotorsEqcResult() {
        return mMotorsEqcResult;
    }

    public void setMotorsEqcResult(SelfCheckResult motorsEqcResult) {
        mMotorsEqcResult = motorsEqcResult;
    }

    public SelfCheckResult getPressureSensorsEqcResult() {
        return mPressureSensorsEqcResult;
    }

    public void setPressureSensorsEqcResult(SelfCheckResult pressureSensorsEqcResult) {
        mPressureSensorsEqcResult = pressureSensorsEqcResult;
    }

    public SelfCheckResult getBGEBottomHeaterEqcResult() {
        return mBGEBottomHeaterEqcResult;
    }

    public void setBGEBottomHeaterEqcResult(SelfCheckResult BGEBottomHeaterEqcResult) {
        this.mBGEBottomHeaterEqcResult = BGEBottomHeaterEqcResult;
    }

    public SelfCheckResult getBGETopHeaterEqcResult() {
        return mBGETopHeaterEqcResult;
    }

    public void setBGETopHeaterEqcResult(SelfCheckResult BGETopHeaterEqcResult) {
        this.mBGETopHeaterEqcResult = BGETopHeaterEqcResult;
    }

    public SelfCheckResult getCooxHeaterEqcResult() {
        return mCooxHeaterEqcResult;
    }

    public void setCooxHeaterEqcResult(SelfCheckResult cooxHeaterEqcResult) {
        mCooxHeaterEqcResult = cooxHeaterEqcResult;
    }

    public SelfCheckResult getAmbientTemperatureEqcResult() {
        return mAmbientTemperatureEqcResult;
    }

    public void setAmbientTemperatureEqcResult(SelfCheckResult ambientTemperatureEqcResult) {
        mAmbientTemperatureEqcResult = ambientTemperatureEqcResult;
    }

    public SelfCheckResult getOpticalSpeedEqcResult() {
        return mOpticalSpeedEqcResult;
    }

    public void setOpticalSpeedEqcResult(SelfCheckResult opticalSpeedEqcResult) {
        mOpticalSpeedEqcResult = opticalSpeedEqcResult;
    }

    public SelfCheckResult getOpticalSampleDetectionEqcResult() {
        return mOpticalSampleDetectionEqcResult;
    }

    public void setOpticalSampleDetectionEqcResult(SelfCheckResult opticalSampleDetectionEqcResult) {
        mOpticalSampleDetectionEqcResult = opticalSampleDetectionEqcResult;
    }

    public SelfCheckResult getBGEOpticalOverflowEqcResult() {
        return mBGEOpticalOverflowEqcResult;
    }

    public void setBGEOpticalOverflowEqcResult(SelfCheckResult BGEOpticalOverflowEqcResult) {
        this.mBGEOpticalOverflowEqcResult = BGEOpticalOverflowEqcResult;
    }

    public SelfCheckResult getCooxOpticalOverflowEqcResult() {
        return mCooxOpticalOverflowEqcResult;
    }

    public void setCooxOpticalOverflowEqcResult(SelfCheckResult cooxOpticalOverflowEqcResult) {
        mCooxOpticalOverflowEqcResult = cooxOpticalOverflowEqcResult;
    }

    public SelfCheckResult getAccelerometerEqcResult() {
        return mAccelerometerEqcResult;
    }

    public void setAccelerometerEqcResult(SelfCheckResult accelerometerEqcResult) {
        mAccelerometerEqcResult = accelerometerEqcResult;
    }

    public SelfCheckResult getBarcodeEqcResult() {
        return mBarcodeEqcResult;
    }

    public void setBarcodeEqcResult(SelfCheckResult barcodeEqcResult) {
        mBarcodeEqcResult = barcodeEqcResult;
    }

    public byte getInterfaces() {
        return mInterfaces;
    }

    public void setInterfaces(byte interfaces) {
        mInterfaces = interfaces;
    }

    public byte getSpare() {
        return mSpare;
    }

    public void setSpare(byte spare) {
        mSpare = spare;
    }

    public HardwareStatus(){}

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(BigEndianBitConverter.getBytes(mBatteryLevel));
        output.write(BigEndianBitConverter.getBytes(mAmbientTemp1));
        output.write(BigEndianBitConverter.getBytes(mAmbientTemp2));
        output.write(BigEndianBitConverter.getBytes(mBarometricPressureSensor1));
        output.write(BigEndianBitConverter.getBytes(mBarometricPressureSensor2));
        output.write(BigEndianBitConverter.getBytes(mBGETopHeaterTemp));
        output.write(BigEndianBitConverter.getBytes(mBGEBottomHeaterTemp));
        output.write(BigEndianBitConverter.getBytes(mBGECooxHeater));
        output.write(mCardStatus.value);

        output.write(mBgeBootupEqcResult.value);
        output.write(mBgeL1EqcResult.value);
        output.write(mBgeL2EqcResult.value);
        output.write(mCooxEqcResult.value);

        output.write(mSampleDeliveryEqcResult.value);
        output.write(mMotorsEqcResult.value);
        output.write(mPressureSensorsEqcResult.value);
        output.write(mBGEBottomHeaterEqcResult.value);
        output.write(mBGETopHeaterEqcResult.value);
        output.write(mCooxHeaterEqcResult.value);
        output.write(mAmbientTemperatureEqcResult.value);
        output.write(mOpticalSpeedEqcResult.value);
        output.write(mOpticalSampleDetectionEqcResult.value);
        output.write(mBGEOpticalOverflowEqcResult.value);
        output.write(mCooxOpticalOverflowEqcResult.value);
        output.write(mAccelerometerEqcResult.value);
        output.write(mBarcodeEqcResult.value);

        output.write(mInterfaces);
        output.write(mSpare);

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mBatteryLevel = BigEndianBitConverter.toSingle(dsr, 4, 0);
        mAmbientTemp1 = BigEndianBitConverter.toSingle(dsr, 4, 0);
        mAmbientTemp2 = BigEndianBitConverter.toSingle(dsr, 4, 0);
        mBarometricPressureSensor1 = BigEndianBitConverter.toSingle(dsr, 4, 0);
        mBarometricPressureSensor2 = BigEndianBitConverter.toSingle(dsr, 4, 0);
        mBGETopHeaterTemp = BigEndianBitConverter.toSingle(dsr, 4, 0);
        mBGEBottomHeaterTemp = BigEndianBitConverter.toSingle(dsr, 4, 0);
        mBGECooxHeater = BigEndianBitConverter.toSingle(dsr, 4, 0);
        mCardStatus = CardStatus.convert(dsr.readByte());

        mBgeBootupEqcResult = SelfCheckResult.convert(dsr.readByte());
        mBgeL1EqcResult = SelfCheckResult.convert(dsr.readByte());
        mBgeL2EqcResult = SelfCheckResult.convert(dsr.readByte());
        mCooxEqcResult = SelfCheckResult.convert(dsr.readByte());

        mSampleDeliveryEqcResult = SelfCheckResult.convert(dsr.readByte());
        mMotorsEqcResult = SelfCheckResult.convert(dsr.readByte());
        mPressureSensorsEqcResult = SelfCheckResult.convert(dsr.readByte());
        mBGEBottomHeaterEqcResult = SelfCheckResult.convert(dsr.readByte());
        mBGETopHeaterEqcResult = SelfCheckResult.convert(dsr.readByte());
        mCooxHeaterEqcResult = SelfCheckResult.convert(dsr.readByte());
        mAmbientTemperatureEqcResult = SelfCheckResult.convert(dsr.readByte());
        mOpticalSpeedEqcResult = SelfCheckResult.convert(dsr.readByte());
        mOpticalSampleDetectionEqcResult = SelfCheckResult.convert(dsr.readByte());
        mBGEOpticalOverflowEqcResult = SelfCheckResult.convert(dsr.readByte());
        mCooxOpticalOverflowEqcResult = SelfCheckResult.convert(dsr.readByte());
        mAccelerometerEqcResult = SelfCheckResult.convert(dsr.readByte());
        mBarcodeEqcResult = SelfCheckResult.convert(dsr.readByte());

        mInterfaces = dsr.readByte();
        mSpare = dsr.readByte();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("BatteryLevel: " + mBatteryLevel);
        sb.append("AmbientTemp1: " + mAmbientTemp1);
        sb.append("AmbientTemp2: " + mAmbientTemp2);
        sb.append("BarometricPressureSensor1: " + mBarometricPressureSensor1);
        sb.append("BarometricPressureSensor2: " + mBarometricPressureSensor2);
        sb.append("BGETopHeaterTemp: " + mBGETopHeaterTemp);
        sb.append("BGEBottomHeaterTemp: " + mBGEBottomHeaterTemp);
        sb.append("BGECooxHeater: " + mBGECooxHeater);
        sb.append("CardStatus: " + mCardStatus.value);

        sb.append("BgeBootupEqcResult: " + mBgeBootupEqcResult.value);
        sb.append("BgeL1EqcResult: " + mBgeL1EqcResult.value);
        sb.append("BgeL2EqcResult: " + mBgeL2EqcResult.value);
        sb.append("CooxEqcResult: " + mCooxEqcResult.value);

        sb.append("SampleDeliveryEqcResult: " + mSampleDeliveryEqcResult.value);
        sb.append("MotorsEqcResult: " + mMotorsEqcResult.value);
        sb.append("PressureSensorsEqcResult: " + mPressureSensorsEqcResult.value);
        sb.append("BGEBottomHeaterEqcResult: " + mBGEBottomHeaterEqcResult.value);
        sb.append("BGETopHeaterEqcResult: " + mBGETopHeaterEqcResult.value);
        sb.append("CooxHeaterEqcResult: " + mCooxHeaterEqcResult.value);
        sb.append("AmbientTemperatureEqcResult: " + mAmbientTemperatureEqcResult.value);
        sb.append("OpticalSpeedEqcResult: " + mOpticalSpeedEqcResult.value);
        sb.append("OpticalSampleDetectionEqcResult: " + mOpticalSampleDetectionEqcResult.value);
        sb.append("BGEOpticalOverflowEqcResult: " + mBGEOpticalOverflowEqcResult.value);
        sb.append("CooxOpticalOverflowEqcResult: " + mCooxOpticalOverflowEqcResult.value);
        sb.append("AccelerometerEqcResult: " + mAccelerometerEqcResult.value);
        sb.append("BarcodeEqcResult: " + mBarcodeEqcResult.value);

        sb.append("Interfaces: " + mInterfaces);
        sb.append("Spare: " + mSpare);
        return sb.toString();
    }

}
