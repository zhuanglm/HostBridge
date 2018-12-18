package com.epocal.reader.enumtype;

/**
 * Created by dning on 8/15/2017.
 */

public enum SensorContactsMotorState {
    Unknown ((byte)0),
    GoingHome ((byte)1),
    NearHome ((byte)2),
    Home ((byte)3),
    SeekingContacts ((byte)4),
    EndPoint ((byte)5);

    public final byte value;
    SensorContactsMotorState(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static SensorContactsMotorState convert(byte value) {return SensorContactsMotorState.values()[value];}
}
