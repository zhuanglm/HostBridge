#ifndef EPOC_TEMPERATURESMAPPER_H
#define EPOC_TEMPERATURESMAPPER_H

#include <limits.h>

#include "Temperatures.h"

using namespace Epoc::Common::Native::Definitions;

class TemperaturesMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int)Temperatures::C;
                break;
            case 1:
                enumValue = (int)Temperatures::F;
                break;
        }
        return enumValue;
    }
};


#endif // EPOC_TEMPERATURESMAPPER_H