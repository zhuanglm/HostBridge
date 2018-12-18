//
// Created by michde on 10/19/2017.
//

#ifndef SRC_AMSERIALIZER_ERROR_HELPER_H
#define SRC_AMSERIALIZER_ERROR_HELPER_H

#include <string>
#include <memory>

#include "LibCallReturnCode.h"

class AMSerializerHelper {
private:
    AMSerializerHelper() {}

public:
    static constexpr const char * ERROR_MESSAGE_EMPTY_INPUT_BUFFER = "Protobuf - empty input buffer";
    static constexpr const char * ERROR_MESSAGE_MISSING_INPUT_PARAMETER = "Protobuf - missing input parameter";
    static constexpr const char * ERROR_MESSAGE_PROTOBUFDECODE_REQUEST = "Protobuf decoding error on Request object";
    static constexpr const char * ERROR_MESSAGE_PROTOBUFENCODE_RESPONSE = "Protobuf encoding error on Response object";

    template<typename T>
    static void serializeErrorResponse(
        const LibraryCallReturnCode errorCode,
        const std::string& errorMessage,
        char*& serializedResponseData,
        int& serializedInputDataSize)
    {
        T responseTO;

        try
        {
            // 1. Convert IN parameter to Transfer Object
            responseTO.set_errorcode((int)errorCode);
            responseTO.set_errormessage(errorMessage);

            // 2. Allocate memory for char[] to hold protobuf encoded byte array
            serializedInputDataSize = responseTO.ByteSize();
            serializedResponseData = new char[serializedInputDataSize];
            // Caller of this method calls "freeMemory" to delete array.

            // 3. Protobuf encode
            if (!responseTO.SerializeToArray((void*)serializedResponseData, serializedInputDataSize))
            {
                // An error occured: free the buffer, and set it to nullptr
                freeMemory(serializedResponseData);
            }
        }
        catch (...)
        {
            // An error occured: free the buffer, and set it to nullptr
            freeMemory(serializedResponseData);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // Utility methods to format error string
    static std::string formatErrorMessage(const char* errorMessage, const char* moduleName = "", const int lineNo = -1);
    static void stripNewLineFromCharArray(char* inChar);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // Utility methods to free memory allocated by this class. (called by the caller of serialize[ResponseType]
    static void freeMemory(char* byteArrayPtr);
};

#endif // SRC_AMSERIALIZER_ERROR_HELPER_H
