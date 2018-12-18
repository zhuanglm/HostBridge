//
// Created by Akaiya on 31/08/2017.
//
#ifndef SRC_AMSERIALIZER_H
#define SRC_AMSERIALIZER_H

#include <memory>

// AM Includes
#include "analyticalmanager.h"
#include "sensorreadings.h"
#include "LibCallReturnCode.h"

// Protobuf Includes
#include "bge_parameters.pb.h"
#include "calc_bge_request.pb.h"
#include "calc_bge_response.pb.h"
#include "check_for_early_injection_request.pb.h"
#include "check_for_early_injection_response.pb.h"
#include "compute_calculated_results_request.pb.h"
#include "compute_calculated_results_response.pb.h"
#include "compute_corrected_results_request.pb.h"
#include "compute_corrected_results_response.pb.h"
#include "final_result.pb.h"
#include "humidity_struct.pb.h"
#include "levels.pb.h"
#include "perform_realtime_qc_request.pb.h"
#include "perform_realtime_qc_response.pb.h"
#include "reading.pb.h"
#include "realtime_qc.pb.h"
#include "sensor_info.pb.h"
#include "sensor_readings.pb.h"

using namespace Epoc::AM::Native::Bge;
using namespace google::protobuf;


class AMSerializer {
public:
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // Utility methods to convert between Transfer Objects and C++ AM types

    static void convert(const SensorReadings& sensorReadings, to::SensorReadings& sensorReadingsTO);
    static void convert(const to::SensorReadings& sensorReadingsTO, SensorReadings& sensorReadings);

    static void convert(const SensorInfo& sensorInfo, to::SensorInfo& sensorInfoTO);
    static void convert(const to::SensorInfo& sensorInfoTO, SensorInfo& sensorInfo);

    static void convert(const Levels& levels, to::Levels& levelsTO);
    static void convert(const to::Levels& levelsTO, Levels& levels);

    static void convert(const Reading& reading, to::Reading& readingTO);
    static void convert(const to::Reading& readingTO, Reading& reading);

    static void convert(const HumidityStruct& humidityStruct, to::HumidityStruct& humidityStructTO);
    static void convert(const to::HumidityStruct& humidityStructTO, HumidityStruct& humidityStruct);

    static void convert(const FinalResult& finalResult, to::FinalResult& finalResultTO);
    static void convert(const to::FinalResult& finalResultTO, FinalResult& finalResult);

    static void convert(const RealTimeQC& realTimeQC, to::RealTimeQC& realTimeQCTO);
    static void convert(const to::RealTimeQC& realTimeQCTO, RealTimeQC& realTimeQC);

    static bool convert(const AnalyticalManager::BGEParameters& bgeParameters, to::BGEParameters& bgeParametersTO);
    static bool convert(const to::BGEParameters& bgeParametersTO, AnalyticalManager::BGEParameters& bgeParameters);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // Utility methods to deserialize Request Transfer Objects

    static LibraryCallReturnCode deserializeCalculateBGERequest(
        const char serializedInputData[], 
        const int inputDataSize, 
        std::vector<std::shared_ptr<SensorReadings>>& sensorReadings, 
        AnalyticalManager::BGEParameters& bgeParameters,
        bool& allowNegativeValues,
        std::string& errorMessage);

    static LibraryCallReturnCode deserializeCheckForEarlyInjectionRequest(
        const char serializedInputData[],
        const int inputDataSize,
        std::shared_ptr<SensorReadings>& hematocritReadings,
        std::shared_ptr<SensorReadings>& topHeaterReadings,
        RealTimeHematocritQCReturnCode& previousReturnCode,
        double& airAfterFluidThreshold,
        float& lastRecordedTime,
        double& firstFluid,
        std::string& errorMessage);

    static LibraryCallReturnCode deserializeComputeCalculatedResultsRequest(
        const char serializedInputData[],
        const int inputDataSize,
        std::vector<FinalResult>& measuredResults,
        std::vector<FinalResult>& calculatedResults,
        double& passedctHb,
        double& FiO2,
        double& temperature,
        double& pressure,
        TestMode& testMode,
        double& patientAge,
        Gender& gender,
        eGFRFormula& egfrFormula,
        double& patientHeight,
        AgeCategory& ageCategory,
        double& RQ,
        bool& calculateAlveolar,
        bool& theApplymTCO2,
        std::string& errorMessage);

    static LibraryCallReturnCode deserializeComputeCorrectedResultsRequest(
        const char serializedInputData[],
        const int inputDataSize,
        std::vector<FinalResult>& measuredResults,
        std::vector<FinalResult>& correctedResults,
        double& temperature,
        double& pressure,
        double& FiO2,
        double& RQ,
        bool& calculateAlveolar,
        std::string& errorMessage);

    static LibraryCallReturnCode deserializePerformRealTimeQCRequest(
        const char serializedInputData[],
        const int inputDataSize,
        std::vector<std::shared_ptr<SensorReadings>>& testReadings,
        RealTimeQC& qcStruct,
        float& lastRecordedTime,
        std::string& errorMessage);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // Utility methods to serialize Response Transfer Objects

    static LibraryCallReturnCode serializeCalculateBGEResponse(
        const std::vector<std::shared_ptr<SensorReadings>>& sensorReadings, 
        const AnalyticalManager::BGEParameters& bgeParameters, 
        char*& serializedResponseData, 
        int& serializedInputDataSize,
        std::string& errorMessage);

    static LibraryCallReturnCode serializeCheckForEarlyInjectionResponse(
        const std::shared_ptr<SensorReadings>& hematocritReadings,
        const RealTimeHematocritQCReturnCode amRC,
        char*& serializedResponseData,
        int& serializedInputDataSize,
        std::string& errorMessage);

    static LibraryCallReturnCode serializeComputeCalculatedResultsResponse(
        const std::vector<FinalResult>& calculatedResults,
        char*& serializedResponseData,
        int& serializedInputDataSize,
        std::string& errorMessage);

    static LibraryCallReturnCode serializeComputeCorrectedResultsResponse(
        const std::vector<FinalResult>& correctedResults,
        char*& serializedResponseData,
        int& serializedInputDataSize,
        std::string& errorMessage);

    static LibraryCallReturnCode serializePerformRealTimeQCResponse(
        const std::vector<std::shared_ptr<SensorReadings>>& testReadings,
        const RealTimeQCReturnCode amRC,
        char*& serializedResponseData,
        int& serializedInputDataSize,
        std::string& errorMessage);
};

#endif //SRC_AMSERIALIZER_H
