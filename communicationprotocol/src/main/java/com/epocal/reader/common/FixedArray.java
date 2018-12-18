package com.epocal.reader.common;

import java.lang.reflect.Array;

/**
 * Created by dning on 6/8/2017.
 */

public class FixedArray<T>
{
    public final int FixedLen;
    private T mData[];
    public T[] getData()
    {
        return mData;
    }

    public void setData(Class<T> t, T[] data)
    {
        if (data == null)
        {
            mData = null;
            return;
        }
        System.arraycopy(data, 0, mData, 0, Math.min(data.length, FixedLen));
    }

    public FixedArray(Class<T> t, int len)
    {
        FixedLen = len;
        mData = (T[]) Array.newInstance(t, len);
    }

    public T getByIndex(int i) {
        if(mData == null || i >= mData.length)
        {
            return null;
        }
        return mData[i];
    }

    public String toString()
    {
        if (mData == null) return "null";
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < mData.length; i++)
        {
            s.append(mData[i] + " ");
        }
        return s.toString();
    }

    public static byte[] toPrimitive(final Byte[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return null;
        }
        final byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].byteValue();
        }
        return result;
    }

    public static Byte[] toClass(final byte[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return null;
        }
        final Byte[] result = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

}
