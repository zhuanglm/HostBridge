package com.epocal.epoctest.testconfiguration;

/**
 * Created by dning on 10/6/2017.
 */

public enum WindowPhase {
    Unknown((byte) 0),
    Calibration((byte) 1),
    Sampling((byte) 2),
    Post((byte) 3);

    public final byte value;

    WindowPhase(byte value) {
        this.value = Byte.valueOf(value);
    }

    public static WindowPhase convert(byte value) {
        return WindowPhase.values()[value];
    }
}
