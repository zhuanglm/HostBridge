package com.epocal.common.types.am;

import com.epocal.common.JNIEnumValueResolverService;

public enum ChannelType {
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
    P1 (0),
    P2 (1),
    P3 (2),
    P4 (3),
    P5 (4),
    P6 (5),
    P7 (6),
    A1 (7),
    A2 (8),
    A3 (9),
    A4 (10),
    Reference (11),
    Ground (12),
    TopHeater (13),
    BottomHeater (14),
    CPUTemperature (15),
    Conductivity (16),
    FiveK (17),
    NineK (18),
    SixteenK (19),
    ThirtyK (20),
    Zinf (21),
    P1HiZ (22),
    P2HiZ (23),
    P3HiZ (24),
    P4HiZ (25),
    P5HiZ (26),
    P6HiZ (27),
    P7HiZ (28),
    P1LowZ (29),
    P2LowZ (30),
    P3LowZ (31),
    P4LowZ (32),
    P5LowZ (33),
    P6LowZ (34),
    P7LowZ (35),
    A1ST0 (36),
    A2ST0 (37),
    A3ST0 (38),
    A4ST0 (39),
    A1STiP (40),
    A1StiM (41),
    A2Sti (42),
    A3Sti (43),
    A4Sti (44),
    Vapp1 (45),
    Vapp2 (46),
    Vapp3 (47),
    Vapp4 (48),
    Ref18 (49),
    RefOut (50),
    GroundCE (51),
    A2_STIP (52),
    A3_STIP (53),
    A4_STIP (54),
    A2_STIM (55),
    A3_STIM (56),
    A4_STIM (57),
    None (58),
    CH_2_CONDUCTIVITY (59),
    CH_2_5K (60),
    CH_2_9K (61),
    CH_2_16K (62),
    CH_2_30K (63),
    CH_2_Zinf (64),
    CH_2_A1 (65),
    CH_2_A2 (66),
    CH_2_A3 (67),
    CH_2_A4 (68),
    Ref_25V (69),
    R_OUTLoZ_2_5 (70),
    GNDCELoZ_2_5 (71),
    A1_Hi_Gain_Leak (72),
    A2_Hi_Gain_Leak (73),
    A3_Hi_Gain_Leak (74),
    A4_Hi_Gain_Leak (75),
    A1_Hi_Gain_Offset (76),
    A2_Hi_Gain_Offset (77),
    A3_Hi_Gain_Offset (78),
    A4_Hi_Gain_Offset (79),
    CONDUCTIVITY_SETTLING (80),
    CONDUCTIVITY_THROUGH_A4 (81),
    GCE_VS_REF_OUT (82),
    RESERVED_CHAN_4 (83),
    Reserved84 (84),
    Reserved85 (85),
    Reserved86 (86),
    Reserved87 (87),
    Reserved88 (88),
    Reserved89 (89),
    Reserved90 (90),
    Reserved91 (91),
    Reserved92 (92),
    Reserved93 (93),
    Reserved94 (94),
    Reserved95 (95),
    Reserved96 (96),
    Reserved97 (97),
    Reserved98 (98),
    Reserved99 (99),
    ENUM_UNINITIALIZED (100); // Keep this one as the last element

    public final int value;
    public final int index;

    ChannelType() {
        // If index is not specified, invalid value 0xDEAD is assigned as value.
        // ENUM_UNINITIALIZED is assigned 0xDEAD value and is used to indicate invalid enum value.
        this.value = 0xDEAD;
        this.index = this.ordinal();
    }

    ChannelType(int enumIndex) {
        // Get the actual enum value from the C++ enum via the JNIEnumResolverService.channelTypeValue() call
        this.value = JNIEnumValueResolverService.ChannelTypeValue(enumIndex);
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
    public static ChannelType convert(int value) {
        int enumIndex = Integer.MAX_VALUE;

        for (ChannelType rc : ChannelType.values()) {
            if (rc.value == value) {
                enumIndex = rc.index;
                break;
            }
        }

        if (enumIndex == Integer.MAX_VALUE) {
            return ChannelType.ENUM_UNINITIALIZED;
        }

        return ChannelType.values()[enumIndex];
    }

    public static ChannelType fromInt(int i) {
        return convert(i);
    }

}

