package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/7/2017.
 */

public enum GenericCommandRequest {
    NotDefined ((byte)0),
    HostIdInfo ((byte)1),
    DeviceIdRequest ((byte)2),
    DeviceAuxInfoRequest ((byte)3),
    DeviceStatusRequest ((byte)4),
    DevicePage ((byte)5),
    DeviceEnable ((byte)6),
    StatisticsRequest ((byte)7),
    CardRequest ((byte)8),
    Results ((byte)9),
    BarcodeModuleRequest ((byte)10),
    CooxModuleRequest ((byte)11),
    PerformEqc ((byte)12),
    PerformDryCard ((byte)13),
    PerformTest ((byte)14);

    public final byte value;
    GenericCommandRequest(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static GenericCommandRequest convert(byte value) {return GenericCommandRequest.values()[value];}

}
