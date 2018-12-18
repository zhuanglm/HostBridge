package com.epocal.reader.enumtype;

/**
 * Created by dning on 8/15/2017.
 */

public enum MotorID {
    SensorContacts ((byte)0),
    Fluidics ((byte)1);

    public final byte value;
    MotorID(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static MotorID convert(byte value) {return MotorID.values()[value];}
}
