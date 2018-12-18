package com.epocal.epoctest;

/**
 * Created by bmate on 7/18/2017.
 */

public enum TestEventInfoType {
    UNKNOWN(0),
    ERROR_INFO (1),
    STATUS_INFO (2);

    public final int value;
    TestEventInfoType(int value)
    {
        this.value = Integer.valueOf(value);
    }
    public static TestEventInfoType convert(int value) {return TestEventInfoType.values()[value];}

}
