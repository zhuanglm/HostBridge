package com.epocal.reader.enumtype;

/**
 * Created by dning on 8/15/2017.
 */

public enum MotorStatus {
    Standby ((byte)0),
    Forward ((byte)1),
    Reverse ((byte)2),
    SlowingDown ((byte)3);

    public final byte value;
    MotorStatus(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static MotorStatus convert(byte value) {return MotorStatus.values()[value];}
}