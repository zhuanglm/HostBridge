package com.epocal.common.types.am;

import com.epocal.common.JNIEnumValueResolverService;

public enum Units {
    /**
     * Set each entry by referencing the C++ enum entry's index for each entry, in
     * sequential order.  The index is used to lookup the value for the enum as defined
     * in the native enum definition.
     * <p>
     * NOTE: enumValue(xyz) => "xyz" is not the property's value, rather it is the index of the
     * entry as defined in the native C++ enum definition.
     * e.g. C++11
     * enum class MyEnum : int
     * {
     * enumEntry0 = 123,
     * enumEntry1 = 456,
     * enumEntry2 = 789,
     * };
     * <p>
     * Java
     * public enum MyEnum {
     * enumEntry_Index_0(0),
     * enumEntry_Index_1(1),
     * enumEntry_Index_2(2);
     * <p>
     * ...
     * }
     */
    None (0),
    Percent (1),
    mmolL (2),
    mmhg (3),
    mldl (4),
    degreesC (5),
    degreesF (6),
    degreesK (7),
    gldl (8),
    pH (9),
    kPa (10),
    mEqL (11),
    mgdl (12),
    LL (13),
    gL (14),
    umolL (15),
    mlmin173m2 (16),
    cm (17),
    fraction (18),
    MilligramPerMilligram (19),  // mgmg in C++
    MillimolePerMillimole (20),  // mmolmmol in C++
    inches (21),

    ENUM_UNINITIALIZED; // Keep this one as the last element

    public final int value;
    public final int index;

    Units() {
        // If index is not specified, invalid value 0xDEAD is assigned as value.
        // ENUM_UNINITIALIZED is assigned 0xDEAD value and is used to indicate invalid enum value.
        this.value = 0xDEAD;
        this.index = this.ordinal();
    }

    Units(int enumIndex) {
        // Get the actual enum value from the C++ enum via the JNIEnumResolverService.channelTypeValue() call
        this.value = JNIEnumValueResolverService.UnitsValue(enumIndex);
        this.index = enumIndex;
    }

    /**
     * Find enum instance whose integer value matches the input.
     * When no match is found, ENUM_UNINITIALIZED is returned
     * indicating the error. (No match found case)
     *
     * @param value -- int value of enum
     * @return SampleType which matches enum value
     * ENUM_UNINITIALIZED is returned when error in conversion
     */
    public static Units convert(int value) {
        int enumIndex = Integer.MAX_VALUE;

        for (Units rc : Units.values()) {
            if (rc.value == value) {
                enumIndex = rc.index;
                break;
            }
        }

        if (enumIndex == Integer.MAX_VALUE) {
            return Units.ENUM_UNINITIALIZED;
        }

        return Units.values()[enumIndex];
    }

    public static Units fromInt(int i) {
        return convert(i);
    }

}

