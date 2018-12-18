package com.epocal.common.types.am;

import com.epocal.common.JNIEnumValueResolverService;

public enum DeliverySystem {
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
    AdultVent(0),
    NeoVent(1),
    RoomAir(2),
    NasalCannula(3),
    HFNC(4),
    VentiMask(5),
    NRB(6),
    AquinOx(7),
    FaceTent(8),
    AeroTx(9),
    AeroMask(10),
    TCollar(11),
    ETTube(12),
    Bagging(13),
    Vapotherm(14),
    OxyHood(15),
    HFOV(16),
    HFJV(17),
    Incubator(18),
    OptiFlow(19),
    Other (20),
    BiPAP (21),
    CPAP (22),
    OxyMask (23),
    PediVent (24),
    PRB (25),
    TTube (26),
    ENUM_UNINITIALIZED; // Keep this one as the last element

    public final int value;
    public final int index;

    DeliverySystem() {
        // If index is not specified, invalid value 0xDEAD is assigned as value.
        // ENUM_UNINITIALIZED is assigned 0xDEAD value and is used to indicate invalid enum value.
        this.value = 0xDEAD;
        this.index = this.ordinal();
    }

    DeliverySystem(int enumIndex) {
        // Get the actual enum value from the C++ enum via the JNIEnumResolverService.channelTypeValue() call
        this.value = JNIEnumValueResolverService.DeliverySystemValue(enumIndex);
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
    public static DeliverySystem convert(int value) {
        int enumIndex = Integer.MAX_VALUE;

        for (DeliverySystem rc : DeliverySystem.values()) {
            if (rc.value == value) {
                enumIndex = rc.index;
                break;
            }
        }

        if (enumIndex == Integer.MAX_VALUE) {
            return DeliverySystem.ENUM_UNINITIALIZED;
        }

        return DeliverySystem.values()[enumIndex];
    }

    public static DeliverySystem fromInt(int i) {
        return convert(i);
    }

}

