#include <jni.h>

#include <limits.h>
#include <vector>

// AM Library Includes
#include "analyticalmanager.h"
#include "LibCallReturnCode.h"

#include "amUtil_common.h"

#include "amhelper.h"
#include "AMSerializerHelper.h"

using namespace Epoc::Common::Native::Definitions;
using namespace Epoc::AM::Native::Bge;

#ifdef __cplusplus
extern "C" {
#endif  // __cplusplus

JNIEXPORT jstring JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniVersion(JNIEnv *env, jobject self)
{
    int major = 0;
    int minor = 0;
    int micro = 0;

    AnalyticalManager::Version(major, minor, micro);

    int charCount = GetVersionNumberCharCount(major, minor, micro);

    char* verBuffer = new char[charCount];
    int buffSize = sizeof(char) * charCount;
    snprintf(verBuffer, buffSize, "%d.%d.%d", major, minor, micro);

    jstring versionString = env->NewStringUTF(verBuffer);

    return versionString;
}

JNIEXPORT jdouble JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniFullNernst(JNIEnv *env, jobject self)
{
    return (jdouble)AnalyticalManager::FullNernst();
}

JNIEXPORT jint JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniLowNoiseSIBVersion(JNIEnv *env, jobject self)
{
    return (jint)AnalyticalManager::LowNoiseSIBVersion();
}

JNIEXPORT jbyteArray JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniCheckForEarlyInjection(JNIEnv *env, jobject self,
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
        unsigned char* serializedOutputData_arr = (unsigned char*)AMUtil::CheckForEarlyInjection((char*)serializedInputData_arr, &serializedInputDataSize);

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
        AMSerializerHelper::serializeErrorResponse<::to::CheckForEarlyInjectionResponse>(
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

JNIEXPORT jbyteArray JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniPerformRealTimeQC(JNIEnv *env, jobject self,
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
        unsigned char* serializedOutputData_arr = (unsigned char*)AMUtil::PerformRealTimeQC((char*)serializedInputData_arr, &serializedInputDataSize);

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
        AMSerializerHelper::serializeErrorResponse<::to::PerformRealTimeQCResponse>(
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

JNIEXPORT jbyteArray JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniCalculateBGE(JNIEnv *env, jobject self,
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
        unsigned char* serializedOutputData_arr = (unsigned char*)AMUtil::CalculateBGE((char*)serializedInputData_arr, &serializedInputDataSize);

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
        AMSerializerHelper::serializeErrorResponse<::to::CalculateBGEResponse>(
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

JNIEXPORT jboolean JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniFailedIQC(JNIEnv *env, jobject self,
                                                        jint rc)
{
    bool failed = AnalyticalManager::FailedIQC((ResultsCalcReturnCode)rc);
    return (jboolean)(failed ? JNI_TRUE : JNI_FALSE);
}

JNIEXPORT jboolean JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniIsBadValue(JNIEnv *env, jobject self,
                                                         jdouble val)
{
    bool isBad = AnalyticalManager::IsBadValue((double)val);
    return (jboolean)(isBad ? JNI_TRUE : JNI_FALSE);
}

JNIEXPORT jbyteArray JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniComputeCalculatedResults(JNIEnv *env, jobject self,
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
        unsigned char* serializedOutputData_arr = (unsigned char*)AMUtil::ComputeCalculatedResults((char*)serializedInputData_arr, &serializedInputDataSize);

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
        AMSerializerHelper::serializeErrorResponse<::to::ComputeCalculatedResultsResponse>(
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

JNIEXPORT jbyteArray JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniComputeCorrectedResults(JNIEnv *env, jobject self,
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
        unsigned char* serializedOutputData_arr = (unsigned char*)AMUtil::ComputeCorrectedResults((char*)serializedInputData_arr, &serializedInputDataSize);

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
        AMSerializerHelper::serializeErrorResponse<::to::ComputeCorrectedResultsResponse>(
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


//*********//
/// ENUMS ///
//*********//

JNIEXPORT jint JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniLibraryCallReturnCodeValue(JNIEnv *env, jobject self, jint index)
{
    jint enumValue = INT_MAX;
    switch(index)
    {
        case 0:
            enumValue = (jint)LibraryCallReturnCode::UNDEFINED;
            break;
        case 1:
            enumValue = (jint)LibraryCallReturnCode::SUCCESS;
            break;
        case 2:
            enumValue = (jint)LibraryCallReturnCode::UNEXPECTED_ERROR;
            break;
        case 3:
            enumValue = (jint)LibraryCallReturnCode::ANALYTICAL_MANAGER_DEFAULT_EXCEPTION;
            break;
        case 4:
            enumValue = (jint)LibraryCallReturnCode::AM_CPP_DEFAULT_EXCEPTION;
            break;
        case 5:
            enumValue = (jint)LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_ERROR;
            break;
        case 6:
            enumValue = (jint)LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER;
            break;
        case 7:
            enumValue = (jint)LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER;
            break;
        case 8:
            enumValue = (jint)LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_FAILED_DESERIALIZE;
            break;
        case 9:
            enumValue = (jint)LibraryCallReturnCode::AM_CPP_PROTOBUF_ENCODING_ERROR;
            break;
        case 10:
            enumValue = (jint)LibraryCallReturnCode::AM_CPP_PROTOBUF_ENCODING_FAILED_SERIALIZE;
            break;
        case 11:
            enumValue = (jint)LibraryCallReturnCode::AM_DOTNET_DEFAULT_EXCEPTION;
            break;
        case 12:
            enumValue = (jint)LibraryCallReturnCode::AM_DOTNET_PROTOBUF_DECODING_ERROR;
            break;
        case 13:
            enumValue = (jint)LibraryCallReturnCode::AM_DOTNET_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER;
            break;
        case 14:
            enumValue = (jint)LibraryCallReturnCode::AM_DOTNET_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER;
            break;
        case 15:
            enumValue = (jint)LibraryCallReturnCode::AM_DOTNET_PROTOBUF_DECODING_FAILED_DESERIALIZE;
            break;
        case 16:
            enumValue = (jint)LibraryCallReturnCode::AM_DOTNET_PROTOBUF_ENCODING_ERROR;
            break;
        case 17:
            enumValue = (jint)LibraryCallReturnCode::AM_DOTNET_PROTOBUF_ENCODING_FAILED_SERIALIZE;
            break;
        case 18:
            enumValue = (jint)LibraryCallReturnCode::AM_DOTNET_PROTOBUF_NULL_BUFFER;
            break;
        case 19:
            enumValue = (jint)LibraryCallReturnCode::AM_DOTNET_PROTOBUF_COPY_BUFFER_ERROR;
            break;
        case 20:
            enumValue = (jint)LibraryCallReturnCode::AM_JAVA_DEFAULT_EXCEPTION;
            break;
        case 21:
            enumValue = (jint)LibraryCallReturnCode::AM_JAVA_PROTOBUF_DECODING_ERROR;
            break;
        case 22:
            enumValue = (jint)LibraryCallReturnCode::AM_JAVA_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER;
            break;
        case 23:
            enumValue = (jint)LibraryCallReturnCode::AM_JAVA_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER;
            break;
        case 24:
            enumValue = (jint)LibraryCallReturnCode::AM_JAVA_PROTOBUF_DECODING_FAILED_DESERIALIZE;
            break;
        case 25:
            enumValue = (jint)LibraryCallReturnCode::AM_JAVA_PROTOBUF_ENCODING_ERROR;
            break;
        case 26:
            enumValue = (jint)LibraryCallReturnCode::AM_JAVA_PROTOBUF_ENCODING_FAILED_SERIALIZE;
            break;
        case 27:
            enumValue = (jint)LibraryCallReturnCode::AM_JNI_DEFAULT_EXCEPTION;
            break;
    }
    return enumValue;
}

JNIEXPORT jint JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniAnalyticalManagerParamTypeValue(JNIEnv *env, jobject self,
                                                                              jint index)
{
    jint enumValue = INT_MAX;
    switch (index)
    {
        case 0:
            enumValue = (jint)AnalyticalManager::AnalyticalManagerParamType::bubbleDetect;
            break;
        case 1:
            enumValue = (jint)AnalyticalManager::AnalyticalManagerParamType::sampleDetect;
            break;
        case 2:
            enumValue = (jint)AnalyticalManager::AnalyticalManagerParamType::ambientTemperature;
            break;
    }
    return enumValue;
}

JNIEXPORT jint JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniBloodSampleType37Value(JNIEnv *env, jobject self, jint index)
{
    jint enumValue = INT_MAX;
    switch(index)
    {
        case 0:
            enumValue = (jint)BloodSampleType_3_7::Unspecified;
            break;
        case 1:
            enumValue = (jint)BloodSampleType_3_7::Unknown;
            break;
        case 2:
            enumValue = (jint)BloodSampleType_3_7::Arterial;
            break;
        case 3:
            enumValue = (jint)BloodSampleType_3_7::Venous;
            break;
        case 4:
            enumValue = (jint)BloodSampleType_3_7::MixedVenous;
            break;
        case 5:
            enumValue = (jint)BloodSampleType_3_7::Cord;
            break;
        case 6:
            enumValue = (jint)BloodSampleType_3_7::Capillary;
            break;
        case 7:
            enumValue = (jint)BloodSampleType_3_7::Last;
            break;
    }
    return enumValue;
}

JNIEXPORT jint JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniTemperaturesValue(JNIEnv *env, jobject self, jint index)
{
    jint enumValue = INT_MAX;
    switch(index)
    {
        case 0:
            enumValue = (jint)Temperatures::C;
            break;
        case 1:
            enumValue = (jint)Temperatures::F;
            break;
    }
    return enumValue;
}

JNIEXPORT jint JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniRealTimeQCTypeValue(JNIEnv *env, jobject self, jint index)
{
    jint enumValue = INT_MAX;
    switch(index)
    {
        case 0:
            enumValue = (jint)RealTimeQCType::onePoint;
            break;
        case 1:
            enumValue = (jint)RealTimeQCType::multiplePoints;
            break;
    }
    return enumValue;
}

JNIEXPORT jint JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniRealTimeHematocritQCReturnCodeValue(JNIEnv *env, jobject self, jint index)
{
    jint enumValue = INT_MAX;
    switch(index)
    {
        case 0:
            enumValue = (jint)RealTimeHematocritQCReturnCode::Success;
            break;
        case 1:
            enumValue = (jint)RealTimeHematocritQCReturnCode::NotPerformed;
            break;
        case 2:
            enumValue = (jint)RealTimeHematocritQCReturnCode::OnePointLowResistance;
            break;
        case 3:
            enumValue = (jint)RealTimeHematocritQCReturnCode::OnePointHighResistance;
            break;
        case 4:
            enumValue = (jint)RealTimeHematocritQCReturnCode::OnePointAir;
            break;
        case 5:
            enumValue = (jint)RealTimeHematocritQCReturnCode::EarlyInjection;
            break;
        case 6:
            enumValue = (jint)RealTimeHematocritQCReturnCode::LowResistance;
            break;
        case 7:
            enumValue = (jint)RealTimeHematocritQCReturnCode::FailedQC;
            break;
    }
    return enumValue;
}

JNIEXPORT jint JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniHumidityReturnCodeValue(JNIEnv *env, jobject self, jint index)
{
    jint enumValue = INT_MAX;
    switch(index)
    {
        case 0:
            enumValue = (jint)HumidityReturnCode::Success;
            break;
        case 1:
            enumValue = (jint)HumidityReturnCode::NotPerformedYet;
            break;
        case 2:
            enumValue = (jint)HumidityReturnCode::Failed;
            break;
    }
    return enumValue;
}

JNIEXPORT jint JNICALL
Java_com_epocal_epoctest_analyticalmanager_AnalyticalManager_jniBubbleDetectModeValue(JNIEnv *env, jobject self, jint index)
{
    jint enumValue = INT_MAX;
    switch(index)
    {
        case 0:
            enumValue = (jint)BubbleDetectMode::AllAlwaysOff;
            break;
        case 1:
            enumValue = (jint)BubbleDetectMode::AllAlwaysOn;
            break;
        case 2:
            enumValue = (jint)BubbleDetectMode::BDChannelSamplingOnlyOn;
            break;
        case 3:
            enumValue = (jint)BubbleDetectMode::BDStageAllOn;
            break;
        case 4:
            enumValue = (jint)BubbleDetectMode::BDStageBDChannelOnOnly;
            break;
    }
    return enumValue;
}

#ifdef __cplusplus
}
#endif  // __cplusplus
