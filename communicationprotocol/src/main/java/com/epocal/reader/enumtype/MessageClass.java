package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/6/2017.
 */

public enum MessageClass {
    NotDefined ((byte)0),
    Generic ((byte)1),
    Test ((byte)2),
    Production ((byte)3);

    public final byte value;
    MessageClass(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static MessageClass convert(byte value) {return MessageClass.values()[value];}
}
