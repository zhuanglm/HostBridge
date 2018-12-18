package com.epocal.common.types.am;

import com.epocal.common.JNIEnumValueResolverService;

public enum RespiratoryMode {
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
    SIMV (0),
    AC (1),
    PC (2),
    PS (3),
    VC (4),
    BiLevel (5),
    CPAPPS (6),
    SIMVPC (7),
    PAV (8),
    PRVC (9),
    TC (10),
    Other (11),
    BiVent (12),
    NCPAP (13),
    NIV (14),
    PCPS (15),
    PRVCPS (16),
    SIMVPS (17),
    SIMVPCPS (18),
    SIMVPRVCPS (19),
    SIMVVCPS (20),
    VS (21),
    ENUM_UNINITIALIZED; // Keep this one as the last element

    public final int value;
    public final int index;

    RespiratoryMode() {
        // If index is not specified, invalid value 0xDEAD is assigned as value.
        // ENUM_UNINITIALIZED is assigned 0xDEAD value and is used to indicate invalid enum value.
        this.value = 0xDEAD;
        this.index = this.ordinal();
    }

    RespiratoryMode(int enumIndex) {
        // Get the actual enum value from the C++ enum via the JNIEnumResolverService.channelTypeValue() call
        this.value = JNIEnumValueResolverService.RespiratoryModeValue(enumIndex);
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
    public static RespiratoryMode convert(int value) {
        int enumIndex = Integer.MAX_VALUE;

        for (RespiratoryMode rc : RespiratoryMode.values()) {
            if (rc.value == value) {
                enumIndex = rc.index;
                break;
            }
        }

        if (enumIndex == Integer.MAX_VALUE) {
            return RespiratoryMode.ENUM_UNINITIALIZED;
        }

        return RespiratoryMode.values()[enumIndex];
    }

    public static RespiratoryMode fromInt(int i) {
        return convert(i);
    }

}
