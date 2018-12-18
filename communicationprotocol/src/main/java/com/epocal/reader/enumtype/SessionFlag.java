package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/7/2017.
 */

public enum SessionFlag {
    None (0),

    Page (1 << 0),
    Status (1 << 1),
    ReaderSettings (1 << 2),
    ReaderDiagnostic (1 << 3),
    Eqc (1 << 4),
    Maintenance (1 << 5),
    PatientTest (1 << 6),
    QATest (1 << 7),
    Production (1 << 8),

    Last (1 << 9),
    All  ((1 << 10) - 3);

    public final int value;
    SessionFlag(int value)
    {
        this.value = Integer.valueOf(value);
    }
    public static SessionFlag convert(int value) {
        return SessionFlag.values()[value];
    }
}
