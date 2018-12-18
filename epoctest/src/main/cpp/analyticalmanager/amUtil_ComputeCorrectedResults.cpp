// AM Library Includes
#include "amUtil_common.h"
#include "AMSerializerHelper.h"

namespace AMUtil
{
    const char* ComputeCorrectedResults(IN const char serializedInputData[], IN OUT int *serializedDataSize)
    {
        // 1. Check nullptr input
        if (serializedInputData == nullptr)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::ComputeCorrectedResultsResponse>(
                LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER,
                AMSerializerHelper::formatErrorMessage(AMSerializerHelper::ERROR_MESSAGE_EMPTY_INPUT_BUFFER, "AMUtil:ComputeCorrectedResults", __LINE__),
                serializedErrorResponsePtr, *serializedDataSize);

            return serializedErrorResponsePtr;
        }

        // 2. Deserialize input
        std::string errorMessage = "";

        std::vector<FinalResult> measuredResults;
        std::vector<FinalResult> correctedResults;
        double patientTemperature;
        double ambientPressure;
        double FiO2;
        double RQ;
        bool calculateAlveolar;

        LibraryCallReturnCode amSerializerReturnCode = AMSerializer::deserializeComputeCorrectedResultsRequest(
            serializedInputData,
            *serializedDataSize,
            measuredResults,
            correctedResults,
            patientTemperature,
            ambientPressure,
            FiO2,
            RQ,
            calculateAlveolar,
            errorMessage);
        
        if (amSerializerReturnCode != LibraryCallReturnCode::SUCCESS)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::ComputeCorrectedResultsResponse>(amSerializerReturnCode, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 3. Call AM function
        try {
            AnalyticalManager::ComputeCorrectedResults(measuredResults, correctedResults, patientTemperature, ambientPressure, FiO2, RQ, calculateAlveolar);
        }
        catch (exception& e) {
            char* serializedErrorResponsePtr = nullptr;
            errorMessage = AMSerializerHelper::formatErrorMessage(e.what(), "AMUtil:ComputeCorrectedResults", __LINE__);
            AMSerializerHelper::serializeErrorResponse<to::ComputeCorrectedResultsResponse>(LibraryCallReturnCode::AM_CPP_DEFAULT_EXCEPTION, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 4. Serialize response
        char* serializedResponseDataPtr = nullptr;
        amSerializerReturnCode = AMSerializer::serializeComputeCorrectedResultsResponse(correctedResults, serializedResponseDataPtr, *serializedDataSize, errorMessage);
        if (amSerializerReturnCode != LibraryCallReturnCode::SUCCESS)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::ComputeCorrectedResultsResponse>(amSerializerReturnCode, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 5. Send back response as char *
        return serializedResponseDataPtr;
    }
}
