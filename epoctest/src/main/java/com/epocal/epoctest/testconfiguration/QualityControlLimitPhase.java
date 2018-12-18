package com.epocal.epoctest.testconfiguration;

/**
 * Created by dning on 10/6/2017.
 */

public enum QualityControlLimitPhase {
    Unknown((byte) 0),
    CalibrationQC((byte) 1),
    SamplingQC((byte) 2),
    PostQC((byte) 3),
    DeltaDriftQC((byte) 4),
    ReaderQC((byte) 5);

    public final byte value;

    QualityControlLimitPhase(byte value) {
        this.value = Byte.valueOf(value);
    }

    public static QualityControlLimitPhase convert(byte value) {
        return QualityControlLimitPhase.values()[value];
    }
}
