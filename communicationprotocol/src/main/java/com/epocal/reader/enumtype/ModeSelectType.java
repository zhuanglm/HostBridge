package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/26/2017.
 */

public enum ModeSelectType {
    ContinousConversion ((byte)0),
    SingleConversion ((byte)1),
    Idle ((byte)2),
    PowerDown ((byte)3),
    InternalZeroScaleCalibration ((byte)4),
    InternalFullScaleCalibration ((byte)5),
    SystemZeroScaleCalibration ((byte)6),
    SystemFullScaleCalibration ((byte)7);

    public final byte value;
    ModeSelectType(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static ModeSelectType convert(byte value) {
        return ModeSelectType.values()[value];
    }
}
