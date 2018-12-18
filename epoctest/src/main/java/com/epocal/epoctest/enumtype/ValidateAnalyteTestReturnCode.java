package com.epocal.epoctest.enumtype;

/**
 * Created by dning on 9/13/2017.
 */

public enum ValidateAnalyteTestReturnCode {
    Unknown ((byte)0),
    AnalytesAvailable ((byte)1),
    NoAnalytesAvailable ((byte)2),
    AnalyteNoCompatibleForReader((byte)3);

    public final byte value;
    ValidateAnalyteTestReturnCode(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static ValidateAnalyteTestReturnCode convert(byte value) {return ValidateAnalyteTestReturnCode.values()[value];}
}
