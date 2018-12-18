//package com.epocal.epoctest.testprocess;
//
///**
// * Created by dning on 6/1/2017.
// */
//
//public enum TestStatusEnum {
//    UnKnown((byte) 0) {
//        @Override
//        public String toString() {
//            return "UnKnown";
//        }
//    },
//    Root((byte) 1) {
//        @Override
//        public String toString() {
//            return "Root";
//        }
//    },
//
//    Connection((byte) 2) {
//        @Override
//        public String toString() {
//            return "Connection";
//        }
//    },
//    Disconnected((byte) 3) {
//        @Override
//        public String toString() {
//            return "Disconnected";
//        }
//    },
//    Connecting((byte) 4) {
//        @Override
//        public String toString() {
//            return "connecting";
//        }
//    },
//    Connected((byte) 5) {
//        @Override
//        public String toString() {
//            return "Connected";
//        }
//    },
//
//    TestStateBody((byte) 6) {
//        @Override
//        public String toString() {
//            return "TestStateBody";
//        }
//    },
//
//    ReaderInitialize((byte) 7) {
//        @Override
//        public String toString() {
//            return "ReaderInitialize";
//        }
//    },
//    GetDeviceId((byte) 8) {
//        @Override
//        public String toString() {
//            return "GetDeviceId";
//        }
//    },
//    SendHostIdInfo((byte) 9) {
//        @Override
//        public String toString() {
//            return "SendHostIdInfo";
//        }
//    },
//    GetDeviceStatus((byte) 10) {
//        @Override
//        public String toString() {
//            return "GetDeviceStatus";
//        }
//    },
//    SetReaderInterface((byte) 11) {
//        @Override
//        public String toString() {
//            return "SetReaderInterface";
//        }
//    },
//
//    ReaderSettings((byte) 12) {
//        @Override
//        public String toString() {
//            return "ReaderSettings";
//        }
//    },
//    EnableReaderSettingsSession((byte) 13) {
//        @Override
//        public String toString() {
//            return "EnableReaderSettingsSession";
//        }
//    },
//    SetReaderGeneralConfig((byte) 14) {
//        @Override
//        public String toString() {
//            return "SetReaderGeneralConfig";
//        }
//    },
//    SetReadereQCConfig((byte) 15) {
//        @Override
//        public String toString() {
//            return "SetReadereQCConfig";
//        }
//    },
//    SetReaderDCCConfig((byte) 16) {
//        @Override
//        public String toString() {
//            return "SetReaderDCCConfig";
//        }
//    },
//    DisableReaderSettingsSession((byte) 17) {
//        @Override
//        public String toString() {
//            return "DisableReaderSettingsSession";
//        }
//    },
//
//    ReaderEQC((byte) 18) {
//        @Override
//        public String toString() {
//            return "ReaderEQC";
//        }
//    },
//    EnableReaderEQCSession((byte) 19) {
//        @Override
//        public String toString() {
//            return "EnableReaderEQCSession";
//        }
//    },
//    PerformReaderEQCStart((byte) 20) {
//        @Override
//        public String toString() {
//            return "PerformReaderEQCStart";
//        }
//    },
//    PerformReaderEQC((byte) 21) {
//        @Override
//        public String toString() {
//            return "PerformReaderEQC";
//        }
//    },
//    PerformReaderEQCEnd((byte) 22) {
//        @Override
//        public String toString() {
//            return "PerformReaderEQCEnd";
//        }
//    },
//    DisableReaderEQCSession((byte) 23) {
//        @Override
//        public String toString() {
//            return "DisableReaderEQCSession";
//        }
//    },
//
//    GetDeviceStatusBeforeTestStart((byte) 24) {
//        @Override
//        public String toString() {
//            return "GetDeviceStatusBeforeTestStart";
//        }
//    },
//
//    PatientTest((byte) 25) {
//        @Override
//        public String toString() {
//            return "PatientTest";
//        }
//    },
//    EnablePatientTestSession((byte) 26) {
//        @Override
//        public String toString() {
//            return "EnablePatientTestSession";
//        }
//    },
//    ReadyTest((byte) 27) {
//        @Override
//        public String toString() {
//            return "ReadyTest";
//        }
//    },
//    EnableTest((byte) 28) {
//        @Override
//        public String toString() {
//            return "EnableTest";
//        }
//    },
//
//    DryCardCheck((byte) 29) {
//        @Override
//        public String toString() {
//            return "DryCardCheck";
//        }
//    },
//    PerformDryCardCheckStart((byte) 30) {
//        @Override
//        public String toString() {
//            return "PerformDryCardCheckStart";
//        }
//    },
//    PerformDryCardCheck((byte) 31) {
//        @Override
//        public String toString() {
//            return "PerformDryCardCheck";
//        }
//    },
//    PerformDryCardCheckEnd((byte) 32) {
//        @Override
//        public String toString() {
//            return "PerformDryCardCheckEnd";
//        }
//    },
//
//    AcceptCard((byte) 33) {
//        @Override
//        public String toString() {
//            return "AcceptCard";
//        }
//    },
//    GetDeviceStatusAfterTestStart((byte) 34) {
//        @Override
//        public String toString() {
//            return "GetDeviceStatusAfterTestStart";
//        }
//    },
//
//    DoTest((byte) 35) {
//        @Override
//        public String toString() {
//            return "DoTest";
//        }
//    },
//    PerformTestStart((byte) 36) {
//        @Override
//        public String toString() {
//            return "PerformTestStart";
//        }
//    },
//    PerformFluidCalibration((byte) 37) {
//        @Override
//        public String toString() {
//            return "PerformFluidCalibration";
//        }
//    },
//    PerformSampleIntroduction((byte) 38) {
//        @Override
//        public String toString() {
//            return "PerformSampleIntroduction";
//        }
//    },
//    PerformSampleProcessing((byte) 39) {
//        @Override
//        public String toString() {
//            return "PerformSampleProcessing";
//        }
//    },
//    PerformTestEnd((byte) 40) {
//        @Override
//        public String toString() {
//            return "PerformTestEnd";
//        }
//    },
//
//    CancelTest((byte) 42) {
//        @Override
//        public String toString() {
//            return "CancelTest";
//        }
//    },
//    UsedCardInReader((byte) 43) {
//        @Override
//        public String toString() {
//            return "UsedCardInReader";
//        }
//    },
//
//    EndTestBeforeTestBegun((byte) 44) {
//        @Override
//        public String toString() {
//            return "EndTestBeforeTestBegun";
//        }
//    },
//    TerminateTest((byte) 45) {
//        @Override
//        public String toString() {
//            return "TerminateTest";
//        }
//    },
//    Done((byte) 46) {
//        @Override
//        public String toString() {
//            return "Done";
//        }
//    },
//
//    EQCFailed((byte) 80) {
//        @Override
//        public String toString() {
//            return "EQCFailed";
//        }
//    },
//    CommunicationFailed((byte) 81) {
//        @Override
//        public String toString() {
//            return "CommunicationFailed";
//        }
//    },
//    ReaderNoCompatible((byte) 82) {
//        @Override
//        public String toString() {
//            return "ReaderNoCompatible";
//        }
//    };
//    public final byte value;
//
//    TestStatusEnum(byte value) {
//        this.value = Byte.valueOf(value);
//    }
//
//    public static TestStatusEnum convert(byte value) {
//        return TestStatusEnum.values()[value];
//    }
//}
