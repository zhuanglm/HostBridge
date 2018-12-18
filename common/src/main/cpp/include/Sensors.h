#ifndef EPOC_SENSORS_H
#define EPOC_SENSORS_H

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
                ENUM_DEF(Sensors) {
                    /// NOTICE: The order these enums are defined in shall remain unchanged,
                    /// only their values may be updated.
                    ///
                    /// The order each enum definition dictates the values applied to the enum's
                    /// Java representation, and thus any changes (adding, deleting, re-oerdering)
                    /// of enum elements must be reflected in the Java/JNI and C#/C++(CLI) code
                    /// as well.
                    None = 0,
                    Conductivity = 1,
                    Ground = 2,
                    Sodium = 3,
                    Potassium = 4,
                    CarbonDioxide = 5,
                    Oxygen = 6,
                    Calcium = 7,
                    pH = 8,
                    Hematocrit = 9,
                    HeaterTop = 10,
                    HeaterBottom = 11,
                    CPUTemperature = 12,
                    Reference = 13,
                    FiveK = 14,
                    NineK = 15,
                    SixteenK = 16,
                    ThirtyK = 17,
                    ConductivityMv = 18,
                    TopHeaterPid = 19,
                    Chloride = 20,
                    Urea = 21,
                    Glucose = 22,
                    Creatinine = 23,
                    NotUsed = 24,
                    Lactate = 25,
                    Gold = 26,
                    Creatine = 27,
                    TCO2 = 28,
                    Last = 29,
                };

            }
        }
    }
}

#endif // EPOC_SENSORS_H