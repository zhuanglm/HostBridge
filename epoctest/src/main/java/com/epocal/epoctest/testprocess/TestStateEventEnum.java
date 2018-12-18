package com.epocal.epoctest.testprocess;

import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 5/24/2017.
 */

public enum TestStateEventEnum implements IEventEnum {
    UnKnown {
        @Override
        public String toString() {
            return "UnKnown";
        }
    },
    UnHandled {
        @Override
        public String toString() {
            return "UnHandled";
        }
    },
    Handled {
        @Override
        public String toString() {
            return "Handled";
        }
    },
    Disconnected {
        @Override
        public String toString() {
            return "Disconnected";
        }
    },
    Connecting {
        @Override
        public String toString() {
            return "connecting";
        }
    },
    Connected {
        @Override
        public String toString() {
            return "Connected";
        }
    },

    TestStateBody {
        @Override
        public String toString() {
            return "TestStateBody";
        }
    },

    GetDeviceId {
        @Override
        public String toString() {
            return "GetDeviceId";
        }
    },

    SendHostIdInfo {
        @Override
        public String toString() {
            return "SendHostIdInfo";
        }
    },
    GetDeviceStatus {
        @Override
        public String toString() {
            return "GetDeviceStatus";
        }
    },
    SetReaderInterface {
        @Override
        public String toString() {
            return "SetReaderInterface";
        }
    },

    ReaderSettings {
        @Override
        public String toString() {
            return "ReaderSettings";
        }
    },
    EnableReaderSettingsSession {
        @Override
        public String toString() {
            return "EnableReaderSettingsSession";
        }
    },
    SetReaderGeneralConfig {
        @Override
        public String toString() {
            return "SetReaderGeneralConfig";
        }
    },
    SetReadereQCConfig {
        @Override
        public String toString() {
            return "SetReadereQCConfig";
        }
    },
    SetReaderDCCConfig {
        @Override
        public String toString() {
            return "SetReaderDCCConfig";
        }
    },
    DisableReaderSettingsSession {
        @Override
        public String toString() {
            return "DisableReaderSettingsSession";
        }
    },

    ReaderEQC {
        @Override
        public String toString() {
            return "ReaderEQC";
        }
    },
    EnableReaderEQCSession {
        @Override
        public String toString() {
            return "EnableReaderEQCSession";
        }
    },
    PerformReaderEQCStart {
        @Override
        public String toString() {
            return "PerformReaderEQCStart";
        }
    },
    PerformReaderEQC {
        @Override
        public String toString() {
            return "PerformReaderEQC";
        }
    },
    PerformReaderEQCEnd {
        @Override
        public String toString() {
            return "PerformReaderEQCEnd";
        }
    },
    DisableReaderEQCSession {
        @Override
        public String toString() {
            return "DisableReaderEQCSession";
        }
    },

    GetDeviceStatusBeforeTestStart {
        @Override
        public String toString() {
            return "GetDeviceStatusBeforeTestStart";
        }
    },

    PatientTest {
        @Override
        public String toString() {
            return "PatientTest";
        }
    },
    EnablePatientTestSession {
        @Override
        public String toString() {
            return "EnablePatientTestSession";
        }
    },
    ReadyTest {
        @Override
        public String toString() {
            return "ReadyTest";
        }
    },
    EnableTest {
        @Override
        public String toString() {
            return "EnableTest";
        }
    },

    DryCardCheck {
        @Override
        public String toString() {
            return "DryCardCheck";
        }
    },
    PerformDryCardCheckStart {
        @Override
        public String toString() {
            return "PerformDryCardCheckStart";
        }
    },
    PerformDryCardCheck {
        @Override
        public String toString() {
            return "PerformDryCardCheck";
        }
    },
    PerformDryCardCheckEnd {
        @Override
        public String toString() {
            return "PerformDryCardCheckEnd";
        }
    },

    AcceptCard {
        @Override
        public String toString() {
            return "AcceptCard";
        }
    },
    GetDeviceStatusAfterTestStart {
        @Override
        public String toString() {
            return "GetDeviceStatusAfterTestStart";
        }
    },

    DoTest {
        @Override
        public String toString() {
            return "DoTest";
        }
    },
    PerformTestStart {
        @Override
        public String toString() {
            return "PerformTestStart";
        }
    },
    PerformFluidCalibration {
        @Override
        public String toString() {
            return "PerformFluidCalibration";
        }
    },
    PerformSampleIntroduction {
        @Override
        public String toString() {
            return "PerformSampleIntroduction";
        }
    },
    PerformSampleProcessing {
        @Override
        public String toString() {
            return "PerformSampleProcessing";
        }
    },
    PerformTestEnd {
        @Override
        public String toString() {
            return "PerformTestEnd";
        }
    },

    CancelTest {
        @Override
        public String toString() {
            return "CancelTest";
        }
    },
    DisableTest {
        @Override
        public String toString() {
            return "DisableTest";
        }
    },
    UsedCardInReader {
        @Override
        public String toString() {
            return "UsedCardInReader";
        }
    },

    CardInReader {
        @Override
        public String toString() {
            return "CardInReader";
        }
    },

    EndTestBeforeTestBegun {
        @Override
        public String toString() {
            return "EndTestBeforeTestBegun";
        }
    },
    TerminateTest {
        @Override
        public String toString() {
            return "TerminateTest";
        }
    },
    Done {
        @Override
        public String toString() {
            return "Done";
        }
    },
    //**added by rzhuang for Legacy
    ActionAfterInitialConnect {
        @Override
        public String toString() {
            return "ActionAfterInitialConnect";
        }
    },

    GetReaderId {
        @Override
        public String toString() {
            return "GetReaderId";
        }
    },

    CheckFirstReaderStatus {
        @Override
        public String toString() {
            return "CheckFirstReaderStatus";
        }
    },

    WaitingForStatisticsResponse {
        @Override
        public String toString() {
            return "WaitingForStatisticsResponse";
        }
    },

    WaitingForDeviceEnableAck {
        @Override
        public String toString() { return "WaitingForDeviceEnableAck"; }
    },

    WaitingForHCMResponse {
        @Override
        public String toString() { return "WaitingForHCMResponse"; }
    },

    WaitingForConfiguration1_2Ack {
        @Override
        public String toString() { return "WaitingForConfiguration1_2Ack"; }
    },

    CheckReaderStatus {
        @Override
        public String toString() {
            return "CheckReaderStatus";
        }
    },

    TestResponseForUpdateReaderStatus {
        @Override
        public String toString() {
            return "TestResponseForUpdateReaderStatus";
        }
    },

    AfterTestResponseForUpdateReaderStatus {
        @Override
        public String toString() {
            return "AfterTestResponseForUpdateReaderStatus";
        }
    },

    OldCardInReader {
        @Override
        public String toString() {
            return "OldCardInReader";
        }
    },

    WaitingForConfiguration1_1Ack {
        @Override
        public String toString() {
            return "WaitingForConfiguration1_1Ack";
        }
    },

    WaitingForConfiguration1_3Ack {
        @Override
        public String toString() {
            return "WaitingForConfiguration1_3Ack";
        }
    },

    Ready {
        @Override
        public String toString() {
            return "Ready";
        }
    },

    WaitingForTestGetReaderStatus {
        @Override
        public String toString() {
            return "WaitingForTestGetReaderStatus";
        }
    },

    LegacyCardInReader {
        @Override
        public String toString() { return "LegacyCardInReader"; }
    },

    NewCardInReader {
        @Override
        public String toString() { return "NewCardInReader"; }
    },

    WaitingForReaderStatusHeaterTemperatures {
        @Override
        public String toString() { return "WaitingForReaderStatusHeaterTemperatures"; }
    },

    WaitingForConfiguration1_4Ack {
        @Override
        public String toString() {
            return "WaitingForConfiguration1_4Ack";
        }
    },

    FluidicsCalibration {
        @Override
        public String toString() {
            return "FluidicsCalibration";
        }
    },

    EndTestAfterTestBegun {
        @Override
        public String toString() {
            return "EndTestAfterTestBegun";
        }
    },

    SampleIntroduction {
        @Override
        public String toString() {
            return "SampleIntroduction";
        }
    },

    SampleProcessing {
        @Override
        public String toString() {
            return "SampleProcessing";
        }
    },

    TestCompleted {
        @Override
        public String toString() {
            return "TestCompleted";
        }
    },

    TestCompletedCardRemoved {
        @Override
        public String toString() {
            return "TestCompletedCardRemoved";
        }
    }
}
