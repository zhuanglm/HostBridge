// AM Library Includes
#include "amUtil_common.h"
#include "AMSerializerHelper.h"

namespace AMUtil
{
    const char* CheckForEarlyInjection(IN const char serializedInputData[], IN OUT int *serializedDataSize)
    {
        // 1. Check nullptr input
        if (serializedInputData == nullptr)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::CheckForEarlyInjectionResponse>(
                LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER,
                AMSerializerHelper::formatErrorMessage(AMSerializerHelper::ERROR_MESSAGE_EMPTY_INPUT_BUFFER, "AMUtil:CheckForEarlyInjection", __LINE__),
                serializedErrorResponsePtr, *serializedDataSize);

            return serializedErrorResponsePtr;
        }

        // 2. Deserialize input
        std::string errorMessage = "";

        std::shared_ptr<SensorReadings> hematocritReadings = std::make_shared<SensorReadings>();
        std::shared_ptr<SensorReadings> topHeaterReadings = std::make_shared<SensorReadings>();
        RealTimeHematocritQCReturnCode  previousReturnCode;
        double airAfterFluidThreshold;
        float lastRecordedTime;
        double firstFluid;

        LibraryCallReturnCode amSerializerReturnCode = AMSerializer::deserializeCheckForEarlyInjectionRequest(
            serializedInputData,
            *serializedDataSize,
            hematocritReadings,
            topHeaterReadings,
            previousReturnCode,
            airAfterFluidThreshold,
            lastRecordedTime,
            firstFluid,
            errorMessage);

        if (amSerializerReturnCode != LibraryCallReturnCode::SUCCESS)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::CheckForEarlyInjectionResponse>(amSerializerReturnCode, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }
        
        // 3. Call AM function
        RealTimeHematocritQCReturnCode rc = RealTimeHematocritQCReturnCode::Success;
        try {
            rc = AnalyticalManager::CheckForEarlyInjection(hematocritReadings, topHeaterReadings, previousReturnCode, airAfterFluidThreshold, lastRecordedTime, firstFluid);
        }
        catch (exception& e) {
            char* serializedErrorResponsePtr = nullptr;
            errorMessage = AMSerializerHelper::formatErrorMessage(e.what(), "AMUtil:CheckForEarlyInjection", __LINE__);
            AMSerializerHelper::serializeErrorResponse<to::CheckForEarlyInjectionResponse>(LibraryCallReturnCode::AM_CPP_DEFAULT_EXCEPTION, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 4. Serialize response
        char* serializedResponseDataPtr = nullptr;
        amSerializerReturnCode = AMSerializer::serializeCheckForEarlyInjectionResponse(hematocritReadings, rc, serializedResponseDataPtr, *serializedDataSize, errorMessage);
        if (amSerializerReturnCode != LibraryCallReturnCode::SUCCESS)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::CheckForEarlyInjectionResponse>(amSerializerReturnCode, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 5. Send back response as char *
        return serializedResponseDataPtr;
    }
}
