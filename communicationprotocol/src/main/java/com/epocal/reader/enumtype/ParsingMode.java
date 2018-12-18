package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/12/2017.
 */

public enum ParsingMode
{
    Request ((int)0),     // DCM NOTES: Reader interface doc claims this is 0x00
    Response  ((int)1);

    public final int value;
    ParsingMode(int value)
    {
        this.value = Integer.valueOf(value);
    }
    public static ParsingMode convert(int value)
    {
        if(value == (int)0)
        {
            return Request;
        }
        else if(value == (int)1)
        {
            return Response;
        }
        return null;
    }
}