package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/26/2017.
 */

public enum GenericSessionRequest {
    NotDefined ((byte)0),
    Page ((byte)1),
    Status  ((byte)2),
    ReaderSettings ((byte)3),
    ReaderDiagnostic ((byte)4),
    Eqc ((byte)5),
    Maintenance ((byte)6),
    PatientTest ((byte)7),
    QATest ((byte)8),
    Production ((byte)9);

    public final byte value;
    GenericSessionRequest(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static GenericSessionRequest convert(byte value) {return GenericSessionRequest.values()[value];}
}
