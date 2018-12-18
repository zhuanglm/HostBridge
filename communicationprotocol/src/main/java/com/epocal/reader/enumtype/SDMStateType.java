package com.epocal.reader.enumtype;

/**
 * Created by dning on 8/15/2017.
 */

public enum SDMStateType {
    NotDefined ((byte)0),
    Idle ((byte)1),
    BGEVentingTankForBGE ((byte)2),
    ChargingTankForBGE ((byte)3),
    BGECalFluidDelivery ((byte)4),
    BGESampleDeliveryPrepare ((byte)5),
    BGESampleDeliveryInProgress ((byte)6),
    BGESampleDeliveryFinishing ((byte)7),
    VentingTankForCoox ((byte)8),
    ChargingTankForCoox ((byte)9),
    CooxSampleDeliveryPrepare ((byte)10),
    CooxSampleDeliveryInProgress ((byte)11),
    CapacitorCycle ((byte)12),
    VentCycle ((byte)13),
    CooxSampleParked ((byte)14),
    CooxSampleDeliveryFinishing ((byte)15),
    TuningTankCharge ((byte)16),
    LastShortTankCharge ((byte)17),
    FinalizingTankCharge ((byte)18);



    public final byte value;
    SDMStateType(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static SDMStateType convert(byte value) {return SDMStateType.values()[value];}
}
