#ifndef EPOC_AGECATEGORYMAPPER_H
#define EPOC_AGECATEGORYMAPPER_H

#include <limits.h>

#include "AgeCategory.h"

using namespace Epoc::Common::Native::Definitions;

class AgeCategoryMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int) AgeCategory::Premature;
                break;
            case 1:
                enumValue = (int) AgeCategory::Newborn;
                break;
            case 2:
                enumValue = (int) AgeCategory::Infant;
                break;
            case 3:
                enumValue = (int) AgeCategory::Youth;
                break;
            case 4:
                enumValue = (int) AgeCategory::Adult;
                break;
            case 5:
                enumValue = (int) AgeCategory::None;
                break;
        }
        return enumValue;
    }
};

#endif // EPOC_AGECATEGORYMAPPER_H