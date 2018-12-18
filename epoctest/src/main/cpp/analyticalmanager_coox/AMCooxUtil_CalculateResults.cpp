// AM Library Includes
#include "AMCooxUtil.h"
#include "AMSerializerHelper.h"

namespace AMCooxUtil {
    const char* CalculateResults(IN const char serializedInputData[], IN OUT int *serializedDataSize)
    {
        // 1. Check nullptr input
        if (serializedInputData == nullptr)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::CalculateResultsResponse>(
                LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER,
                AMSerializerHelper::formatErrorMessage(AMSerializerHelper::ERROR_MESSAGE_EMPTY_INPUT_BUFFER, "AMCooxUtil:CalculateResults", __LINE__),
                serializedErrorResponsePtr, *serializedDataSize);

            return serializedErrorResponsePtr;
        }

        // 2. Deserialize input
        std::string errorMessage = "";

        CooxModel cooxModel(0, 0, 0, 0);
        float opticalPathLength;
        std::vector<ResultsCalculator::Point> reference;
        std::vector<ResultsCalculator::Point> sample;
        bool allowNegativeValues;

        LibraryCallReturnCode amSerializerReturnCode = AMCooxSerializer::deserializeCalculateResultsRequest(
                serializedInputData,
                *serializedDataSize,
                cooxModel,
                opticalPathLength,
                reference,
                sample,
                allowNegativeValues,
                errorMessage);

        if (amSerializerReturnCode != LibraryCallReturnCode::SUCCESS)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::CalculateResultsResponse>(amSerializerReturnCode, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 3. Call AM function
        std::map<HgbResult, float> calculatedResults;
        try {
            calculatedResults = ResultsCalculator::CalculateResults(cooxModel, opticalPathLength, reference, sample, allowNegativeValues);
        }
        catch (exception& e) {
            char* serializedErrorResponsePtr = nullptr;
            errorMessage = AMSerializerHelper::formatErrorMessage(e.what(), "AMCooxUtil:CalculateResults", __LINE__);
            AMSerializerHelper::serializeErrorResponse<to::CalculateResultsResponse>(LibraryCallReturnCode::AM_CPP_DEFAULT_EXCEPTION, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 4. Serialize response
        char* serializedResponseDataPtr = nullptr;
        amSerializerReturnCode = AMCooxSerializer::serializeCalculateResultsResponse(calculatedResults, serializedResponseDataPtr, *serializedDataSize, errorMessage);
        if (amSerializerReturnCode != LibraryCallReturnCode::SUCCESS)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::CalculateResultsResponse>(amSerializerReturnCode, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 5. Send back response as char *
        return serializedResponseDataPtr;
    }
}
