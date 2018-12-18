///////////////////////////////////////////////////////////////////////////////////
///
/// globals.h
///
/// Copyright Epocal Inc 2017
///
/// This module will contains definitions that the AnalyticalManager, EpocHost,
/// and Analytical Tools all use.
///
///////////////////////////////////////////////////////////////////////////////////

#ifndef EPOC_AM_NATIVE_BGE_GLOBALS_H
#define EPOC_AM_NATIVE_BGE_GLOBALS_H

// Define enums based on the project-type that includes this header file
#include <memory>
#include <vector>

#include <AGAPType.h>
#include <AgeCategory.h>
#include <AllensTest.h>
#include <Analytes.h>
#include <BloodSampleType.h>
#include <BubbleDetectMode.h>
#include <ChannelType.h>
#include <DeliverySystem.h>
#include <DrawSites.h>
#include <EGFRFormula.h>
#include <EGFRType.h>
#include <Fio2Units.h>
#include <FluidType.h>
#include <Gender.h>
#include <HumidityReturnCode.h>
#include <RealTimeHematocritQCReturnCode.h>
#include <RealTimeQCReturnCode.h>
#include <RealTimeQCType.h>
#include <RespiratoryMode.h>
#include <ResultsCalcReturnCode.h>
#include <Sensors.h>
#include <Temperatures.h>
#include <TestMode.h>

#ifdef _MANAGED
using namespace Epoc::Common::Dotnet::Definitions;
#else
using namespace Epoc::Common::Native::Definitions;
#endif

#ifdef _MANAGED
#define ENUM_DEF(ENUM_NAME) public enum class ENUM_NAME         // Managed C++/CLI
#else
#define ENUM_DEF(ENUM_NAME) enum class ENUM_NAME : int          // C++11
#endif


// NOTE: Cannot use nested namespace declarations (e.g. Epoc::AM::Native::Bge); nested namespace declarations is a C++17 feature.
//       (C++17 is not officially supported by clang++ for Android native C++ builds at this time)
#ifdef _MANAGED
namespace Epoc {
    namespace AM {
        namespace Dotnet {
            namespace Bge {
#else
namespace Epoc {
    namespace AM {
        namespace Native {
            namespace Bge {
#endif

                ///=======///
                /// ENUMS ///
                ///=======///

                //used to update config file from host 3.7 and before
                ENUM_DEF(BloodSampleType_3_7)
                {
                    /// NOTICE: The order these enums are defined in shall remain unchanged,
                    /// only their values may be updated.
                    ///
                    /// The order each enum definition dictates the values applied to the enum's
                    /// Java representation, and thus any changes (adding, deleting, re-oerdering)
                    /// of enum elements must be reflected in the Java/JNI and C#/C++(CLI) code
                    /// as well.

                    Unspecified = 0,
                    Unknown = 1,
                    Arterial = 2,
                    Venous = 3,
                    MixedVenous = 4,
                    Cord = 5,
                    Capillary = 6,
                    Last = 7
                };

                ///=========///
                /// STRUCTS ///
                ///=========///

                struct FinalResult
                {
                    ChannelType channelType = (ChannelType)0;
                    Analytes analyte = (Analytes)0;
                    Analytes correctedWhat = (Analytes)0;
                    double reading = 0;
                    ResultsCalcReturnCode returnCode = (ResultsCalcReturnCode)0;
                    bool requirementsFailedIQC = false;
                };

                struct Reading
                {
                    float Time;
                    float Value;
                };

                struct SensorInfo
                {
                    ChannelType channelType;
                    Sensors sensorType;
                    uint8_t sensorDescriptorNumber;         

                    double calDelimit;
                    double sampleDelimit;
                    double postDelimit;

                    double extrapolation;

                    double calWindowSize;
                    double sampleWindowSize;
                    double postWindowSize;

                    double calCurveWeight;
                    double sampleCurveWeight;

                    double calConcentration;
                    double offset;
                    double slopeFactor;

                    double CalMeanLowQC;
                    double CalMeanHighQC;
                    double CalDriftLowQC;
                    double CalDriftHighQC;
                    double CalSecondLowQC;
                    double CalSecondHighQC;
                    double CalNoiseHighQC;

                    double SampleMeanLowQC;
                    double SampleMeanHighQC;
                    double SampleDriftLowQC;
                    double SampleDriftHighQC;
                    double SampleSecondLowQC;
                    double SampleSecondHighQC;
                    double SampleNoiseHighQC;

                    double PostMeanLowQC;
                    double PostMeanHighQC;
                    double PostDriftLowQC;
                    double PostDriftHighQC;
                    double PostSecondLowQC;
                    double PostSecondHighQC;
                    double PostNoiseHighQC;

                    double DeltaDriftLowQC;
                    double DeltaDriftHighQC;

                    double param1;
                    double param2;
                    double param3;
                    double param4;
                    double param5;
                    double param6;
                    double param7;
                    double param8;
                    double param9;
                    double param10;

                    double param11;
                    double param12;
                    double param13;
                    double param14;
                    double param15;
                    double param16;
                    double param17;
                    double param18;
                    double param19;
                    double param20;
                    double param21;
                    double param22;
                    double param23;
                    double param24;
                    double param25;
                    double param26;
                    double param27;
                    double param28;
                    double param29;
                    double param30;

                    double param31;
                    double param32;
                    double param33;
                    double param34;
                    double param35;
                    double param36;
                    double param37;
                    double param38;
                    double param39;
                    double param40;
                    double param41;
                    double param42;
                    double param43;
                    double param44;
                    double param45;
                    double param46;
                    double param47;
                    double param48;
                    double param49;
                    double param50;

                    double param51;
                    double param52;
                    double param53;
                    double param54;
                    double param55;
                    double param56;
                    double param57;
                    double param58;
                    double param59;
                    double param60;
                    double param61;
                    double param62;
                    double param63;
                    double param64;
                    double param65;
                    double param66;
                    double param67;
                    double param68;
                    double param69;
                    double param70;
                    double param71;
                    double param72;
                    double param73;
                    double param74;
                    double param75;
                    double param76;
                    double param77;
                    double param78;
                    double param79;
                    double param80;
                    double param81;
                    double param82;
                    double param83;
                    double param84;
                    double param85;
                    double param86;
                    double param87;
                    double param88;
                    double param89;
                    double param90;
                    double param91;
                    double param92;
                    double param93;
                    double param94;
                    double param95;
                    double param96;
                    double param97;
                    double param98;
                    double param99;
                    double param100;

                    float readerMeanLow;
                    float readerMeanHigh;
                    float readerDriftLow;
                    float readerDriftHigh;
                    float readerNoiseLow;
                    float readerNoiseHigh;

                    double tMinus;
                    double tPlus;
                    double postCurvatureWeight;

                    int bloodPointsToSkip;
                    int bloodPointsInWindow;
                    double bloodNoiseHigh;

                    int aqPointsToSkip;
                    int aqPointsInWindow;
                    double aqNoiseHigh;

                    int lateBloodPointsToSkip;
                    int lateBloodPointsInWindow;
                    double lateBloodNoiseHigh;

                    int lateAqPointsToSkip;
                    int lateAqPointsInWindow;
                    double lateAqNoiseHigh;

                    double rtPointLimitLow;
                    double rtPointLimitHigh;
                    double d1Low;
                    double d1High;
                    double p1d2Low;
                    double p1d2High;
                    double p2d2Low;
                    double p2d2High;
                    double p3d2Low;
                    double p3d2High;

                    double A;
                    double B;
                    double C;
                    double D;
                    double F;
                    double G;
                    double TAmbOffset;
                    double InjectionTimeOffset;
                    double AgeOffset;
                    double PowerOffset;

                    std::string NeuralNetBlood;
                    std::string NeuralNetAQ;
                };

                struct BDConfig
                {
                    // bubble detect
                    BubbleDetectMode BDMode;
                    long BDFrequency;
                    uint8_t CalInitTime;          
                    float AirInitThreshold;
                    float FluidInitThreshold;
                    float AirAfterFluidThreshold;
                    float FluidAfterFluidThreshold;
                    short calibrationExpiry;
                };

                struct Levels
                {
                    double calMean = 0;
                    double calSlope = 0;
                    double calNoise = 0;
                    double calSecond = 0;
                    double calEx = 0;
                    double sampleMean = 0;
                    double sampleSlope = 0;
                    double sampleNoise = 0;
                    double sampleSecond = 0;
                    double sampleEx = 0;
                    double postMean = 0;
                    double postSlope = 0;
                    double postNoise = 0;
                    double postSecond = 0;
                    double postEx = 0;
                    double additionalMean = 0;
                    double additionalSlope = 0;
                    double additionalNoise = 0;
                    double additionalSecond = 0;
                    double additionalEx = 0;
                    int additionalFirst = 0;
                    int additionalLast = 0;
                    double peakMean = 0;
                    double peakSlope = 0;
                    double peakNoise = 0;
                    double peakSecond = 0;
                    double peakEx = 0;
                    double response = 0;
                    double uncorrectedResponse = 0;
                    int calLast = 0;
                    int calFirst = 0;
                    int sampleFirst = 0;
                    int sampleLast = 0;
                    int postFirst = 0;
                    int postLast = 0;
                    int peakFirst = 0;
                    int peakLast = 0;
                    int calWindowMovedBack = 0;

                    double output1;
                    double output2;
                    double output3;
                    double output4;
                    double output5;
                    double output6;
                    double output7;
                    double output8;
                    double output9;
                    double output10;

                    double output11;
                    double output12;
                    double output13;
                    double output14;
                    double output15;
                    double output16;
                    double output17;
                    double output18;
                    double output19;
                    double output20;
                };

                struct CardSetupStruct
                {
                    int cardType;
                    int analyte;
                    int resultType;
                    std::string analyteString;
                };

                struct HumidityStruct
                {
                    Sensors sensorType;
                    int windowStart;
                    int windowSize;
                    double low;
                    double high;
                    double extra1;
                    double extra2;
                    double extra3;
                    double extra4;
                    double extra5;
                    double extra6;
                    double extra7;
                    double extra8;
                    double extra9;
                    double extra10;
                };

                struct RealTimeQC
                {
                    bool enabled;
                    int startTime;
                    int intervalTime;
                    RealTimeQCType type;
                    int numPoints;
                    double extra1;
                    double extra2;
                    double extra3;
                    double extra4;
                    double extra5;
                    double extra6;
                    bool continueIfFailed;
                    int humidityUntil;
                    std::vector<HumidityStruct> humidityConfig;
                };

            }
        }
    }
}

#endif //EPOC_AM_NATIVE_BGE_GLOBALS_H
