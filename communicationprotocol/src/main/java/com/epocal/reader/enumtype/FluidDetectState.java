package com.epocal.reader.enumtype;

/**
 * Created by dning on 7/11/2017.
 */

public enum FluidDetectState {
    NotDefined ((byte)0),
    Air ((byte)1),
    Fluid ((byte)2);

    public final byte value;
    FluidDetectState(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static FluidDetectState convert(byte value) {return FluidDetectState.values()[value];}
}
