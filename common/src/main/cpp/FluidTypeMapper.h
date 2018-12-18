#ifndef EPOC_FLUIDTYPEMAPPER_H
#define EPOC_FLUIDTYPEMAPPER_H

#include <limits.h>
#include "FluidType.h"

using namespace Epoc::Common::Native::Definitions;

class FluidTypeMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int) FluidType::AutoDetect;
                break;
            case 1:
                enumValue = (int) FluidType::AutoDetectAqueous;
                break;
            case 2:
                enumValue = (int) FluidType::Blood;
                break;
            case 3:
                enumValue = (int) FluidType::Aqueuous;
                break;
            case 4:
                enumValue = (int) FluidType::HematocritControl;
                break;
            case 5:
                enumValue = (int) FluidType::Unknown;
                break;
        }
        return enumValue;
    }
};

#endif // EPOC_FLUIDTYPEMAPPER_H