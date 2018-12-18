package com.epocal.reader.enumtype.legacy;

import com.epocal.common.JNIEnumValueResolverService;

public enum LegacyBubbleDetectMode {
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
    AllAlwaysOff (0),
    AllAlwaysOn (1),
    BDChannelSamplingOnlyOn (2),
    BDStageAllOn (4),
    BDStageBDChannelOnOnly (5),
    ENUM_UNINITIALIZED; // Keep this one as the last element

    public final int value;
    public final int index;

    LegacyBubbleDetectMode() {
        // If index is not specified, invalid value 0xDEAD is assigned as value.
        // ENUM_UNINITIALIZED is assigned 0xDEAD value and is used to indicate invalid enum value.
        this.value = 0xDEAD;
        this.index = this.ordinal();
    }

    LegacyBubbleDetectMode(int enumIndex) {
        // Get the actual enum value from the C++ enum via the JNIEnumResolverService.channelTypeValue() call
        this.value = JNIEnumValueResolverService.BubbleDetectModeValue(enumIndex);
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
    public static LegacyBubbleDetectMode convert(int value) {
        int enumIndex = Integer.MAX_VALUE;

        for (LegacyBubbleDetectMode rc : LegacyBubbleDetectMode.values()) {
            if (rc.value == value) {
                enumIndex = rc.index;
                break;
            }
        }

        if (enumIndex == Integer.MAX_VALUE) {
            return LegacyBubbleDetectMode.ENUM_UNINITIALIZED;
        }

        return LegacyBubbleDetectMode.values()[enumIndex];
    }

    public static LegacyBubbleDetectMode fromInt(int i) {
        return convert(i);
    }

}

