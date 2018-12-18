package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/6/2017.
 */

public enum InterfaceType {
    NotDefined ((byte)0),
    NextGen ((byte)1),
    BGE ((byte)2),
    BARCODE ((byte)3),
    Legacy ((byte)4);

    public final byte value;
    InterfaceType(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static InterfaceType convert(byte value) {return InterfaceType.values()[value];}
}
