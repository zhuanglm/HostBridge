package com.epocal.reader.type.Legacy;

import java.util.ArrayList;

/**
 * Created by rzhuang on Aug 2 2018.
 */

public class ReaderQCInfo
{
//    private String mQCDateTime;
//    private String mReaderSerialNumber;
//    private String mOperatorId;
//    private String mBatteryLevel;
//    private String mAmbientTemperature;
//    private String mAmbientPressure;
//    private String mSensorConfig;
//    private String mHostSerialNumber;
//    private String mHostVersion;
//    private String mReaderHardware;
//    private String mReaderMechanical;
//    private String mReaderSWVersion;
//    private String mPassFail;
//    private String mSelfTestResult;
//    private String mErrorCode;
    public ArrayList<SelfTestPacketInfo> selfTestValues;

    public ReaderQCInfo()
    {
        selfTestValues = new ArrayList<>();
    }
}