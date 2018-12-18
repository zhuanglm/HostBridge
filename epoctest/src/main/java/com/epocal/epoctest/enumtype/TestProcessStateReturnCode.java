package com.epocal.epoctest.enumtype;

/**
 * Created by dning on 9/14/2017.
 */

public enum TestProcessStateReturnCode  {
    Unknown ((byte)0),
    Continue ((byte)1),
    RemoveCard ((byte)2),
    StopTest ((byte)3);

    public final byte value;
    TestProcessStateReturnCode(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static TestProcessStateReturnCode convert(byte value) {return TestProcessStateReturnCode.values()[value];}
}