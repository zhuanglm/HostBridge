#ifndef AMUTIL_COMMON_H
#define AMUTIL_COMMON_H

#include <string>

#include "AMSerializer.h"

#include "LibCallReturnCode.h"

#ifndef IN_OUT
#define IN_OUT
#endif

#ifndef IN
#define IN
#endif

#ifndef OUT
#define OUT
#endif

namespace AMUtil
{
    template<typename responseType>
    std::string GenerateFailedToSerializeAsStringResponseString(void);

    const char* CheckForEarlyInjection(IN const char serializedInputData[], IN OUT int* serializedDataSize);
    const char* PerformRealTimeQC(IN const char serializedInputData[], IN OUT int* serializedDataSize);
    const char* CalculateBGE(IN const char serializedInputData[], IN OUT int* serializedDataSize);
    const char* ComputeCalculatedResults(IN const char serializedInputData[], IN OUT int* serializedDataSize);
    const char* ComputeCorrectedResults(IN const char serializedInputData[], IN OUT int* serializedDataSize);
}

#endif //AMUTIL_COMMON_H
