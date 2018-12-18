package com.epocal.reader.protocolcommontype;

/**
 * Created by dning on 7/26/2017.
 */

public enum HostErrorCode {
    NoError ((byte)0),
    CardNotInserted ((byte)1),
    HandleNotTurned ((byte)2),
    CalNotDetected ((byte)3),
    SampleNotDetected ((byte)4),
    HandleOpenedDuringFluidics ((byte)5),
    CardRemovedDuringFluidics ((byte)6),
    ErrorOccurredDuringFluidics ((byte)7),
    UserStoppedTestDuringFluidics ((byte)8),
    ReaderStoppedTestDuringFluidics ((byte)9),
    DisconnectedDuringFluidics ((byte)10),
    TestIncomplete ((byte)11),
    ReaderStoppedRespondingDuringFluidics ((byte)12),
    RealtimeQCFailedDuringFluidics ((byte)13),
    CommunicationsFailureDuringFluidics ((byte)14),
    HandleOpenedDuringSampleIntro ((byte)15),
    CardRemovedDuringSampleIntro ((byte)16),
    ErrorOccurredDuringSampleIntro ((byte)17),
    UserStoppedTestDuringSampleIntro ((byte)18),
    ReaderStoppedTestDuringSampleIntro ((byte)19),
    DisconnectedDuringSampleIntro ((byte)20),
    ReaderStoppedRespondingDuringSampleIntro ((byte)21),
    RealtimeQCFailedDuringSampleIntro ((byte)22),
    CommunicationsFailureDuringSampleIntro ((byte)23),
    HandleOpenedDuringSampling ((byte)24),
    CardRemovedDuringSampling ((byte)25),
    ErrorOccurredDuringSampling ((byte)26),
    UserStoppedTestDuringSampling ((byte)27),
    ReaderStoppedTestDuringSampling ((byte)28),
    DisconnectedDuringSampling ((byte)29),
    ReaderStoppedRespondingDuringSampling ((byte)30),
    RealtimeQCFailedDuringSampling ((byte)31),
    CommunicationsFailureDuringSampling ((byte)32),
    HandleClosedTooFast ((byte)33),
    SampleInjectedTooFast ((byte)34),
    SampleInjectedTooSlowly ((byte)35),
    NoPatientId ((byte)36),
    HandleTurnedBeforeReaderReady ((byte)37),
    ThermalQCFailedDuringFluidics ((byte)38),
    ThermalQCFailedDuringSampleIntro ((byte)39),
    ThermalQCFailedDuringSampling ((byte)40),
    NoLotNumber ((byte)41),
    SavedDuringCalibration ((byte)42),
    SavedDuringSampleIntro ((byte)43),
    SavedDuringSampleProcessing ((byte)44),
    ExpiredCard ((byte)45),
    NoSampleType ((byte)46),
    NoApplyHemodilution ((byte)47),
    NoTestsSelected ((byte)48),
    SampleFailedQC ((byte)49),
    FluidicsFailedQCDuringCalibration ((byte)50),
    FluidicsFailedQCDuringSampleIntro ((byte)51),
    FluidicsFailedQCDuringSampling ((byte)52),
    HumidityCheckFailed ((byte)53),
    HematocritLowResistance ((byte)54),
    EarlyInjection ((byte)55),
    ReferenceBubble ((byte)56);

    public final byte value;
    HostErrorCode(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static HostErrorCode convert(byte value) {return HostErrorCode.values()[value];}
}
