#ifndef EPOC_UNITS_H
#define EPOC_UNITS_H

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
                ENUM_DEF(Units) {
                    /// NOTICE: The order these enums are defined in shall remain unchanged,
                    /// only their values may be updated.
                    ///
                    /// The order each enum definition dictates the values applied to the enum's
                    /// Java representation, and thus any changes (adding, deleting, re-oerdering)
                    /// of enum elements must be reflected in the Java/JNI and C#/C++(CLI) code
                    /// as well.

                    None = 0,
                    Percent = 1,
                    mmolL = 2,
                    mmhg = 3,
                    mldl = 4,
                    degreesC = 5,
                    degreesF = 6,
                    degreesK = 7,
                    gldl = 8,
                    pH = 9,
                    kPa = 10,
                    mEqL = 11,
                    mgdl = 12,
                    LL = 13,
                    gL = 14,
                    umolL = 15,
                    mlmin173m2 = 16,
                    cm = 17,
                    fraction = 18,
                    mgmg = 19,
                    mmolmmol = 20,
                    inches = 21
#ifdef NEXT_GEN
                    LitersPerMinute = 19,
                    MillimolePerMillimole = 20,     // NOTE: Same unit as mmolmmol (20)
                    MilligramPerMilligram = 21,     // NOTE: Same unit as mgmg (19)
                    MilliLiterPerLiter = 22,
#endif
                };

            }
        }
    }
}

#endif // EPOC_UNITS_H