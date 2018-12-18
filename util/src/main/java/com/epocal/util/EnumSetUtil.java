package com.epocal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by bmate on 6/6/2017.
 */

public class EnumSetUtil {

    /**
     * enumset to long
     * @param set
     * @param <T>
     * @return long
     */
    public static <T extends Enum<T>> long encode(EnumSet<T> set)
    {
        long r = 0;
        for(T value : set)
        {
            r |= 1L << value.ordinal();
        }
        return r;
    }

    /**
     * long to enumset
     * @param k
     * @param type
     * @return EnumSet
     */

    public static <E extends Enum<E>> EnumSet<E> toSet(long k, Class<E> type) {
        E[] enums = type.getEnumConstants();
        EnumSet<E> enumSet = EnumSet.noneOf(type);
        for (E e: enums) {
            if ((k & ((long)Math.pow(2,e.ordinal()))) != 0){
                enumSet.add(e);
            }
        }
        return enumSet;
    }

    public static <E extends Enum<E>> ArrayList<String> getValues(Class<E> type)
    {
        final ArrayList<String> enumList = new ArrayList<String>();
        E[] enums = type.getEnumConstants();
        EnumSet<E> enumSet = EnumSet.noneOf(type);
        for (E e: enums) {
            enumList.add( e.toString() );
        }
        return enumList;
    }
}
