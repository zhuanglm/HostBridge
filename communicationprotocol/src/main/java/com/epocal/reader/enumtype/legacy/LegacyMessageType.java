package com.epocal.reader.enumtype.legacy;

/**
 * Created by rzhuang on July/24/2018.
 */

public enum LegacyMessageType {

    // reader to host messages
    DeviceIdResponse ((byte)0x00),
    ReaderStatusResponse ((byte)0x01),
    CardInserted ((byte)0x02),
    CardRemoved ((byte)0x03),
    HandleStatus ((byte)0x04),
    TestStatusResponse ((byte)0x05),
    DataPacket ((byte)0x06),
    TestStopped ((byte)0x07),
    Ack ((byte)0x08),
    Error ((byte)0x09),
    LinkStatus ((byte)0x0A),
    ConfigurationResponse1_1 ((byte)0x0B),
    ConfigurationResponse1_2 ((byte)0x0C),
    ConfigurationResponse1_3 ((byte)0x0D),
    ConfigurationResponse1_4 ((byte)0x0E),
    CustomConfigurationResponse ((byte)0x0F),
    ConfigurationResponse3 ((byte)0x10),
    Alive ((byte)17 /*0x11*/),
    MessageReceived ((byte)0x12),
    HCMConfiguration ((byte)0x13),
    ArmDebugInfo ((byte)0x14),
    ArmFirmwareAck ((byte)0x15),
    StatusUpdate ((byte)0x16),
    CardDetected ((byte)0x17),
    UpgradeBegin ((byte)0x19),
    StatisticsResponse ((byte)0x1C),
    UpgradeProgress ((byte)0x20),
    UpgradeEnd ((byte)0x21),


    // host to reader messages
    GetDeviceId ((byte)0x80),
    GetReaderStatus ((byte)0x81),
    GetTestStatus ((byte)0x82),
    GetStatistics ((byte)0x83),
    SomethingElse ((byte)0x84),
    ConfigurationMessage1_1 ((byte)0x85),
    ConfigurationMessage1_2 ((byte)0x86),
    ConfigurationMessage1_3 ((byte)0x87),
    ConfigurationMessage1_4 ((byte)0x88),
    ConfigurationMessage2 ((byte)0x89),
    ConfigurationMessage3 ((byte)0x8a),
    InvalidCard ((byte)0x8b),
    EnableTest ((byte)0x8c),
    StopTest ((byte)0x8d),

    DestroyTestResults ((byte)0x90),
    EnableReader ((byte)0x96),

    GetHCM ((byte)0x9B),

    None ((byte)0xFF);

    public final byte value;
    LegacyMessageType(byte value) {
        this.value = value;
    }
    public static LegacyMessageType convert(byte value) {return LegacyMessageType.values()[value];}

//    ResumeTransmission = 0x8e,
//    GetTestResults = 0x8f,

//    FirmwareUpgradePrepare = 0x91,
//    FirmwareUpgradePacket = 0x92,
//    ReaderPage = 0x93,
//    GetConfiguration1_1 = 0x94,
//    GetConfiguration1_2 = 0x95,

//    GetConfiguration1_4 = 0x97,
//    GetConfiguration2 = 0x98,
//    GetConfiguration3 = 0x99,
//    Thingy = 0x9A,

//    ArmFirmwarePrepare = 0x9C,
//
//    // host to pc messages
//    ResultsFile = 0xD0,
//    ConnectionSetup = 0xD1,
//    ConnectionClose = 0xD2,
//    TestSetup = 0xD3,
//    TestComplete = 0xD4,
//    PCDataPacket = 0xD5,
//    RawFile = 0xD6,
//
//    //host to host messages
//    //HostUpgrade
//    FileRequest = 0xE0,
//    FileRequestAccept = 0xE1,
//    RequestRefuse = 0xE2,
//    DataReadyToReceive = 0xE3,
//    RawByteData = 0xE4,
//    FileReceivedSucceed = 0xE5,
//    TextMessageRequest = 0xE6,
//    RequestAccept = 0xE7,
//    TextDataMessage = 0xE8,
//
//    ModemSignalChanged = 0xF3,
//    PortEventMessage = 0xF4,
//    Connection = 0xf5,
//    Disconnection = 0xf6,
//    CorruptedMessage = 0xf7,
//    DataReceived = 0xf8,
//    Inactivity = 0xf9,
//    DataSkipped = 0xfa,
//    NextGen = 0xFE,

}