package com.epocal.reader.enumtype.legacy;

/**
 * Created by rzhuang on Aug 23 2018.
 */

public enum TransmissionMode {
    AllSampledData((byte)0x01),
    WindowData((byte)0x02),
    IncludeDryCardCheck((byte)0x04),
    Reserved((byte)0x08);

    public final byte value;

    TransmissionMode(byte value) {
        this.value = value;
    }

    public static TransmissionMode convert(byte val) {
        return TransmissionMode.values()[val];
    }
}
