package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/26/2017.
 */

public enum ConfigGroupFlag {
    General (0),
    BtDescriptor (1 << 0),
    MaintenanceTestRecord (1 << 1),

    Last (1 << 2),
    All ((1 << 3) - 3);

    public int value;
    ConfigGroupFlag(int value)
    {
        this.value = Integer.valueOf(value);
    }
    public static ConfigGroupFlag convert(int value) {
        return ConfigGroupFlag.values()[value];
    }

    public boolean hasFlag(ConfigGroupFlag flag)
    {
        if((value & flag.value) != 0)
        {
            return true;
        }
        return false;
    }
}
