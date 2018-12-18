#include <jni.h>

// AM Library Includes
#include "ResultsCalculator.h"
#include "LibCallReturnCode.h"

#include "AMCooxUtil.h"

#include "amhelper.h"
#include "AMSerializerHelper.h"

using namespace Epoc::Common::Native::Definitions;
using namespace Epoc::AM::Native::Coox;

#ifdef __cplusplus
extern "C" {
#endif  // __cplusplus

JNIEXPORT jstring JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniAMCooxVersion(JNIEnv *env, jobject self)
{
    int major = 0;
    int minor = 0;
    int micro = 0;

    ResultsCalculator::Version(major, minor, micro);

    int charCount = GetVersionNumberCharCount(major, minor, micro);

    char* verBuffer = new char[charCount];
    int buffSize = sizeof(char) * charCount;
    snprintf(verBuffer, buffSize, "%d.%d.%d", major, minor, micro);

    jstring versionString = env->NewStringUTF(verBuffer);

    return versionString;
}

JNIEXPORT jbyteArray JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniCalculateResults(JNIEnv *env, jobject self,
                                                                                 jbyteArray serializedInputData, jint serializedInputDataSize)
{
    jbyteArray serializedReturnData = nullptr;

    try
    {
        // 1. Copy the IN byte buffer to serializedInputData_arr buffer
        int len = env->GetArrayLength(serializedInputData);
        unsigned char* serializedInputData_arr = new unsigned char[len];
        as_unsigned_char_array(env, serializedInputData, serializedInputData_arr);

        // 2. Call AM function
        unsigned char* serializedOutputData_arr = (unsigned char*)AMCooxUtil::CalculateResults((char*)serializedInputData_arr, &serializedInputDataSize);

        // 3. Check the result
        if (serializedOutputData_arr != nullptr) {
            // Success:
            // Copy the data over to OUT buffer (serializedReturnData)
            serializedReturnData = env->NewByteArray(serializedInputDataSize);
            env->SetByteArrayRegion(serializedReturnData, 0, serializedInputDataSize,
                                    (const jbyte *) serializedOutputData_arr);
            // Free memory allocated during the serialization of response
            AMSerializerHelper::freeMemory((char *)serializedOutputData_arr);
        } else {
            // Error: Send back nullptr to indicate error
            serializedReturnData = nullptr;
        }

        // 4. Clean up input buffer
        delete[] serializedInputData_arr;
        serializedInputData_arr = nullptr;
    }
    catch (std::exception &e)
    {
        jbyteArray serializedErrorResponseDataOut = nullptr;
        char* serializedErrorResponseDataPtr = nullptr;
        int serializedErrorResponseDataSize;
        AMSerializerHelper::serializeErrorResponse<to::CalculateResultsResponse>(
                LibraryCallReturnCode::AM_JNI_DEFAULT_EXCEPTION,
                e.what(),
                serializedErrorResponseDataPtr, serializedErrorResponseDataSize);

        if (serializedErrorResponseDataPtr != nullptr) {
            // Copy the data over to OUT buffer (serializedReturnData)
            serializedErrorResponseDataOut = env->NewByteArray(serializedErrorResponseDataSize);
            env->SetByteArrayRegion(serializedErrorResponseDataOut, 0, serializedErrorResponseDataSize,
                                    (const jbyte *) serializedErrorResponseDataPtr);
            // Free memory allocated during the serialization of response
            AMSerializerHelper::freeMemory((char *)serializedErrorResponseDataPtr);
        }
        return serializedErrorResponseDataOut;
    }

    return serializedReturnData;
}

#ifdef __cplusplus
}
#endif  // __cplusplus
