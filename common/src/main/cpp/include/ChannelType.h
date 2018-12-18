#ifndef EPOC_CHANNELTYPE_H
#define EPOC_CHANNELTYPE_H

// Define enums based on the project-type that includes this header file
#ifdef _MANAGED
#define ENUM_DEF(ENUM_NAME) public enum class ENUM_NAME     // Managed C++/CLI
#else
#define ENUM_DEF(ENUM_NAME) enum class ENUM_NAME : int          // C++11
#endif


// NOTE: Cannot use nested namespace declarations (e.g. Epoc::AM::Native::Bge); nested namespace declarations is a C++17 feature.
//       (C++17 is not officially supported by clang++ for Android native C++ builds at this time)
#ifdef _MANAGED
namespace Epoc {
    namespace Common {
        namespace Dotnet {
            namespace Definitions {
#else
namespace Epoc {
    namespace Common {
        namespace Native {
            namespace Definitions {
#endif

//                // when this is changed, config1_2 and the testconfig must be changed because there and
//                // more channel limits
                ENUM_DEF(ChannelType)
                {
                    /// NOTICE: The order these enums are defined in shall remain unchanged,
                    /// only their values may be updated.
                    ///
                    /// The order each enum definition dictates the values applied to the enum's
                    /// Java representation, and thus any changes (adding, deleting, re-oerdering)
                    /// of enum elements must be reflected in the Java/JNI and C#/C++(CLI) code
                    /// as well.

                    P1 = 0,
                    P2 = 1,
                    P3 = 2,
                    P4 = 3,
                    P5 = 4,
                    P6 = 5,
                    P7 = 6,
                    A1 = 7,
                    A2 = 8,
                    A3 = 9,
                    A4 = 10,
                    Reference = 11,
                    Ground = 12,
                    TopHeater = 13,
                    BottomHeater = 14,
                    CPUTemperature = 15,
                    Conductivity = 16,
                    FiveK = 17,
                    NineK = 18,
                    SixteenK = 19,
                    ThirtyK = 20,
                    Zinf = 21,
                    P1HiZ = 22,
                    P2HiZ = 23,
                    P3HiZ = 24,
                    P4HiZ = 25,
                    P5HiZ = 26,
                    P6HiZ = 27,
                    P7HiZ = 28,
                    P1LowZ = 29,
                    P2LowZ = 30,
                    P3LowZ = 31,
                    P4LowZ = 32,
                    P5LowZ = 33,
                    P6LowZ = 34,
                    P7LowZ = 35,
                    A1ST0 = 36,
                    A2ST0 = 37,
                    A3ST0 = 38,
                    A4ST0 = 39,
                    A1STiP = 40,
                    A1StiM = 41,
                    A2Sti = 42,
                    A3Sti = 43,
                    A4Sti = 44,
                    Vapp1 = 45,
                    Vapp2 = 46,
                    Vapp3 = 47,
                    Vapp4 = 48,
                    Ref18 = 49,
                    RefOut = 50,
                    GroundCE = 51,
                    A2_STIP = 52,
                    A3_STIP = 53,
                    A4_STIP = 54,
                    A2_STIM = 55,
                    A3_STIM = 56,
                    A4_STIM = 57,
                    None = 58,
                    CH_2_CONDUCTIVITY = 59,
                    CH_2_5K = 60,
                    CH_2_9K = 61,
                    CH_2_16K = 62,
                    CH_2_30K = 63,
                    CH_2_Zinf = 64,
                    CH_2_A1 = 65,
                    CH_2_A2 = 66,
                    CH_2_A3 = 67,
                    CH_2_A4 = 68,
                    Ref_25V = 69,
                    R_OUTLoZ_2_5 = 70,
                    GNDCELoZ_2_5 = 71,
                    A1_Hi_Gain_Leak = 72,
                    A2_Hi_Gain_Leak = 73,
                    A3_Hi_Gain_Leak = 74,
                    A4_Hi_Gain_Leak = 75,
                    A1_Hi_Gain_Offset = 76,
                    A2_Hi_Gain_Offset = 77,
                    A3_Hi_Gain_Offset = 78,
                    A4_Hi_Gain_Offset = 79,
                    CONDUCTIVITY_SETTLING = 80,
                    CONDUCTIVITY_THROUGH_A4 = 81,
                    GCE_VS_REF_OUT = 82,
                    RESERVED_CHAN_4 = 83,
                    Reserved84 = 84,
                    Reserved85 = 85,
                    Reserved86 = 86,
                    Reserved87 = 87,
                    Reserved88 = 88,
                    Reserved89 = 89,
                    Reserved90 = 90,
                    Reserved91 = 91,
                    Reserved92 = 92,
                    Reserved93 = 93,
                    Reserved94 = 94,
                    Reserved95 = 95,
                    Reserved96 = 96,
                    Reserved97 = 97,
                    Reserved98 = 98,
                    Reserved99 = 99,
                    Last = 100
                };

            }
        }
    }
}

#endif //EPOC_CHANNELTYPE_H