// AM Library Includes
#include "amUtil_common.h"
#include "AMSerializerHelper.h"

namespace AMUtil
{
    const char* ComputeCalculatedResults(IN const char serializedInputData[], IN OUT int *serializedDataSize)
    {
        // 1. Check nullptr input
        if (serializedInputData == nullptr)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::ComputeCalculatedResultsResponse>(
                LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER,
                AMSerializerHelper::formatErrorMessage(AMSerializerHelper::ERROR_MESSAGE_EMPTY_INPUT_BUFFER, "AMUtil:ComputeCalculatedResults", __LINE__),
                serializedErrorResponsePtr, *serializedDataSize);

            return serializedErrorResponsePtr;
        }

        // 2. Deserialize input
        std::string errorMessage = "";

        std::vector<FinalResult> measuredResults;
        std::vector<FinalResult> calculatedResults;
        double passedctHb;
        double FiO2;
        double patientTemperature;
        double ambientPressure;
        TestMode testMode;
        double patientAge;
        Gender gender;
        eGFRFormula egfrFormula;
        double patientHeight;
        AgeCategory ageCategory;
        double RQ;
        bool calculateAlveolar;
        bool theApplymTCO2;

        LibraryCallReturnCode amSerializerReturnCode = AMSerializer::deserializeComputeCalculatedResultsRequest(
            serializedInputData,
            *serializedDataSize,
            measuredResults,
            calculatedResults,
            passedctHb,
            FiO2,
            patientTemperature,
            ambientPressure,
            testMode,
            patientAge,
            gender,
            egfrFormula,
            patientHeight,
            ageCategory,
            RQ,
            calculateAlveolar,
            theApplymTCO2,
            errorMessage);
        
        if (amSerializerReturnCode != LibraryCallReturnCode::SUCCESS)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::ComputeCalculatedResultsResponse>(amSerializerReturnCode, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 3. Call AM function
        try {
            AnalyticalManager::ComputeCalculatedResults(measuredResults, calculatedResults, passedctHb, FiO2, patientTemperature, ambientPressure, testMode,
                                                        patientAge, gender, egfrFormula, patientHeight, ageCategory, RQ, calculateAlveolar, theApplymTCO2);
        }
        catch (exception& e) {
            char* serializedErrorResponsePtr = nullptr;
            errorMessage = AMSerializerHelper::formatErrorMessage(e.what(), "AMUtil:ComputeCalculatedResults", __LINE__);
            AMSerializerHelper::serializeErrorResponse<to::ComputeCalculatedResultsResponse>(LibraryCallReturnCode::AM_CPP_DEFAULT_EXCEPTION, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 4. Serialize response
        char* serializedResponseDataPtr = nullptr;
        amSerializerReturnCode = AMSerializer::serializeComputeCalculatedResultsResponse(calculatedResults, serializedResponseDataPtr, *serializedDataSize, errorMessage);
        if (amSerializerReturnCode != LibraryCallReturnCode::SUCCESS)
        {
            char* serializedErrorResponsePtr = nullptr;
            AMSerializerHelper::serializeErrorResponse<to::ComputeCalculatedResultsResponse>(amSerializerReturnCode, errorMessage, serializedErrorResponsePtr, *serializedDataSize);
            return serializedErrorResponsePtr;
        }

        // 5. Send back response as char *
        return serializedResponseDataPtr;
    }
}
