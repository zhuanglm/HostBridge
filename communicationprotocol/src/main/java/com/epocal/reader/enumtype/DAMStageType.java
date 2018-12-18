package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/30/2017.
 */

public enum DAMStageType {
    Idle ((byte)0),
    Initialization ((byte)1),
    Ready ((byte)2),
    SelfCheck ((byte)3),
    DryCardCheck ((byte)4),
    // Test Stages
    CalFluidDetection ((byte)5),
    FluidicsCalibration ((byte)6),
    BubbleDetectFluid ((byte)7),
    BubbleDetectAir ((byte)8),
    SampleCollection ((byte)9),

    StopProcessing ((byte)10),
    StopCompleting ((byte)11),
    SelfCheckPrepare ((byte)12),
    DryCardCheckPrepare ((byte)13),
    CIDCheck ((byte)14);

    public final byte value;
    DAMStageType(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static DAMStageType convert(byte value) {return DAMStageType.values()[value];}
}
