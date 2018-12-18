package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/13/2017.
 */

public enum DestinationDevice {
    NotDefined ((byte)0),
    AT ((byte)1),
    EpocHost ((byte)2),
    EpocReader ((byte)3),
    CP ((byte)4),
    VP ((byte)5),
    ProductionTools ((byte)6),
    Barcode ((byte)7);

    public final byte value;
    DestinationDevice(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static DestinationDevice convert(byte value) {return DestinationDevice.values()[value];}
}