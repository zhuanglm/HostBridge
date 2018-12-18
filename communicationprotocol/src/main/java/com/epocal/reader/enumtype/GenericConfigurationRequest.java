package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/26/2017.
 */

public enum GenericConfigurationRequest
{
    NotDefined ((byte)0),
    General ((byte)1),
    SIBeQC ((byte)2),
    DryCardCheck ((byte)3);

    public final byte value;
    GenericConfigurationRequest(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static GenericConfigurationRequest convert(byte value) {return GenericConfigurationRequest.values()[value];}
}
