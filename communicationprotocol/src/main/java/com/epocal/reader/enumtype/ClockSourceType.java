package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/26/2017.
 */

public enum ClockSourceType {
    ExternalCrystal ((byte)0),
    ExternalClock ((byte)1),
    InternalMhzTristatedClk ((byte)2),
    InternalMhzNotTrisatedClk ((byte)3);

    public final byte value;
    ClockSourceType(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static ClockSourceType convert(byte value) {
        return ClockSourceType.values()[value];
    }
}
