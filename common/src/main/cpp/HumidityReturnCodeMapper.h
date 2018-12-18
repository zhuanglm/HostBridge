//
// Created by akaiya on 07/09/2017.
//

#ifndef EPOC_HUMIDITYRETURNCODEMAPPER_H
#define EPOC_HUMIDITYRETURNCODEMAPPER_H
#include <limits.h>
#include "HumidityReturnCode.h"

using namespace Epoc::Common::Native::Definitions;

class HumidityReturnCodeMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex)
        {
            case 0:
                enumValue = (int) HumidityReturnCode::Success;
                break;
            case 1:
                enumValue = (int) HumidityReturnCode::NotPerformedYet;
                break;
            case 2:
                enumValue = (int) HumidityReturnCode::Failed;
                break;
        }
        return enumValue;
    }
};

#endif //EPOC_HUMIDITYRETURNCODEMAPPER_H
