package com.epocal.reader.enumtype;

/**
 * Created by dning on 8/15/2017.
 */

public enum GenericDebugResponse {

    NotDefined ((byte)0),
    DebugCommInterface ((byte)1),
    DebugFirmware ((byte)2),
    DebugDevice ((byte)3),
    Information ((byte)4);

    public final byte value;
    GenericDebugResponse(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static GenericDebugResponse convert(byte value) {return GenericDebugResponse.values()[value];}
}
