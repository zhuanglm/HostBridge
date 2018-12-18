// AM Library Includes
#include "amUtil_common.h"
#include "AMSerializerHelper.h"

namespace AMUtil
{
    const char* CalculateBGE(IN const char serializedInputData[], IN OUT int* serializedDataSize)
    {
        // 1. Check nullptr input
        if (serializedInputData == nullptr)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::CalculateBGEResponse>(
                LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER,
                AMSerializerHelper::formatErrorMessage(AMSerializerHelper::ERROR_MESSAGE_EMPTY_INPUT_BUFFER, "AMUtil:CalculateBGE", __LINE__),
                serializedErrorResponsePtr, *serializedDataSize);

            return serializedErrorResponsePtr;
        }

        // 2. Deserialize input
        std::string errorMessage = "";

        std::vector<std::shared_ptr<SensorReadings>> sensorReadings;
        AnalyticalManager::BGEParameters bgeParameters;
        bool allowNegativeValues = false;

        LibraryCallReturnCode amSerializerReturnCode = AMSerializer::deserializeCalculateBGERequest(
            serializedInputData,
            *serializedDataSize,
            sensorReadings,
            bgeParameters,
            allowNegativeValues,
            errorMessage);

        if (amSerializerReturnCode != LibraryCallReturnCode::SUCCESS)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::CalculateBGEResponse>(amSerializerReturnCode, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 3. Call AM function
        try {
            AnalyticalManager::CalculateBGE(sensorReadings, bgeParameters, allowNegativeValues);
        }
        catch (exception& e) {
            char* serializedErrorResponsePtr = nullptr;
            errorMessage = AMSerializerHelper::formatErrorMessage(e.what(), "AMUtil:CalculateBGE", __LINE__);
            AMSerializerHelper::serializeErrorResponse<to::CalculateBGEResponse>(LibraryCallReturnCode::AM_CPP_DEFAULT_EXCEPTION, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 4. Serialize response
        char* serializedResponseDataPtr = nullptr;
        amSerializerReturnCode = AMSerializer::serializeCalculateBGEResponse(sensorReadings, bgeParameters, serializedResponseDataPtr, *serializedDataSize, errorMessage);
        if (amSerializerReturnCode != LibraryCallReturnCode::SUCCESS)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::CalculateBGEResponse>(amSerializerReturnCode, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 5. Send back response as char *
        return serializedResponseDataPtr;
    }
}
