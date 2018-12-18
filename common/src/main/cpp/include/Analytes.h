#ifndef EPOC_ANALYTES_H
#define EPOC_ANALYTES_H
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

                ENUM_DEF(Analytes)
                {
                    /// NOTICE: The order these enums are defined in shall remain unchanged,
                    /// only their values may be updated.
                    ///
                    /// The order each enum definition dictates the values applied to the enum's
                    /// Java representation, and thus any changes (adding, deleting, re-oerdering)
                    /// of enum elements must be reflected in the Java/JNI and C#/C++(CLI) code
                    /// as well.

                    None = 0,
                    Sodium = 1,
                    Potassium = 2,
                    CarbonDioxide = 3,
                    Oxygen = 4,
                    Calcium = 5,
                    pH = 6,
                    Hematocrit = 7,
                    CorrectedpH = 8,
                    correctedCO2 = 9,
                    correctedPO2 = 10,
                    ActualBicarbonate = 11,
                    StandardBicarbonate = 12,
                    CalciumIon = 13,
                    BaseExcessECF = 14,
                    BaseExcessBlood = 15,
                    OxygenConcentration = 16,
                    OxygenSaturation = 17,
                    TotalCO2 = 18,
                    AlveolarO2 = 19,
                    ArtAlvOxDiff = 20,
                    ArtAlvOxRatio = 21,
                    Hemoglobin = 22,
                    UncorrectedHematocrit = 23,
                    Glucose = 24,
                    Chloride = 25,
                    Lactate = 26,
                    Urea = 27,
                    Creatinine = 28,
                    AnionGap = 29,
                    AnionGapK = 30,
                    Creatine = 31,
                    eGFR = 32,
                    eGFRa = 33,
                    eGFRj = 34,
                    correctedOxygenSaturation = 35,
                    correctedAlveolarO2 = 36,
                    correctedArtAlvOxDiff = 37,
                    correctedArtAlvOxRatio = 38,
                    MeasuredTCO2 = 39,
                    UreaCreaRatio = 40,
                    BUN = 41,
                    BUNCreaRatio = 42,
                    GFRckd = 43,
                    GFRckda = 44,
                    GFRswz = 45,

#ifdef NEXT_GEN
                    FO2Hb  = 46, // Oxyhemoglobin (NextGen only)
                    FHHb   = 47, // Deoxyhemoglobin (NextGen only)
                    FMetHb = 48, // Methemoglobin (NextGen only)
                    FCOHb  = 49, // Carboxyhemoglobin (NextGen only)
                    Hgb    = 50, // Hemoglobin (measured, NextGen only)
                    SO2    = 51, // Oxygen Saturation (measured, NextGen only)
                    ctO2   = 52, // Oxygen content or concentration (NextGen only)
                    BO2    = 53, // Oxygen capacity (NextGen only)
                    Last   = 54
#else
                    Last = 46,
#endif
                };
            }
        }
    }
}
#endif //EPOC_ANALYTES_H
