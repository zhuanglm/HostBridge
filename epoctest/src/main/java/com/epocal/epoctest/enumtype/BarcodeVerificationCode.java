package com.epocal.epoctest.enumtype;

/**
 * Created by dning on 9/12/2017.
 */

public enum BarcodeVerificationCode {
    Unknown ((byte)0),
    Success ((byte)1),
    Expired ((byte)2),
    Invalid ((byte)3),
    ExpiryInTheFuture ((byte)4),
    Failure ((byte)5);

    public final byte value;
    BarcodeVerificationCode(byte value)
    {
        this.value = value;
    }
    public static BarcodeVerificationCode convert(byte value) {return BarcodeVerificationCode.values()[value];}
}