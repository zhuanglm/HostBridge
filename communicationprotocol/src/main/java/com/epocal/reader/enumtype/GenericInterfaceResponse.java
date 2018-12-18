package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/23/2017.
 */

public enum GenericInterfaceResponse {
    NotDefined ((byte)0),
    Bge ((byte)1),
    Coox ((byte)2),         // UNUSED BY BRIDGE (NextGen only)
    Barcode ((byte)3),
    BgeLegacy ((byte)4);

    public final byte value;
    GenericInterfaceResponse(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static GenericInterfaceResponse convert(byte value) {return GenericInterfaceResponse.values()[value];}
}
