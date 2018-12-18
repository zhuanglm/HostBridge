package com.epocal.reader.enumtype.legacy;

/**
 * Created by rzhuang on Sep 18 2018.
 */

public enum CustomConfigType
{
    ConfigTypeUndef ((byte)0x0),
    Descriptor ((byte)0x01),
    Block ((byte)0x02);

    public final byte value;
    CustomConfigType(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static CustomConfigType convert(byte value) {return CustomConfigType.values()[value];}
}