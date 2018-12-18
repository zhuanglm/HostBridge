package com.epocal.epoctest.enumtype;

/**
 * Created by dning on 10/17/2017.
 */

public enum ResultType {
    Measured ((byte)0),
    Calculated ((byte)1),
    Corrected ((byte)2);

    public final byte value;
    ResultType(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static ResultType convert(byte value) {return ResultType.values()[value];}
}
