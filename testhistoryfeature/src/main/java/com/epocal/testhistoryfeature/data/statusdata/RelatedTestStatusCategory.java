package com.epocal.testhistoryfeature.data.statusdata;

public enum RelatedTestStatusCategory {
    QC(0),
    CV(1),
    EQC(2),
    TQC(3),
    DEVICE_INFO(4),
    REFERENCE_RANGE(5),
    CRITICAL_RANGE (6),
    REPORTABLE_RANGE(7);

    public final Integer value;
    RelatedTestStatusCategory(Integer value) {
        this.value = value;
    }
}
