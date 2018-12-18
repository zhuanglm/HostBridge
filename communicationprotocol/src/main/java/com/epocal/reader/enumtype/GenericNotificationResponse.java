package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/29/2017.
 */

public enum GenericNotificationResponse {
    NotDefined ((byte)0),
    KeepAlive ((byte)1),
    Card ((byte)2),
    Sample ((byte)3),
    FluidicsControl ((byte)4),
    CommandStarted ((byte)5),
    CommandFinished ((byte)6);

    public final byte value;
    GenericNotificationResponse(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static GenericNotificationResponse convert(byte value) {return GenericNotificationResponse.values()[value];}
}
