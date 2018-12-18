package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/26/2017.
 */

public enum GainType {
    Gain1 ((byte)0),
    Gain8 ((byte)3),
    Gain16 ((byte)4),
    Gain32 ((byte)5),
    Gain64 ((byte)6),
    Gain128 ((byte)7);

    public final byte value;
    GainType(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static GainType convert(byte value) {
        return GainType.values()[value];
    }
}
