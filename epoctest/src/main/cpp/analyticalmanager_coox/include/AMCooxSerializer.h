//
// Created by michde on 10/16/2017.
//

#ifndef SRC_AMCOOXSERIALIZER_H
#define SRC_AMCOOXSERIALIZER_H

#include <memory>

// AM Includes
#include "ResultsCalculator.h"
#include "LibCallReturnCode.h"

// Protobuf Includes
#include "calculate_results_request.pb.h"
#include "calculate_results_response.pb.h"
#include "coox_model.pb.h"
#include "point.pb.h"

using namespace Epoc::AM::Native::Coox;
using namespace google::protobuf;

class AMCooxSerializer {
public:
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // Utility methods to convert between Transfer Objects and C++ AM types

    static void convert(const CooxModel& cooxModel, to::CooxModel& cooxModelTO);
    static void convert(const to::CooxModel& cooxModelTO, CooxModel& cooxModel);

    static void convert(const ResultsCalculator::Point& point, to::Point& pointTO);
    static void convert(const to::Point& pointTO, ResultsCalculator::Point& point);

    static void convert(const HgbResult& HgbResultType, const float& hgbResultValue, to::CalculatedHgbResult &hgbResultTO);
    static void convert(const to::CalculatedHgbResult& hgbResultTO, HgbResult& HgbResultType, float& hgbResultValue);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // Utility methods to deserialize Request Transfer Objects

    static LibraryCallReturnCode deserializeCalculateResultsRequest(
        const char serializedInputData[],
        const int inputDataSize,
        CooxModel& cooxModel,
        float& opticalPathLength,
        std::vector<ResultsCalculator::Point>& reference,
        std::vector<ResultsCalculator::Point>& sample,
        bool& allowNegativeValues,
        std::string& errorMessage);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // Utility methods to serialize Response Transfer Objects

    static LibraryCallReturnCode serializeCalculateResultsResponse(
        const std::map<HgbResult, float>& calculatedResults,
        char*& serializedResponseData,
        int& serializedInputDataSize,
        std::string& errorMessage);
};

#endif //SRC_AMCOOXSERIALIZER_H
