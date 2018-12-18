package com.epocal.reader.enumtype;

/**
 * Created by dning on 7/18/2017.
 */

public enum TestActionResponse {
    NotDefined ((byte)0),
    BGETest ((byte)1),
    DevicePeripheralMonitor ((byte)2);

    public final byte value;
    TestActionResponse(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static TestActionResponse convert(byte value) {return TestActionResponse.values()[value];}
}
