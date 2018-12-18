package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/22/2017.
 */

public enum AccessTypeRsp {
    Enabled ((byte)0),
    Disabled ((byte)1),
    OperationFailed ((byte)2);

    public final byte value;
    AccessTypeRsp(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static AccessTypeRsp convert(byte value) {return AccessTypeRsp.values()[value];}
}
