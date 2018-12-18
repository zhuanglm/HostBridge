package com.epocal.common.types.am;

import com.epocal.common.JNIEnumValueResolverService;

public enum ResultsCalcReturnCode {
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
    FailedQCStart (1),
    CalMeanQCLow (2),
    CalMeanQCHigh (3),
    CalDriftQCLow (4),
    CalDriftQCHigh (5),
    CalSecondQCLow (6),
    CalSecondQCHigh (7),
    CalNoiseQCHigh (8),
    SampleMeanQCLow (9),
    SampleMeanQCHigh (10),
    SampleDriftQCLow (11),
    SampleDriftQCHigh (12),
    SampleSecondQCLow (13),
    SampleSecondQCHigh (14),
    SampleNoiseQCHigh (15),
    PostMeanQCLow (16),
    PostMeanQCHigh (17),
    PostDriftQCLow (18),
    PostDriftQCHigh (19),
    PostSecondQCLow (20),
    PostSecondQCHigh (21),
    PostNoiseQCHigh (22),
    DeltaDriftLow (23),
    DeltraDriftHigh (24),
    DeltaMeanPostSample (25),
    InterferentDetected (26),
    GenericQCLow (27),
    GenericQCHigh (28),
    EarlyWindowLow (29),
    EarlyWindowHigh (30),
    PO2Bubble (31),
    AdditionalMeanLow (32),
    AdditionalMeanHigh (33),
    AdditionalDriftLow (34),
    AdditionalDriftHigh (35),
    AdditionalNoiseHigh (36),
    FailedQCLast (37),
    ReportableLow (38),
    ReportableHigh (39),
    CannotCalculate (40),
    BubbleAbnormality (41),
    UnexplainedFailure (42),
    UnderReportableRange (43),
    OverReportableRange (44),
    UncorrectedHematocrit (45),
    RequirementsNotFound (46),
    DipInSample (47),
    SpikeInSample (48),
    SampleWindowNoise (49),
    OverInsanityRange (50),
    UnderInsanityRange (51),
    SWPeakNoiseHigh (52),
    SWPeakDriftLow (53),
    SWPeakDriftHigh (54),
    ExpiredCard (55),
    EarlyDipInSample (56),
    EarlySpikeInSample (57),
    LateDipInSample (58),
    LateSpikeInSample (59),
    CreaEarlyWindowLow (60),
    CreaEarlyWindowHigh (61),
    CreaDriftFailure (62),
    HematocritShortSample (63),
    FailedQCEver (64),
    SampleWindowAllPointCheck (65),
    Reserved (66),
    CalWindowDip (67),
    SampleDipEarly (68),
    SampleDipLate (69),
    CreaSampleBubble (70),
    CreaCalDriftTermLowerConcLimit (71),
    ENUM_UNINITIALIZED; // Keep this one as the last element

    public final int value;
    public final int index;

    ResultsCalcReturnCode() {
        // If index is not specified, invalid value 0xDEAD is assigned as value.
        // ENUM_UNINITIALIZED is assigned 0xDEAD value and is used to indicate invalid enum value.
        this.value = 0xDEAD;
        this.index = this.ordinal();
    }

    ResultsCalcReturnCode(int enumIndex) {
        // Get the actual enum value from the C++ enum via the JNIEnumResolverService.channelTypeValue() call
        this.value = JNIEnumValueResolverService.ResultsCalcReturnCodeValue(enumIndex);
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
    public static ResultsCalcReturnCode convert(int value) {
        int enumIndex = Integer.MAX_VALUE;

        for (ResultsCalcReturnCode rc : ResultsCalcReturnCode.values()) {
            if (rc.value == value) {
                enumIndex = rc.index;
                break;
            }
        }

        if (enumIndex == Integer.MAX_VALUE) {
            return ResultsCalcReturnCode.ENUM_UNINITIALIZED;
        }

        return ResultsCalcReturnCode.values()[enumIndex];
    }

    public static ResultsCalcReturnCode fromInt(int i) {
        return convert(i);
    }

}

