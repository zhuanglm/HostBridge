package com.epocal.reader.enumtype;

/**
 * Created by dning on 7/5/2017.
 */

public enum MessageType {
    Undefined ((byte)0xFF);

    public final byte value;
    MessageType(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static MessageType convert(byte value) {
        return MessageType.values()[value];
    }
}
