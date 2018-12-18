package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/7/2017.
 */

public enum ReaderDistribution {
    NotAssigned ((byte)0),
    Customer ((byte)1),
    SpecialPurpose ((byte)2),
    Investigation ((byte)3),
    InHouse ((byte)4),
    Production ((byte)5),
    RAndD ((byte)6),
    Mine ((byte)7);

    public final byte value;
    ReaderDistribution(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static ReaderDistribution convert(byte value) {return ReaderDistribution.values()[value];}
}
