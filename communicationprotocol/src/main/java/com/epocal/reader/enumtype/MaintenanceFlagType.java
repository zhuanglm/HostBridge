package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/22/2017.
 */

public enum MaintenanceFlagType {
    Unlocked ((byte)0),
    LockedByHost ((byte)1),
    LockedByReader ((byte)2),
    LockedByHostAndReader ((byte)3);

    public final byte value;
    MaintenanceFlagType(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static MaintenanceFlagType convert(byte value) {return MaintenanceFlagType.values()[value];}
}
