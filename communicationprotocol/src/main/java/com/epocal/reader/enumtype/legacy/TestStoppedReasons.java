package com.epocal.reader.enumtype.legacy;

/**
 * Created by rzhuang on Aug 3 2018.
 */

public enum TestStoppedReasons
{
    CPStopMessageReceived ((byte)0),
    MultipleQCFailed ((byte)1),
    SelfTestEnded ((byte)2),
    DryCardCheckEnded ((byte)3),
    CalDetectionTimerExpired ((byte)4),
    SampleIntroductionTimerExpired ((byte)5),
    SamplingTimerExpired ((byte)6),
    Reserved1 ((byte)7),
    Reserved2 ((byte)8),
    Reserved3 ((byte)9),
    HandleSwitchOff ((byte)0xA),
    HandleTurningTimerExpired ((byte)0xB),
    ThermalQCFailed ((byte)0xC),
    CardRemoved ((byte)0xD),
    CalFluidNotDetected ((byte)0xE),
    DetectionSwitchBounce ((byte)0xF);

    public final byte value;
    TestStoppedReasons(byte value) {
        this.value = value;
    }

    public static TestStoppedReasons convert(byte val) {
        return TestStoppedReasons.values()[val];
    }
}
