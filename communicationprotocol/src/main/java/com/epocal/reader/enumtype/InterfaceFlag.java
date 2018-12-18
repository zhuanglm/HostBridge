package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/7/2017.
 */

public enum InterfaceFlag
{
    None (0),

    Bge (1 << 0),
    Coox (1 << 1),          // UNUSED BY BRIDGE (NextGen only)
    Barcode (1 << 2),
    BgeLegacy (1 << 3),

    Last (1 << 4),
    All ((1 << 5) - 3);

    public final int value;
    InterfaceFlag(int value)
    {
        this.value = Integer.valueOf(value);
    }
    public static InterfaceFlag convert(int value) {
        return InterfaceFlag.values()[value];
    }
}
