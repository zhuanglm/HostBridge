package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/7/2017.
 */

public enum ReaderType {
    NotDefined ((byte)0),
    BGEM ((byte)1),
    BGEMAndCoox ((byte)2);

    public final byte value;
    ReaderType(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static ReaderType convert(byte value) {return ReaderType.values()[value];}
}
