package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/19/2017.
 */

public enum TestStatus {
    NotDefined ((byte)0),
    ReaderForTest ((byte)1),
    TestInProgress ((byte)2);

    public final byte value;
    TestStatus(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static TestStatus convert(byte value) {return TestStatus.values()[value];}
}
