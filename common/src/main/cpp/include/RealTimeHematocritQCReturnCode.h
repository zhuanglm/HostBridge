#ifndef EPOC_REALTIMEHEMATOCRITQCRETURNCODE_H
#define EPOC_REALTIMEHEMATOCRITQCRETURNCODE_H

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
                ENUM_DEF(RealTimeHematocritQCReturnCode) {
                    /// NOTICE: The order these enums are defined in shall remain unchanged,
                    /// only their values may be updated.
                    ///
                    /// The order each enum definition dictates the values applied to the enum's
                    /// Java representation, and thus any changes (adding, deleting, re-oerdering)
                    /// of enum elements must be reflected in the Java/JNI and C#/C++(CLI) code
                    /// as well.

                    Success,
                    NotPerformed,
                    OnePointLowResistance,
                    OnePointHighResistance,
                    OnePointAir,
                    EarlyInjection,
                    LowResistance,
                    FailedQC
                };

            }
        }
    }
}

#endif // EPOC_REALTIMEHEMATOCRITQCRETURNCODE_H