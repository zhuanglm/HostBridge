package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/26/2017.
 */

public enum BDMode
{
    AllAlwaysOff ((byte)0),
    AllAlwaysOn ((byte)1),
    BDChannelSamplingOnlyOn ((byte)2),
    BDStageAllOn ((byte)3),
    BDStageBDChannelOnOnly ((byte)4);

    public final byte value;
    BDMode(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static BDMode convert(byte value) {return BDMode.values()[value];}
}
