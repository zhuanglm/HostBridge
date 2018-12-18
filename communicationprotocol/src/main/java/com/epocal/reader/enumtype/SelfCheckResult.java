package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/22/2017.
 */

public enum SelfCheckResult {
    NotPerformed ((byte)0),
    FailedNotCompleted ((byte)1),
    FailedCompleted ((byte)2),
    Passed ((byte)3);

    public final byte value;
    SelfCheckResult(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static SelfCheckResult convert(byte value) {return SelfCheckResult.values()[value];}
}
