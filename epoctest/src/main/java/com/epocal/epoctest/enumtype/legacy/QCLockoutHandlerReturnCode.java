package com.epocal.epoctest.enumtype.legacy;

public enum QCLockoutHandlerReturnCode
{
    None ((byte)0),
    Disconnected ((byte)1),
    RemoveCard ((byte)2),
    GraceMessage ((byte)3),
    ExpiringSoon ((byte)4),
    WarningExpired ((byte)5),
    NotSupported ((byte)6);

    public final byte value;
    QCLockoutHandlerReturnCode(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static QCLockoutHandlerReturnCode convert(byte value) {return QCLockoutHandlerReturnCode.values()[value];}
}
