package com.epocal.epoctest.testconfiguration;

/**
 * Created by dning on 8/30/2017.
 */

public enum OperatorRoles {
    Administrator((byte) 0),
    Tester((byte) 1),
    Analyzer((byte) 2),
    TestAndAnalysis((byte) 3),
    Manager((byte) 4),
    Developer((byte) 5);

    public final byte value;

    OperatorRoles(byte value) {
        this.value = Byte.valueOf(value);
    }

    public static OperatorRoles convert(byte value) {
        return OperatorRoles.values()[value];
    }
}
