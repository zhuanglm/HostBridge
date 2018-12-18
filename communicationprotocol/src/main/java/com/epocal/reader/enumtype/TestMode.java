package com.epocal.reader.enumtype;

/**
 * Created by dning on 7/17/2017.
 */

public enum TestMode {
    Unknown ((byte)0),
    Patient ((byte)1),
    QA ((byte)2);

    public byte value;
    TestMode(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static TestMode convert(byte value) {return TestMode.values()[value];}
}
