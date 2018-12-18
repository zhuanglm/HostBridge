package com.epocal.common.types.am;

import com.epocal.common.JNIEnumValueResolverService;

public enum Sensors {
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
    Conductivity (1),
    Ground (2),
    Sodium (3),
    Potassium (4),
    CarbonDioxide (5),
    Oxygen (6),
    Calcium (7),
    pH (8),
    Hematocrit (9),
    HeaterTop (10),
    HeaterBottom (11),
    CPUTemperature (12),
    Reference (13),
    FiveK (14),
    NineK (15),
    SixteenK (16),
    ThirtyK (17),
    ConductivityMv (18),
    TopHeaterPid (19),
    Chloride (20),
    Urea (21),
    Glucose (22),
    Creatinine (23),
    NotUsed (24),
    Lactate (25),
    Gold (26),
    Creatine (27),
    TCO2 (28),
    ENUM_UNINITIALIZED; // Keep this one as the last element

    public final int value;
    public final int index;

    Sensors() {
        // If index is not specified, invalid value 0xDEAD is assigned as value.
        // ENUM_UNINITIALIZED is assigned 0xDEAD value and is used to indicate invalid enum value.
        this.value = 0xDEAD;
        this.index = this.ordinal();
    }

    Sensors(int enumIndex) {
        // Get the actual enum value from the C++ enum via the JNIEnumResolverService.channelTypeValue() call
        this.value = JNIEnumValueResolverService.SensorsValue(enumIndex);
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
    public static Sensors convert(int value) {
        int enumIndex = Integer.MAX_VALUE;

        for (Sensors rc : Sensors.values()) {
            if (rc.value == value) {
                enumIndex = rc.index;
                break;
            }
        }

        if (enumIndex == Integer.MAX_VALUE) {
            return Sensors.ENUM_UNINITIALIZED;
        }

        return Sensors.values()[enumIndex];
    }

    public static Sensors fromInt(int i) {
        return convert(i);
    }

}

