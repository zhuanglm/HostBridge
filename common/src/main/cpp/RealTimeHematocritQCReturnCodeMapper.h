#ifndef EPOC_REALTIMEHEMATOCRITQCRETURNCODEMAPPER_H
#define EPOC_REALTIMEHEMATOCRITQCRETURNCODEMAPPER_H

#include <limits.h>

#include "RealTimeHematocritQCReturnCode.h"

using namespace Epoc::Common::Native::Definitions;

class RealTimeHematocritQCReturnCodeMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int)RealTimeHematocritQCReturnCode::Success;
                break;
            case 1:
                enumValue = (int)RealTimeHematocritQCReturnCode::NotPerformed;
                break;
            case 2:
                enumValue = (int)RealTimeHematocritQCReturnCode::OnePointLowResistance;
                break;
            case 3:
                enumValue = (int)RealTimeHematocritQCReturnCode::OnePointHighResistance;
                break;
            case 4:
                enumValue = (int)RealTimeHematocritQCReturnCode::OnePointAir;
                break;
            case 5:
                enumValue = (int)RealTimeHematocritQCReturnCode::EarlyInjection;
                break;
            case 6:
                enumValue = (int)RealTimeHematocritQCReturnCode::LowResistance;
                break;
            case 7:
                enumValue = (int)RealTimeHematocritQCReturnCode::FailedQC;
                break;
        }
        return enumValue;
    }
};


#endif // EPOC_REALTIMEHEMATOCRITQCRETURNCODEMAPPER_H