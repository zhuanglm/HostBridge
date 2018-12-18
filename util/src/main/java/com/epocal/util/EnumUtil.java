package com.epocal.util;

import java.util.EnumSet;

/**
 * Created by dning on 5/17/2017.
 */

public class EnumUtil {
    /**
     * get Enum item by name (string)
     * @param enumset
     * @param name
     * @param <T>
     * @return Enum item
     */
    public static <T extends Enum<T>> Enum<T> ofEx(EnumSet<T> enumset, String name) {
        Enum<T> en = null;
        for (T item : enumset) {
            if (item.toString().equals(name)) {
                en = item;
                break;
            }
        }
        return en;
    }
}
