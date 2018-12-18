package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/22/2017.
 */

public enum AccessType {
    Enabled ((byte)0),
    Disabled ((byte)1);

    public final byte value;
    AccessType(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static AccessType convert(byte value) {return AccessType.values()[value];}
}
