package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/13/2017.
 */

public enum ProtoGeneration
{
    Unknown ((int)0xFF),     // DCM NOTES: Reader interface doc claims this is 0x00
    Legacy  ((int)0xCA),
    NextGen ((int)1);

    public final int value;
    ProtoGeneration(int value)
    {
        this.value = Integer.valueOf(value);
    }
    public static ProtoGeneration convert(int value)
    {
        if(value == (int)1)
        {
            return NextGen;
        }
        else if(value == (int)0xCA)
        {
            return Legacy;
        }
        else if(value == (int)0xFF)
        {
            return Unknown;
        }
        return null;
    }

}
