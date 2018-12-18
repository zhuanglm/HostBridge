package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 6/8/2017.
 */

public enum EnabledSelectedOptionType {
    Unknown(0),
    Disabled(1),
    EnabledSelected(2),
    EnabledUnselected(3);
    public final Integer value;

    EnabledSelectedOptionType(Integer value) {
        this.value = value;
    }

    private static final Map<Integer, EnabledSelectedOptionType> typeMap = new HashMap<Integer, EnabledSelectedOptionType>();

    static {
        for (EnabledSelectedOptionType type : EnabledSelectedOptionType.values()) {
            typeMap.put(type.value, type);
        }
    }

    public static EnabledSelectedOptionType fromInt(int i) {
        EnabledSelectedOptionType retval = typeMap.get(Integer.valueOf(i));
        if (retval == null) {
            return EnabledSelectedOptionType.Unknown;
        }
        return retval;
    }
}
