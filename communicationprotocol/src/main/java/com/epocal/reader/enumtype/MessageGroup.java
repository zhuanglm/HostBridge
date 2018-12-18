package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/6/2017.
 */

public enum MessageGroup {
    NotDefined ((byte)0),
    Interface ((byte)1),
    Session ((byte)2),
    Command ((byte)3),
    Configuration ((byte)4),
    Action ((byte)5),
    Notification ((byte)6),
    Ack ((byte)7),
    Error ((byte)8),
    Debug ((byte)9),
    Upgrade ((byte)10);

    public final byte value;
    MessageGroup(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static MessageGroup convert(byte value) {
        return MessageGroup.values()[value];
    }
}
