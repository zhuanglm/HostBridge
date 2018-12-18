package com.epocal.reader.enumtype;

/**
 * Created by dning on 8/15/2017.
 */

public enum GenericErrorResponse {
    NotDefined ((byte)0xFF),
    CommunicationInterface ((byte)1),
    Firmware ((byte)2),
    Device ((byte)3);

    public final byte value;
    GenericErrorResponse(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static GenericErrorResponse convert(byte value) {return GenericErrorResponse.values()[value];}
}
