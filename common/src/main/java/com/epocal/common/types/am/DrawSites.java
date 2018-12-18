package com.epocal.common.types.am;

import com.epocal.common.JNIEnumValueResolverService;

public enum DrawSites {
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
    RRadial (0),
    LRadial (1),
    RBrach (2),
    LBrach (3),
    ArtLine (4),
    RFem (5),
    LFem (6),
    CentLine (7),
    SwanGanz (8),
    RHeel (9),
    LHeel (10),
    UAC (11),
    UVC (12),
    RA (13),
    RV (14),
    PA (15),
    RFinger (16),
    LFinger (17),
    RToe (18),
    LToe (19),
    PICC (20),
    Other (21),
    ENUM_UNINITIALIZED; // Keep this one as the last element

    public final int value;
    public final int index;

    DrawSites() {
        // If index is not specified, invalid value 0xDEAD is assigned as value.
        // ENUM_UNINITIALIZED is assigned 0xDEAD value and is used to indicate invalid enum value.
        this.value = 0xDEAD;
        this.index = this.ordinal();
    }

    DrawSites(int enumIndex) {
        // Get the actual enum value from the C++ enum via the JNIEnumResolverService.channelTypeValue() call
        this.value = JNIEnumValueResolverService.DrawSitesValue(enumIndex);
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
    public static DrawSites convert(int value) {
        int enumIndex = Integer.MAX_VALUE;

        for (DrawSites rc : DrawSites.values()) {
            if (rc.value == value) {
                enumIndex = rc.index;
                break;
            }
        }

        if (enumIndex == Integer.MAX_VALUE) {
            return DrawSites.ENUM_UNINITIALIZED;
        }

        return DrawSites.values()[enumIndex];
    }

    public static DrawSites fromInt(int i) {
        return convert(i);
    }

}
