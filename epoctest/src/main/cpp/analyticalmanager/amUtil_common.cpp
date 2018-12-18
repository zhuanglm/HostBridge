#include "amUtil_common.h"

namespace AMUtil
{
    template<typename responseType>
    std::string GenerateFailedToSerializeAsStringResponseString(void)
    {
        AMSerializer serializeUtil;

        responseType response;

        response.set_errorcode((int)LibraryCallReturnCode::AM_CPP_PROTOBUF_ENCODING_FAILED_SERIALIZE);

        std::string response_string = response.SerializePartialAsString();

        return response_string;
    }
}
