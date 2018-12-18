package com.epocal.reader.enumtype;

/**
 * Created by dning on 7/5/2017.
 */

public enum MessageCode{
    NotDefined ((byte)0),
    HostIDInfo ((byte)1),
    DeviceIDRequest ((byte)2),
    DeviceAuxilliaryInfoRequest ((byte)3),
    DeviceStatusRequest ((byte)4),
    DevicePage ((byte)5),
    DeviceEnable ((byte)6),
    SessionEnable ((byte)7),
    StatisticsRequest ((byte)8),
    CardRequest ((byte)9),
    Results ((byte)10),
    BarcodeModuleRequest ((byte)11),
    COOXModuleRequest ((byte)12);       // UNUSED BY BRIDGE (NextGen only)

    public final byte value;
    MessageCode(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static MessageCode convert(byte value) {return MessageCode.values()[value];}
}