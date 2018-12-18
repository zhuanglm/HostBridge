package com.epocal.util;

public class NetworkUtil {
    public static boolean isBluetoothAddress(String address)
    {
        if ((address.length() == 17) && (address.charAt(2) == ':') && (address.charAt(5) == ':') && (address.charAt(8) == ':')
                && (address.charAt(11) == ':') && (address.charAt(14) == ':') )
        {
            return true;
        }
        return false;
    }
}
