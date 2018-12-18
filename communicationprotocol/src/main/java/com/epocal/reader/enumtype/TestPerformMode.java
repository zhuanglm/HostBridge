package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/29/2017.
 */

public enum TestPerformMode {
    NotDefined ((byte)0),
    Auto ((byte)1),
    Manual ((byte)2);

    public final byte value;
    TestPerformMode(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static TestPerformMode convert(byte value) {return TestPerformMode.values()[value];}
}