#ifndef SRC_AMCOOXUTIL_H
#define SRC_AMCOOXUTIL_H

#include <string>

#include "AMCooxSerializer.h"

#ifndef IN_OUT
#define IN_OUT
#endif

#ifndef IN
#define IN
#endif

#ifndef OUT
#define OUT
#endif

namespace AMCooxUtil {
    template<typename responseType>
    std::string GenerateFailedToSerializeAsStringResponseString(void) {
        AMCooxSerializer serializeUtil;

        responseType response;

        response.set_errorcode(
                (int) LibraryCallReturnCode::AM_CPP_PROTOBUF_ENCODING_FAILED_SERIALIZE);

        std::string response_string = response.SerializePartialAsString();

        return response_string;
    }

    const char *CalculateResults(IN const char serializedInputData[], IN OUT int *serializedDataSize);
}

#endif //SRC_AMCOOXUTIL_H
