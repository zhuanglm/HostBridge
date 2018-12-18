package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/22/2017.
 */

public enum CardStatus {
    NoCard ((byte)0),
    CardPresent ((byte)1);

    public final byte value;
    CardStatus(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static CardStatus convert(byte value) {return CardStatus.values()[value];}
}
