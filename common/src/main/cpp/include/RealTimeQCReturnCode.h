#ifndef EPOC_REALTIMEQCRETURNCODE_H
#define EPOC_REALTIMEQCRETURNCODE_H
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
                ENUM_DEF(RealTimeQCReturnCode)
                {
                    /// NOTICE: The order these enums are defined in shall remain unchanged,
                    /// only their values may be updated.
                    ///
                    /// The order each enum definition dictates the values applied to the enum's
                    /// Java representation, and thus any changes (adding, deleting, re-oerdering)
                    /// of enum elements must be reflected in the Java/JNI and C#/C++(CLI) code
                    /// as well.

                    Success = 0,
                    FirstFailed = 1,
                    FailedPointLowQC = 2,
                    FailedPointHighQC = 3,
                    FailedD1Low = 4,
                    FailedD1High = 5,
                    FailedD2Low = 6,
                    FailedD2High = 7,
                    Failed = 8,
                    LastFailed = 9,
                    NotPerformed = 20,
                    HumidityFailed = 21,
                    CreaEarlyWindowLow = 22,
                    CreaEarlyWindowHigh = 23,
                    AdditionalWindowMeanLow = 24,
                    AdditionalWindowMeanHigh = 25,
                    AdditionalWindowDriftLow = 26,
                    AdditionalWindowDriftHigh = 27,
                    AdditionalWindowNoiseHigh = 28,
                    ReferenceBubble = 29,
                };
            }
        }
    }
}

#endif //EPOC_REALTIMEQCRETURNCODE_H
