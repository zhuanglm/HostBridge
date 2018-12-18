package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/7/2017.
 */

public enum GenericCommandResponse {
    NotDefined ((byte)0),

    HostIdInfo ((byte)1),
    DeviceIdResponse ((byte)2),
    DeviceAuxInfoResponse ((byte)3),
    DeviceStatusResponse ((byte)4),
    DevicePage ((byte)5),
    DeviceEnable ((byte)6),
    StatisticsResponse ((byte)7),
    CardResponse ((byte)8),
    Results ((byte)9),
    BarcodeModuleResponse ((byte)10),
    CooxModuleResponse ((byte)11),      // UNUSED BY BRIDGE (NextGen only)
    PerformEqc ((byte)12),
    PerformDryCard ((byte)13),
    PerformTest ((byte)14);

    public final byte value;
    GenericCommandResponse(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static GenericCommandResponse convert(byte value) {return GenericCommandResponse.values()[value];}
}
