package com.epocal.reader.enumtype.legacy;

/**
 * Created by rzhuang on Aug 9 2018.
 */

public enum DryCardTransmissionMode {
    SupressTransmission ((byte)0),
    TransmitToHost ((byte)1);

    public final byte value;

    DryCardTransmissionMode(byte value) {
        this.value = value;
    }

    public static DryCardTransmissionMode convert(byte val) {
        return DryCardTransmissionMode.values()[val];
    }
}
