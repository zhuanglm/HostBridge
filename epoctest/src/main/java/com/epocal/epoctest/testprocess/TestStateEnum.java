package com.epocal.epoctest.testprocess;

import com.epocal.statemachine.IStateEnum;

/**
 * Created by dning on 5/24/2017.
 */

public enum TestStateEnum implements IStateEnum {
    UnKnown((byte) 0) {
        @Override
        public String toString() {
            return "UnKnown";
        }
    },
    Root((byte) 1) {
        @Override
        public String toString() {
            return "Root";
        }
    },

    Connection((byte) 2) {
        @Override
        public String toString() {
            return "Connection";
        }
    },
    Disconnected((byte) 3) {
        @Override
        public String toString() {
            return "Disconnected";
        }
    },
    Connecting((byte) 4) {
        @Override
        public String toString() {
            return "connecting";
        }
    },
    Connected((byte) 5) {
        @Override
        public String toString() {
            return "Connected";
        }
    },

    TestStateBody((byte) 6) {
        @Override
        public String toString() {
            return "TestStateBody";
        }
    },

    ReaderInitialize((byte) 7) {
        @Override
        public String toString() {
            return "ReaderInitialize";
        }
    },
    GetDeviceId((byte) 8) {
        @Override
        public String toString() {
            return "GetDeviceId";
        }
    },
    SendHostIdInfo((byte) 9) {
        @Override
        public String toString() {
            return "SendHostIdInfo";
        }
    },
    GetDeviceStatus((byte) 10) {
        @Override
        public String toString() {
            return "GetDeviceStatus";
        }
    },
    SetReaderInterface((byte) 11) {
        @Override
        public String toString() {
            return "SetReaderInterface";
        }
    },

    ReaderSettings((byte) 12) {
        @Override
        public String toString() {
            return "ReaderSettings";
        }
    },
    EnableReaderSettingsSession((byte) 13) {
        @Override
        public String toString() {
            return "EnableReaderSettingsSession";
        }
    },
    SetReaderGeneralConfig((byte) 14) {
        @Override
        public String toString() {
            return "SetReaderGeneralConfig";
        }
    },
    SetReadereQCConfig((byte) 15) {
        @Override
        public String toString() {
            return "SetReadereQCConfig";
        }
    },
    SetReaderDCCConfig((byte) 16) {
        @Override
        public String toString() {
            return "SetReaderDCCConfig";
        }
    },
    DisableReaderSettingsSession((byte) 17) {
        @Override
        public String toString() {
            return "DisableReaderSettingsSession";
        }
    },

    ReaderEQC((byte) 18) {
        @Override
        public String toString() {
            return "ReaderEQC";
        }
    },
    EnableReaderEQCSession((byte) 19) {
        @Override
        public String toString() {
            return "EnableReaderEQCSession";
        }
    },
    PerformReaderEQCStart((byte) 20) {
        @Override
        public String toString() {
            return "PerformReaderEQCStart";
        }
    },
    PerformReaderEQC((byte) 21) {
        @Override
        public String toString() {
            return "PerformReaderEQC";
        }
    },
    PerformReaderEQCEnd((byte) 22) {
        @Override
        public String toString() {
            return "PerformReaderEQCEnd";
        }
    },
    DisableReaderEQCSession((byte) 23) {
        @Override
        public String toString() {
            return "DisableReaderEQCSession";
        }
    },

    GetDeviceStatusBeforeTestStart((byte) 24) {
        @Override
        public String toString() {
            return "GetDeviceStatusBeforeTestStart";
        }
    },

    PatientTest((byte) 25) {
        @Override
        public String toString() {
            return "PatientTest";
        }
    },
    EnablePatientTestSession((byte) 26) {
        @Override
        public String toString() {
            return "EnablePatientTestSession";
        }
    },
    ReadyTest((byte) 27) {
        @Override
        public String toString() {
            return "ReadyTest";
        }
    },
    EnableTest((byte) 28) {
        @Override
        public String toString() {
            return "EnableTest";
        }
    },

    DryCardCheck((byte) 29) {
        @Override
        public String toString() {
            return "DryCardCheck";
        }
    },
    PerformDryCardCheckStart((byte) 30) {
        @Override
        public String toString() {
            return "PerformDryCardCheckStart";
        }
    },
    PerformDryCardCheck((byte) 31) {
        @Override
        public String toString() {
            return "PerformDryCardCheck";
        }
    },
    PerformDryCardCheckEnd((byte) 32) {
        @Override
        public String toString() {
            return "PerformDryCardCheckEnd";
        }
    },

    AcceptCard((byte) 33) {
        @Override
        public String toString() {
            return "AcceptCard";
        }
    },
    GetDeviceStatusAfterTestStart((byte) 34) {
        @Override
        public String toString() {
            return "GetDeviceStatusAfterTestStart";
        }
    },

    DoTest((byte) 35) {
        @Override
        public String toString() {
            return "DoTest";
        }
    },
    PerformTestStart((byte) 36) {
        @Override
        public String toString() {
            return "PerformTestStart";
        }
    },
    PerformFluidCalibration((byte) 37) {
        @Override
        public String toString() {
            return "PerformFluidCalibration";
        }
    },
    PerformSampleIntroduction((byte) 38) {
        @Override
        public String toString() {
            return "PerformSampleIntroduction";
        }
    },
    PerformSampleProcessing((byte) 39) {
        @Override
        public String toString() {
            return "PerformSampleProcessing";
        }
    },
    PerformTestEnd((byte) 40) {
        @Override
        public String toString() {
            return "PerformTestEnd";
        }
    },

    CancelTest((byte) 42) {
        @Override
        public String toString() {
            return "CancelTest";
        }
    },
    DisableTest((byte) 43) {
        @Override
        public String toString() {
            return "DisableTest";
        }
    },
    UsedCardInReader((byte) 44) {
        @Override
        public String toString() {
            return "UsedCardInReader";
        }
    },

    CardInReader((byte) 45) {
        @Override
        public String toString() {
            return "CardInReader";
        }
    },

    EndTestBeforeTestBegun((byte) 46) {
        @Override
        public String toString() {
            return "EndTestBeforeTestBegun";
        }
    },
    TerminateTest((byte) 47) {
        @Override
        public String toString() {
            return "TerminateTest";
        }
    },
    Done((byte) 48) {
        @Override
        public String toString() {
            return "Done";
        }
    },
    //**added by rzhuang for Legacy
    ActionAfterInitialConnect((byte) 49) {
        @Override
        public String toString() {
            return "ActionAfterInitialConnect";
        }
    },
    GetReaderId((byte) 50) {
        @Override
        public String toString() {
            return "GetReaderId";
        }
    },
    CheckFirstReaderStatus((byte) 51) {
        @Override
        public String toString() {
            return "CheckFirstReaderStatus";
        }
    },

    WaitingForStatisticsResponse((byte) 52) {
        @Override
        public String toString() {
            return "WaitingForStatisticsResponse";
        }
    },

    WaitingForHCMResponse((byte) 53) {
        @Override
        public String toString() {
            return "WaitingForHCMResponse";
        }
    },

    WaitingForConfiguration1_2Ack((byte) 54) {
        @Override
        public String toString() {
            return "WaitingForConfiguration1_2Ack";
        }
    },

    CheckReaderStatus((byte) 55) {
        @Override
        public String toString() {
            return "CheckReaderStatus";
        }
    },

    TestResponseForUpdateReaderStatus((byte) 56) {
        @Override
        public String toString() {
            return "TestResponseForUpdateReaderStatus";
        }
    },

    AfterTestResponseForUpdateReaderStatus((byte) 57) {
        @Override
        public String toString() {
            return "AfterTestResponseForUpdateReaderStatus";
        }
    },

    OldCardInReader((byte) 58) {
        @Override
        public String toString() {
            return "OldCardInReader";
        }
    },

    WaitingForConfiguration1_1Ack((byte) 59) {
        @Override
        public String toString() {
            return "WaitingForConfiguration1_1Ack";
        }
    },

    WaitingForConfiguration1_3Ack((byte) 60) {
        @Override
        public String toString() {
            return "WaitingForConfiguration1_3Ack";
        }
    },

    Ready((byte) 61) {
        @Override
        public String toString() {
            return "Ready";
        }
    },

    WaitingForDeviceEnableAck((byte) 62) {
        @Override
        public String toString() { return "WaitingForDeviceEnableAck"; }
    },

    WaitingForTestGetReaderStatus((byte) 63) {
        @Override
        public String toString() { return "WaitingForTestGetReaderStatus";
        }
    },

    LegacyCardInReader((byte) 64) {
        @Override
        public String toString() { return "LegacyCardInReader"; }
    },

    NewCardInReader((byte) 65) {
        @Override
        public String toString() { return "NewCardInReader"; }
    },

    WaitingForReaderStatusHeaterTemperatures((byte) 66) {
        @Override
        public String toString() { return "WaitingForReaderStatusHeaterTemperatures"; }
    },

    WaitingForConfiguration1_4Ack((byte) 67) {
        @Override
        public String toString() {
            return "WaitingForConfiguration1_4Ack";
        }
    },

    FluidicsCalibration((byte) 68) {
        @Override
        public String toString() {
            return "FluidicsCalibration";
        }
    },

    EndTestAfterTestBegun((byte) 69) {
        @Override
        public String toString() {
            return "EndTestAfterTestBegun";
        }
    },

    SampleIntroduction((byte) 70) {
        @Override
        public String toString() {
            return "SampleIntroduction";
        }
    },

    SampleProcessing((byte) 71) {
        @Override
        public String toString() {
            return "SampleProcessing";
        }
    },

    TestCompleted((byte) 72) {
        @Override
        public String toString() {
            return "TestCompleted";
        }
    },

    TestCompletedCardRemoved((byte) 73) {
        @Override
        public String toString() {
            return "TestCompletedCardRemoved";
        }
    };

    public final byte value;

    TestStateEnum(byte value) {
        this.value = Byte.valueOf(value);
    }

    public static TestStateEnum convert(byte value) {
        return TestStateEnum.values()[value];
    }
}
