package com.epocal.reader.enumtype.legacy;

/**
 * Created by rzhuang on Aug 8 2018.
 */

public enum ConfigBlockFlag
{
    General ((byte)0x01),
    Bluetooth ((byte)0x02),
    BluetoothConfig ((byte)0x04),
    MaintenanceTestRecordNumber ((byte)0x08);

    public final byte value;
    ConfigBlockFlag(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static ConfigBlockFlag convert(byte value) {return ConfigBlockFlag.values()[value];}
}