package com.epocal.reader.enumtype;

/**
 * Created by dning on 7/5/2017.
 */

public enum GenericAckRequest {
    NotDefined ((byte)0),
    Ack ((byte)1);

    public final byte value;
    GenericAckRequest(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static GenericAckRequest convert(byte value) {return GenericAckRequest.values()[value];}

}
