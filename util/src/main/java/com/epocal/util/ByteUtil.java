package com.epocal.util;

/**
 * Created by dning on 6/20/2017.
 */

public class ByteUtil {

    /***
     * search byte array, get index of key in array within ranges
     * @param bytes
     * @param fromIndex included
     * @param toIndex excluded
     * @param key
     * @return index
     */
    public static int indexOf(byte[] bytes, int fromIndex, int toIndex, byte key)
    {
        if (bytes == null || fromIndex < 0|| fromIndex > toIndex ||
            toIndex > bytes.length)
        {
            return -1;
        }
        for(int i = fromIndex; i < toIndex; i++)
        {
            if(bytes[i] == key)
            {
                return i;
            }
        }
        return -1;
    }

    public static int toUnsignedInt(byte b)
    {
        return new UnsignedType.UInt16(b).getValue();
    }
}
