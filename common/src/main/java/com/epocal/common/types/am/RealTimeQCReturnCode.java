package com.epocal.common.types.am;

import com.epocal.common.JNIEnumValueResolverService;

public enum RealTimeQCReturnCode {
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
    Success (0),
    FirstFailed (1),
    FailedPointLowQC (2),
    FailedPointHighQC (3),
    FailedD1Low (4),
    FailedD1High (5),
    FailedD2Low (6),
    FailedD2High (7),
    Failed (8),
    LastFailed (9),
    NotPerformed (10),
    HumidityFailed (11),
    CreaEarlyWindowLow (12),
    CreaEarlyWindowHigh (13),
    AdditionalWindowMeanLow (14),
    AdditionalWindowMeanHigh (15),
    AdditionalWindowDriftLow (16),
    AdditionalWindowDriftHigh (17),
    AdditionalWindowNoiseHigh (18),
    ReferenceBubble (19),
    ENUM_UNINITIALIZED; // Keep this one as the last element

    public final int value;
    public final int index;

    RealTimeQCReturnCode() {
        // If index is not specified, invalid value 0xDEAD is assigned as value.
        // ENUM_UNINITIALIZED is assigned 0xDEAD value and is used to indicate invalid enum value.
        this.value = 0xDEAD;
        this.index = this.ordinal();
    }

    RealTimeQCReturnCode(int enumIndex) {
        // Get the actual enum value from the C++ enum via the JNIEnumResolverService.channelTypeValue() call
        this.value = JNIEnumValueResolverService.RealTimeQCReturnCodeValue(enumIndex);
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
    public static RealTimeQCReturnCode convert(int value) {
        int enumIndex = Integer.MAX_VALUE;

        for (RealTimeQCReturnCode rc : RealTimeQCReturnCode.values()) {
            if (rc.value == value) {
                enumIndex = rc.index;
                break;
            }
        }

        if (enumIndex == Integer.MAX_VALUE) {
            return RealTimeQCReturnCode.ENUM_UNINITIALIZED;
        }

        return RealTimeQCReturnCode.values()[enumIndex];
    }

    public static RealTimeQCReturnCode fromInt(int i) {
        return convert(i);
    }

}

