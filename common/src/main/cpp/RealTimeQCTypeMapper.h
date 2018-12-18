#ifndef EPOC_REALTIMEQCTYPEMAPPER_H
#define EPOC_REALTIMEQCTYPEMAPPER_H

#include <limits.h>

#include "RealTimeQCType.h"

using namespace Epoc::Common::Native::Definitions;

class RealTimeQCTypeMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int)RealTimeQCType::onePoint;
                break;
            case 1:
                enumValue = (int)RealTimeQCType::multiplePoints;
                break;
        }
        return enumValue;
    }
};


#endif // EPOC_REALTIMEQCTYPEMAPPER_H