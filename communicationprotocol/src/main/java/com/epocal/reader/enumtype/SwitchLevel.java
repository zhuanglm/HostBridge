package com.epocal.reader.enumtype;

/**
 * Created by dning on 7/4/2017.
 */

public enum SwitchLevel {
    Closed ((byte)0),
    Open ((byte)1);

    public final byte value;
    SwitchLevel(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static SwitchLevel convert(byte value) {return SwitchLevel.values()[value];}
}
