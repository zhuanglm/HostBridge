package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 5/29/2017.
 */

public enum TestErrorCode {
    NoError(0),
    CardNotInserted (1),
    HandleNotTurned (2),
    CalibrationFluidNotDetected (3),
    SampleNotIntroducedInTime (4),
    HandleOpenedDuringFluidics (5),
    CardRemovedFromReader1 (6),
    ReaderGeneralError1 (7),
    UserCancelledTest1 (8),
    ReaderGeneralError2 (9),
    ReaderConnectionLost1 (10),
    TestIncomplete (11),
    ReaderStoppedResponding1 (12),
    SensorCheck1 (13),
    ReaderConnectionLost2 (14),
    HandleOpenedDuringSampleIntro (5),
    CardRemovedFromReader2 (16),
    ReaderGeneralError3(17),
    UserCancelledTest2 (18),
    ReaderGeneralError4 (19),
    ReaderConnectionLost3 (20),
    ReaderStoppedResponding2 (21),
    SensorCheck2 (22),
    ReaderConnectionLost4 (23),
    HandleOpenedDuringSampling (24),
    CardRemovedFromReader3 (25),
    ReaderGeneralError5 (26),
    UserCancelledTest3 (27),
    ReaderGeneralError6 (28),
    ReaderConnectionLost5 (29),
    ReaderStoppedResponding3 (30),
    SensorCheck3 (31),
    ReaderConnectionLost6 (32),
    HandleClosedTooFast (33),
    FastSampleInjection (34),
    InsufficientSampleDetected (35),
    MissingPatiendId (36),
    HandleTurnedBeforeReaderReady (37),
    ThermalCheck1 (38),
    ThermalCheck2 (39),
    ThermalCheck3 (40),
    MissingLotNumber (41),
    SavedDuringCalibration (42),
    SavedDuringSampleIntro (43),
    SavedDuringSampleProcessing (44),
    ExpiredCard (45),
    MissingSampleType (46),
    MissingHemodilutionCorrectionFactor (47),
    MissingTestSelection (48),
    SampleDelivery (49),
    FluidicsCheck1 (50),
    FluidicsCheck2 (51),
    FluidicsCheck3 (52),
    HumditiyCheck (53),
    ResistanceCheck (54),
    EarlySample (55),
    ReferenceBubble (56),
    CardNotInsertedWithinLimit (201),
    LeverNotTurnedWithinLimit (202),
    NoCalibrationFluidInCard (203),
    SampleNotIntroducedWithinLimit (204),
    LeverWasopenedDuringCalibration (205),
    CardRemovedFromReaderDuringCalibration (206),
    ErrorOccurredDurnigCalibration (207),
    UserStoppedTestDuringCalibration (208),
    ReaderStoppedDuringCalibration (209),
    HostDisconnectedFromReaderDuringCalibration (210),
    TestNotCompleted (211),
    ReaderStoppedRespondingDuringCalibration (212),
    InternalQualityControlFailedDuringCalibration (213),
    CommunicationsFailureDuringCalibration (214),
    LeverOpenedWhileWaitingForSample (215),
    CardRemovedFromReaderWhileWaitingForSample (216),
    ErrorOccurredWhileWaitingForSample (217),
    UserStoppedTestWhileWaitingForSample (218),
    ReaderStoppedTestWhileWaitingForSample (219),
    HostDisconnectedFromReaderWhileWaitingForSample (220),
    ReaderStoppedRespondingWhileWaitingForSample (221),
    InternalQualityControlFailedWhileWaitingForSample (222),
    CommunicationsFailureWhileWaitingForSample (223),
    LeverOpenedDuringSampling (224),
    CardRemovedFromReaderDuringSampling (225),
    ErrorOccurredDuringSampling (226),
    UserStoppedTestDuringSampling (227),
    ReaderStoppedTestDuringSampling (228),
    HostDisconnectedFromReaderDuringSampling (229),
    ReaderStoppedRespondingDuringSampling (230),
    InternalQualityControlFailedDuringSampling (231),
    CommunicationsFailureDuringSampling (232),
    LeverClosedTooQuickly (233),
    SampleInjectedTooQuickly (234),
    SampleInjectedTooSlowly (235),
    PatientIdNotEnteredWithTest (236),
    HandleTurnedBeforeTheReaderPreparedToStartTest (237),
    ThermalQcFailedDuringCalibration (238),
    ThermalQcFailedWhileWaitingForSample (239),
    ThermalQcFailedDuringSampling (240),
    LotNumberNotEnteredWithTest (241),
    TestSavedDuringCalibration (242),
    TestSavedDuringSampleIntroduction (243),
    TestSavedDuringSampleProcessing (244),
    TestRunWithExpiredCard (245),
    SampleTypeNotEnteredWithTest (246),
    HemodilutionApplicationNotEnteredWithTest (247),
    NoTestsSelectedForDisplay (248),
    CriticalResultHandlingNotEnteredWithTest (249),
    Unknown (1000);

    public final Integer value;
    TestErrorCode(Integer value)
    {
        this.value = value;
    }
    private  static final Map<Integer,TestErrorCode> typeMap = new HashMap<Integer,TestErrorCode>();
    static {
        for (TestErrorCode type : TestErrorCode.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static TestErrorCode fromInt(int i){
        TestErrorCode retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return TestErrorCode.Unknown;
        }
        return retval;
    }
}
