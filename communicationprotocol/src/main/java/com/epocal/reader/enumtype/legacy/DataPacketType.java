package com.epocal.reader.enumtype.legacy;

/**
 * Created by rzhuang on Aug 3 2018.
 */

public enum DataPacketType {
    SelfCheck((byte) 0),
    DryCardCheck((byte) 1),
    TestData((byte) 2);

    public final byte value;

    DataPacketType(byte value) {
        this.value = value;
    }

    public static DataPacketType convert(byte val) {
        return DataPacketType.values()[val];
    }
}