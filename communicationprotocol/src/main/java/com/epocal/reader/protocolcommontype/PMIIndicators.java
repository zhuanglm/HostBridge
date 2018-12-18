package com.epocal.reader.protocolcommontype;

/**
 * Created by dning on 7/26/2017.
 */

public enum PMIIndicators {
    BGBottomHeaterT1 ((byte)0),
    BGBottomHeaterT2 ((byte)1),
    BGTopHeaterT1 ((byte)2),
    BGTopHeaterT2 ((byte)3),
    BoardMajorId ((byte)4),
    BoardMinorId ((byte)5),
    OpticalSpeed ((byte)6),
    OpticalSample ((byte)7),
    OpticalOverflow ((byte)8),
    VBattery ((byte)9),
    Pressure1 ((byte)10),
    Pressure2 ((byte)11),
    TesterId ((byte)12),
    CooxTopHeater1 ((byte)13),
    CooxTopHeater2 ((byte)14),
    CooxBottomHeater1 ((byte)15),
    CooxBottomHeater2 ((byte)16),
    AmbientT1 ((byte)17),
    AmbientT2 ((byte)18),
    BGBottomHeaterPID ((byte)19),
    BGTopHeaterPID ((byte)20),
    CooxTopHeaterPID ((byte)21),
    PumpControl ((byte)22),
    ValveControl ((byte)23),
    OpticalSensorControl ((byte)24),
    PumpPressure ((byte)25),
    OpticalOverflowCoox ((byte)26),
    SibTesterT1 ((byte)27),
    SibTesterT2 ((byte)28),
    PmiLabel1 ((byte)100),
    PmiLabel2 ((byte)101),
    PmiLabel3 ((byte)102),
    PmiLabel4 ((byte)103),
    PmiLabel5 ((byte)104),
    PmiLabel6 ((byte)105),
    PmiLabel7 ((byte)106),
    PmiLabel8 ((byte)107),
    PmiLabel9 ((byte)108),
    PmiLabel10 ((byte)109);

    public final byte value;
    PMIIndicators(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static PMIIndicators convert(byte value) {return PMIIndicators.values()[value];}
}
