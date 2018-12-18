package com.epocal.reader.enumtype.legacy;

/**
 * Created by rzhuang on Aug 3 2018.
 */
public enum DAMStages
{
    IDLE ((byte)0),
    INITIALIZATION ((byte)1),
    READY ((byte)2),
    SELF_CHECK ((byte)3),
    DRY_CARD_CHECK ((byte)4),
    CAL_FLUID_DETECTION ((byte)5),
    FLUIDICS_CALIBRATION ((byte)6),
    BD_FLUID ((byte)7),
    BD_AIR ((byte)8),
    SAMPLE_COLLECTION ((byte)9),
    STOP_PROCESSING ((byte)10),
    STOP_COMPLETING ((byte)11),
    HEATER_TEMP_COLLECTING ((byte)12),
    DRY_CARD_CHECK_PREPARE ((byte)13);

    public final byte value;
    DAMStages(byte value) {
        this.value = value;
    }

    public static DAMStages convert(byte val) {
        return DAMStages.values()[val];
    }
}
