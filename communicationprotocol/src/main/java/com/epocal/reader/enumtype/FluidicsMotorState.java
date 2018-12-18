package com.epocal.reader.enumtype;

/**
 * Created by dning on 8/15/2017.
 */

public enum FluidicsMotorState {
    Unknown ((byte)0),
    GoingHome ((byte)1),
    NearHome ((byte)2),
    Home ((byte)3),
    BreakingValve ((byte)4),
    CalDelivery ((byte)5),
    CalFound ((byte)6),
    CalFoundStopping ((byte)7),
    EndCalDelivery ((byte)8),
    EndPoint ((byte)9);

    public final byte value;
    FluidicsMotorState(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static FluidicsMotorState convert(byte value) {return FluidicsMotorState.values()[value];}
}
