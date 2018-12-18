package com.epocal.testhistoryfeature.data.type;

public enum THDateRange {
    NORANGE(0),
    TODAY(1),
    YESTERDAY(2),
    LASTWEEK(3),
    OLDER(4);

    public final int value;
    THDateRange(int value) {
        this.value = value;
    }

    public static THDateRange fromInt(int intValue) {
        for (THDateRange dateRange : THDateRange.values()) {
            if (dateRange.value == intValue) return dateRange;
        }
        return null; // Not a valid intValue representing this enum
    }
}
