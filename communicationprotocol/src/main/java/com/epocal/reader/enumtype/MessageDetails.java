package com.epocal.reader.enumtype;

/**
 * Created by dning on 7/5/2017.
 */

public enum MessageDetails {
    NotDefined ((byte)0);

    public final byte value;
    MessageDetails(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static MessageDetails convert(byte value) {
        return MessageDetails.values()[value];
    }
}
