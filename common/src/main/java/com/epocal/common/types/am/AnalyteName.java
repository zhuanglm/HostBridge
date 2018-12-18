package com.epocal.common.types.am;

import com.epocal.common.JNIEnumValueResolverService;

public enum AnalyteName {
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
    Unknown               (0), // The value 0 is not used in the code. It exists to maintain consistency with the C# code.
    Na                    (1), // Sodium
    K                     (2), // Potassium
    pCO2                  (3), // Carbon Dioxide
    pO2                   (4), // Oxygen
    Ca                    (5), // Calcium
    pH                    (6), // pH
    Hct                   (7), // Hematocrit
    pHT                   (8), // Corrected pH
    pCO2T                 (9), // Corrected CO2
    pO2T                  (10), // Corrected PO2
    HCO3act               (11), // Actual Bicarbonate
    HCO3Std               (12), // Standard Bicarbonate
    CaIon                 (13), // Calcium Ion
    BEecf                 (14), // Base Excess ECF
    BEb                   (15), // Base Excess Blood
    OxygenConcentration   (16), // Oxygen Concentration
    O2SAT                 (17), // Oxygen Saturation
    cTCO2                 (18), // (Calculated) Total CO2
    AlveolarO2            (19), // AlveolarO2       A
    ArtAlvOxDiff          (20), // ArtAlvOxDiff     A-a
    ArtAlvOxRatio         (21), // ArtAlvOxRatio    a/A
    cHgb                  (22), // Hemoglobin
    UncorrectedHematocrit (23), // UncorrectedHematocrit
    Glucose               (24), // Glucose
    Chloride              (25), // Chloride
    Lactate               (26), // Lactate
    Urea                  (27), // Urea
    Creatinine            (28), // Creatinine
    AnionGap              (29), // AnionGap
    AnionGapK             (30), // AnionGapK
    Creatine              (31), // Creatine
    eGFR                  (32), // eGFR
    eGFRa                 (33), // eGFRa
    eGFRj                 (34), // eGFRj
    cO2SAT                (35), // correctedOxygenSaturation
    cAlveolarO2           (36), // correctedAlveolarO2      A(T)
    cArtAlvOxDiff         (37), // correctedArtAlvOxDiff    A-a(T)
    cArtAlvOxRatio        (38), // correctedArtAlvOxRatio   a/A(T)
    TCO2                  (39), // (Measured) Total CO2
    UreaCreaRatio         (40), // Urea/Creatinine Ratio
    BUN                   (41), // Blood Urea Nitrogen
    BUNCreaRatio          (42), // BUN/Creatinine Ratio
    GFRckd                (43), // GFRckd
    GFRckda               (44), // GFRckda
    GFRswz                (45), // GFRswz

    ENUM_UNINITIALIZED; // Keep this one as the last element

    public final int value;
    public final int index;

    AnalyteName() {
        // If index is not specified, invalid value 0xDEAD is assigned as value.
        // ENUM_UNINITIALIZED is assigned 0xDEAD value and is used to indicate invalid enum value.
        this.value = 0xDEAD;
        this.index = this.ordinal();
    }

    AnalyteName(int enumIndex) {
        // Get the actual enum value from the C++ enum via the JNIEnumResolverService.channelTypeValue() call
        this.value = JNIEnumValueResolverService.AnalyteNameValue(enumIndex);
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
    public static AnalyteName convert(int value) {
        int enumIndex = Integer.MAX_VALUE;

        for (AnalyteName rc : AnalyteName.values()) {
            if (rc.value == value) {
                enumIndex = rc.index;
                break;
            }
        }

        if (enumIndex == Integer.MAX_VALUE) {
            return AnalyteName.ENUM_UNINITIALIZED;
        }

        return AnalyteName.values()[enumIndex];
    }

    public static AnalyteName fromInt(int i) {
        return convert(i);
    }

}

