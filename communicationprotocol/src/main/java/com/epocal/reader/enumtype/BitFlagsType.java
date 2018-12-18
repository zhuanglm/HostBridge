package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/26/2017.
 */

public enum BitFlagsType
{
    AppendStatusRegister (1 << 0),
    Sinc3 (1 << 1),
    EnableParity (1 << 2),
    ClockDivision (1 << 3),
    SingleCycle (1 << 4),
    Reject60hz (1 << 5),
    ChopEnable (1 << 6),
    ReferenceSelect (1 << 7),
    Burn (1 << 8),
    ReferenceDetect (1 << 9),
    BufferEnable (1 << 10),
    Unipolar (1 << 11);

    public final int value;
    BitFlagsType(int value)
    {
        this.value = Integer.valueOf(value);
    }
    public static BitFlagsType convert(int value) {
        return BitFlagsType.values()[value];
    }

    public boolean hasFlag(BitFlagsType flag)
    {
        if((value & flag.value) != 0)
        {
            return true;
        }
        return false;
    }
}
