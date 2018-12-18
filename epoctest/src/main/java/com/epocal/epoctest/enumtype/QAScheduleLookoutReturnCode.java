package com.epocal.epoctest.enumtype;

/**
 * Created by dning on 9/14/2017.
 */

public enum QAScheduleLookoutReturnCode  {
    Unknown ((byte)0),
    QALookout ((byte)1),
    RemoveCard ((byte)2),
    GraceWarning ((byte)3),
    ExpiringSoon ((byte)4),
    ExpiredWarning ((byte)5),
    NotSupportedQAFeature ((byte)6),
    QAPassed ((byte)7);

    public final byte value;
    QAScheduleLookoutReturnCode(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static QAScheduleLookoutReturnCode convert(byte value) {return QAScheduleLookoutReturnCode.values()[value];}
}