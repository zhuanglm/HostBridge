package com.epocal.common.am;

import com.epocal.common.types.am.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a mirror of struct SensorReadings defined in sensorreadings.h
 * to be compatible with Analytical Manager C++.
 * DO NOT EDIT THIS FILE without consulting with AM-C++ team first!
 */
public class SensorReadings {
    public boolean requirementsFailedQC;
    public ResultsCalcReturnCode returnCode;
    public Sensors sensorType;
    public byte sensorDescriptorNumber;
    public ChannelType channelType;
    public AnalyteName analyte;
    public String analyteString;
    public List<Reading> readings;
    public int readingPointer;
    public int numThisTypeReading;
    public double result;
    public double multiplicationFactor;
    public SensorInfo sensorDescriptor;
    public RealTimeQCReturnCode realTimeQCPassed;
    public RealTimeQCReturnCode realTimeQCFailedEver;
    public boolean checkRealtimeQC;
    public double reportableLow;
    public double reportableHigh;
    public Levels levels;
    public HumidityReturnCode humidityPassed;
    public double insanityLow;
    public double insanityHigh;
    public double insanityQALow;
    public double insanityQAHigh;

    public int RealTimeQCFailureTotal;
    public int HumidityQCFailureTotal;
    public int AirQCFailureTotal;
    public String RealTimeQCFailureOccuranceString;
    public String HumidityQCFailureOccuranceString;
    public String AirQCFailureOccuranceString;
    public String extraString;
    public String resultString;

    public SensorReadings() {
        this.sensorType = Sensors.ENUM_UNINITIALIZED;
        this.channelType = ChannelType.ENUM_UNINITIALIZED;
        this.analyte = AnalyteName.ENUM_UNINITIALIZED;
        this.readings = new ArrayList();
        this.sensorDescriptor = new SensorInfo();
        this.levels = new Levels();
        this.readingPointer = 0;
        this.checkRealtimeQC = true;
        this.analyteString = "";
        this.RealTimeQCFailureOccuranceString = "";
        this.HumidityQCFailureOccuranceString = "";
        this.AirQCFailureOccuranceString = "";
        this.extraString = "";
        this.resultString = "";

        // Initialization code
        this.realTimeQCPassed = RealTimeQCReturnCode.Success;
        this.realTimeQCFailedEver = RealTimeQCReturnCode.Success;
        this.multiplicationFactor = 1;
        this.returnCode = ResultsCalcReturnCode.Success;
        this.humidityPassed = HumidityReturnCode.NotPerformedYet;
        this.requirementsFailedQC = false;
    }

    @Override
    public String toString() {
        return "SensorReadings{" +
                "requirementsFailedQC=" + requirementsFailedQC +
                ", returnCode=" + returnCode +
                ", sensorType=" + sensorType +
                ", sensorDescriptorNumber=" + sensorDescriptorNumber +
                ", channelType=" + channelType +
                ", analyte=" + analyte +
                ", analyteString='" + analyteString + '\'' +
                ", readings=" + readings +
                ", readingPointer=" + readingPointer +
                ", numThisTypeReading=" + numThisTypeReading +
                ", result=" + result +
                ", multiplicationFactor=" + multiplicationFactor +
                ", sensorDescriptor=" + sensorDescriptor +
                ", realTimeQCPassed=" + realTimeQCPassed +
                ", realTimeQCFailedEver=" + realTimeQCFailedEver +
                ", checkRealtimeQC=" + checkRealtimeQC +
                ", reportableLow=" + reportableLow +
                ", reportableHigh=" + reportableHigh +
                ", levels=" + levels +
                ", humidityPassed=" + humidityPassed +
                ", insanityLow=" + insanityLow +
                ", insanityHigh=" + insanityHigh +
                ", insanityQALow=" + insanityQALow +
                ", insanityQAHigh=" + insanityQAHigh +
                ", RealTimeQCFailureTotal=" + RealTimeQCFailureTotal +
                ", HumidityQCFailureTotal=" + HumidityQCFailureTotal +
                ", AirQCFailureTotal=" + AirQCFailureTotal +
                ", RealTimeQCFailureOccuranceString='" + RealTimeQCFailureOccuranceString + '\'' +
                ", HumidityQCFailureOccuranceString='" + HumidityQCFailureOccuranceString + '\'' +
                ", AirQCFailureOccuranceString='" + AirQCFailureOccuranceString + '\'' +
                ", extraString='" + extraString + '\'' +
                ", resultString='" + resultString + '\'' +
                '}';
    }
}