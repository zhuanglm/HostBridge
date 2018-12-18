package com.epocal.reader.enumtype;

/**
 * Created by dning on 7/18/2017.
 */

public enum TestAckRequest {
    NotDefined ((byte)0),
    Ack ((byte)1);

    public final byte value;
    TestAckRequest(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static TestAckRequest convert(byte value) {return TestAckRequest.values()[value];}
}
