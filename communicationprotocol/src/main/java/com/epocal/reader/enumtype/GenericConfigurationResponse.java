package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/27/2017.
 */

public enum GenericConfigurationResponse {
    NotDefined ((byte)0),
    General ((byte)1),
    SIBeQC ((byte)2),
    DryCardCheck ((byte)3);

    public final byte value;
    GenericConfigurationResponse(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static GenericConfigurationResponse convert(byte value) {return GenericConfigurationResponse.values()[value];}
}
