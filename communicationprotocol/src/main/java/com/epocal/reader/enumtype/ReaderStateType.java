package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/19/2017.
 */

public enum ReaderStateType {
    NotDefined ((byte)0),
    Idle ((byte)1),
    Standby ((byte)2),
    Ready ((byte)3),
    CardDetected ((byte)4),
    CardApproved ((byte)5),
    Calibration ((byte)6),
    BubbleDetect ((byte)7),
    SampleCollection ((byte)8),
    CooxAcquisition ((byte)9),          // UNUSED BY BRIDGE (NextGen only)
    Done ((byte)10),
    FirmwareUpgrade ((byte)11),
    OldTestUpload ((byte)12),
    SelfCheck ((byte)13),
    DryCardCheck ((byte)14),
    DoneError ((byte)15),
    Connected ((byte)16),
    Paging ((byte)17),
    SelfCheckBeforeTest ((byte)18),
    FluidDetection ((byte)19),
    CPUpgrade ((byte)20),
    VPUpgrade ((byte)21),
    CTUpgrade ((byte)22),
    StatusCheck ((byte)23),
    BootupCheck ((byte)24);

    public final byte value;
    ReaderStateType(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static ReaderStateType convert(byte value) {return ReaderStateType.values()[value];}
}
