package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/30/2017.
 */

public enum GenericActionResponse {
    NotDefined ((byte)0),
    eQC ((byte)1),
    ThermalCheck ((byte)2),
    DryCardCheck ((byte)3);

    public final byte value;
    GenericActionResponse(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static GenericActionResponse convert(byte value) {return GenericActionResponse.values()[value];}
}
