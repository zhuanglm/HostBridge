package com.epocal.reader.enumtype;

/**
 * Created by dning on 7/12/2017.
 */

public enum TestNotificationResponse {
    NotDefined ((byte)0),
    TimingUpdate ((byte)1),
    SDMEvent ((byte)2);

    public final byte value;
    TestNotificationResponse(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static TestNotificationResponse convert(byte value) {return TestNotificationResponse.values()[value];}
}
