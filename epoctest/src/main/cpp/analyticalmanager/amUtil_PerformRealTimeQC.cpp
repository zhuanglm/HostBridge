// AM Library Includes
#include "amUtil_common.h"
#include "AMSerializerHelper.h"

namespace AMUtil
{
    const char* PerformRealTimeQC(IN const char serializedInputData[], IN OUT int *serializedDataSize)
    {
        // 1. Check nullptr input
        if (serializedInputData == nullptr)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::PerformRealTimeQCResponse>(
                LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER,
                AMSerializerHelper::formatErrorMessage(AMSerializerHelper::ERROR_MESSAGE_EMPTY_INPUT_BUFFER, "AMUtil:PerformRealTimeQC", __LINE__),
                serializedErrorResponsePtr, *serializedDataSize);

            return serializedErrorResponsePtr;
        }

        // 2. Deserialize input
        std::string errorMessage = "";

        std::vector<std::shared_ptr<SensorReadings>> testReadings;
        RealTimeQC qcStruct;
        float lastRecordedTime;

        LibraryCallReturnCode amSerializerReturnCode = AMSerializer::deserializePerformRealTimeQCRequest(
            serializedInputData,
            *serializedDataSize,
            testReadings,
            qcStruct,
            lastRecordedTime,
            errorMessage);

        if (amSerializerReturnCode != LibraryCallReturnCode::SUCCESS)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::PerformRealTimeQCResponse>(amSerializerReturnCode, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 3. Call AM function
        RealTimeQCReturnCode amRC = RealTimeQCReturnCode::Success;
        try {
            amRC = AnalyticalManager::PerformRealTimeQC(testReadings, qcStruct, lastRecordedTime);
        }
        catch (exception& e) {
            char* serializedErrorResponsePtr = nullptr;
            errorMessage = AMSerializerHelper::formatErrorMessage(e.what(), "AMUtil:PerformRealTimeQC", __LINE__);
            AMSerializerHelper::serializeErrorResponse<to::PerformRealTimeQCResponse>(LibraryCallReturnCode::AM_CPP_DEFAULT_EXCEPTION, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 4. Serialize response
        char* serializedResponseDataPtr = nullptr;
        amSerializerReturnCode = AMSerializer::serializePerformRealTimeQCResponse(testReadings, amRC, serializedResponseDataPtr, *serializedDataSize, errorMessage);
        if (amSerializerReturnCode != LibraryCallReturnCode::SUCCESS)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::PerformRealTimeQCResponse>(amSerializerReturnCode, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 5. Send back response as char *
        return serializedResponseDataPtr;
    }
}
