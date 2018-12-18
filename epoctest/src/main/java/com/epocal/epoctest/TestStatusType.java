package com.epocal.epoctest;

/**
 * Created by bmate on 7/19/2017.
 * received from testcontroller
 */

public enum TestStatusType {
    UNKNOWN(0),
    NEWTESTSTARTING(1),
    CONNECTING(2),
    CONFIGURATION(3),
    READYTOTEST(4),
    FLUIDICSCALIBRATION(5),
    SAMPLEINTRODUCTION(6),
    SAMPLEPROCESSING(7),
    TESTCALCULATING(8),
    TESTCALCULATED(9),
    MISSINGDATAREQUIRED(10),
    NOTESTRUNNING(11),
    TESTRECALCULATED(12),
    CALCULATEDANDUNEDITABLE(13),
    READINGCARD(14),
    QCLOCKOUT(15),
    QCEXPIRINGWARNING(16),
    QCEXPIREDWARNING(17),
    QCEXPIREDWARNINGCARDINREADER(18),
    QCGRACEPERIODWARNING(19),
    CONNECTED(20),
    DISCONNECTED(21),
    DEVICEINFO(22),
    CARDINSERTED(23),
    CARDREMOVED(24);

    public final int value;

    TestStatusType(int value) {
        this.value = Integer.valueOf(value);
    }

    public static TestStatusType convert(int value) {
        return TestStatusType.values()[value];
    }

}
