package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmate on 8/3/2017.
 * These are intended for any input type on Host, included in defining input rules
 * like barcode trimming, max-min values etc.
 * Some of them might overlap with EpocTestFieldType, but used in different classes.
 */

public enum InputFieldType {
    UNKNOWN(0)
            {@Override
            public String toString() {
                return "Unknown";
            }},
    USER_ID(1)
            {@Override
            public String toString() {
                return "User id";
            }},
    PASSWORD(2)
            {@Override
            public String toString() {
                return "Password";
            }},
    PATIENT_ID(3)
            {@Override
            public String toString() {
                return "Patient id";
            }},
    PATIENT_OR_LOT_ID(4)
            {@Override
            public String toString() {
                return "Fluid lot";
            }},
    ID_2(5)
            {@Override
            public String toString() {
                return "Id 2";
            }},
    COMMENT(6)
            {@Override
            public String toString() {
                return "Comment";
            }},
    OTHER(7)
            {@Override
            public String toString() {
                return "Other";
            }};

    public final Integer value;

    InputFieldType(Integer value) {
        this.value = value;
    }

    private static final Map<Integer, InputFieldType> typeMap = new HashMap<>();

    static {
        for (InputFieldType type : InputFieldType.values()) {
            typeMap.put(type.value, type);
        }
    }

    public static InputFieldType fromInt(int i) {
        InputFieldType retval = typeMap.get(Integer.valueOf(i));
        if (retval == null) {
            return InputFieldType.UNKNOWN;
        }
        return retval;
    }
}
