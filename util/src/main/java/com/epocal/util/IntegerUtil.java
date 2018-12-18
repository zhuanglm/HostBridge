package com.epocal.util;

public class IntegerUtil {
    public static boolean isInteger(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException ex){}
        return false;
    }
}
