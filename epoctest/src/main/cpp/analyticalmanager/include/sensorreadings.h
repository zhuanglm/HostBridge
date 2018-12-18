///////////////////////////////////////////////////////////////////////////////////
///
/// SensorReadings.h
///
/// Copyright Epocal Inc 2017
///
/// This class represents all information about a specific sampled sensor
///
///////////////////////////////////////////////////////////////////////////////////
#ifndef EPOC_AM_NATIVE_BGE_SENSORREADINGS_H
#define EPOC_AM_NATIVE_BGE_SENSORREADINGS_H

#include <memory>
#include <string>
#include <vector>

#include "globals.h"
#include "HumidityReturnCode.h"
#include "RealTimeQCReturnCode.h"
#include "ResultsCalcReturnCode.h"
#include "Sensors.h"

using namespace Epoc::Common::Native::Definitions;

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

                ///=========///
                /// CLASSES ///
                ///=========///

                class SensorReadings
                {
                public:
                    bool requirementsFailedQC;
                    ResultsCalcReturnCode returnCode;
                    Sensors sensorType;
                    uint8_t sensorDescriptorNumber;         
                    ChannelType channelType;
                    Analytes analyte;
                    std::string analyteString;
                    std::shared_ptr<std::vector<Reading>> readings;
                    int readingPointer = 0;
                    int numThisTypeReading;
                    double result;
                    double multiplicationFactor;
                    std::shared_ptr<SensorInfo> sensorDescriptor;
                    RealTimeQCReturnCode realTimeQCPassed;
                    RealTimeQCReturnCode realTimeQCFailedEver;
                    bool checkRealtimeQC = true;
                    double reportableLow;
                    double reportableHigh;
                    std::shared_ptr<Levels> levels;
                    HumidityReturnCode humidityPassed;
                    double insanityLow;
                    double insanityHigh;
                    double insanityQALow;
                    double insanityQAHigh;

                    // requested by etienne
                    int RealTimeQCFailureTotal;
                    int HumidityQCFailureTotal;
                    int AirQCFailureTotal;
                    std::string RealTimeQCFailureOccuranceString;
                    std::string HumidityQCFailureOccuranceString;
                    std::string AirQCFailureOccuranceString;
                    std::string extraString;
                    std::string resultString;

                    SensorReadings()
                    {
                        realTimeQCPassed = RealTimeQCReturnCode::Success;
                        realTimeQCFailedEver = RealTimeQCReturnCode::Success;
                        readings = std::make_shared<std::vector<Reading>>();
                        multiplicationFactor = 1;
                        returnCode = ResultsCalcReturnCode::Success;
                        levels = std::make_shared<Levels>();
                        humidityPassed = HumidityReturnCode::NotPerformedYet;
                        sensorDescriptor = std::make_shared<SensorInfo>();
                        requirementsFailedQC = false;
                    }

                    ~SensorReadings() {}

                    void FixValues() {
                        if (readings != nullptr) {
                            for (size_t i = 0; i < readings->size(); i++) {
                                (*readings)[i].Value = ((*readings)[i].Value *
                                                                multiplicationFactor);
                            }
                        }
                    }
                };

            }
        }
    }
}

#endif //EPOC_AM_NATIVE_BGE_SENSORREADINGS_H
