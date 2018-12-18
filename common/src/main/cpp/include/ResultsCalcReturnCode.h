#ifndef EPOC_RESULTSCALCRETURNCODE_H
#define EPOC_RESULTSCALCRETURNCODE_H

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
                ENUM_DEF(ResultsCalcReturnCode) {
                    /// NOTICE: The order these enums are defined in shall remain unchanged,
                    /// only their values may be updated.
                    ///
                    /// The order each enum definition dictates the values applied to the enum's
                    /// Java representation, and thus any changes (adding, deleting, re-oerdering)
                    /// of enum elements must be reflected in the Java/JNI and C#/C++(CLI) code
                    /// as well.

                    Success = 0,
                    FailedQCStart = 2,
                    CalMeanQCLow = 3,
                    CalMeanQCHigh = 4,
                    CalDriftQCLow = 5,
                    CalDriftQCHigh = 6,
                    CalSecondQCLow = 7,
                    CalSecondQCHigh = 8,
                    CalNoiseQCHigh = 9,
                    SampleMeanQCLow = 10,
                    SampleMeanQCHigh = 11,
                    SampleDriftQCLow = 12,
                    SampleDriftQCHigh = 13,
                    SampleSecondQCLow = 14,
                    SampleSecondQCHigh = 15,
                    SampleNoiseQCHigh = 16,
                    PostMeanQCLow = 17,
                    PostMeanQCHigh = 18,
                    PostDriftQCLow = 20,
                    PostDriftQCHigh = 21,
                    PostSecondQCLow = 22,
                    PostSecondQCHigh = 23,
                    PostNoiseQCHigh = 24,
                    DeltaDriftLow = 25,
                    DeltraDriftHigh = 26,
                    DeltaMeanPostSample = 27,
                    InterferentDetected = 28,
                    GenericQCLow = 29,
                    GenericQCHigh = 30,
                    EarlyWindowLow = 31,
                    EarlyWindowHigh = 32,
                    PO2Bubble = 33,
                    AdditionalMeanLow = 34,
                    AdditionalMeanHigh = 35,
                    AdditionalDriftLow = 36,
                    AdditionalDriftHigh = 37,
                    AdditionalNoiseHigh = 38,
                    FailedQCLast = 40,
                    ReportableLow = 41,
                    ReportableHigh = 42,
                    CannotCalculate = 43,
                    BubbleAbnormality = 44,
                    UnexplainedFailure = 45,
                    UnderReportableRange = 46,
                    OverReportableRange = 47,
                    UncorrectedHematocrit = 48,
                    RequirementsNotFound = 49,
                    DipInSample = 50,
                    SpikeInSample = 51,
                    SampleWindowNoise = 52,
                    OverInsanityRange = 53,
                    UnderInsanityRange = 54,
                    SWPeakNoiseHigh = 55,
                    SWPeakDriftLow = 56,
                    SWPeakDriftHigh = 57,
                    ExpiredCard = 58,
                    EarlyDipInSample = 59,
                    EarlySpikeInSample = 60,
                    LateDipInSample = 61,
                    LateSpikeInSample = 62,
                    CreaEarlyWindowLow = 63,
                    CreaEarlyWindowHigh = 64,
                    CreaDriftFailure = 65,
                    HematocritShortSample = 66,
                    FailedQCEver = 67,
                    SampleWindowAllPointCheck = 68,
                    Reserved = 69,
                    CalWindowDip = 70,
                    SampleDipEarly = 71,
                    SampleDipLate = 72,
                    CreaSampleBubble = 73,
                    CreaCalDriftTermLowerConcLimit = 74
                };

            }
        }
    }
}

#endif // EPOC_RESULTSCALCRETURNCODE_H