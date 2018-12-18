package com.epocal.reader.enumtype.legacy;

/**
 * Created by rzhuang on Sep 18 2018.
 */

public enum CustomConfigSubcode
{
    eCustomConfigNA ((byte)0x0),
    eCustomConfigGet ((byte)0x01),
    eCustomConfigSet ((byte)0x02);

    public final byte value;
    CustomConfigSubcode(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static CustomConfigSubcode convert(byte value) {return CustomConfigSubcode.values()[value];}
}